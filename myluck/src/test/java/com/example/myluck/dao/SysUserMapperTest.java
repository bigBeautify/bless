package com.example.myluck.dao;

import com.example.myluck.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SysUserMapperTest {
    @Autowired
    SysUserMapper sysUserMapper;


    @Test
    void selectByPrimaryKey() {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(1);
        System.out.println(sysUser);
    }
}