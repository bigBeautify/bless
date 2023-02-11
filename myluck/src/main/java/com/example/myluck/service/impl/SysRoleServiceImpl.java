package com.example.myluck.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myluck.dao.SysAuthMapper;
import com.example.myluck.dao.SysRoleAuthMapper;
import com.example.myluck.dao.SysRoleMapper;
import com.example.myluck.dao.SysUserMapper;
import com.example.myluck.entity.SysAuth;
import com.example.myluck.entity.SysRole;
import com.example.myluck.entity.SysRoleAuth;
import com.example.myluck.entity.SysUser;
import com.example.myluck.service.SysRoleService;
import com.example.myluck.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysRoleAuthMapper sysRoleAuthMapper;
    @Autowired
    SysAuthMapper sysAuthMapper;

    @Override
    public List<SysRole> findRolesByUid(int uid) {

        return sysRoleMapper.findRolesByUid(uid);
    }

    @Override
    public IPage<SysRole> selectRoleIncludeAuths(Page page, String userName) {
        QueryWrapper<SysRole> sw = new QueryWrapper<>();
        sw.like("role_name",userName);
        Page page1 = sysRoleMapper.selectPage(page, sw);
        List<SysRole> roles = page1.getRecords();
        roles.stream().forEach(role ->{
            Integer rid = role.getRid();
            QueryWrapper<SysRoleAuth> sw1 = new QueryWrapper<>();
            sw1.eq("role_id",rid);
            List<SysRoleAuth> sysRoleAuths = sysRoleAuthMapper.selectList(sw1);
            List<Integer> authIds = sysRoleAuths.stream().map(item -> item.getAuthId()).collect(Collectors.toList());
            List<SysAuth> sysAuths = new ArrayList<>();
            if(authIds != null && authIds.size()>0){
                QueryWrapper<SysAuth> sw2 = new QueryWrapper<>();
                sw2.in("aid",authIds);
                sysAuths = sysAuthMapper.selectList(sw2);
            }
            role.setAuths(sysAuths);
        });
        return page1;
    }
}
