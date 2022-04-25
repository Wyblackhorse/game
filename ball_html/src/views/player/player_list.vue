<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t('table.search') }}
      </el-button>
      <el-button v-if="hasAuth('/ball/palyer/add')" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        {{ $t('table.add') }}
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="id" prop="id" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账号" align="center">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="余额" align="center">
        <template slot-scope="{row}">
          <span>{{ row.balance|balance }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后登录IP" align="center">
        <template slot-scope="{row}">
          <span>{{ row.theLastIp }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邀请码" align="center">
        <template slot-scope="{row}">
          <span>{{ row.invitationCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账号类型" align="center">
        <template slot-scope="{row}">
          <span>{{ accountTypes[row.accountType-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会员级别" align="center">
        <template slot-scope="{row}">
          <span>{{ row.vipLevel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会员级别" align="center">
        <template slot-scope="{row}">
          <span>{{ row.vipLevel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createdAt|formatDate('y-M-d') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" min-width="200px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="hasAuth('/ball/player/edit')" @click.native="handleCommandData('a1',row)">修改</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/edit_pwd')" @click.native="handleCommandData('a2',row)">改密码</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/edit_pay_pwd')" @click.native="handleCommandData('a3',row)">改支付密码</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/status')" @click.native="handleCommandData('a4',row)">{{ statusOper[row.status-1].name }}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/add_balance')" @click.native="handleCommandData('a5',row)">上分</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/captcha_pass')" @click.native="handleCommandData('a6',row)">改出款打码量</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              查询<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="hasAuth('/ball/player/info')" @click.native="handleCommandData('a7',row)">查看</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/log')" @click.native="handleCommandData('a8',row)">账变记录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item v-if="showEdit" label="用户名" prop="username">
          {{ temp.username }}
        </el-form-item>
        <el-form-item v-if="showEdit" label="手机号" prop="phone">
          <el-input v-model="temp.phone" />
        </el-form-item>
        <el-form-item v-if="showEdit" label="eMail" prop="eMail">
          <el-input v-model="temp.eMail" />
        </el-form-item>
        <el-form-item v-if="showEdit" label="所属上级" prop="superName">
          <el-input v-model="temp.superName" />
        </el-form-item>
        <el-input v-model="temp.superiorId" type="hidden" />
        <el-form-item v-if="showEdit" label="账号类型" prop="superName">
          <el-select v-model="temp.accountType" style="width: 100%" placeholder="账号类型">
            <el-option
              v-for="item in accountTypes"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="showEditPwd" label="密码" prop="editPwd">
          <el-input v-model="temp.editPwd" />
        </el-form-item>
        <el-form-item v-if="showEditAddBalance" label="上分">
          <el-input v-model="temp.balance" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          {{ $t('table.cancel') }}
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          {{ $t('table.confirm') }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog width="70%" title="账变记录" :visible.sync="dialogLogisible">
      <el-table
        :key="tableKeyLog"
        v-loading="listLogLoading"
        :data="listLog"
        border
        fit
        highlight-current-row
        style="width: 100%;"
      >
        <el-table-column label="id" prop="id" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="变动前余额" prop="initMoney" align="center">
          <template slot-scope="{row}">
            <span>{{ row.initMoney|balance }}</span>
          </template>
        </el-table-column>
        <el-table-column label="变动金额" prop="changeMoney" align="center">
          <template slot-scope="{row}">
            <span>{{ row.changeMoney|balance }}</span>
          </template>
        </el-table-column>
        <el-table-column label="变动后金额" prop="dnedMoney" align="center">
          <template slot-scope="{row}">
            <span>{{ row.dnedMoney|balance }}</span>
          </template>
        </el-table-column>
        <el-table-column label="变动类型" prop="balanceChangeType" align="center">
          <template slot-scope="{row}">
            <span>{{ changeTypes[row.balanceChangeType-1].name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createdAt|formatDate('y-M-d') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" align="center">
          <template slot-scope="{row}">
            <span>{{ row.remark }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="totalLog>0" :total="totalLog" :page.sync="listLogQuery.pageNo" :limit.sync="listLogQuery.pageSize" @pagination="log" />
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination基于el分页的二次包
import request from '@/utils/request'
import { MessageBox } from 'element-ui'
import store from '@/store'

export default {
  name: 'ComplexTable',
  components: { Pagination },
  data() {
    return {
      user: store.getters.user,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      showEdit: false,
      showEditPwd: false,
      showEditPayPwd: false,
      showEditAddBalance: false,
      showEditCaptchaPass: false,
      listQuery: {
        pageNo: 1,
        pageSize: 20,
        username: ''
      },
      dialogLogisible: false,
      tableKeyLog: 1,
      listLog: [],
      totalLog: 0,
      listLogLoading: false,
      listLogQuery: {
        pageNo: 1,
        pageSize: 20,
        playerId: 0
      },
      changeTypes: [
        { name: '充值', value: 1 },
        { name: '提现', value: 2 },
        { name: '下注', value: 3 },
        { name: '赢', value: 4 },
        { name: '佣金', value: 5 },
        { name: '人工', value: 6 }
      ],
      temp: {
        id: 1,
        username: '',
        password: '',
        nickname: '',
        roleId: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      accountTypes: [
        { name: '测试号', value: 1 },
        { name: '正常号', value: 2 },
        { name: '代理号', value: 3 }
      ],
      statusOper: [
        { name: '禁用', value: 1 },
        { name: '启用', value: 2 }
      ],
      rules: {
        username: [{ required: true, message: '用户名必填', trigger: 'blur' }],
        roleId: [{ required: true, message: '角色必选', trigger: 'blur' }]
      },
      roles: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    witchEditShow(witch) {
      this.showEdit = false
      this.showEditPwd = false
      this.showEditPayPwd = false
      this.showEditAddBalance = false
      this.showEditCaptchaPass = false
      switch (witch) {
        case 1:
          this.showEdit = true
          break
        case 2:
          this.showEditPwd = true
          break
        case 3:
          this.showEditPayPwd = true
          break
        case 4:
          this.temp.balance = ''
          this.showEditAddBalance = true
          break
        case 5:
          this.showEditCaptchaPass = true
          break
      }
    },
    handleCommandData(command, row) {
      switch (command) {
        case 'a1':
          this.handleUpdate(row, 1)
          break
        case 'a2':
          this.handleUpdate(row, 2)
          break
        case 'a3':
          this.handleUpdate(row, 3)
          break
        case 'a4':
          // status
          this.changeStatus(row)
          break
        case 'a5':
          this.handleUpdate(row, 4)
          break
        case 'a6':
          this.handleUpdate(row, 5)
          break
        case 'a7':
          this.info(row)
          break
        case 'a8':
          this.log(row)
          break
      }
    },
    changeStatus(row) {
      MessageBox.confirm('你确定要' + (row.status == 1 ? '禁用' : '启用') + '该账号吗？', '重要提醒', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/player/status',
          method: 'post',
          data: {
            id: row.id,
            status: row.status
          }
        }).then((response) => {
          if (response.code === 200) {
            const index = this.list.findIndex(v => v.id === row.id)
            this.list[index].status = (row.status == 1 ? 2 : 1)
            this.$notify({
              message: '修改成功',
              type: 'success',
              duration: 2 * 1000
            })
          }
        })
      })
    },
    log(row) {
      this.dialogLogisible = true
      this.listLogLoading = true
      const _this = this
      if (row.id != undefined) {
        this.listLogQuery.playerId = row.id
      }
      request({
        url: 'ball/player/log',
        method: 'post',
        params: _this.listLogQuery
      }).then((response) => {
        if (response.code === 200) {
          this.listLog = response.data.results
          this.totalLog = response.data.totalCount
        }
        this.listLogLoading = false
      }).catch(() => {
        this.listLogLoading = false
      })
    },
    getList() {
      this.listLoading = true
      const _this = this
      request({
        url: 'ball/player',
        method: 'post',
        params: _this.listQuery
      }).then((response) => {
        if (response.code === 200) {
          this.list = response.data.results
          this.total = response.data.totalCount
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() { // 添加属性
      this.temp = {
        id: undefined,
        username: '',
        password: '',
        nickname: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        // console.log(this.temp)
        this.temp.rate = parseInt(this.temp.rate)
        this.temp.type = parseInt(this.temp.type)
        // console.log(this.temp)
        if (valid) {
          request({
            url: 'ball/player/add',
            method: 'post',
            data: this.temp
          }).then((response) => {
            if (response.code === 200) {
              this.dialogFormVisible = false
              this.list.unshift(response.data)
              this.$message({
                message: '添加成功',
                type: 'success',
                duration: 3 * 1000
              })
            }
          })
        }
      })
    },
    handleUpdate(row, witch) {
      this.temp = Object.assign({}, row) // copy obj
      this.witchEditShow(witch)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.temp.witch = witch
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          let url = 'ball/player/edit'
          switch (tempData.witch) {
            case 2:
              url = 'ball/player/edit_pwd'
              break
            case 3:
              url = 'ball/player/edit_pay_pwd'
              break
            case 4:
              url = 'ball/player/add_balance'
              break
            case 5:
              url = 'ball/player/captcha_pass'
              break
          }
          request({
            url: url,
            method: 'post',
            data: tempData
          }).then((response) => {
            if (response.code === 200) {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.dialogFormVisible = false
              this.list.splice(index, 1, this.temp)
              this.$notify({
                message: '修改成功',
                type: 'success',
                duration: 2 * 1000
              })
            }
          })
        }
      })
    },
    handleDelete(row, index) {
      var ids = row.id
      MessageBox.confirm('你确定要删除吗？', '删除提醒', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/player/del?id=' + ids,
          method: 'get'
        }).then((response) => {
          if (response.code === 200) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.list.splice(index, 1)
          }
        })
      }).catch(() => {
      })
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
