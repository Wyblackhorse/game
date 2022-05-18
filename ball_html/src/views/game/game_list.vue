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
      <!--<el-table-column label="Logo" align="center">-->
      <!--<template slot-scope="{row}">-->
      <!--<span>{{ row.nickname }}</span>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column :label="$t('page.game.allianceName')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.allianceName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.game.allianceLogo')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.allianceLogo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="" align="center">
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
          <span>{{ $t('page.game.gameStatusArr')[row.gameStatus-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="置顶状态" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('page.game.topStatusArr')[row.top-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" min-width="200px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              {{ $t('table.actions') }}<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="hasAuth('/ball/game/edit')" @click.native="handleCommandData('a1',row)">{{ $t('page.edit') }}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/game/status')" @click.native="handleCommandData('a2',row)">{{ $t('form.statusOper')[row.status%2].name }}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/game/top')" @click.native="handleCommandData('a3',row)">{{ $t('page.game.top') }}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/game/hot')" @click.native="handleCommandData('a4',row)">{{ $t('page.game.hot') }}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/game/even')" @click.native="handleCommandData('a5',row)">{{ $t('page.game.even') }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-button v-if="hasAuth('/ball/game/info')" type="primary" size="mini" @click="gameInfo(row)">
            {{ $t('table.info') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="$t('form.textMap')[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item :label="$t('form.game.homeHalf')" prop="homeHalf">
          <el-input v-model.number="temp.homeHalf" />
        </el-form-item>
        <el-form-item :label="$t('form.game.guestHalf')" prop="guestHalf">
          <el-input v-model.number="temp.guestHalf" />
        </el-form-item>
        <el-form-item :label="$t('form.game.homeFull')" prop="homeFull">
          <el-input v-model.number="temp.homeFull" />
        </el-form-item>
        <el-form-item :label="$t('form.game.guestFull')" prop="guestFull">
          <el-input v-model.number="temp.guestFull" />
        </el-form-item>
        <el-form-item :label="$t('form.game.homeOvertime')" prop="homeOvertime">
          <el-input v-model.number="temp.homeOvertime" />
        </el-form-item>
        <el-form-item :label="$t('form.game.guestOvertime')" prop="guestOvertime">
          <el-input v-model.number="temp.guestOvertime" />
        </el-form-item>
        <el-form-item :label="$t('form.game.homePenalty')" prop="homePenalty">
          <el-input v-model.number="temp.homePenalty" />
        </el-form-item>
        <el-form-item :label="$t('form.game.guestPenalty')" prop="guestPenalty">
          <el-input v-model.number="temp.guestPenalty" />
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
        gameStatus: 0
      },
      temp: {
      },
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
        url: 'ball/game/info',
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
    changeStatus(row) {
      MessageBox.confirm(this.$t('form.game.statusConfirm' + (row.status % 2)), this.$t('tips.importentTitle'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/game/status',
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
    changeStatusTop(row) {
      MessageBox.confirm(this.$t('form.game.statusTop' + (row.top % 2)), this.$t('tips.importentTitle'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/game/top',
          method: 'post',
          data: {
            id: row.id,
            top: row.top
          }
        }).then((response) => {
          if (response.code === 200) {
            const index = this.list.findIndex(v => v.id === row.id)
            this.list[index].top = (row.top == 1 ? 2 : 1)
            this.$notify({
              message: this.$t('messages.successEdit'),
              type: 'success',
              duration: 2 * 1000
            })
          }
        })
      })
    },
    changeStatusHot(row) {
      MessageBox.confirm(this.$t('form.game.statusHot' + (row.hot % 2)), this.$t('tips.importentTitle'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/game/hot',
          method: 'post',
          data: {
            id: row.id,
            hot: row.hot
          }
        }).then((response) => {
          if (response.code === 200) {
            const index = this.list.findIndex(v => v.id === row.id)
            this.list[index].hot = (row.hot == 1 ? 2 : 1)
            this.$notify({
              message: this.$t('messages.successEdit'),
              type: 'success',
              duration: 2 * 1000
            })
          }
        })
      })
    },
    changeStatusEven(row) {
      MessageBox.confirm(this.$t('form.game.statusEven' + (row.even % 2)), this.$t('tips.importentTitle'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: 'ball/game/even',
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
      this.dialogStatus = '1'
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
            url: 'ball/game/edit',
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
