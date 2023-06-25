package com.example.myluck.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.myluck.entity.SysRole;
import com.example.myluck.entity.SysUser;
import com.example.myluck.entity.SysUserRole;
import com.example.myluck.service.SysUserRoleService;
import com.example.myluck.service.SysUserService;
import com.example.myluck.util.Md5Utils;
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

@Api(value = "用户接口", tags = {"用户接口"})
@RestController
@RequestMapping("/sys_user")
public class SysUserController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserRoleService sysUserRoleService;

    @GetMapping("")
    public Json test(){
        return Json.succ("测试","user","user");
    }
    @ApiOperation("根据名字查用户")
    @RequestMapping("/find/{name}")
    public Json findUserByName(@PathVariable String name){
        String oper = "find user roles";
        if (StringUtils.isBlank(name)){
            return Json.fail(oper, "无法查询当名字：参数为空（用户username）");
        }
        SysUser user = sysUserService.getUserByAccount(name);
        return Json.succ(oper,"user",user);
    }


    @ApiOperation("根据条件查询用户")
    @PostMapping("/query")
    public Json query(@RequestBody String body) {
        String oper = "query user";
        JSONObject json = JSON.parseObject(body);
        String userName = json.getString("nick");
        IPage<SysUser> page = sysUserService.selectUserIncludeRoles(PageUtils.getPageParam(json), userName);
        return Json.succ(oper).data("page", page);
    }


    @ApiOperation("新增用户")
    @PostMapping("/add")
    public Json add(@RequestBody String body) {
        String oper = "add sys user";
        SysUser user = JSON.parseObject(body, SysUser.class);
        if (StringUtils.isEmpty(user.getUserName())) {
            return Json.fail(oper, "用户帐号名不能为空");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return Json.fail(oper, "密码不能为空");
        }
        try {
            user.setPassword(Md5Utils.md5(user.getPassword()));
        } catch (Exception e) {
            return Json.fail("", "密码设置失败");
        }
        SysUser userDB = sysUserService.getOne(new QueryWrapper<SysUser>().eq("user_name", user.getUserName()));
        if (userDB != null) {
            return Json.fail(oper, "用户已注册");
        }
        user.setCreateTime(new Date());
        boolean success = sysUserService.save(user);
        return Json.result(oper, success);
    }

    @ApiOperation("更新用户")
    @RequestMapping("/update")
    public Json update(@RequestBody String body) {
        String oper = "update user";
        SysUser user = JSON.parseObject(body, SysUser.class);
        try {
            user.setPassword(Md5Utils.md5(user.getPassword()));
        } catch (Exception e) {
            return Json.fail("", "密码设置失败");
        }
        user.setUpdateTime(new Date());
        boolean b = sysUserService.updateById(user);
        return Json.result(oper, b);
    }

    @ApiOperation("删除用户")
    @RequestMapping("/del")
    public Json del(@RequestBody String body) {
        String oper = "del user";
        SysUser user = JSON.parseObject(body, SysUser.class);
        boolean b = sysUserService.removeById(user.getUid());
        return Json.result(oper, b);
    }

    @ApiOperation("根据用户id更新角色")
    @RequestMapping("/role")
    public Json updateUserRole(@RequestBody String body) {
        String oper = "update user's roles";
        JSONObject json = JSON.parseObject(body);
        final String uid = json.getString("uid");
        List<String> rids = json.getJSONArray("rids").toJavaList(String.class);
        //删除：原来绑定的角色
        boolean deleteSucc = sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", uid));
        //更新：绑定新的角色
        List<SysUserRole> list = rids.stream().map(roleId -> new SysUserRole(Integer.parseInt(uid), Integer.parseInt(roleId),new Date())).collect(Collectors.toList());
        if (!rids.isEmpty()){
            boolean addSucc = sysUserRoleService.saveBatch(list);
            return Json.result(oper, addSucc);
        }
        return Json.succ(oper);
    }
}
