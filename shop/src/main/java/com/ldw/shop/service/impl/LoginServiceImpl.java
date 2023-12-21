package com.ldw.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ldw.shop.common.constant.ErrorCode;
import com.ldw.shop.common.constant.Result;
import com.ldw.shop.common.constant.userConstant;
import com.ldw.shop.dao.pojo.User;
import com.ldw.shop.service.LoginService;
import com.ldw.shop.service.UserService;
import com.ldw.shop.utils.JWTUtils;
import com.ldw.shop.utils.UserThreadLocal;
import com.ldw.shop.vo.param.LoginVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private  RedisTemplate<String,String> redisTemplate;//获取redis用户信息

    @Override
    public Result login(LoginVo loginVo) {
        /*
          1.检查参数是否合法
          2.根据用户名和密码去user表中查询，是否合法
          3.如果不存在，登录失败
          4.如果存在，使用jwt 生成token，返回给前端
          5.token放入redis中，使用redis token：user信息，设置过期时间
          （登录认证的时候，先认证token的字符串是否合法，去redis人认证是否存在）
         */
        String username=loginVo.getUsername();
        String password=loginVo.getPassword();
        if(StringUtils.isAllBlank(username)||StringUtils.isAllBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //把密码做加密之后再传入数据库中查询 要不然直接查前端传入的是无法查到的
        password= DigestUtils.md5Hex(password+ userConstant.SALT);
        User user = userService.checkUser(username, password);
        if(user==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(Long.valueOf(user.getId()));
        //使用redis
        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(user),1,TimeUnit.DAYS);
        return Result.success(token);
    }
    //校验登录 token的合法，返回当前登录用户信息
    @Override
    public User chckenToken(String token) {
        if(StringUtils.isAllBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap==null){//检查token合法性
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isAllBlank(userJson)){//token可能会过期
            return null;
        }
        User sysUser = JSON.parseObject(userJson, User.class);//将json数据转换成java对象
        return sysUser;
    }

    @Transactional
    @Override
    public String register(User user) {
        /**
         * 1.判断参数是否合法
         * 2.判断账户是否已存在，存在，则此账户已被注册
         * 3.如果账户不存在，注册用户
         * 4.注意，加上事务，一旦中间的任何过程问题，注册的用户，需要回滚
         */
        if(StringUtils.isAllBlank(user.getUsername())
                ||StringUtils.isAllBlank(user.getPassword())
                ||StringUtils.isAllBlank(user.getNickName())){
            return "(账号，密码，昵称)不能为空";
        }
        Boolean result = userService.selecetUserByUsername(user.getUsername());

        if(result){
            return "该用户已被注册";
        }
        User newuser =  User.builder()
                .username(user.getUsername())
                .password(DigestUtils.md5Hex(user.getPassword()+userConstant.SALT))
                .userFace(user.getUserFace())
                .nickName(user.getNickName())
                .realName(user.getRealName())
                .userMail(user.getUserMail())
//                .userMail(user.getUserMail())
                .sex(user.getSex())
                .birthDate(user.getBirthDate())
                .phone(user.getPhone())
                .payPassword(user.getPayPassword())
//                .userLasttime() 最后登录时间要在退出登录时操作
                .modifyTime(new Date())
                .userRegtime(new Date())
                .status(1)
                .remark(user.getRemark())
                .build();
        if( userService.save(newuser)){
            return "注册成功！";
        }
        return "系统故障，注册失败";
    }
    @Transactional
    @Override
    public Result exit(String token) {
//        //校验token的合法性
        User user = UserThreadLocal.get();
        //删除redis中的token
        redisTemplate.delete("TOKEN_" + token);
        //更新用户最后登录时间
        user.setUserLasttime(new Date());
        userService.updateById(user);
        return Result.success("登录已退出");
    }


}
