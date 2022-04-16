const host = 'localhost'
const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  user: state => state.user,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  permission_routes: state => state.permission.routes,
  errorLogs: state => state.errorLog.logs,
  menus: state => state.user.menus, // 定义路由
  baseUrl: state => 'http://' + host + ':10100/'
  // baseUrl: state => 'http://116.62.131.140:10010/'
  // baseUrl: state => 'http://8.136.103.79:10010/'
  // baseUrl: state => '/'
}
export default getters
