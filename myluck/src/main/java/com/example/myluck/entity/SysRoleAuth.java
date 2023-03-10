package com.example.myluck.entity;

import lombok.Data;

import java.util.Date;
@Data
public class SysRoleAuth {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role_auth.role_auth_id
     *
     * @mbggenerated
     */
    private Integer roleAuthId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role_auth.role_id
     *
     * @mbggenerated
     */
    private Integer roleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role_auth.auth_id
     *
     * @mbggenerated
     */
    private Integer authId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role_auth.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    public SysRoleAuth(Integer roleId, Integer authId, Date createTime) {
        this.roleId = roleId;
        this.authId = authId;
        this.createTime = createTime;
    }
    public SysRoleAuth() {

    }
}