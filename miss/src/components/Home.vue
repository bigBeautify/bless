<template>
	<el-row class="panel m-w-1280">
		<el-col :span="24" class="panel-top">
			<el-col :span="20">
        <template v-if="logo_type == '1'">
          <img :src="img" class="logo">
        </template>
        <template v-else>
          <span class="p-l-20">{{title}}</span>
        </template>
			</el-col>
			<el-col :span="4" class="pos-rel">
				<el-dropdown @command="handleMenu" class="user-menu">
		      <span class="el-dropdown-link c-gra" style="cursor: default;">
		        {{userName}}&nbsp;&nbsp;<i class="fa fa-user" aria-hidden="true"></i>
		      </span>
		      <el-dropdown-menu slot="dropdown">
		        <el-dropdown-item command="changePwd">修改密码</el-dropdown-item>
		        <el-dropdown-item command="logout">退出</el-dropdown-item>
		      </el-dropdown-menu>
		    </el-dropdown>
			</el-col>
		</el-col>
		<el-col :span="24" class="panel-center">
			<!--<el-col :span="4">-->
			<aside class="w-180 ovf-hd" v-show="!showLeftMenu">
				<leftMenu :menuData="menuData" :menu="menu" ref="leftMenu"></leftMenu>
			</aside>
			<section class="panel-c-c" :class="{'hide-leftMenu': hasChildMenu}">
				<div class="grid-content bg-purple-light">
					<el-col :span="24">
						<transition name="fade" mode="out-in" appear>
							<router-view v-loading="showLoading"></router-view>
						</transition>
					</el-col>
				</div>
			</section>
		</el-col>

		<changePwd ref="changePwd"></changePwd>

	</el-row>
</template>

<script>
  import leftMenu from './Common/leftMenu.vue'
  import changePwd from './Account/changePwd.vue'
  import http from '../assets/js/http'

  export default {
    data() {
      return {
        userName: '',
        topMenu: [],
        childMenu: [],
        menuData: [],
        hasChildMenu: false,
        menu: null,
        module: null,
        img: '',
        title: '权限管理系统',
        logo_type: null,
        aa:'1111'
      }
    },
    methods: {
      logout() {
        this.$confirm('确认退出吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(() => {
         // _g.openGlobalLoading()
          let data = {
            authkey: Lockr.get('authKey'),
            sessionId: Lockr.get('sessionId')
          }
          debugger
          this.apiPost('admin/logout', data).then((res) => {
         //   _g.closeGlobalLoading()
            this.handelResponse(res, (data) => {
              debugger
              Lockr.rm('menus')
              Lockr.rm('authKey')
              Lockr.rm('rememberKey')
              Lockr.rm('authList')
              Lockr.rm('userInfo')
              Lockr.rm('sessionId')
              Cookies.remove('rememberPwd')
              _g.toastMsg('success', '退出成功')
              setTimeout(() => {
                router.replace('/')
              }, 1500)
            })
          })
        }).catch(() => {

        })
      },
      handleMenu(val) {
        switch (val) {
          case 'logout':
            this.logout()
            break
          case 'changePwd':
            this.changePwd()
            break
        }
      },
      changePwd() {
        this.$refs.changePwd.open()
      },

      getuserName() {
        this.userName = Lockr.get('userInfo').userName
      }
    },
    created() {
      let authKey = Lockr.get('authKey')
      let sessionId = Lockr.get('sessionId')
      if (!authKey || !sessionId) {
        _g.toastMsg('warning', '您尚未登录')
        setTimeout(() => {
          router.replace('/')
        }, 1500)
        return
      }
      this.getuserName()
      this.menuData = Lockr.get('menus')
      this.menu = this.$route.meta.menu
    },
    computed: {
      routerShow() {
        return store.state.routerShow
      },
      showLeftMenu() {
        this.hasChildMenu = store.state.showLeftMenu
        return store.state.showLeftMenu
      }
    },
    components: {
      leftMenu,
      changePwd
    },
    watch: {
      '$route' (to, from) {

      }
    },
    mixins: [http]
  }
</script>
<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity .5s
}

.fade-enter,
.fade-leave-active {
  opacity: 0
}

.panel {
  position: absolute;
  top: 0px;
  bottom: 0px;
  width: 100%;
}

.panel-top {
  height: 120px;
  line-height: 120px;
  background: #6F6F91;
  color: #c0ccda;
}

.panel-center {
  background: #324057;
  position: absolute;
  top: 120px;
  bottom: 0px;
  overflow: hidden;
}

.panel-c-c {
  background: #f1f2f7;
  position: absolute;
  right: 0px;
  top: 0px;
  bottom: 0px;
  left: 180px;
  overflow-y: scroll;
  padding: 20px;
}

.logout {
  background: url(../assets/images/logout_36.png);
  background-size: contain;
  width: 20px;
  height: 20px;
  float: left;
}

.logo {
  width: 150px;
  float: left;
  margin: 10px 10px 10px 18px;
}

.tip-logout {
  float: right;
  margin-right: 20px;
  padding-top: 5px;
  cursor: pointer;
}

.admin {
  color: #c0ccda;
  text-align: center;
}
.hide-leftMenu {
  left: 0px;
}
</style>