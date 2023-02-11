<template>
  <div class="app-container">
    <!--查询-->
    <el-row>
      <el-col :span="20" >
        <el-input style="width:200px;" v-model="tableQuery.roleNme" placeholder="角色名"></el-input>
        <span style="margin-right: 15px;"></span>
        <el-input style="width:200px;" v-model="tableQuery.roleCode" placeholder="角色值"></el-input>
        <span style="margin-right: 15px;"></span>
        <el-tooltip content="搜索" placement="top">
          <el-button type="primary" icon="el-icon-search" circle @click="fetchData(1)"></el-button>
        </el-tooltip>
      </el-col>
      <el-col :span="4" >
        <el-button type="primary" icon="el-icon-plus"  @click="handleCreate">{{textMap.create}}</el-button>
      </el-col>
    </el-row>
    <div style="margin-bottom: 30px;"></div>
    <!--列表-->
    <el-table style="width: 100%"
              :data="tableData"
              border fit highlight-current-row>
      <el-table-column prop="rid" label="角色id" width="80"></el-table-column>
      <el-table-column prop="roleName" label="角色名" ></el-table-column>
      <el-table-column prop="roleDescribe" label="角色描述" ></el-table-column>
      <el-table-column prop="roleCode" label="角色值" ></el-table-column>
      <el-table-column prop="createTime" label="创建时间" >
        <template slot-scope="scope">
          <span>{{parseTime(scope.row.createTime)}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" >
        <template slot-scope="scope">
          <span>{{parseTime(scope.row.updateTime)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-tooltip content="编辑" placement="top">
            <el-button @click="handleUpdate(scope.row)" size="medium" type="info" icon="el-icon-edit" circle plain></el-button>
          </el-tooltip>
          <el-tooltip content="修改权限" placement="top" v-if="!hasAdminRole(scope.row)">
            <el-button @click="handleUpdateAuth(scope.row)" size="medium" type="warning" icon="el-icon-view" circle plain></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top" v-if="!hasAdminRole(scope.row)">
            <el-button @click="handleDelete(scope.row)" size="medium" type="danger" icon="el-icon-delete" circle plain></el-button>
          </el-tooltip>
          <el-popover trigger="hover" placement="top" v-else style="display: inline-block;">
            <el-alert type="warning" :closable="false" title="权限说明">
              <div>为保证管理员在系统中的最高权限</div>
              <div>不允许编辑管理员自身的权限</div>
              <div>不允许删除管理员角色</div>
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
    <!--弹出窗口：编辑角色-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="30%">
      <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="150px" >
        <el-form-item label="角色名" prop="roleName" >
          <el-input v-model="temp.roleName"></el-input>
        </el-form-item>
        <el-form-item label="角色值" prop="roleCode" v-if="dialogStatus=='create'">
          <el-input v-model="temp.roleCode"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="roleDescribe">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入" v-model="temp.roleDescribe">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">创建</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>
    <!--弹出窗口：匹配权限-->
    <el-dialog :title="authData.roleName" :visible.sync="editAuthsDialogVisible" width="50%">
      <div>
        <el-checkbox :indeterminate="isIndeterminate"  @change="handleCheckAllChange">全选</el-checkbox>
        <div style="margin: 15px 0;"></div>
        <el-checkbox-group v-model="authData.aids">
          <el-checkbox class="role-checkbox" v-for="auth in authOptions" :label="auth.aid"  :key="auth.aid">
            {{auth.authName}}
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editAuthsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateAuth">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import { parseTime, resetTemp } from 'src/utils'
  import { pageParamNames, confirm, root } from 'src/utils/constants'
  import debounce from 'lodash/debounce'
  import http from '../../../assets/js/http'

  export default {
    name: 'RoleManage',
    data() {
      return {
        tableData: [],
        tableQuery:{
          roleName: "",
          roleCode: ""
        },
        tablePage: {
          current: null,
          pages: null,
          size:null,
          total: null
        },
        dialogFormVisible: false,
        dialogStatus: '',
        temp: {
          rid: null,
          roleName: "",
          roleDescribe: "",
          roleCode: "",
          createTime: null,
          updateTime: null
        },
        authData:{
          roleName:"",
          rid: null,
          aids: []
        },
        isIndeterminate: true,
        authOptions:[],
        editAuthsDialogVisible: false,
        textMap: {
          update: '编辑角色',
          create: '新增角色'
        },
        rules: {
          roleName: [{ required: true, message: '必填', trigger: 'blur' }],
          roleCode: [{ required: true, message: '必填', trigger: 'blur' }]
        },
      }
    },

    created(){
      this.fetchData()
      this.initData()
    },

    watch:{
      //延时查询
      'tableQuery.roleName': debounce( function(){
        this.fetchData()
      },500),
      'tableQuery.roleCode': debounce( function(){
        this.fetchData()
      },500),
    },//watch

    methods: {
      parseTime,
      hasAdminRole(row){
        return row && row.roleCode=='super'
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
          roleName: this.tableQuery.roleName,
          roleCode: this.tableQuery.roleCode,
          current: this.tablePage.current,
          size: this.tablePage.size
        }
        this.apiPost('sys_role/query', data).then((res) => {
          this.handelResponse(res, (data) => {
            this.tableData = res.page.records
            pageParamNames.forEach(name => this.$set(this.tablePage, name, res.page[name]))
          })
        })
      },

      //更新
      handleUpdate(row) {
        this.temp = Object.assign({}, row) // copy obj
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => this.$refs['dataForm'].clearValidate())
      },
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (!valid) return;
          this.apiPost('sys_role/update', this.temp).then((res) => {
            this.handelResponse(res, (data) => {
              this.dialogFormVisible = false
              this.$message.success("更新成功")
              this.fetchData()
            })
          })
        })
      },

      //删除
      handleDelete(row) {
        this.$confirm('您确定要永久删除该用户？', '提示', confirm).then(() => {
          this.apiPost('sys_role/del', {rid: row.rid}).then((res) => {
            this.handelResponse(res, (data) => {
              this.$message.success('删除成功')
              this.fetchData()
            })
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      //新增
      handleCreate() {
        resetTemp(this.temp)
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => this.$refs['dataForm'].clearValidate())
      },
      createData() {
        this.$refs['dataForm'].validate((valid) => {
          if (!valid) return;
          this.apiPost('sys_role/add', this.temp).then((res) => {
            this.handelResponse(res, (data) => {
              this.dialogFormVisible = false
              this.$message.success("添加成功")
              this.fetchData()
            })
          })
        })
      },
      //更新用户的权限
      handleUpdateAuth( row) {
        // 显示用户的权限
        this.authData = {
          roleName: row.roleName,
          rid: row.rid,
          aids: row.auths.map(auth=>auth.aid)
        }
        // 显示弹窗
        this.editAuthsDialogVisible = true
      },
      updateAuth() {
        const noRolesSelected = this.authData && this.authData.aids && this.authData.aids.length ==0;
        if(noRolesSelected){
          this.$confirm('当前没有选中任何权限，会清除该用户已有的权限, 是否继续?', '提示', confirm).then(() => {
            this.authMethod()
          }).catch(() => {
            this.$message("已取消编辑用户权限");
          });
        }else{
          this.authMethod()
        }
      },
      authMethod(){
        this.apiPost('sys_role/auth',this.authData).then((res) => {
          this.handelResponse(res, (data) => {
            this.editAuthsDialogVisible = false
            this.fetchData()
            this.$message.success("权限已更新");
          })
        })
      },
      //全选
      handleCheckAllChange(val) {
        let allAids = this.authOptions.map(auth => auth.aid)
        this.authData.aids = val ? allAids : [];
        this.isIndeterminate = false;
      },
      initData(){
        //所有权限选项
        this.apiPost('sys_auth/allAuth').then((res) => {
          this.handelResponse(res, (data) => {
            this.authOptions = res.list
            console.log(this.authOptions)
          })
        })
      },
    },
    mixins: [http]
  }
</script>


