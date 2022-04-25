<template>
  <div class="app-container">
    <!--<div class="filter-container">-->
      <!--<el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->
      <!--<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">-->
        <!--{{ $t('table.search') }}-->
      <!--</el-button>-->
      <!--<el-button v-if="hasAuth('/ball/admin/add')" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">-->
        <!--{{ $t('table.add') }}-->
      <!--</el-button>-->
    <!--</div>-->
    <el-form ref="dataForm" :model="sysConfig" label-position="left" label-width="180px" style="margin-left:50px;">
      <el-tabs :value="activeName" @tab-click="handlerTabClick">
        <el-tab-pane  v-if="hasAuth('/ball/merchant/param/loreg')" label="注册与登录配置" name="loreg">
          <el-row>
            <el-col :span="12">
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="注册是否需要邀请码" prop="registerIfNeedVerificationCode">
                <el-radio v-model="sysConfig.registerIfNeedVerificationCode" :label="0">不需要</el-radio>
                <el-radio v-model="sysConfig.registerIfNeedVerificationCode" :label="1">需要</el-radio>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="密码连续错误的次数" prop="passwordMaxErrorTimes">
                <el-input v-model="sysConfig.passwordMaxErrorTimes"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="密码连续错误锁屏时间(秒)" prop="passwordErrorLockTime">
                <el-input v-model="sysConfig.passwordErrorLockTime"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane v-if="hasAuth('/ball/merchant/param/servicer')" label="客服配置" name="servicer">
          <el-row>
            <el-col :span="12">
              <el-form-item label="客服连接" prop="serverUrl">
                <el-input v-model="sysConfig.serverUrl"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane v-if="hasAuth('/ball/merchant/param/finance')" label="财务配置" name="finance">
          <el-row>
            <el-col :span="12">
              <el-form-item label="会员最多绑卡数量" prop="cardCanNeedNums">
                <el-input v-model="sysConfig.cardCanNeedNums"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="会员最多绑卡数量" prop="cardCanNeedNums">
                <el-input v-model="sysConfig.cardCanNeedNums"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="充值打码量转换比例" prop="rechargeCodeConversionRate">
                <el-input v-model="sysConfig.rechargeCodeConversionRate"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户打码设量设置阀值" prop="captchaThreshold">
                <el-input v-model="sysConfig.captchaThreshold"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="会员最多绑卡数量" prop="cardCanNeedNums">
                <el-input v-model="sysConfig.cardCanNeedNums"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="投注手续费率" prop="betHandMoneyRate">
                <el-input v-model="sysConfig.betHandMoneyRate"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="快捷金额" prop="fastMoney">
                <el-input v-model="sysConfig.fastMoney"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="usdt提现汇率" prop="usdtWithdrawPer">
                <el-input v-model="sysConfig.usdtWithdrawPer"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="提现usdt自动汇率" prop="withdrawUsdtAutomaticPer">
                <el-input v-model="sysConfig.withdrawUsdtAutomaticPer"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="保本扣除手续费" prop="evenNeedHandMoney">
                <el-input v-model="sysConfig.evenNeedHandMoney"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="最多可绑定usdt账号数量" prop="maxUsdtAccountNums">
                <el-input v-model="sysConfig.maxUsdtAccountNums"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="最多可绑定pix账号数量" prop="maxPixAccountNums">
                <el-input v-model="sysConfig.maxPixAccountNums"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="提现密码是否可以修改" prop="withdrawPasswordCanUpdate">
                <el-input v-model="sysConfig.withdrawPasswordCanUpdate"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="是否可以连续发起提现" prop="canWithdrawContinuity">
                <el-input v-model="sysConfig.canWithdrawContinuity"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="控制首页提现密码是否可以关闭" prop="withdrawPasswordShowNeed">
                <el-input v-model="sysConfig.withdrawPasswordShowNeed"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane v-if="hasAuth('/ball/merchant/param/risk')" label="风控配置" name="risk">
          <el-row>
            <el-col :span="12">
              <el-form-item label="每日的提现上限次数" prop="everydayWithdrawTimes">
                <el-input v-model="sysConfig.everydayWithdrawTimes"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane v-if="hasAuth('/ball/merchant/param/operate')" label="运营配置" name="operate">

        </el-tab-pane>
        <el-tab-pane v-if="hasAuth('/ball/merchant/param/share')" label="推广配置" name="share">

        </el-tab-pane>
      </el-tabs>
      <el-button type="primary" @click="updateData()">
        {{ $t('table.confirm') }}
      </el-button>
    </el-form>
  </div>
</template>

<script>
import request from '@/utils/request'
import { MessageBox } from 'element-ui'
import store from '@/store'

export default {
  name: 'ComplexTable',
  data() {
    return {
      user: store.getters.user,
      activeName: '',
      listLoading: true,
      sysConfig: {},
      editSubmitUrl: ''
    }
  },
  created() {
    this.getSysconfig()
  },
  methods: {
    getSysconfig() {
      request({
        url: '/ball/merchant/param',
        method: 'get'
      }).then((response) => {
        if (response.code === 200) {
          this.sysConfig = response.data
        }
      }).catch(() => {
      })
    },
    handlerTabClick(tab) {
      this.editSubmitUrl = tab.name
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.sysConfig)
          request({
            url: '/ball/merchant/param/' + this.editSubmitUrl,
            method: 'post',
            data: tempData
          }).then((response) => {
            if (response.code === 200) {
              this.$notify({
                message: '修改成功',
                type: 'success',
                duration: 2 * 1000
              })
            }
          })
        }
      })
    }
  }
}
</script>
