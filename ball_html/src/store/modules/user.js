import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken, getUser, setUser, removeUser } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import getters from '../getters'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: ''
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_MENUS: (state, menus) => {
    state.menus = menus
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { user, pwd, googleCode } = userInfo
    return new Promise((resolve, reject) => {
      login({ user: user.trim(), pwd: pwd, googleCode: googleCode }).then(response => {
        const { data } = response
        if (data.gtokenKey != 'ok') {
          data.gtoken = 1
        } else {
          commit('SET_TOKEN', data.token)
          commit('SET_NAME', data.user)
          setToken(data.token)
          setUser(JSON.stringify(data))
        }
        resolve(data)
      }).catch((error) => {
        reject(error)
      })
    })
  },
  loging({ commit }, data) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', data.token)
      commit('SET_NAME', data.user)
      setToken(data.token)
      setUser(JSON.stringify(data))
      resolve()
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        // roles must be a non-empty array
        if (!data || data.auths.length <= 0) {
          reject('getInfo: roles must be a non-null array!')
        }

        commit('SET_ROLES', data.auths)
        commit('SET_NAME', data.name)
        resolve({ roles: data.auths })
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', '')
        removeToken()
        removeUser()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', '')
      removeToken()
      removeUser()
      resolve()
    })
  },

  // dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { roles } = await dispatch('getInfo')

      resetRouter()

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      // reset visited views and cached views
      dispatch('tagsView/delAllViews', null, { root: true })

      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
