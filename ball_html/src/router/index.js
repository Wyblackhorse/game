import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements没有权限要求的基页
 * all roles can be accessed可以访问所有角色
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'dashboard', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: 'profile', icon: 'user', noCache: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles需要根据用户角色动态加载的路由
 */
export const asyncRoutes = [
  {
    component: Layout,
    alwaysShow: true,
    name: 'player_main',
    path: '/player_main',
    meta: { title: '会员管理', icon: 'player', roles: ['/ball/player/main'] },
    children: [
      {
        path: 'player',
        name: 'player',
        component: () => import('@/views/player/player_list'),
        meta: { title: '会员管理', icon: 'player', roles: ['/ball/player'] }
      },
      {
        path: 'player_finance',
        name: 'player_finance',
        component: () => import('@/views/player/finance_list'),
        meta: { title: '会员账务汇总', icon: 'money', roles: ['/ball/player/finance'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'game_main',
    path: '/game_main',
    meta: { title: '赛事管理', icon: 'game', roles: ['/ball/game/main'] },
    children: [
      {
        path: 'game',
        name: 'game',
        component: () => import('@/views/game/game_list'),
        meta: { title: '赛事管理', icon: 'game', roles: ['/ball/game'] }
      },
      {
        path: 'game_odds',
        name: 'game_odds',
        component: () => import('@/views/game/odds_list'),
        meta: { title: '赔率管理', icon: 'odds', roles: ['/ball/odds'] }
      },
      {
        path: 'game_finish',
        name: 'game_finish',
        component: () => import('@/views/game/game_finish_list'),
        meta: { title: '赛事结算管理', icon: 'game_finish', roles: ['/ball/game/finish'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'report_main',
    path: 'report_main',
    meta: { title: '报表管理', icon: 'report', roles: ['/ball/report/main'] },
    children: [
      {
        path: 'report_data',
        name: 'report_data',
        component: () => import('@/views/report/data_list'),
        meta: { title: '数据总览', icon: 'list', roles: ['/ball/report/data'] }
      },
      {
        path: 'report_proxy',
        name: 'report_proxy',
        component: () => import('@/views/report/proxy_list'),
        meta: { title: '代理报表', icon: 'list', roles: ['/ball/report/proxy'] }
      },
      {
        path: 'report_balance_change',
        name: 'report_balance_change',
        component: () => import('@/views/report/balance_change_list'),
        meta: { title: '账变报表', icon: 'list', roles: ['/ball/report/balance_change'] }
      },
      {
        path: 'report_player_day',
        name: 'report_player_day',
        component: () => import('@/views/report/player_day_list'),
        meta: { title: '会员日报表', icon: 'list', roles: ['/ball/report/player_day'] }
      },
      {
        path: 'report_bet',
        name: 'report_bet',
        component: () => import('@/views/report/bet_list'),
        meta: { title: '下注报表', icon: 'list', roles: ['/ball/report/bet'] }
      },
      {
        path: 'report_recharge',
        name: 'report_recharge',
        component: () => import('@/views/report/recharge_list'),
        meta: { title: '充提报表', icon: 'list', roles: ['/ball/report/recharge'] }
      },
      {
        path: 'report_recharge_way',
        name: 'report_recharge_way',
        component: () => import('@/views/report/recharge_way_list'),
        meta: { title: '充值渠道', icon: 'list', roles: ['/ball/report/recharge_way'] }
      },
      {
        path: 'report_game',
        name: 'report_game',
        component: () => import('@/views/report/game_list'),
        meta: { title: '赛事报表', icon: 'list', roles: ['/ball/report/game'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'bets_main',
    path: '/bets_main',
    meta: { title: '订单管理', icon: 'bets', roles: ['/ball/bets/main'] },
    children: [
      {
        path: 'bets',
        name: 'bets',
        component: () => import('@/views/bets/bets_list'),
        meta: { title: '足球订单', icon: 'bets', roles: ['/ball/bets'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'tactics_main',
    path: 'tactics_main',
    meta: { title: '策略配置', icon: 'tactics', roles: ['/ball/tactics/main'] },
    children: [
      {
        path: 'tactics_inout',
        name: 'tactics_inout',
        component: () => import('@/views/tactics/inout_list'),
        meta: { title: '存款优惠', icon: 'money', roles: ['/ball/tactics/inout'] }
      },
      {
        path: 'tactics_back',
        name: 'tactics_back',
        component: () => import('@/views/tactics/back_list'),
        meta: { title: '返佣策略', icon: 'tactics_back', roles: ['/ball/tactics/back'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'finance_main',
    path: 'finance_main',
    meta: { title: '账务管理', icon: 'finance', roles: ['/ball/finance/main'] },
    children: [
      {
        path: 'finance_pay',
        name: 'finance_pay',
        component: () => import('@/views/finance/pay_list'),
        meta: { title: '支付管理', icon: 'money', roles: ['/ball/finance/pay'] }
      },
      {
        path: 'finance_withdraw',
        name: 'finance_withdraw',
        component: () => import('@/views/finance/withdraw_list'),
        meta: { title: '提现方式管理', icon: 'finance_withdraw', roles: ['/ball/finance/withdraw'] }
      },
      {
        path: 'finance_bind',
        name: 'finance_bind',
        component: () => import('@/views/finance/bind_list'),
        meta: { title: '绑卡审核', icon: 'finance_bind', roles: ['/ball/finance/bind'] }
      },
      {
        path: 'finance_offline',
        name: 'finance_offline',
        component: () => import('@/views/finance/offline_list'),
        meta: { title: '线下充值', icon: 'finance_offline', roles: ['/ball/finance/offline'] }
      },
      {
        path: 'finance_online',
        name: 'finance_online',
        component: () => import('@/views/finance/online_list'),
        meta: { title: '线上充值', icon: 'finance_online', roles: ['/ball/finance/online'] }
      },
      {
        path: 'finance_out',
        name: 'finance_out',
        component: () => import('@/views/finance/out_list'),
        meta: { title: '提现管理', icon: 'finance_out', roles: ['/ball/finance/out'] }
      },
      {
        path: 'finance_inlog',
        name: 'finance_inlog',
        component: () => import('@/views/finance/inlog_list'),
        meta: { title: '上分记录', icon: 'finance_inlog', roles: ['/ball/finance/inlog'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'operation_main',
    path: '/operation_main',
    meta: { title: '运营管理', icon: 'operation', roles: ['/ball/operation/main'] },
    children: [
      {
        path: 'operation_banner',
        name: 'operation_banner',
        component: () => import('@/views/operation/banner_list'),
        meta: { title: 'Banner管理', icon: 'operation_banner', roles: ['/ball/operation/banner'] }
      },
      {
        path: 'operation_swiper',
        name: 'operation_swiper',
        component: () => import('@/views/operation/swiper_list'),
        meta: { title: '轮播公告', icon: 'operation_swiper', roles: ['/ball/operation/swiper'] }
      },
      {
        path: 'operation_notice',
        name: 'operation_notice',
        component: () => import('@/views/operation/notice_list'),
        meta: { title: '系统公告', icon: 'operation_notice', roles: ['/ball/operation/notice'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'merchant_main',
    path: 'merchant_main',
    meta: { title: '商户配置', icon: 'merchant', roles: ['/ball/merchant/main'] },
    children: [
      {
        path: 'merchant_param',
        name: 'merchant_param',
        component: () => import('@/views/merchant/param_list'),
        meta: { title: '参数配置', icon: 'merchant_param', roles: ['/ball/merchant/param'] }
      },
      {
        path: 'merchant_vip',
        name: 'merchant_vip',
        component: () => import('@/views/merchant/vip_list'),
        meta: { title: 'VIP配置', icon: 'merchant_vip', roles: ['/ball/merchant/vip'] }
      },
      {
        path: 'merchant_white',
        name: 'merchant_white',
        component: () => import('@/views/merchant/white_list'),
        meta: { title: '后台白名单', icon: 'merchant_white', roles: ['/ball/merchant/white'] }
      },
      {
        path: 'merchant_black',
        name: 'merchant_black',
        component: () => import('@/views/merchant/black_list'),
        meta: { title: '前端黑名单', icon: 'merchant_black', roles: ['/ball/merchant/black'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'log_main',
    path: 'log_main',
    meta: { title: '日志管理', icon: 'logs', roles: ['/ball/log/main'] },
    children: [
      {
        path: 'log_bet',
        component: () => import('@/views/log/bet_list'),
        name: 'log_bet',
        meta: { title: '下注日志', icon: 'log_bet', roles: ['/ball/log/bet'] }
      },
      {
        path: 'log_login',
        component: () => import('@/views/log/login_list'),
        name: 'log_login',
        meta: { title: '登录日志', icon: 'log_login', roles: ['/ball/log/login'] }
      },
      {
        path: 'log_operate',
        component: () => import('@/views/log/operate_list'),
        name: 'log_operate',
        meta: { title: '操作日志', icon: 'log_operate', roles: ['/ball/log/operate'] }
      },
      {
        path: 'log_proxy',
        component: () => import('@/views/log/proxy_list'),
        name: 'log_proxy',
        meta: { title: '代理迁移', icon: 'log_proxy', roles: ['/ball/log/proxy_change'] }
      }
    ]
  },
  {
    component: Layout,
    alwaysShow: true,
    name: 'sys_config_main',
    path: '/sys_config_main',
    meta: { title: '系统管理', icon: 'sys_config', roles: ['/ball/sysconfig_root'] },
    children: [
      {
        path: 'user',
        component: () => import('@/views/user/user_list'),
        name: 'sys_users',
        meta: { title: '用户管理', icon: 'peoples', roles: ['/ball/admin'] }
      },
      {
        path: 'role',
        component: () => import('@/views/role/role_list'),
        name: 'sys_roles',
        meta: { title: '角色管理', icon: 'role', roles: ['/ball/group'] }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
