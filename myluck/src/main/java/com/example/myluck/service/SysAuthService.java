package com.example.myluck.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myluck.entity.SysAuth;
import com.example.myluck.entity.SysUser;

import java.util.List;

public interface SysAuthService extends IService<SysAuth> {
    JSONArray getAuthByUid(int uid);
    int insert(SysAuth record);


}
