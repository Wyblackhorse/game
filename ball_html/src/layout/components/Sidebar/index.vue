<template>
  <div :class="{'has-logo':showLogo}">
    <logo :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="true"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in permission_routes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="true"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  data() {
    return {
      listAct: {},
      props: {
        label: 'name',
        children: 'zones',
        isLeaf: 'leaf'
      }
    }
  },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar',
      'introduction'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  created() {
  },
  mounted() {
    // 将Vue方法传到全局对象window中
    window.setData = this.setData
  },
  methods: {
  }
}
</script>
<style>
  /*.el-submenu__title{*/
    /*padding-left: 100px !important;*/
  /*}*/
  .el-tree{
    background: rgb(48, 65, 86) ;
    color: white;
    width: 100%;
    overflow-x: auto;
  }
  .el-tree>.el-tree-node{
    padding-right: 100px ;
    min-width:100%;
    display: inline-block !important;
  }
  .el-tree-node:focus{
    background-color: rgb(48, 65, 86);
  }
  .el-tree-node__content {
    height: 56px !important;
    line-height: 56px !important;
    font-size: 14px;
    background: rgb(48, 65, 86);
  }
  .el-tree-node__content:hover{
    background-color: rgb(48, 65, 86);
  }
  .el-tree.is-focusable{
    background: rgb(48, 65, 86) !important; ;
  }
  .el-tree-node:focus>.el-tree-node__content{
    background-color: rgb(48, 65, 86);
  }

</style>
