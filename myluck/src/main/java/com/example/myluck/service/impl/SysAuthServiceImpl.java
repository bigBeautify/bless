package com.example.myluck.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myluck.dao.SysAuthMapper;
import com.example.myluck.entity.SysAuth;
import com.example.myluck.entity.SysUser;
import com.example.myluck.service.SysAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysAuthServiceImpl extends ServiceImpl<SysAuthMapper, SysAuth> implements SysAuthService{
    @Autowired
    SysAuthMapper sysAuthMapper;
    @Override
    public JSONArray getAuthByUid(int uid) {

        List<SysAuth> list = sysAuthMapper.getAuthByUid(uid);
        //获取父id
        int[] pids = list.stream().mapToInt(SysAuth::getPid).distinct().sorted().toArray();
        JSONArray arr = new JSONArray();
        for (int pid: pids) {
            JSONObject obj = new JSONObject();
            QueryWrapper<SysAuth> sw = new QueryWrapper<>();
            sw.eq("aid",pid);
            SysAuth one = getOne(sw);
            obj.put("root",one);
            List<SysAuth> collect = list.stream().filter(item -> item.getPid() == pid).collect(Collectors.toList());
            obj.put("children",collect);
            arr.add(obj);
        }

        return arr;
    }

    @Override
    public int insert(SysAuth record) {
        int insert = sysAuthMapper.insert(record);
        Integer aid = record.getAid();
        return aid;
    }
}
