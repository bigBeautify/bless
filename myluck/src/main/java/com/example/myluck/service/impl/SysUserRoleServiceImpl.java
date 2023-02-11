package com.example.myluck.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myluck.dao.SysUserRoleMapper;
import com.example.myluck.entity.SysUserRole;
import com.example.myluck.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

}
