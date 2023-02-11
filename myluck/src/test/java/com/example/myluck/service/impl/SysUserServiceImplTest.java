package com.example.myluck.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myluck.entity.SysUser;
import com.example.myluck.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SysUserServiceImplTest {
    @Autowired
    SysUserService sysUserService;

    @Test
    void selectUserIncludeRoles() {
        IPage<SysUser> page = sysUserService.selectUserIncludeRoles(new Page(1, 4), "");
        System.out.println(page);
    }
}