package com.example.myluck.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myluck.entity.SysUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserService extends IService<SysUser> {

    SysUser getUserByAccount(String username);
    SysUser getUserByAccount(SysUser sysUser);

    IPage<SysUser> selectUserIncludeRoles(Page page,String userName);

}
