<template>
  <div class="app-container">
    <div class="filter-container">
      <!--<el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->
      <!--<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">-->
      <!--{{ $t('table.search') }}-->
      <!--</el-button>-->
      <el-button
        v-if="hasAuth('/ball/tactics/back/add')"
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
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
      <el-table-column :label="$t('form.commissionStrategy.name')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.commissionStrategy.commissionStrategyType')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.commissionStrategy.commissionStrategyTypes')[row.commissionStrategyType-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.commissionStrategy.commissionLevel')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.commissionLevel }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.commissionStrategy.automaticDistribution')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.commissionStrategy.automaticDistributions')[row.automaticDistribution-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.status')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.statusOper')[row.status-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('table.actions')"
        align="center"
        min-width="200px"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{row,$index}">
          <el-button v-if="hasAuth('/ball/tactics/back/edit')" type="primary" size="mini" @click="handleUpdate(row)">
            {{ $t('table.edit') }}
          </el-button>
          <el-button
            v-if="hasAuth('/ball/tactics/back/del')"
            size="mini"
            type="danger"
            @click="handleDelete(row,$index)"
          >
            {{ $t('table.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNo"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="$t('form.textMap')[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="90px"
        style="padding-left:30px;padding-right: 30px"
      >
        <el-form-item :label="$t('form.commissionStrategy.name')" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item :label="$t('form.commissionStrategy.commissionStrategyType')" prop="depositPolicyType">
          <el-select v-model="temp.commissionStrategyType" style="width: 100%" clearable :placeholder="$t('form.commissionStrategy.commissionStrategyType')">
            <el-option
              v-for="item in $t('form.commissionStrategy.commissionStrategyTypes')"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.commissionStrategy.commissionLevel')" prop="commissionLevel">
          <el-input v-model="temp.commissionLevel" />
        </el-form-item>
        <el-form-item :label="$t('form.commissionStrategy.automaticDistribution')" prop="automaticDistribution">
          <el-radio v-model="temp.automaticDistribution" :label=1>{{$t('form.commissionStrategy.automaticDistributions')[0].name}}</el-radio>
          <el-radio v-model="temp.automaticDistribution" :label=2>{{$t('form.commissionStrategy.automaticDistributions')[1].name}}</el-radio>
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
      listQuery: {
        pageNo: 1,
        pageSize: 20,
        username: ''
      },
      temp: {
        depositPolicyType: ''
      },
      dialogFormVisible: false,
      dialogStatus: 0,
      rules: {
        username: [{ required: true, message: '用户名必填', trigger: 'blur' }],
        roleId: [{ required: true, message: '角色必选', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getRoles() {
      request({
        url: 'ball/tactics/back',
        method: 'get'
      }).then((response) => {
        if (response.code === 200) {
          this.roles = response.data
        }
      }).catch(() => {
      })
    },
    getList() {
      this.listLoading = true
      const _this = this
      request({
        url: 'ball/tactics/back',
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
      this.dialogStatus = 1
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
            url: 'ball/tactics/back/add',
            method: 'post',
            data: this.temp
          }).then((response) => {
            if (response.code === 200) {
              this.dialogFormVisible = false
              this.list.unshift(response.data)
              this.$message({
                message: this.$t('messages.successAdd'),
                type: 'success',
                duration: 3 * 1000
              })
            }
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 0
      this.dialogFormVisible = true
      this.temp.start = this.temp.startStr
      this.temp.end = this.temp.endStr
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          request({
            url: 'ball/tactics/back/edit',
            method: 'post',
            data: tempData
          }).then((response) => {
            if (response.code === 200) {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.dialogFormVisible = false
              this.list.splice(index, 1, this.temp)
              this.$notify({
                message: this.$t('messages.successEdit'),
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
      MessageBox.confirm(this.$t('tips.delContent'), this.$t('tips.delTitle'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/tactics/back/del?id=' + ids,
          method: 'get'
        }).then((response) => {
          if (response.code === 200) {
            this.$notify({
              title: this.$t('messages.success'),
              message: this.$t('messages.successDel'),
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
<style>
</style>
