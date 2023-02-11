import Login from './components/Account/Login.vue'
import refresh from './components/refresh.vue'
import Home from './components/Home.vue'
import menuList from './components/Administrative/system/menu/index.vue'
import rolesList from './components/Administrative/role/index.vue'
import usersList from './components/Administrative/users/index.vue'

const routes = [
  { path: '/', component: Login, name: 'Login' },
  {
    path: '/home',
    component: Home,
    children: [
      { path: '/refresh', component: refresh, name: 'refresh' }
    ]
  },
  {
    path: '/home',
    component: Home,
    children: [
      { path: 'menu/list', component: menuList, name: 'menuList', meta: { hideLeft: false, module: 'Administrative', menu: 'menu' }},
    ]
  },
  {
    path: '/home',
    component: Home,
    children: [
      { path: 'role/list', component: rolesList, name: 'RoleManage', meta: { hideLeft: false, module: 'Administrative', menu: 'groups' }},
    ]
  },
  {
    path: '/home',
    component: Home,
    children: [
      { path: 'users/list', component: usersList, name: 'usersList', meta: { hideLeft: false, module: 'Administrative', menu: 'users' }},
    ]
  }
]
export default routes
