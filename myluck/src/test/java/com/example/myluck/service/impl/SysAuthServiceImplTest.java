package com.example.myluck.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.example.myluck.service.SysAuthService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class SysAuthServiceImplTest {

    @Autowired
    SysAuthService sysAuthService;

    @Test
    void getAuthByUid() {
        JSONArray authByUid = sysAuthService.getAuthByUid(10);
        System.out.println(authByUid);
    }
}