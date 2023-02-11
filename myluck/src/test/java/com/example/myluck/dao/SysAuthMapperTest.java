package com.example.myluck.dao;

import com.example.myluck.entity.SysAuth;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SysAuthMapperTest {
    @Autowired
    SysAuthMapper sysAuthMapper;

    @Test
    void getAuthByUid() {
        List<SysAuth> authByUid = sysAuthMapper.getAuthByUid(1);
        System.out.println(authByUid);
    }
}