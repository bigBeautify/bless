package com.example.myluck.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myluck.dao.SysRoleAuthMapper;
import com.example.myluck.entity.SysRoleAuth;
import com.example.myluck.service.SysRoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SysRoleAuthServiceImpl extends ServiceImpl<SysRoleAuthMapper, SysRoleAuth> implements SysRoleAuthService {
    @Autowired
    SysRoleAuthMapper sysRoleAuthMapper;

}
