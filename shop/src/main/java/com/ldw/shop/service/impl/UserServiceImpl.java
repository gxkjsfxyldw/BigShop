package com.ldw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldw.shop.common.constant.ErrorCode;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.common.constant.userConstant;
import com.ldw.shop.dao.mapper.UserMapper;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.UserService;
import com.ldw.shop.utils.UserThreadLocal;
import com.ldw.shop.vo.param.UserVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Override
    public User checkUser(String username, String password) {

        return  userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername,username)
                .eq(User::getPassword,password)
        );
    }

    @Override
    public Boolean selecetUserByUsername(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if(user==null){
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public Result updateUser(UserVo userVo) {

        User user = UserThreadLocal.get();

        user.setPassword(DigestUtils.md5Hex(userVo.getPassword()+ userConstant.SALT));
        user.setUserFace(userVo.getUserFace());
        user.setNickName(userVo.getNickName());
        user.setRealName(userVo.getRealName());
        user.setUserMail(userVo.getUserMail());
        user.setSex(userVo.getSex());
        user.setBirthDate(userVo.getBirthDate());
        user.setPhone(userVo.getPhone());
        user.setPassword(userVo.getPayPassword());
        user.setRemark(userVo.getRemark());

        int result = userMapper.updateById(user);
        if(result>0){
            return Result.success("更新成功");
        }
        return Result.fail(409,ErrorCode.PARAMS_ERROR.getMsg());
    }

    @Override
    public List<User> getUserListByUserIds(List<Integer> userIds) {
        return userMapper.selectList(new LambdaQueryWrapper<User>()
                .in(User::getId,userIds)
        );
    }
}
