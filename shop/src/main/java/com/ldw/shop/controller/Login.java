package com.ldw.shop.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class Login {


    @GetMapping("")
    public ResponseEntity<Integer> login(){

        return ResponseEntity.ok(1);
    }
}
