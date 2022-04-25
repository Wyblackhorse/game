<template>
  <div class="app-container">
    <div class="filter-container">
      <!--<el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->
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
      <el-table-column label="会员账号"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.playerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="顶级父代"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.superPlayerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="ip"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.ip}}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录时间"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.createdAt|formatDate}}</span>
        </template>
      </el-table-column>
      <!--<el-table-column :label="$t('table.actions')" align="center" min-width="200px" class-name="small-padding fixed-width">-->
        <!--<template slot-scope="{row,$index}">-->
          <!--<el-button v-if="row.id!=1 && hasAuth('//ball/log/login/edit')" type="primary" size="mini" @click="handleUpdate(row)">-->
            <!--{{ $t('table.edit') }}-->
          <!--</el-button>-->
          <!--<el-button v-if="row.id!=1 && hasAuth('//ball/log/login/del')" size="mini" type="danger" @click="handleDelete(row,$index)">-->
            <!--{{ $t('table.delete') }}-->
          <!--</el-button>-->
        <!--</template>-->
      <!--</el-table-column>-->
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
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
        id: 1,
        username: '',
        password: '',
        nickname: '',
        roleId: ''
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
      roles: []
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
        url: '/ball/log/login',
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
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
