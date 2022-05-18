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
      <el-table-column :label="$t('form.game.homeHalf')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.homeHalf }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.game.guestHalf')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.guestHalf }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.game.homeFull')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.homeFull }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.game.guestFull')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.guestFull }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.game.homeOvertime')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.homeOvertime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.game.guestOvertime')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.guestOvertime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.game.homePenalty')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.homePenalty }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.game.guestPenalty')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.guestPenalty }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.game.settlementType')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.game.settlementTypes')[row.settlementType].name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('table.actions')"
        align="center"
        min-width="200px"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{row,$index}">
          <el-button v-if="hasAuth('/ball/game/finish/recount')" type="success" size="mini" @click="recount(row)">
            {{ $t('page.game.recount') }}
          </el-button>
          <el-button v-if="hasAuth('/ball/game/finish/rollback')" type="warning" size="mini" @click="rollback(row)">
            {{ $t('page.game.rollback') }}
          </el-button>
          <el-button v-if="hasAuth('/ball/game/finish/info')" type="primary" size="mini" @click="gameInfo(row)">
            {{ $t('table.info') }}
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
    <el-dialog width="80%" :title="$t('form.info')" :visible.sync="infoDialogVisible">
      <el-form ref="infoForm" :visible.sync="infoDialogVisible" :model="temp" label-position="left" label-width="100px">
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.allianceLogo')" prop="allianceLogo">
              {{ temp.allianceLogo }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.allianceName')" prop="allianceName">
              {{ temp.allianceName }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.mainLogo')" prop="mainLogo">
              {{ temp.mainLogo }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.mainName')" prop="mainName">
              {{ temp.mainName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.guestLogo')" prop="guestLogo">
              {{ temp.guestLogo }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.guestName')" prop="guestName">
              {{ temp.guestName }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.startTime')" prop="startTime">
              {{ temp.startTime | formatDate }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.homeHalf')" prop="homeHalf">
              {{ temp.homeHalf }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.guestHalf')" prop="guestHalf">
              {{ temp.guestHalf }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.homeFull')" prop="homeFull">
              {{ temp.homeFull }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.guestFull')" prop="guestFull">
              {{ temp.guestFull }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.homeOvertime')" prop="homeOvertime">
              {{ temp.homeOvertime }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.guestOvertime')" prop="guestOvertime">
              {{ temp.guestOvertime }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.homePenalty')" prop="homePenalty">
              {{ temp.homePenalty }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.guestPenalty')" prop="guestPenalty">
              {{ temp.guestPenalty }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.gameStatus')" prop="gameStatus">
              {{ temp.gameStatus|gameStatusArr }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.top')" prop="top">
              {{ temp.top | topStatusArr }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.hot')" prop="hot">
              {{ temp.hot | hotStatusArr }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.even')" prop="even">
              {{ temp.even | evenStatusArr }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.status')" prop="status">
              {{ temp.status | statusArr }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('form.game.settlementType')" prop="settlementType">
              {{ temp.settlementType | osettlementTypeArr }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="infoDialogVisible = false">
          {{ $t('table.cancel') }}
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

let gthat
export default {
  name: 'ComplexTable',
  components: { Pagination },
  filters: {
    osettlementTypeArr: function(value) {
      if (value == undefined) return ''
      return gthat.$t('form.game.settlementTypes')[value].name
    },
    gameStatusArr: function(value) {
      if (!value) return ''
      return gthat.$t('page.game.gameStatusArr')[value - 1].name
    },
    topStatusArr: function(value) {
      if (!value) return ''
      return gthat.$t('page.game.topStatusArr')[value - 1].name
    },
    hotStatusArr: function(value) {
      if (!value) return ''
      return gthat.$t('page.game.hotStatusArr')[value - 1].name
    },
    evenStatusArr: function(value) {
      if (!value) return ''
      return gthat.$t('page.game.evenStatusArr')[value - 1].name
    },
    statusArr: function(value) {
      if (!value) return ''
      return gthat.$t('page.game.statusArr')[value - 1].name
    }
  },
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
        gameStatus: 1
      },
      temp: {},
      dialogFormVisible: false,
      dialogStatus: 0,
      rules: {
        homeHalf: [{ type: 'number', message: this.$t('form.game.socreMustNumber'), trigger: 'blur' }],
        guestHalf: [{ type: 'number', message: this.$t('form.game.socreMustNumber'), trigger: 'blur' }],
        homeFull: [{ type: 'number', message: this.$t('form.game.socreMustNumber'), trigger: 'blur' }],
        guestFull: [{ type: 'number', message: this.$t('form.game.socreMustNumber'), trigger: 'blur' }],
        homeOvertime: [{ type: 'number', message: this.$t('form.game.socreMustNumber'), trigger: 'blur' }],
        guestOvertime: [{ type: 'number', message: this.$t('form.game.socreMustNumber'), trigger: 'blur' }],
        homePenalty: [{ type: 'number', message: this.$t('form.game.socreMustNumber'), trigger: 'blur' }],
        guestPenalty: [{ type: 'number', message: this.$t('form.game.socreMustNumber'), trigger: 'blur' }]
      },
      closeingTask: null,
      infoDialogVisible: false
    }
  },
  beforeCreate: function() {
    gthat = this
  },
  created() {
    this.getList()
  },
  methods: {
    gameInfo(row) {
      this.infoDialogVisible = true
      request({
        url: 'ball/game/finish/info',
        method: 'post',
        data: {
          id: row.id
        }
      }).then((response) => {
        if (response.code === 200) {
          this.temp = response.data
        }
      })
    },
    handleCommandData(command, row) {
      switch (command) {
        case 'a1':
          this.handleUpdate(row)
          break
        case 'a2':
          this.changeStatus(row)
          break
        case 'a3':
          this.changeStatusTop(row)
          break
        case 'a4':
          this.changeStatusHot(row)
          break
        case 'a5':
          this.changeStatusEven(row)
          break
      }
    },
    recount(row) {
      MessageBox.confirm(this.$t('form.game.recountConfirm'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/game/finish/recount',
          method: 'post',
          data: {
            id: row.id
          }
        }).then((response) => {
          if (response.code === 200) {
            this.$notify({
              message: this.$t('form.game.recountSuccess'),
              type: 'success',
              duration: 2 * 1000
            })
          }
        })
      })
    },
    rollback(row) {
      MessageBox.confirm(this.$t('form.game.rollbackConfirm'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/game/finish/rollback',
          method: 'post',
          data: {
            id: row.id
          }
        }).then((response) => {
          if (response.code === 200) {
            this.$notify({
              message: this.$t('form.game.rollbackSuccess'),
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
        url: 'ball/game/finish',
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
      this.temp = {}
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = '1'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
