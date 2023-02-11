<template>
  <div class="app-container">
    <!--查询-->
    <el-row>
      <el-col :span="20" >
        <el-input style="width:200px;" v-model="tableQuery.authName" placeholder="权限名"></el-input>
        <el-select v-model="tableQuery.pid"  placeholder="请选择父节点" >
          <el-option>请选择</el-option>
          <el-option
              v-for="item in parents"
              :key="item.aid"
              :label="item.authName"
              :value="item.aid">
          </el-option>
        </el-select>
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
      <el-table-column prop="aid" label="权限id" width="80"></el-table-column>
      <el-table-column prop="authName" label="权限名" ></el-table-column>
      <el-table-column prop="authDescribe" label="角色描述" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.pid == 0">根节点</el-tag>
          <span v-else> {{scope.row.authDescribe}} </span>
        </template>
      </el-table-column>
      <el-table-column prop="pid" label="父节点" :formatter="father"></el-table-column>
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
          <el-tooltip content="删除" placement="top" >
            <el-button @click="handleDelete(scope.row)" size="medium" type="danger" icon="el-icon-delete" circle plain></el-button>
          </el-tooltip>
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
        <el-form-item label="权限名" prop="authName" >
          <el-input v-model="temp.authName"></el-input>
        </el-form-item>
        <el-form-item label="title" prop="title" >
          <el-input v-model="temp.title"></el-input>
        </el-form-item>
        <el-form-item label="父节点" prop="pid" >
          <el-select v-model="temp.pid"  placeholder="请选择" @change="parentChange">
            <el-option
                v-for="item in parents"
                :key="item.aid"
                :label="item.authName"
                :value="item.aid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="url" prop="url" >
          <el-input v-model="temp.url"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="authDescribe">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入" v-model="temp.authDescribe">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">创建</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import { parseTime, resetTemp } from 'src/utils'
  import { pageParamNames, confirm, root } from 'src/utils/constants'
  import debounce from 'lodash/debounce'
  import http from '../../../../assets/js/http'

  export default {
    name: 'authManage',
    data() {
      return {
        tableData: [],
        tableQuery:{
          authName: "",
          pid:""
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
          pid:"",
          rid: null,
          url:"",
          authName: "",
          title:"",
          authDescribe: "",
          createTime: null,
          updateTime: null
        },
        textMap: {
          update: '编辑角色',
          create: '新增角色'
        },
        parents:[],
        rules: {
          authName: [{ required: true, message: '必填', trigger: 'blur' }],
        },
      }
    },

    created(){
      this.fetchData()
      this.init()
    },

    watch:{
      //延时查询
      'tableQuery.authName': debounce( function(){
        this.fetchData()
      },500),
      'tableQuery.pid': debounce( function(){
        this.fetchData()
      },500)
    },//watch

    methods: {
      parseTime,
      parentChange(val){
        console.log(val)
      },
      init(){
        this.apiPost('sys_auth/parent').then((res) => {
          this.handelResponse(res, (data) => {
            this.parents = res.list
          })
        })
      },
      father(row){
        let hh = ""
        this.parents.forEach(auth=>{
          if(auth.aid == row.pid){
            hh = auth.authName
          }
        })
        return hh
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
          authName: this.tableQuery.authName,
          pid: this.tableQuery.pid,
          current: this.tablePage.current,
          size: this.tablePage.size
        }
        this.apiPost('sys_auth/query', data).then((res) => {
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
          this.apiPost('sys_auth/update', this.temp).then((res) => {
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
          this.apiPost('sys_auth/del', {aid: row.aid}).then((res) => {
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
          this.apiPost('sys_auth/add', this.temp).then((res) => {
            this.handelResponse(res, (data) => {
              this.dialogFormVisible = false
              this.$message.success("添加成功")
              this.fetchData()
            })
          })
        })
      },
    },
    mixins: [http]
  }
</script>


