package com.example.myluck.dao;

import com.example.myluck.entity.SysRole;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SysRoleMapperTest {
    @Autowired
    SysRoleMapper sysRoleMapper;

    @Test
    void findRolesByUid() {
        List<SysRole> rolesByUid = sysRoleMapper.findRolesByUid(1);
        System.out.println(rolesByUid);
    }
}