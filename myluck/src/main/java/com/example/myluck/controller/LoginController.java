package com.example.myluck.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myluck.contants.Constant;
import com.example.myluck.entity.SysRole;
import com.example.myluck.entity.SysUser;
import com.example.myluck.service.SysAuthService;
import com.example.myluck.service.SysRoleService;
import com.example.myluck.service.SysUserService;
import com.example.myluck.util.EncryptUtil;
import com.example.myluck.util.Md5Utils;
import com.example.myluck.vo.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录控制层
 * @author leo.aqing
 *
 */
@Api(value = "登录接口", tags = {"登录接口"})
@RestController
@RequestMapping("/admin")
public class LoginController{
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysAuthService sysAuthService;

	/**
	 * 登录
	 * @param record
	 * @param request
	 * @return
	 */
	@ApiOperation("登录方法")
	@PostMapping(value = "/login", produces = {"application/json;charset=UTF-8"})
	public Json login(@RequestBody SysUser record, HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		if(StringUtils.isBlank(record.getUserName())) {
			return Json.fail("账号不能为空", "账号不能为空") ;
		}
		try {
			record.setPassword(Md5Utils.md5(record.getPassword()));
		} catch (Exception e) {
			return Json.fail("", "帐号与密码错误不正确");
		}
		SysUser user = sysUserService.getUserByAccount(record);
		if(user == null) {
			return Json.fail("", "帐号与密码错误不正确");
		}
		String authKey = EncryptUtil.encryptBase64(user.getUserName()+"|"+user.getPassword(), Constant.SECRET_KEY);
		// 返回信息
		data.put("rememberKey", authKey);
		data.put("authKey", authKey);
		data.put("sessionId", request.getSession().getId());
		data.put("userInfo", user);
		List<SysRole> roleList = sysRoleService.findRolesByUid(user.getUid());
		data.put("roleList", roleList);
		data.put("menusList", sysAuthService.getAuthByUid(user.getUid()));

		return Json.succ("登录成功","data",data);
	}

	@ApiOperation("检查登录方法")
	@RequestMapping(value = "/relogin", produces = {"application/json;charset=UTF-8"})
	public Json relogin(String rememberKey,HttpServletRequest request) {
		String rememberValue = EncryptUtil.decryptBase64(rememberKey, Constant.SECRET_KEY);
		String[] splits = rememberValue.split("|");
		SysUser record = new SysUser();
		record.setUserName(splits[0]);
		record.setPassword(splits[1]);
		QueryWrapper<SysUser> qw = new QueryWrapper<>();
		qw.eq("user_name", splits[0]);
		qw.eq("password", splits[1]);
		SysUser user = sysUserService.getOne(qw);
		if(user == null) {
			return Json.fail("", "帐号与密码错误不正确");
		}
		return Json.succ("登录成功","登录成功");
	}


	@ApiOperation("登出方法")
	@PostMapping(value = "/logout", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json logout(HttpSession session){
		session.invalidate();
		return Json.succ("登出成功","登出成功");
	}


	@ApiOperation("修改密码")
	@PostMapping(value = "/changePassword", produces = "application/json;charset=UTF-8")
	public Json changePassword(HttpServletRequest request,@RequestBody String body) {
		JSONObject json = JSON.parseObject(body);
		String newPwd = json.getString("newPwd");
		String authKey = request.getHeader(Constant.AUTH_KEY);
		if (StringUtils.isNotBlank(authKey)) {
			String decryptAuthKey = EncryptUtil.decryptBase64(authKey, Constant.SECRET_KEY);
			String[] auths = decryptAuthKey.split("\\|");
			String username = auths[0];
			String password = auths[1];
			QueryWrapper<SysUser> qw = new QueryWrapper<>();
			qw.eq("user_name", username);
			qw.eq("password", password);
			SysUser currentUser = sysUserService.getOne(qw);
			if (currentUser == null) {
				return Json.fail("", "用户没登录，请登录后修改密码");
			}
			SysUser record = new SysUser();
			record.setUid(currentUser.getUid());
			String ss ;
			try {
				ss = Md5Utils.md5(newPwd);;
				record.setPassword(ss);
			} catch (Exception e) {
				return Json.fail("", "新密码设置失败");
			}
			record.setUpdateTime(new Date());
			sysUserService.updateById(record);
			String newauthKey = EncryptUtil.encryptBase64(currentUser.getUserName() + "|" + ss, Constant.SECRET_KEY);
			return Json.succ("更新密码").data("authKey", newauthKey);
		}
		return Json.fail("", "用户没登录，请登录后修改密码");
	}
}
