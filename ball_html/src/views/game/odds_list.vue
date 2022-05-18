<template>
  <div class="app-container">
    <div class="filter-container">
      <!--<el-input v-model="listQuery.orderNo" placeholder="订单号" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t('table.search') }}
      </el-button>
      <!--<el-button v-if="hasAuth('/ball/admin/add')" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">-->
      <!--{{ $t('table.add') }}-->
      <!--</el-button>-->
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
      <el-table-column :label="$t('page.odds.gameId')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.gameId }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.odds.id')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.odds.score')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.scoreHome }}-{{ row.scoreAway }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.odds.gameType')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.odds.gameType')[row.gameType-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.odds.lossPerCent')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.lossPerCent }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.odds.antiPerCent')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.antiPerCent }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.odds.updatedAt')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updatedAt | formatDate}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.odds.even')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('page.game.evenStatusArr')[row.even-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.odds.status')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('page.game.statusArr')[row.status-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" min-width="180px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="hasAuth('/ball/odds/edit')" type="primary" size="mini" @click="handleUpdate(row)">
            {{$t('page.edit')}}
          </el-button>
          <!--<el-button v-if="hasAuth('/ball/odds/info')" type="primary" size="mini" @click="oddsinfo(row)">-->
            <!--{{$t('table.info')}}-->
          <!--</el-button>-->
          <el-button v-if="hasAuth('/ball/odds/status')" type="primary" size="mini" @click="changeStatus(row)">
            {{ $t('form.statusOper')[row.status%2].name }}
          </el-button>
          <el-button v-if="hasAuth('/ball/odds/down')" type="primary" size="mini" @click="changeStatusEven(row)">
            {{ $t('page.game.evenStatusArr')[row.even%2].name }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="$t('form.textMap')[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item :label="$t('page.odds.scoreHome')" prop="scoreHome">
          <el-input v-model="temp.scoreHome" />
        </el-form-item>
        <el-form-item :label="$t('page.odds.scoreAway')" prop="scoreAway">
          <el-input v-model="temp.scoreAway" />
        </el-form-item>
        <el-form-item :label="$t('page.odds.lossPerCent')" prop="lossPerCent">
          <el-input v-model.number="temp.lossPerCent" />
        </el-form-item>
        <el-form-item :label="$t('page.odds.antiPerCent')" prop="antiPerCent">
          <el-input v-model.number="temp.antiPerCent" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          {{ $t('table.cancel') }}
        </el-button>
        <el-button type="primary" @click="updateData()">
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
        id: 1,
        username: '',
        password: '',
        nickname: '',
        roleId: ''
      },
      dialogFormVisible: false,
      dialogStatus: 1,
      rules: {
        lossPerCent: [{ required: true, message: this.$t('form.odds.oddRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('form.odds.oddMustNumber'), trigger: 'blur' }],
        antiPerCent: [{ required: true, message: this.$t('form.odds.oddRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('form.odds.oddMustNumber'), trigger: 'blur' }],
      },
      roles: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    changeStatusEven(row) {
      MessageBox.confirm(this.$t('form.odds.statusEven' + (row.even % 2)), this.$t('tips.importentTitle'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/odds/down',
          method: 'post',
          data: {
            id: row.id,
            even: row.even
          }
        }).then((response) => {
          if (response.code === 200) {
            const index = this.list.findIndex(v => v.id === row.id)
            this.list[index].even = (row.even == 1 ? 2 : 1)
            this.$notify({
              message: this.$t('messages.successEdit'),
              type: 'success',
              duration: 2 * 1000
            })
          }
        })
      })
    },
    changeStatus(row) {
      MessageBox.confirm(this.$t('form.odds.statusConfirm' + (row.status % 2)), this.$t('tips.importentTitle'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/odds/status',
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
              message: this.$t('messages.successEdit'),
              type: 'success',
              duration: 2 * 1000
            })
          }
        })
      })
    },
    getList() {
      this.listLoading = true
      const _this = this
      request({
        url: 'ball/odds',
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
      }
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 1
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          request({
            url: 'ball/odds/edit',
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
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
