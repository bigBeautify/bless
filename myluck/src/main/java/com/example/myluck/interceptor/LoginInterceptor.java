package com.example.myluck.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myluck.contants.Constant;
import com.example.myluck.entity.SysUser;
import com.example.myluck.service.SysUserService;
import com.example.myluck.util.EncryptUtil;
import com.example.myluck.vo.Json;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private SysUserService sysUserService;
	
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("请求url:" + request.getRequestURI());
		System.out.println("header auth key:" + request.getHeader(Constant.AUTH_KEY));
		String authKey = request.getHeader(Constant.AUTH_KEY);
		String sessionId = request.getHeader(Constant.SESSION_ID);
		// 校验sessionid和authKey
		if(StringUtils.isEmpty(authKey) || StringUtils.isEmpty(sessionId)) {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write( Json.fail("检查是否登录", "authKey或sessionId不能为空").toString());
			writer.flush();
			return false;
		} 
		//检查账号有效性
		String decryptAuthKey = EncryptUtil.decryptBase64(authKey, Constant.SECRET_KEY);
		String[]  auths = decryptAuthKey.split("\\|");
		String username = auths[0];
		String password = auths[1];
		QueryWrapper<SysUser> qw = new QueryWrapper<>();
		qw.eq("user_name",username);
		qw.eq("password",password);
		SysUser sessionUser = sysUserService.getOne(qw);
		if(sessionUser == null ) {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write(Json.fail("检查是否登录", "账号已被删除").toString());
			writer.flush();
			return false;
		}
		return true;
	}
}
