package com.ldw.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ldw.shop.dao.pojo.Order;
import com.ldw.shop.vo.param.ChangeStock;
import com.ldw.shop.vo.param.OrderConfirmDto;
import com.ldw.shop.vo.param.OrderStatus;
import com.ldw.shop.vo.param.OrderVo;

public interface OrderService extends IService<Order>{

    /**
     * * 查询用户订单总览信息
     * @param userId
     * @return
     */
    OrderStatus selectUserOrderStatus(Integer userId);

    /**
     * * 订单确认
     * @param userId
     * @param orderConfirmDto
     * @return
     */
    OrderVo selectOrderConfirmInfo(Integer userId, OrderConfirmDto orderConfirmDto);

    String submitOrder(Integer userId, OrderVo orderVo);

    void orderRollBack(Order order, ChangeStock changeStock);
}
