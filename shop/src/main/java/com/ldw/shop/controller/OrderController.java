package com.ldw.shop.controller;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.OrderService;
import com.ldw.shop.utils.UserThreadLocal;
import com.ldw.shop.vo.param.OrderConfirmDto;
import com.ldw.shop.vo.param.OrderStatus;
import com.ldw.shop.vo.param.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@Api(tags = "订单模块")
@ApiModel("OrderController")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("查询用户订单总览信息")
    @GetMapping("orderCount")
    public ResponseEntity<OrderStatus> loadUserOrderStatus() {
        User user = UserThreadLocal.get();
        OrderStatus orderStatus = orderService.selectUserOrderStatus(user.getId());
        return ResponseEntity.ok(orderStatus);
    }

    //    p/myOrder/confirm
    @ApiOperation("订单确认")
    @PostMapping("confirm")
    public ResponseEntity<OrderVo> loadOrderConfirmInfo(@RequestBody OrderConfirmDto orderConfirmDto) {
        User user = UserThreadLocal.get();
        OrderVo orderVo = orderService.selectOrderConfirmInfo(user.getId(),orderConfirmDto);
        return ResponseEntity.ok(orderVo);
    }

    //    p/myOrder/submit
    @ApiOperation("提交订单")
    @PostMapping("submit")
    public ResponseEntity<String> submitOrder(@RequestBody OrderVo orderVo) {
        User user = UserThreadLocal.get();
        String orderNumber = orderService.submitOrder(user.getId(),orderVo);
        return ResponseEntity.ok(orderNumber);
    }
}
