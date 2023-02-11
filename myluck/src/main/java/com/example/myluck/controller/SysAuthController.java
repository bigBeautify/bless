package com.example.myluck.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.myluck.contants.Constant;
import com.example.myluck.entity.SysAuth;
import com.example.myluck.entity.SysRole;
import com.example.myluck.entity.SysRoleAuth;
import com.example.myluck.entity.SysUserRole;
import com.example.myluck.service.SysAuthService;
import com.example.myluck.service.SysRoleAuthService;
import com.example.myluck.service.SysRoleService;
import com.example.myluck.util.PageUtils;
import com.example.myluck.vo.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "权限接口", tags = {"权限接口"})
@RestController
@RequestMapping("/sys_auth")
public class SysAuthController {
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysAuthService sysAuthService;
    @Autowired
    SysRoleAuthService sysRoleAuthService;

    @ApiOperation("查询所有权限")
    @PostMapping("/allAuth")
    public Json query() {
        String oper = "query Auth";
        List<SysAuth> list = sysAuthService.list();
        list= list.stream().filter(item->item.getPid() != 0).collect(Collectors.toList());
        return Json.succ(oper).data("list", list);
    }

    @ApiOperation("根据权限名称查询权限")
    @PostMapping("/query")
    public Json query(@RequestBody String body) {
        String oper = "query auth";
        JSONObject json = JSON.parseObject(body);
        String authName = json.getString("authName");
        String pid = json.getString("pid");
        QueryWrapper<SysAuth> sw = new QueryWrapper<>();
        sw.like("auth_name",authName);
        if(!StringUtils.isBlank(pid)){
            sw.eq("pid",pid);
        }
        IPage<SysAuth> page = sysAuthService.page(PageUtils.getPageParam(json), sw);
        return Json.succ(oper).data("page", page);
    }

    @ApiOperation("查询父菜单节点")
    @PostMapping("/parent")
    public Json parent() {
        String oper = "parent node";
        QueryWrapper<SysAuth> sw = new QueryWrapper<>();
        sw.eq("pid",0);
        List<SysAuth> temp = new ArrayList<>();
        SysAuth sysAuth = new SysAuth();
        sysAuth.setAid(0);
        sysAuth.setAuthName("根节点");
        temp.add(sysAuth);
        List<SysAuth> list = sysAuthService.list(sw);
        temp.addAll(list);
        return Json.succ(oper).data("list", temp);
    }

    @ApiOperation("添加权限")
    @PostMapping("/add")
    public Json add(@RequestBody String body) {
        String oper = "add sys Auth";
        SysAuth auth = JSON.parseObject(body, SysAuth.class);
        auth.setCreateTime(new Date());
        Integer aid = sysAuthService.insert(auth);

        Integer pid = auth.getPid();
        if(pid != 0){ //把该权限添加到超级角色中
            List<SysRole> list = sysRoleService.list(new QueryWrapper<SysRole>().eq("role_code", Constant.SUPER));
            Integer rid = list.get(0).getRid();
            sysRoleAuthService.save(new SysRoleAuth(rid,aid,new Date()));
        }
        return Json.result(oper, true);
    }

    @ApiOperation("更新权限")
    @RequestMapping("/update")
    public Json update(@RequestBody String body) {
        String oper = "update Auth";
        SysAuth auth = JSON.parseObject(body, SysAuth.class);
        auth.setUpdateTime(new Date());
        boolean b = sysAuthService.updateById(auth);
        return Json.result(oper, b);
    }

    @ApiOperation("删除权限")
    @RequestMapping("/del")
    public Json del(@RequestBody String body) {
        String oper = "del Auth";
        SysAuth auth = JSON.parseObject(body, SysAuth.class);
        boolean b = sysAuthService.removeById(auth.getAid());
        if(b){
            sysRoleAuthService.remove(new QueryWrapper<SysRoleAuth>().eq("auth_id", auth.getAid()));
        }
        return Json.result(oper, b);
    }
}
