package com.ldw.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.dao.mapper.GoodsCommentMapper;
import com.ldw.shop.dao.mapper.GoodsMapper;
import com.ldw.shop.dao.pojo.Goods;
import com.ldw.shop.dao.pojo.GoodsComment;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.GoodsCommentService;
import com.ldw.shop.service.GoodsService;
import com.ldw.shop.service.UserService;
import com.ldw.shop.vo.GoodsCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsCommentServiceIpml extends ServiceImpl<GoodsCommentMapper, GoodsComment> implements GoodsCommentService {

    @Autowired
    private GoodsCommentMapper goodsCommentMapper;
    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public GoodsCommentVo selectGoodsCommOverview(Long goodsId) {

        GoodsComment goodsComment=new GoodsComment();
        goodsComment.setGoodsId(goodsId);
        goodsCommentMapper.selectComm(goodsComment);

        //好评率 = 好评数 / 评论总数量
        BigDecimal positiveRating = BigDecimal.ZERO;
        if (0 != goodsComment.getNumber()) {
            positiveRating = new BigDecimal(goodsComment.getPraiseNumber())
                    .divide(new BigDecimal(goodsComment.getNumber()),2, RoundingMode.HALF_DOWN)
                    .multiply(new BigDecimal(100));
        }
        //组装数据
        return GoodsCommentVo.builder()
                .number(goodsComment.getNumber())
                .praiseNumber(goodsComment.getPraiseNumber())
                .secondaryNumber(goodsComment.getSecondaryNumber())
                .negativeNumber(goodsComment.getNegativeNumber())
                .picNumber(goodsComment.getPicNumber())
                .positiveRating(positiveRating).build();
    }
    /**
     * 1.分页查询商品评论
     * 2.获取评论用户的id集合
     * 3.根据用户id集合查询用户信息
     * 4.组装数据
     * @param page
     * @param prodId
     * @param evaluate
     * @return
     */
    @Override
    public Page<GoodsComment> selectGoodsCommPageByProd(Page<GoodsComment> page, Long prodId, Long evaluate) {
        //分页查询商品评论
        /**
         * 根据条件分页查询商品评论
         */
        page = goodsCommentMapper.selectPage(page,new LambdaQueryWrapper<GoodsComment>()
                .eq(GoodsComment::getGoodsId,prodId)
                .eq(GoodsComment::getStatus,1)
                //下面的eq的意思是，当逗号前面的表达式满足时，才会做逗号后面的操作
                .eq(evaluate != null && evaluate != -1 && evaluate != 3,GoodsComment::getEvaluate,evaluate)
                //比如，当evaluate不为空，并且当evaluate等于3时，才会去判断getPics这个字段是否为空
                .isNotNull(evaluate != null && evaluate == 3,GoodsComment::getPics)
                .orderByDesc(GoodsComment::getRecTime)
        );
        //获取评论记录
        List<GoodsComment> GoodsCommentList = page.getRecords();
        //判断是否有值
        if (CollectionUtil.isEmpty(GoodsCommentList) || GoodsCommentList.size() == 0) {
            return page;
        }
        //获取评论的用户id集合
        List<Integer> userIds = GoodsCommentList.stream().map(GoodsComment::getUserId).collect(Collectors.toList());
        //根据用户id集合查询用户对象集合
        List<User> userList = userService.getUserListByUserIds(userIds);
        if (CollectionUtil.isEmpty(userList) || userList.size() == 0) {
            throw new RuntimeException("服务器开小差了");
        }
        //循环遍历评论记录并组装数据
        GoodsCommentList.forEach(GoodsComment -> {
           //从用户集合中过滤出与当前评论的用户id一致的用户对象
            User user1 = userList.stream()
                    .filter(user -> user.getId().equals(GoodsComment.getUserId()))
                    .collect(Collectors.toList()).get(0);
            //组装数据  将用户名改成 l***w
            String nickName = user1.getNickName();
            StringBuilder stringBuffer = new StringBuilder(nickName);
            stringBuffer.replace(1,stringBuffer.length()-1,"***");
            String pic = user1.getUserFace();
            GoodsComment.setNickName(stringBuffer.toString());
            GoodsComment.setPic(pic);
        });
        return page;
    }
    @Override
    public boolean addProdComm(GoodsComment goodsComment) {
        GoodsComment newgoodsCommentnew = new GoodsComment();
        newgoodsCommentnew.setGoodsId(goodsComment.getGoodsId());
        newgoodsCommentnew.setOrderItemId(goodsComment.getOrderItemId());
        newgoodsCommentnew.setUserId(goodsComment.getUserId());
        newgoodsCommentnew.setContent(goodsComment.getContent());
        newgoodsCommentnew.setReplyContent(goodsComment.getReplyContent());
        newgoodsCommentnew.setRecTime(new Date());

        newgoodsCommentnew.setReplySts(0);
        //ip地址
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String remoteHost = request.getRemoteHost();
        newgoodsCommentnew.setPostip(remoteHost);

        newgoodsCommentnew.setScore(goodsComment.getScore());
        newgoodsCommentnew.setUsefulCounts(goodsComment.getUsefulCounts());
        newgoodsCommentnew.setPics(goodsComment.getPics());
        newgoodsCommentnew.setIsAnonymous(goodsComment.getIsAnonymous());
        newgoodsCommentnew.setStatus(0);
        newgoodsCommentnew.setEvaluate(goodsComment.getEvaluate());

        return goodsCommentMapper.insert(newgoodsCommentnew)>0;
    }

    @Override
    public boolean updateById(GoodsComment goodsComment) {
        //获取回复评论内容
        String replyContent = goodsComment.getReplyContent();
        if (StringUtils.hasText(replyContent)) {
            goodsComment.setReplyTime(new Date());
            goodsComment.setReplySts(1);
            goodsComment.setStatus(1);
        }
        return goodsCommentMapper.updateById(goodsComment)>0;
    }

}
