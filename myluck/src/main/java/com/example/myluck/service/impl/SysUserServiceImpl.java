package com.example.myluck.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myluck.dao.SysRoleMapper;
import com.example.myluck.dao.SysUserMapper;
import com.example.myluck.entity.SysRole;
import com.example.myluck.entity.SysUser;
import com.example.myluck.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Override
    public SysUser getUserByAccount(String username) {
        SysUser user = getOne(new QueryWrapper<SysUser>().eq("user_name", username));
        return user;
    }

    @Override
    public SysUser getUserByAccount(SysUser sysUser) {
        QueryWrapper<SysUser> sw = new QueryWrapper<>();
        sw.eq("user_name",sysUser.getUserName());
        sw.eq("password",sysUser.getPassword());
        List<SysUser> list = sysUserMapper.selectList(sw);
        if(list != null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public IPage<SysUser> selectUserIncludeRoles(Page page, String userName) {
        QueryWrapper<SysUser> sw = new QueryWrapper<>();
        sw.like("user_name",userName);
        Page page1 = sysUserMapper.selectPage(page, sw);
        List<SysUser> records = page1.getRecords();
        if(records != null && records.size()>0){
            records.stream().forEach(item ->{
                Integer uid = item.getUid();
                List<SysRole> roles = sysRoleMapper.findRolesByUid(uid);
                item.setRoles(roles);
            });
        }
        return page1;

    }
}
