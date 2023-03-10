package com.example.myluck.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SysRole {

    @TableField(exist = false)
    private List<SysAuth> auths = new ArrayList<>();
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.rid
     *
     * @mbggenerated
     */
    @TableId(type = IdType.AUTO)
    private Integer rid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.role_name
     *
     * @mbggenerated
     */
    private String roleName;

    private String roleCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.role_describe
     *
     * @mbggenerated
     */
    private String roleDescribe;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;


}