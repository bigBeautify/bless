package com.example.myluck.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.myluck.contants.Constant;
import com.example.myluck.entity.SysRole;
import com.example.myluck.entity.SysRoleAuth;
import com.example.myluck.entity.SysUserRole;
import com.example.myluck.service.SysRoleAuthService;
import com.example.myluck.service.SysRoleService;
import com.example.myluck.service.SysRoleService;
import com.example.myluck.service.SysUserRoleService;
import com.example.myluck.util.PageUtils;
import com.example.myluck.vo.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "角色接口", tags = {"角色接口"})
@RestController
@RequestMapping("/sys_role")
public class SysRoleController {
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysRoleAuthService sysRoleAuthService;
    @Autowired
    SysUserRoleService sysUserRoleService;

    @ApiOperation("所有角色")
    @PostMapping("/all")
    public Json query() {
        String oper = "query Role";
        List<SysRole> list = sysRoleService.list();
        List<SysRole> collect = list.stream().filter(item -> !Constant.SUPER.equals(item.getRoleCode())).collect(Collectors.toList());
        return Json.succ(oper).data("list", collect);
    }

    @ApiOperation("根据角色名字查询角色")
    @PostMapping("/query")
    public Json query(@RequestBody String body) {
        String oper = "query Role";
        JSONObject json = JSON.parseObject(body);
        String roleName = json.getString("roleName");
        IPage<SysRole> page = sysRoleService.selectRoleIncludeAuths(PageUtils.getPageParam(json), roleName);
        return Json.succ(oper).data("page", page);
    }

    @ApiOperation("添加角色")
    @PostMapping("/add")
    public Json add(@RequestBody String body) {
        String oper = "add sys Role";
        SysRole Role = JSON.parseObject(body, SysRole.class);
        Role.setCreateTime(new Date());
        boolean success = sysRoleService.save(Role);
        return Json.result(oper, success);
    }

    @ApiOperation("更新角色")
    @RequestMapping("/update")
    public Json update(@RequestBody String body) {
        String oper = "update Role";
        SysRole Role = JSON.parseObject(body, SysRole.class);
        Role.setUpdateTime(new Date());
        boolean b = sysRoleService.updateById(Role);
        return Json.result(oper, b);
    }

    @ApiOperation("删除角色")
    @RequestMapping("/del")
    public Json del(@RequestBody String body) {
        String oper = "del Role";
        SysRole Role = JSON.parseObject(body, SysRole.class);
        Integer rid = Role.getRid();
        boolean b = sysRoleService.removeById(rid);
        if(b){
            sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("role_id", rid));
        }
        return Json.result(oper, b);
    }

    @ApiOperation("根据角色id更新权限")
    @RequestMapping("/auth")
    public Json updateUserRole(@RequestBody String body) {
        String oper = "update role's auths";
        JSONObject json = JSON.parseObject(body);
        final String rid = json.getString("rid");
        List<String> aids = json.getJSONArray("aids").toJavaList(String.class);
        //删除：原来绑定的角色
        boolean deleteSucc = sysRoleAuthService.remove(new QueryWrapper<SysRoleAuth>().eq("role_id", rid));
        //更新：绑定新的角色
        List<SysRoleAuth> list = aids.stream().map(authId -> new SysRoleAuth(Integer.parseInt(rid), Integer.parseInt(authId),new Date())).collect(Collectors.toList());
        if (!aids.isEmpty()){
            boolean addSucc = sysRoleAuthService.saveBatch(list);
            return Json.result(oper, addSucc);
        }
        return Json.succ(oper);
    }
}
