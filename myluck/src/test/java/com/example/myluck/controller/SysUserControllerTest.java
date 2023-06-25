package com.example.myluck.controller;

import com.example.myluck.util.Md5Utils;
import com.example.myluck.vo.Json;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SysUserControllerTest {
    @Autowired
    SysUserController sysUserController;

    @Test
    void findUserRoles() {
        Json user = sysUserController.findUserByName("admin");
        System.out.println(user);
    }

    @Test
    void password() {

    }



}