<template>
  <div >
    <el-row>
      <el-col :span="20" >
        <el-input style="width:200px;" v-model="tableQuery.nick" placeholder="姓名"></el-input>
        <span style="margin-right: 15px;"></span>
        <el-tooltip content="搜索" placement="top" >
          <el-button type="primary" icon="el-icon-search" circle @click="fetchData(1)" ></el-button>
        </el-tooltip>
      </el-col>
      <el-col :span="4" >
        <el-button type="primary" icon="el-icon-plus"  @click="handleCreate" >新增用户</el-button>
      </el-col>
    </el-row>
    <div style="margin-bottom: 30px;"></div>

    <div style="margin-bottom: 30px;"></div>
    <el-table style="width: 100%"
              :data="tableData"
              element-loading-text="加载中"
              border fit highlight-current-row>
      <el-table-column prop="uid" label="用户id" width="80"></el-table-column>
      <el-table-column prop="userName" label="登录名" width="140"></el-table-column>
      <el-table-column prop="realName" label="姓名" width="140"></el-table-column>
      <el-table-column label="角色" >
        <template slot-scope="scope">
          <el-tag style="margin: 2px;" v-for="role in scope.row.roles" :key="role.rid">{{role.roleName}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template slot-scope="scope">
          <span v-text="parseTime(scope.row.createTime)"></span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-tooltip content="编辑" placement="top">
            <el-button @click="handleUpdate(scope.row)" size="medium" type="info" icon="el-icon-edit" circle plain></el-button>
          </el-tooltip>
          <el-tooltip content="修改角色" placement="top" v-if="!hasAdminRole(scope.row)">
            <el-button @click="handleUpdateUserRoles(scope.row)" size="medium" type="warning" icon="el-icon-star-off" circle plain></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top" v-if="!hasAdminRole(scope.row)">
            <el-button @click="handleDelete(scope.row)" size="medium" type="danger" icon="el-icon-delete" circle plain></el-button>
          </el-tooltip>
          <el-popover trigger="hover" placement="top" v-else style="display: inline-block;">
            <el-alert type="warning" :closable="false" title="权限说明">
              <div>为保证管理员在系统中的最高权限</div>
              <div>不允许编辑管理员自身的角色</div>
              <div>不允许删除管理员账号</div>
            </el-alert>
            <div slot="reference" >
              <el-tag style="margin-left: 10px;" type="info">权限说明</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-bottom: 30px;"></div>
    <!--分页-->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="tablePage.current"
      :page-sizes="[3,5,10, 20, 30, 40, 50]"
      :page-size="tablePage.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tablePage.total">
    </el-pagination>

    <!--弹出窗口：新增/编辑用户-->
    <el-dialog title="新增用户" :visible.sync="dialogFormVisible" width="50%">
      <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="120px">

        <el-form-item label="登录名" prop="userName" >
          <el-input v-model="temp.userName"></el-input>
        </el-form-item>

        <el-form-item label="姓名" prop="realName">
          <el-input v-model="temp.realName"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="pwd">
          <el-input type="password" v-model="temp.pwd"></el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmp">
          <el-input type="password" v-model="temp.confirmp"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="createData">创建</el-button>
      </div>
    </el-dialog>
    
    <el-dialog title="修改用户" :visible.sync="dialogFormUpdate" width="50%">
      <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="120px">

        <el-form-item label="姓名" prop="realName">
          <el-input v-model="temp.realName"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="pwd">
          <el-input type="password" v-model="temp.pwd"></el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmp">
          <el-input type="password" v-model="temp.confirmp"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormUpdate = false">取消</el-button>
        <el-button type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>
    <!--弹出窗口：修改用户角色-->
    <el-dialog :title="updateUserRolesData.uname" :visible.sync="editRolesDialogVisible" width="50%">
      <div>
        <el-checkbox :indeterminate="isIndeterminate"  @change="handleCheckAllChange">全选</el-checkbox>
        <div style="margin: 15px 0;"></div>
        <el-checkbox-group v-model="updateUserRolesData.rids">
          <el-checkbox class="role-checkbox" v-for="role in roleOptions" :label="role.rid"  :key="role.rid">
            {{role.roleName}}
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editRolesDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="checkUpdateUserRolesData">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import {parseTime, resetTemp} from 'src/utils'
  import {confirm,pageParamNames} from 'src/utils/constants'
  import debounce from 'lodash/debounce'
  import http from '../../../assets/js/http'

  export default {

    name: 'UserManage',

    data() {
      let validateName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('必填'));
        } else {
          callback();
        }
      };

      let validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.temp.confirmp !== '') {
            this.$refs.dataForm.validateField('confirmp');
          }
          callback();
        }
      };

      let validateconfirmp = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value != this.temp.pwd) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };

      return {
        parseTime: parseTime,
        tableData: [],
        tableQuery: {
          nick: ""
        },
        tablePage: {
          current: null,
          pages: null,
          size: null,
          total: null
        },
        dialogFormVisible: false,
        dialogFormUpdate: false,
        editRolesDialogVisible: false,
        dialogStatus: '',
        temp: {
          uid: null,
          userName: null,
          realName: null,
          pwd: null,
          confirmp: null,
          password:""
        },
        updateObj: {
          uid: null,
          userName: null,
          realName: null,
          updatePwd: null,
          updateConfirmp: null,
          password:""
        },
        rules: {
          userName: [{validator: validateName, trigger: 'blur'}],
          pwd: [{validator: validatePass, trigger: 'change'}],
          confirmp: [{validator: validateconfirmp, trigger: 'change'}]

        },
        isIndeterminate: true,
        //所有角色(管理员除外)
        roleOptions:[],
        roleMap: new Map(),
        // 更新用户的角色的数据
        updateUserRolesData: {
          uname:"",
          uid: null,
          rids: []
        },
      }

    },

    created() {
       this.initData()
       this.fetchData()
    },

    watch: {
      //延时查询
      'tableQuery.nick': debounce(function () {
         this.fetchData()
      }, 500)
    },//watch

    methods: {



      hasAdminRole(row){
        if(row && row.roles){
          return row.roles.some(role=>role.roleCode=='super')
        }
        return false
      },

      //分页
      handleSizeChange(val) {
        this.tablePage.size = val;
        this.fetchData();
      },
      handleCurrentChange(val) {
        this.tablePage.current = val;
        this.fetchData();
      },

      //查询
      fetchData(current) {
        if(current){
          this.tablePage.current = current
        }
        let data = {
          nick :this.tableQuery.nick,
          current: this.tablePage.current,
          size: this.tablePage.size
        }
        this.apiPost('sys_user/query', data).then((res) => {
          this.handelResponse(res, (data) => {
            this.tableData = res.page.records

            pageParamNames.forEach(name => this.$set(this.tablePage, name, res.page[name]))
          })
        })
      },
      //更新
      handleUpdate(row) {
        this.temp.realName = row.realName
        this.temp.uid = row.uid
        this.temp.pwd = null
        this.temp.confirmp = null
        this.temp.password = row.password
        this.dialogFormUpdate = true
        this.$nextTick(() => this.$refs['dataForm'].clearValidate())
      },
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (!valid) return;
          this.temp.password = this.temp.pwd
          this.apiPost('sys_user/update', this.temp).then((res) => {
            this.handelResponse(res, (data) => {
              this.dialogFormUpdate = false
              this.$message.success("更新成功")
              this.fetchData()
            })
          })
        })
      },
      //新增
      handleCreate() {
        resetTemp(this.temp)
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      createData() {
        this.$refs['dataForm'].validate((valid) => {
          if (!valid) return;
          this.temp.password = this.temp.pwd
          this.apiPost('sys_user/add', this.temp).then((res) => {
            this.handelResponse(res, (data) => {
              this.dialogFormVisible = false
              this.$message.success("添加成功")
              this.fetchData()
            })
          })
        })
      },

      //更新用户的角色
      handleUpdateUserRoles(row) {
        // 显示用户的角色
        this.updateUserRolesData = {
          uname: row.userName,
          uid: row.uid,
          rids: row.roles.map(role=>role.rid)
        }
        // 显示弹窗
        this.editRolesDialogVisible = true
      },
      checkUpdateUserRolesData() {
        const noRolesSelected = this.updateUserRolesData && this.updateUserRolesData.rids && this.updateUserRolesData.rids.length ==0;
        if(noRolesSelected){
          this.$confirm('当前没有选中任何角色，会清除该用户已有的角色, 是否继续?', '提示', confirm).then(() => {
            this.invokeUpdateUserRolesApi()
          }).catch(() => {
            this.$message("已取消编辑用户角色");
          });
        }else{
          this.invokeUpdateUserRolesApi()
        }
      },

      invokeUpdateUserRolesApi(){
          this.apiPost('sys_user/role',this.updateUserRolesData).then((res) => {
            this.handelResponse(res, (data) => {
              this.editRolesDialogVisible = false
              this.fetchData()
            })
          })
      },
      //全选
      handleCheckAllChange(val) {
        let allRids = this.roleOptions.map(role => role.rid)
        this.updateUserRolesData.rids = val ? allRids : [];
        this.isIndeterminate = false;
      },
      initData(){
        //所有角色选项
        this.apiPost('sys_role/all').then((res) => {
          this.handelResponse(res, (data) => {
            this.roleOptions = res.list
            console.log(this.roleOptions)
          })
        })

      },
      //删除
      handleDelete(row) {
        this.$confirm('您确定要永久删除该用户？', '提示', confirm).then(() => {
          this.apiPost('sys_user/del', {uid: row.uid}).then((res) => {
            this.handelResponse(res, (data) => {
              this.fetchData()
            })
          })
        }).catch(() => {
          this.$message.info("已取消删除")
        });

      },



    },
    mixins: [http]
  }
</script>

<!--<style rel="stylesheet/scss" lang="scss" scoped>
  .role-checkbox{
    margin-left: 0px;
    margin-right: 15px;
  }
</style>-->
