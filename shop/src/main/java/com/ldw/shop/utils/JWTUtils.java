package com.ldw.shop.utils;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    //  自定义密钥生成
    private static final String jwtToken="123456ldw!@#";
    //  创建一个Token
    public static String createToken(Long userId){
        Map<String,Object> claims=new HashMap<>(); //B部分
        claims.put("userId",userId);
        JwtBuilder jwtBuilder= Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken)// A部分 签发算法注意使用HS开头的，密钥为jwtToken
                .setClaims(claims)//body数据，要唯一，自定义，这里使用用户的id
                .setIssuedAt(new Date())//设置签发的时间,保证每次生成的token不重复
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000));//token一天的有效时间
        String token=jwtBuilder.compact();
        return token;
    }
    //     检测token是否合法
    public static Map<String,Object>checkToken(String token){
        try {
            //parser：解析token  setSigningKey：需要提供密钥 .parse(token)根据密钥解析值
            Jwt parse= Jwts.parser().setSigningKey(jwtToken).parse(token);
            //将取得token的键值对返回
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
//测试
//    public static void main(String[] args) {
//        String token=JWTUtils.createToken(100L);
//        System.out.println("生成的token::"+token);
//        Map<String,Object>map=JWTUtils.checkToken(token);
//        System.out.println("解析出来的token::"+map.get("userId"));
//    }

}
