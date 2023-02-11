package com.example.myluck.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myluck.entity.SysRole;
import com.example.myluck.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class SysRoleServiceImplTest {
    @Autowired
    SysRoleService sysRoleService;

    @Test
    void selectRoleIncludeAuths() {
        IPage<SysRole> sysRoleIPage = sysRoleService.selectRoleIncludeAuths(new Page(1, 5), "");
        System.out.println(sysRoleIPage);
    }
}