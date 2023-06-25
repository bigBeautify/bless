<template>
	<el-dialog ref="dialog" :visible.sync="dialogForm" custom-class="w-400 h-300" title="修改密码">
		<div class="ovf-auto">
			<el-form ref="form" :model="form" :rules="rules" label-width="80px">
				<el-form-item label="新密码" prop="firstnewPwd">
					<el-input type="password" v-model.trim="form.firstnewPwd"></el-input>
				</el-form-item>
				<el-form-item label="新密码" prop="newPwd">
					<el-input type="password" v-model.trim="form.newPwd"></el-input>
				</el-form-item>
			</el-form>
		</div>
		<div class="p-t-20">
			<el-button type="primary" class="fl m-l-20" :disabled="disable" @click="submit()">提交</el-button>
		</div>
	</el-dialog>


</template>
<style>

</style>
<script>
  import http from '../../assets/js/http'

  export default {
    data() {
      let validateNew = (rule, value, callback) => {
        if (value === '' || value.trim() ==='') {
          callback(new Error('必填'));
        }else if(this.form.firstnewPwd != this.form.newPwd){
          callback(new Error('2次密码不一致'));
        }
        else {
          callback();
        }
      };
      let validatefirstNew = (rule, value, callback) => {
        if (value === '' || value.trim() ==='') {
          callback(new Error('必填'));
        }else if(this.form.firstnewPwd != this.form.newPwd){
          callback(new Error('2次密码不一致'));
        }
        else {
          callback();
        }
      };
      return {
        disable: false,
        dialogForm:false,
        form: {
          auth_key: '',
          firstnewPwd: '',
          newPwd: ''
        },
        rules: {
          firstnewPwd: [
            { required: true, validator: validatefirstNew, trigger: 'change' },
            { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
          ],
          newPwd: [
            { required: true, validator: validateNew, trigger: 'change' },
            { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      open() {
        this.dialogForm = true
      },
      close() {
        this.dialogForm = false
      },
      submit() {
        this.$refs.form.validate((pass) => {
          if (pass) {
            this.disable = !this.disable
            let data={
              newPwd: this.form.newPwd
            }
            this.apiPost('admin/changePassword', data).then((res) => {
              this.handelResponse(res, (data) => {
                this.$message.success("修改成功")
                Lockr.rm('authKey')
                Lockr.rm('authList')
                Lockr.rm('sessionId')
                setTimeout(() => {
                  router.replace('/')
                }, 1500)
              }, () => {
                this.disable = !this.disable
              })
            })
          }
        })
      }
    },
    created() {
      this.form.auth_key = Lockr.get('authKey')
    },
    mixins: [http]
  }
</script>