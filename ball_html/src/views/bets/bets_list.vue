<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.orderNo" placeholder="订单号" style="width: 200px;" class="filter-item"
                @keyup.enter.native="handleFilter"/>
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
      <el-table-column label="id" prop="id" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.bets.orderNo')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.orderNo }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="会员账号"  align="center">-->
      <!--<template slot-scope="{row}">-->
      <!--<span>{{ row.nickname }}</span>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--<el-table-column label="赛事信息" align="center">-->
      <!--<template slot-scope="{row}">-->
      <!--<span>{{ row.gameinfo }}</span>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--<el-table-column label="开赛时间" align="center">-->
      <!--<template slot-scope="{row}">-->
      <!--<span>{{ row.startTime|formatDate('y-M-d h:m:s') }}</span>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--<el-table-column label="开赛时间" align="center">-->
      <!--<template slot-scope="{row}">-->
      <!--<span>{{ row.finishTime|formatDate('y-M-d h:m:s') }}</span>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column :label="$t('page.bets.remark')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.remark }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.bets.betType')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.bets.betTypes')[row.betType-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.bets.betMoney')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.betMoney|balance }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.bets.handMoney')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.handMoney|balance }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.bets.winningAmount')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.winningAmount|balance }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.bets.status')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.bets.status')[row.status-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" min-width="200px"
                       class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="hasAuth('/ball/bets/info')" type="primary" size="mini" @click="betInfo(row)">
            {{ $t('table.info') }}
          </el-button>
          <el-button v-if="row.status ===1 && hasAuth('/ball/bets/undo')" type="primary" size="mini"
                     @click="unbet(row)">
            撤单
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize"
                @pagination="getList"/>

    <el-dialog width="80%" :title="$t('form.info')" :visible.sync="infoDialogVisible">
      <el-form v-if="temp.game" ref="infoForm" :visible.sync="infoDialogVisible" :model="temp" label-position="left"
               label-width="100px">
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.allianceLogo')">
              {{ temp.game.allianceLogo }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.allianceName')">
              {{ temp.game.allianceName }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.mainLogo')">
              {{ temp.game.mainLogo }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.mainName')">
              {{ temp.game.mainName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.guestLogo')">
              {{ temp.game.guestLogo }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.guestName')">
              {{ temp.game.guestName }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.startTime')">
              {{ temp.game.startTime | formatDate }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.score')" prop="score">
              {{ temp.game.score }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider></el-divider>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.game.score')">
              {{ temp.odds.scoreHome }}-{{ temp.odds.scoreAway }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.odds.gameType')">
              {{ $t('form.odds.gameType')[temp.odds.gameType-1].name }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.odds.lossPerCent')">
              {{ temp.odds.lossPerCent }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.odds.antiPerCent')">
              {{ temp.odds.antiPerCent }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider></el-divider>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.username')">
              {{ temp.player.username }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.balance')">
              {{ temp.player.balance|balance }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.bets.orderNo')">
              {{ temp.betinfo.orderNo }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.bets.betMoney')">
              {{ temp.betinfo.betMoney|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.bets.handMoney')">
              {{ temp.betinfo.handMoney|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.bets.winningAmount')">
              {{ temp.betinfo.winningAmount|balance }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.bets.status')">
              {{ $t('form.bets.status')[temp.betinfo.status-1].name }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('page.bets.remark')">
              {{ temp.betinfo.remark }}
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
  import {MessageBox} from 'element-ui'
  import store from '@/store'

  export default {
    name: 'ComplexTable',
    components: {Pagination},
    filters: {
      nodata: function (value) {
        if (!value) return ''
        return value
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
          pageSize: 20
        },
        temp: {},
        dialogFormVisible: false,
        dialogStatus: '',
        infoDialogVisible: false
      }
    },
    created() {
      this.getList()
    },
    methods: {
      betInfo(row) {
        this.infoDialogVisible = true
        request({
          url: 'ball/bets/info',
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
      getList() {
        this.listLoading = true
        const _this = this
        request({
          url: 'ball/bets',
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
        const {prop, order} = data
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
      resetTemp() {
        this.temp = {}
      },
      unbet(row) {
        MessageBox.confirm(this.$t('form.bets.unbetConfirm'), this.$t('tips.importentTitle'), {
          confirmButtonText: this.$t('button.ok'),
          cancelButtonText: this.$t('button.cancel'),
          type: 'warning'
        }).then(() => {
          request({
            url: 'ball/bets/undo',
            method: 'post',
            data: {id: row.id}
          }).then((response) => {
            if (response.code === 200) {
              const index = this.list.findIndex(v => v.id === row.id)
              this.list[index].status = 3
              this.$notify({
                message: this.$t('form.bets.unbetSuccess'),
                type: 'success',
                duration: 2 * 1000
              })
            }
          })
        })

      },
      getSortClass: function (key) {
        const sort = this.listQuery.sort
        return sort === `+${key}` ? 'ascending' : 'descending'
      }
    }
  }
</script>
