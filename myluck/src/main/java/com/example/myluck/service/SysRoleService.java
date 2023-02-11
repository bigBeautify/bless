package com.example.myluck.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myluck.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    List<SysRole> findRolesByUid(int uid);

    IPage<SysRole> selectRoleIncludeAuths(Page page, String userName);

}
