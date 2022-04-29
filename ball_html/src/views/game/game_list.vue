<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t('table.search') }}
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
      <el-table-column label="赛事ID" align="center">
        <template slot-scope="{row}">
          <span>{{ row.orderNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Logo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.nickname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联盟名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.allianceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联盟Logo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.allianceLogo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="主队名字" align="center">
        <template slot-scope="{row}">
          <span>{{ row.mainName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="主队Logo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.mainLogo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="客队名字" align="center">
        <template slot-scope="{row}">
          <span>{{ row.guestName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="客队Logo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.guestLogo }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="距离开赛" align="center">-->
        <!--<template slot-scope="{row}">-->
          <!--<span>{{ row.remainingTime|countDown }}</span>-->
        <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column label="开赛时间" align="center">
        <template slot-scope="{row}">
          <span>{{ row.startTime|formatDate('y-M-d h:m:s') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="比分" align="center">
        <template slot-scope="{row}">
          <span>{{ row.score }}</span>
        </template>
      </el-table-column>
      <el-table-column label="赛事状态" align="center">
        <template slot-scope="{row}">
          <span>{{ row.gameStatus }}</span>
        </template>
      </el-table-column>
      <el-table-column label="置顶状态" align="center">
        <template slot-scope="{row}">
          <span>{{ row.top }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" min-width="200px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="hasAuth('/ball/game/info')" type="primary" size="mini" @click="betInfo(row)">
            {{$t('table.info')}}
          </el-button>
          <el-button v-if="row.status ===1 && hasAuth('/ball/game/undo')" type="primary" size="mini" @click="handleUpdate(row)">
            撤单
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username" />
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
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      rules: {
        username: [{ required: true, message: '用户名必填', trigger: 'blur' }],
        roleId: [{ required: true, message: '角色必选', trigger: 'blur' }]
      },
      closeingTask: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      const _this = this
      request({
        url: 'ball/game',
        method: 'post',
        params: _this.listQuery
      }).then((response) => {
        if (response.code === 200) {
          this.list = response.data.results
          this.total = response.data.totalCount
          // this.closing()
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    closing() {
      const that = this
      this.closeingTask = setInterval(function() {
        that.list.forEach(function(item) {
          item.remainingTime -= 1000
        })
      }, 1000)
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
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
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
          url: 'ball/odds/del?id=' + ids,
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
