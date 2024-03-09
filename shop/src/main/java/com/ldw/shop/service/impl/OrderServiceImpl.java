package com.ldw.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.common.constant.QueueConstant;
import com.ldw.shop.dao.mapper.*;
import com.ldw.shop.dao.pojo.*;
import com.ldw.shop.service.*;
import com.ldw.shop.vo.param.*;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private DiscountMapper discountMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private SkuService skuService;
    @Autowired
    private GoodsCartService goodsCartService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Redisson redisson;

    @Override
    public OrderStatus selectUserOrderStatus(Integer userId) {
        //查询待支付数量
        Integer unPay = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .eq(Order::getDeleteStatus, 0)
                .eq(Order::getStatus, 1)
        );

        //查询待发货数量
        Integer payed = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .eq(Order::getDeleteStatus, 0)
                .eq(Order::getStatus, 2)
        );

        //查询待收货数量
        Integer consignment = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .eq(Order::getDeleteStatus, 0)
                .eq(Order::getStatus, 3)
        );

        //使用创建者设计模式填充对象
        return OrderStatus.builder().unPay(unPay).payed(payed).consignment(consignment).build();
    }

    @Override
    public OrderVo selectOrderConfirmInfo(Integer userId, OrderConfirmDto orderConfirmDto) {
        OrderVo orderVo = new OrderVo();
        //远程调用：查询用户默认收货地址
        UserAddress userDefaultAddr = userAddressService.getOne(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getCommonAddr, 1)
                .eq(UserAddress::getUserId, userId));
        orderVo.setUserAddress(userDefaultAddr);

        //判断订单确认来自于哪儿（商品详情页面还是购物车页面）
        List<Long> basketIds = orderConfirmDto.getBasketIds();
        if (CollectionUtil.isEmpty(basketIds) || basketIds.size() == 0) {
            //订单来自于：商品详情页面
            productToConfirm(userId,orderVo,orderConfirmDto.getOrderItem());
        } else {
            //订单来自于：购物车页面
            orderVo.setSource(1);
            cartToConfirm(userId,orderVo,basketIds);
        }

        return orderVo;
    }


    private void cartToConfirm(Integer userId, OrderVo orderVo, List<Long> basketIds) {

        //获取redis中的key:根据购物车id集合  查询购物车对象集合
        String redisKey = "cat" + "::" +userId;
        List<GoodsCart> goodsCartlist = new ArrayList<>();
        Map<Object, Object> usercart = redisTemplate.opsForHash().entries(redisKey);
        int i=0;
        for (Object shopid:usercart.keySet()) {
            if(basketIds.get(i)==shopid){
                goodsCartlist.add(JSON.parseObject(String.valueOf(usercart.get(shopid)), GoodsCart.class));
            }
            i++;
        }

        //从购物车集合对象中获取商品skuId集合
        List<Long> skuIdList = goodsCartlist.stream().map(GoodsCart::getSkuId).collect(Collectors.toList());
        //根据商品skuId集合查询商品sku对象集合
        List<Sku> skuList = skuService.listByIds(skuIdList);
        List<ShopOrder> shopOrderList = new ArrayList<>();

        //将购物车对象集合使用stream流程进行分组
        Map<Long, List<GoodsCart>> allShopOrderMap = goodsCartlist.stream().collect(Collectors.groupingBy(GoodsCart::getShopId));
        List<BigDecimal> allOneSkuTotalAmounts = new ArrayList<>();
        List<Integer> allOneSkuCounts = new ArrayList<>();

        //循环遍历map集合
        allShopOrderMap.forEach((shopId,baskets) -> {
            //创建一个店铺对象
            ShopOrder shopOrder = new ShopOrder();
            List<OrderItem> orderItemList = new ArrayList<>();
            //循环遍历当前店铺中的购物车记录
            for(GoodsCart basket:baskets){
                //创建订单商品条目对象
                OrderItem orderItem = new OrderItem();
                //将购物对象中的属性copy到商品条目对象中
                BeanUtils.copyProperties(basket,orderItem);
                //从商品sku集合对象中过滤出与当前购物车记录中商品skuId一致的商品对象
                Sku sku1 = skuList.stream().filter(sku -> sku.getSkuId().equals(basket.getSkuId())).collect(Collectors.toList()).get(0);
                BeanUtils.copyProperties(sku1,orderItem);
                orderItem.setUserId(String.valueOf(userId));
                orderItem.setRecTime(new Date());
                orderItem.setCommSts(0);

                //计算 单个商品总金额
                Integer basketCount = basket.getCartCount();
                orderItem.setProdCount(basketCount);
                allOneSkuCounts.add(basketCount);
                BigDecimal oneSkuTotalAmount = sku1.getPrice().multiply(new BigDecimal(basketCount));
                allOneSkuTotalAmounts.add(oneSkuTotalAmount);
                orderItem.setProductTotalAmount(oneSkuTotalAmount);

                orderItemList.add(orderItem);
            }

            shopOrder.setShopCartItemDiscounts(orderItemList);
            shopOrderList.add(shopOrder);
        });

        //计算购买商品总数量
        Integer allSkuTotalCount = allOneSkuCounts.stream().reduce(Integer::sum).get();
        //计算所有商品总金额
        BigDecimal allSkuTotalAmount = allOneSkuTotalAmounts.stream().reduce(BigDecimal::add).get();
        orderVo.setTotalCount(allSkuTotalCount);
        orderVo.setTotal(allSkuTotalAmount);
        orderVo.setActualTotal(allSkuTotalAmount);
        //计算运费
        if (allSkuTotalAmount.compareTo(new BigDecimal(99)) == -1) {
            orderVo.setTransfee(new BigDecimal(6));
            orderVo.setActualTotal(allSkuTotalAmount.add(new BigDecimal(6)));
        }
        orderVo.setShopCartOrders(shopOrderList);
    }

    private void productToConfirm(Integer userId, OrderVo orderVo, OrderItem orderItem) {
        Integer prodCount = orderItem.getProdCount();
        //获取商品sku
        Sku sku = skuMapper.selectOne(new LambdaQueryWrapper<Sku>()
                .eq(Sku::getSkuId, orderItem.getSkuId())
                .eq(Sku::getProdId, orderItem.getProdId())
        );
        if (sku!=null) {
            throw new RuntimeException("服务器开小差了");
        }
        BigDecimal price = sku.getPrice();
        //计算单个商品总金额
        orderVo.setTotal(price);
        orderVo.setTotalCount(prodCount);
        //优惠金额 打折以及优惠卷
        if(orderItem.getDiscountId()!=null) {
            Discount discount = discountMapper.selectOne(new QueryWrapper<Discount>().eq("id", orderItem.getDiscountId()));
            System.out.println("aa" + discount);
            //查看单品是否符合打折要求
            if (price.compareTo(BigDecimal.valueOf(discount.getFull_money())) == 1) {
                //打折后的价格
                BigDecimal prices = sku.getPrice().multiply(discount.getDiscountAmount().multiply(BigDecimal.valueOf(0.1)));
                //原价减去打折后的价格 即 优惠的金额
                BigDecimal shopReduce = sku.getPrice().subtract(prices);
                orderVo.setShopReduce(shopReduce);
            }
        }
        //计算运费，分省外省内之类的
        orderVo.setTransfee(new BigDecimal(6));
        //运费 加 原价 减去 优惠金额
        BigDecimal sum= orderVo.getTransfee().add(orderVo.getTotal().subtract(orderVo.getShopReduce()));
        //需付款
        orderVo.setActualTotal(sum);

        //创建店铺对象
        List<ShopOrder> shopOrderList = new ArrayList<>();
        ShopOrder shopOrder = new ShopOrder();
        List<OrderItem> orderItemList = new ArrayList<>();

        //将商品sku对象中的属性copy到商品条目对象中
        BeanUtils.copyProperties(sku,orderItem);
        orderItem.setUserId(String.valueOf(userId));
        orderItem.setProductTotalAmount(sum);
        orderItem.setRecTime(new Date());
        orderItem.setCommSts(0);

        orderItemList.add(orderItem);
        shopOrder.setShopCartItemDiscounts(orderItemList);
        shopOrderList.add(shopOrder);
        orderVo.setShopCartOrders(shopOrderList);
    }

    /**
     * 1.判断订单确认页面的请求是否来自于购物车页面
     *     是：将当前购买的商品从购物车中清除
     *     不是：不需要做任何处理
     * 2.修改商品在数据库中库存数量：商品prod和商品sku的库存数量
     * 3.生成订单：生成全局唯一的订单号，生成订单记录和生成订单详情商品记录
     * 4.如果订单超时，我们需要将商品购买的数量进行回滚
     *     使用消息队列中的延迟队列+死信队列
     * @param userId
     * @param orderVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String submitOrder(Integer userId, OrderVo orderVo) {
        //获取订单确认页面来源标签
        Integer source = orderVo.getSource();
        if (source.equals(1)) {
            //来自于购物车页面->清除购物车中购买的商品
            clearUserCart(userId,orderVo);
        }
        //生成全局唯一订单号
        String orderNumber = generateOrderNumber();
        String lockKey="order_"+orderNumber;
//        //获取分布式锁
        RLock redissionlock = redisson.getLock(lockKey);
//        //加锁
        redissionlock.lock();
        try {
            //修改商品和sku库存数量
            ChangeStock changeStock = changeProdAndSkuStock(orderVo);
            //写订单和订单详情信息
            writeOrder(userId,orderNumber,orderVo);
            //将商品修改的库存数量消息存放到消息队列
            sendMsMsg(orderNumber,changeStock);
        }finally {
            redissionlock.unlock();
        }
        return orderNumber;
    }

    private void sendMsMsg(String orderNumber, ChangeStock changeStock) {
        //将数据转换为json数据格式存放到消息队列中
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNumber",orderNumber);
        jsonObject.put("changeStock",changeStock);
        //给指定队列发送消息，函数有三个参数：
        // 第一个是交换机(exchange)的名字
        // 第二个是路由键(routing-key)的名字
        // 第三个则为消息的内容
        rabbitTemplate.convertAndSend(QueueConstant.ORDER_MS_QUEUE,jsonObject.toJSONString());
    }


    @Override
    public void orderRollBack(Order order, ChangeStock changeStock) {
        //修改订单状态
        order.setStatus(6);
        order.setCloseType(1);
        order.setUpdateTime(new Date());
        order.setFinallyTime(new Date());
        orderMapper.updateById(order);

        //回滚商品购买的数量
        List<SkuChange> skuChangeList = changeStock.getSkuChangeList();
        skuChangeList.forEach(skuChange -> {
            skuChange.setCount(skuChange.getCount()*-1);
        });
        List<GoodsChange> prodChangeList = changeStock.getProdChangeList();
        prodChangeList.forEach(prodChange -> {
            prodChange.setCount(prodChange.getCount()*-1);
        });
        goodsService.changeStock(changeStock);
    }

    /**
     * 清除用户购物车中商品
     * @param userId
     * @param orderVo
     */
    private void clearUserCart(Integer userId, OrderVo orderVo) {
        //获取订单店铺集合对象
        List<ShopOrder> shopOrderList = orderVo.getShopCartOrders();
        //从订单店铺对象集合中的订单商品条目对象集合中获取商品skuId集合
        List<Long> skuIdList = new ArrayList<>();
        shopOrderList.forEach(shopOrder -> {
            //从店铺对象中获取商品条目集合对象
            List<OrderItem> orderItemList = shopOrder.getShopCartItemDiscounts();
            //循环遍历商品条目集合对象
            orderItemList.forEach(orderItem -> {
                //获取商品skuId
                Long skuId = orderItem.getSkuId();
                skuIdList.add(skuId);
            });
        });
        //删除购物车中的商品
        goodsCartService.remove(new LambdaQueryWrapper<GoodsCart>()
                .eq(GoodsCart::getUserId,userId)
                .in(GoodsCart::getSkuId,skuIdList)
        );

    }

    /**
     * 修改商品prod和sku库存数量
     * @param orderVo
     * @return
     */
    private ChangeStock changeProdAndSkuStock(OrderVo orderVo) {

        List<GoodsChange> prodChangeList = new ArrayList<>();
        List<SkuChange> skuChangeList = new ArrayList<>();

        //获取店铺集合对象
        List<ShopOrder> shopOrderList = orderVo.getShopCartOrders();

        //循环店铺集合对象
        shopOrderList.forEach(shopOrder -> {
            //从店铺对象中获取商品条目集合对象
            List<OrderItem> orderItemList = shopOrder.getShopCartItemDiscounts();
            //循环遍历商品条目集合对象
            orderItemList.forEach(orderItem -> {
                //获取商品prodId
                Long prodId = orderItem.getProdId();
                //获取商品skuId
                Long skuId = orderItem.getSkuId();

                //获取商品购买数量
                Integer prodCount = orderItem.getProdCount()*-1;

                //判断当前orderItem商品条目对象中的商品prodId是否与prodChangeList中的商品prodId有一致
                List<GoodsChange> prodChanges = prodChangeList.stream()
                        .filter(prodChange -> prodChange.getProdId().equals(prodId))
                        .collect(Collectors.toList());

                //商品prod第1次
                SkuChange skuChange = new SkuChange();
                skuChange.setSkuId(skuId);
                skuChange.setCount(prodCount);
                skuChangeList.add(skuChange);

                GoodsChange prodChange = null;
                if (CollectionUtil.isEmpty(prodChanges) || prodChanges.size() == 0) {
                    prodChange = new GoodsChange();
                    prodChange.setProdId(prodId);
                    prodChange.setCount(prodCount);
                    prodChangeList.add(prodChange);
                } else {
                    prodChange = prodChanges.get(0);
                    Integer beforeCount = prodChange.getCount();
                    int finalCount = beforeCount + prodCount;
                    prodChange.setCount(finalCount);
                }
            });
        });
        //组装数据
        ChangeStock changeStock = new ChangeStock(prodChangeList,skuChangeList);
        //修改商品prod和sku库存数量
        goodsService.changeStock(changeStock);
        return changeStock;
    }
    //雪花算法
    private String generateOrderNumber() {
        return snowflake.nextIdStr();
    }

    private void writeOrder(Integer userId, String orderNumber, OrderVo orderVo) {
        List<BigDecimal> allOneSkuTotalAmounts = new ArrayList<>();
        List<Integer> allOneSkuTotalCounts = new ArrayList<>();
        StringBuffer prodNames = new StringBuffer();
        List<OrderItem> orderItems = new ArrayList<>();
        //获取订单店铺对象集合
        List<ShopOrder> shopOrderList = orderVo.getShopCartOrders();
        //循环遍历订单店铺对象集合
        shopOrderList.forEach(shopOrder -> {
            //获取店铺中订单商品条目集合对象
            List<OrderItem> orderItemList = shopOrder.getShopCartItemDiscounts();
            //循环遍历订单商品条目集合对象
            orderItemList.forEach(orderItem -> {
                //完善订单商品条目对象信息
                orderItem.setOrderNumber(orderNumber);
                orderItem.setUserId(String.valueOf(userId));
                //单个商品总金额
                BigDecimal oneSkuTotalAmount = orderItem.getProductTotalAmount();
                allOneSkuTotalAmounts.add(oneSkuTotalAmount);
                //单个商品购买数量
                Integer prodCount = orderItem.getProdCount();
                allOneSkuTotalCounts.add(prodCount);
                //商品名称
                String prodName = orderItem.getProdName();
                prodNames.append(prodName).append(",");
                orderItems.add(orderItem);
            });
        });

        //批量生成订单详情信息
        if (!orderItemService.saveBatch(orderItems)) {
            throw new RuntimeException("服务器开小差了");
        }
        //组装订单总览信息
        Order order = new Order();
        order.setProdName(prodNames.toString());
        order.setUserId(String.valueOf(userId));
        order.setOrderNumber(orderNumber);
        //计算总值
        BigDecimal allSkuTotalAmount = allOneSkuTotalAmounts.stream().reduce(BigDecimal::add).get();
        order.setTotal(allSkuTotalAmount);
        order.setActualTotal(allSkuTotalAmount);
        order.setFreightAmount(BigDecimal.ZERO);
        if (allSkuTotalAmount.compareTo(new BigDecimal(99)) == -1) {
            order.setActualTotal(allSkuTotalAmount.add(new BigDecimal(6)));
            order.setFreightAmount(new BigDecimal(6));

        }
        order.setPayType(1);
        order.setRemarks(orderVo.getRemarks());
        order.setStatus(1);
        order.setAddrOrderId(orderVo.getUserAddress().getAddrId());
        //计算商品总数量
        Integer allSkuCount = allOneSkuTotalCounts.stream().reduce(Integer::sum).get();
        order.setProductNums(allSkuCount);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setIsPayed(0);
        order.setDeleteStatus(0);
        order.setRefundSts(0);
        int i = orderMapper.insert(order);
        if (i <= 0) {
            throw new RuntimeException("服务器开小差了");
        }

    }
}
