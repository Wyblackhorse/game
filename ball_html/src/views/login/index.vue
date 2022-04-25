<template>
  <div class="login-container">
    <img class="bg-login" src="@/assets/login.jpg">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">
      <div class="login-screen">
        <div class="title-container">
          <h3 class="title">
            用户登陆
          </h3>
        </div>

        <el-form-item prop="user">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="user"
            v-model="loginForm.user"
            :placeholder="$t('login.username')"
            name="user"
            type="text"
            tabindex="1"
            autocomplete="on"
          />
        </el-form-item>

        <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
          <el-form-item prop="pwd">
            <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.pwd"
              :type="passwordType"
              :placeholder="$t('login.password')"
              name="pwd"
              tabindex="2"
              autocomplete="on"
              @keyup.native="checkCapslock"
              @blur="capsTooltip = false"
              @keyup.enter.native="handleLogin"
            />
            <span class="show-pwd" @click="showPwd">
              <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
          </el-form-item>
        </el-tooltip>
        <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
          <el-form-item prop="googleCode">
            <span class="svg-container">
              <svg-icon icon-class="google" />
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.googleCode"
              :placeholder="$t('login.gpwd')"
              name="gpwd"
              tabindex="3"
              autocomplete="on"
              @keyup.native="checkCapslock"
              @blur="capsTooltip = false"
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>
        </el-tooltip>

        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">
          {{ $t('login.logIn') }}
        </el-button>
      </div>
    </el-form>
    <el-dialog :title="$t('login.googleValid')" :visible="googleValidVisible">
      <el-image :src="otherQuery.qrcode" style="width: 180px;height: 180px;" />
      <h3>扫码绑定后输入验证码提交验证~</h3>
      <br>
      <el-form ref="gvalidform" :model="otherQuery" label-position="left" label-width="120px">
        <el-form-item label="GOOGLE验证码">
          <el-input v-model="otherQuery.googleCode" class="googleValidInput" style="color: black !important;background: white !important;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="googleValidVisible = false">
          {{ $t('table.cancel') }}
        </el-button>
        <el-button type="primary" @click="googleValidSubmit">
          {{ $t('login.googleValidButton') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import SocialSign from './components/SocialSignin'
import request from '@/utils/request'

export default {
  name: 'Login',
  components: { SocialSign },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('请输入最少6位用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('请输入最少6位密码'))
      } else {
        callback()
      }
    }
    return {
      googleValidVisible: false,
      loginForm: {
        user: '',
        pwd: ''
      },
      loginRules: {
        user: [{ required: true, trigger: 'blur', validator: validateUsername }],
        pwd: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
    // window.addEventListener('storage', this.afterQRScan)
  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm)
            .then((data) => {
              if (data.gtoken === 1) {
                this.googleValidVisible = true
                this.otherQuery.user = data.user
                this.otherQuery.googleKey = data.gtokenKey
                this.otherQuery.qrcode = data.gtokenQr
              } else {
                this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
                this.loading = false
              }
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          // console.log('请勿重复提交')
          return false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    },
    googleValidSubmit(query) {
      request({
        url: '/auth/userinfo',
        method: 'post',
        data: this.otherQuery
      }).then((response) => {
        if (response.code === 200) {
          // 登录成功
          this.$store.dispatch('user/loging', response.data).then(() => {
            this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
            this.loading = false
          })
        }
      }).catch(() => {
      })
    },
    crateQrcode: function(qrimg) {
      this.qrcode = qrimg
      if (this.$refs['qrcode'] != undefined) {
        this.$refs['qrcode'].innerHTML = ''
      }
      this.$nextTick(() => {
        this.qr = new QRCode('qrcode', {
          width: 150,
          height: 150,
          text: this.qrcode,
          render: 'table'
        })
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}
.googleValidInput{
  background: white !important;
  color: black !important;
  input{
    color: black !important;
    caret-color: black !important;
  }
}
/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#214156;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .bg-login{
    min-width: 100%;
    min-height: 100%;
    position: fixed;
    top: 0px;
  }

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }

    .set-language {
      color: #fff;
      position: absolute;
      top: 3px;
      font-size: 18px;
      right: 0px;
      cursor: pointer;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }
.login-screen {
    min-height: 20px;
    padding: 19px;
    margin-bottom: 20px;
    background-color: #f5f5f5;
    border: 1px solid #e3e3e3;
    border-radius: 3px;
    -webkit-box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background: rgba(255,255,255, 0.2);
}
  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}
</style>
