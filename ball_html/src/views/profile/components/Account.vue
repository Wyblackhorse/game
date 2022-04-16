<template>
  <el-form>
    <el-form-item label="原密码">
      <el-input v-model="formData.origin" name="origin" placeholder="请输入原密码" />
    </el-form-item>
    <el-form-item label="密码">
      <el-input v-model="formData.newpwd" name="newpwd" type="password" placeholder="请输入密码" />
    </el-form-item>
    <el-form-item label="确认密码">
      <el-input v-model="formData.confirmed" name="confirmed" type="password" placeholder="再次输入密码" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">修改</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import request from '@/utils/request'
export default {
  data() {
    return {
      formData: {
        origin: '',
        newpwd: '',
        confirmed: ''
      }
    }
  },
  methods: {
    submit() {
      const _this = this
      request({
        url: 'auth/editPwd',
        method: 'post',
        data: _this.formData
      }).then((data) => {
        if (data.code === 200) {
          this.$message({
            message: '修改完成',
            type: 'success',
            duration: 5 * 1000
          })
        }
      })
    }
  }
}
</script>
