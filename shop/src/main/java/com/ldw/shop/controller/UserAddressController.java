package com.ldw.shop.controller;

import com.ldw.shop.dao.pojo.UserAddress;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.UserAddressService;
import com.ldw.shop.utils.UserThreadLocal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "收货地址接口管理")
@RestController
@RequestMapping("address")
@ApiModel("UserAddressController")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @ApiOperation("查询用户收货地址列表")
    @GetMapping("list")
    public ResponseEntity<List<UserAddress>> loadUserAddrList() {
        User user = UserThreadLocal.get();
        Integer userId = user.getId();
        List<UserAddress> userAddressList = userAddressService.selectOrderAddrList(userId);
        return ResponseEntity.ok(userAddressList);
    }

    //    address/addAddr
    @ApiOperation("新增收货地址")
    @PostMapping("addAddr")
    public ResponseEntity<Void> saveUserAddr(@RequestBody UserAddress userAddress) {
        User user = UserThreadLocal.get();
        Integer userId = user.getId();
        userAddress.setUserId(String.valueOf(userId));
        userAddressService.save(userAddress);
        return ResponseEntity.ok().build();
    }

    //    address/addrInfo/10
    @ApiOperation("根据标识查询地址详情")
    @GetMapping("addrInfo/{addrId}")
    public ResponseEntity<UserAddress> loadUserAddrInfo(@PathVariable Long addrId) {
        UserAddress userAddress = userAddressService.getById(addrId);
        return ResponseEntity.ok(userAddress);
    }

    //    address/updateAddr
    @ApiOperation("修改用户收货地址信息")
    @PutMapping("updateAddr")
    public ResponseEntity<Void> updateUserAddr(@RequestBody UserAddress userAddress) {
        User user = UserThreadLocal.get();
        Integer userId = user.getId();
        userAddress.setUserId(String.valueOf(userId));
        userAddressService.updateById(userAddress);
        return ResponseEntity.ok().build();
    }

    //    p/address/deleteAddr/9
    @ApiOperation("删除用户收货地址")
    @DeleteMapping("deleteAddr/{addrId}")
    public ResponseEntity<Void> deleteUserAddr(@PathVariable Long addrId) {
        User user = UserThreadLocal.get();
        Integer userId = user.getId();
        userAddressService.deleteUserAddr(userId,addrId);
        return ResponseEntity.ok().build();
    }

    //    p/address/defaultAddr/13
    @ApiOperation("设置用户默认收货地址")
    @PutMapping("defaultAddr/{addrId}")
    public ResponseEntity<Void> updateUserDefaultAddr(@PathVariable Long addrId) {
        User user = UserThreadLocal.get();
        Integer userId = user.getId();
        userAddressService.updateUserDefaultAddr(userId,addrId);
        return ResponseEntity.ok().build();
    }
}
