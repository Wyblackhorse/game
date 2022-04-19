<template>
  <div class="app-container">
    <div class="filter-container">
      <!--<el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->
      <!--<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">-->
        <!--{{ $t('table.search') }}-->
      <!--</el-button>-->
      <el-button v-if="hasAuth('/ball/tactics/inout/add')" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
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
      <el-table-column label="优惠名称"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优惠类型"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.depositPolicyType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始时间"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.startTime|formatDate('y-M-d h:m:s') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.endTime|formatDate('y-M-d h:m:s') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优惠标准"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.preferentialStandard }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优惠百分比"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.preferentialPer }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优惠上限"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.preferentialTop }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column label="协议"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.remark }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" min-width="200px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="hasAuth('/ball/tactics/inout/edit')" type="primary" size="mini" @click="handleUpdate(row)">
            {{ $t('table.edit') }}
          </el-button>
          <el-button v-if="hasAuth('/ball/tactics/inout/del')" size="mini" type="danger" @click="handleDelete(row,$index)">
            {{ $t('table.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="90px" style="padding-left:30px;padding-right: 30px">
        <el-row>
          <el-col span="12">
          </el-col>
          <el-col span="12">
          </el-col>
        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="优惠名称" prop="name">
              <el-input v-model="temp.name" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="优惠类型" prop="depositPolicyType">
              <el-select style="width: 100%"  v-model="temp.depositPolicyType" clearable placeholder="优惠类型">
                <el-option
                  v-for="item in policyTypes"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="开始时间" prop="start">
              <el-date-picker style="width: 100%;" v-model="temp.start" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="选择开始日期时间" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="结束时间" prop="end">
              <el-date-picker style="width: 100%;" v-model="temp.end" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="选择结束日期时间" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="优惠标准" prop="preferentialStandard">
              <el-input v-model="temp.preferentialStandard" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="优惠百分比" prop="preferentialPer">
              <el-input v-model="temp.preferentialPer" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="优惠上限" prop="preferentialTop">
              <el-input v-model="temp.preferentialTop" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="协议" prop="remark">
              <el-input v-model="temp.remark" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  @click="dialogFormVisible = false">
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
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      policyTypes: [{ name: '首冲', value: 1 }, { name: '每次', value: 2 }],
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
    getRoles() {
      request({
        url: 'ball/tactics/inout',
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
        url: 'ball/tactics/inout',
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
            url: 'ball/tactics/inout/add',
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
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
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
            url: 'ball/tactics/inout/edit',
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
          url: 'ball/tactics/inout/del?id=' + ids,
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
<style>
</style>
