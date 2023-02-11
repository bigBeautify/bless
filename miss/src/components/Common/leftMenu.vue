<template>
	<div>
		<div v-for="secMenu in menuData">
			<div class="c-light-gray p-l-10 m-t-15">{{secMenu.root.title}}</div>
			<div class="h-50" v-for="item in secMenu.children">
        <div class="w-100p h-50 p-l-40 left-menu pointer " :class="{'c-blue':item.selected,'c-gra': !item.selected}" @click="routerChange(item)">
          {{item.title}}
        </div>
			</div>
		</div>
	</div>
</template>

<script>
export default {
  props: ['menuData', 'menu'],
  data() {
    return {

    }
  },
  methods: {
    routerChange(item) 	{
      this.menuData.forEach((res, key) => {
        res.children.forEach((child,i) =>{
          if (item.aid == child.aid) {
            child.selected = true
          } else {
            child.selected = false
          }
        })
      })
      // 与当前页面路由相等则刷新页面
      if (item.url != this.$route.path) {
        router.push(item.url)
      } else {
        _g.shallowRefresh(this.$route.name)
      }
    }
  },
  created() {
    console.log(this.menuData)
  }
}
</script>