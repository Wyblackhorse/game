<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" :placeholder="$t('page.player.username')" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.invitationCode" :placeholder="$t('page.player.invitationCode')" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t('table.search') }}
      </el-button>
      <el-button v-if="hasAuth('/ball/player/add')" class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-edit" @click="handleCreate">
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
      <el-table-column :label="$t('page.player.username')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.balance')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.balance|balance }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.theLastIp')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.theLastIp }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.superiorName')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.superiorName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.invitationCode')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.invitationCode }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.directlySubordinateNum')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.directlySubordinateNum }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.groupSize')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.groupSize }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.accountType')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.player.accountTypes')[row.accountType-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.vipLevel')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.vipLevel }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.status')" align="center">
        <template slot-scope="{row}">
          <span>{{ $t('form.statusOper')[row.status-1].name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('page.player.createdAt')" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createdAt|formatDate('y-M-d') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" min-width="120px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              {{$t('table.actions')}}<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="hasAuth('/ball/player/edit')" @click.native="handleCommandData('a1',row)">{{$t('page.player.edit')}}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/edit_pwd')" @click.native="handleCommandData('a2',row)">{{$t('page.player.editPwd')}}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/edit_pay_pwd')" @click.native="handleCommandData('a3',row)">{{$t('page.player.editPayPwd')}}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/status')" @click.native="handleCommandData('a4',row)">{{ $t('form.statusOper')[row.status%2].name }}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/add_balance')" @click.native="handleCommandData('a5',row)">{{$t('page.player.editBalance')}}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/captcha_pass')" @click.native="handleCommandData('a6',row)">{{$t('page.player.editBalanceOut')}}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-dropdown trigger="click">
            <el-button type="success" size="mini">
              {{$t('table.search')}}<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="hasAuth('/ball/player/info')" @click.native="handleCommandData('a7',row)">{{$t('table.search')}}</el-dropdown-item>
                <el-dropdown-item v-if="hasAuth('/ball/player/log')" @click.native="handleCommandData('a8',row)">{{$t('page.player.balanceLog')}}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="$t('form.textMap')[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item v-if="showEdit" :label="$t('page.player.username')" prop="username">
          {{ temp.username }}
        </el-form-item>
        <el-form-item v-if="showAdd" :label="$t('page.player.username')" prop="username" :error.sync="tempError.username">
          <el-input v-model="temp.username" />
        </el-form-item>
        <el-form-item v-if="showEditPwd||showAdd" :label="$t('page.player.pwd')" prop="editPwd">
          <el-input v-model="temp.editPwd" />
        </el-form-item>
        <el-form-item v-if="showEdit||showAdd" :label="$t('page.player.phone')" prop="phone">
          <el-input v-model="temp.phone" />
        </el-form-item>
        <el-form-item v-if="showEdit||showAdd" label="email" prop="eMail">
          <el-input v-model="temp.eMail" />
        </el-form-item>
        <el-form-item v-if="showEdit||showAdd" :label="$t('page.player.superiorName')" prop="superiorName" :error="tempError.superiorName">
          <el-input v-model="temp.superiorName">
            <el-button slot="append" icon="el-icon-search" @click="getParentList" />
          </el-input>
        </el-form-item>
        <el-input v-model="temp.superiorId" type="hidden" />
        <el-form-item v-if="showEdit||showAdd" :label="$t('page.player.accountType')" prop="superName">
          <el-select v-model="temp.accountType" style="width: 100%" :placeholder="$t('page.player.accountType')">
            <el-option
              v-for="item in $t('form.player.accountTypes')"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="showEdit||showAdd" :label="$t('page.player.remark')" prop="superName">
          <el-input v-model="temp.remark" type="textarea" />
        </el-form-item>
        <el-form-item v-if="showEditAddBalance" :label="$t('page.player.editBalance')">
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
    <el-dialog width="70%" :title="$t('page.player.balanceLog')" :visible.sync="dialogLogisible">
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
        <el-table-column :label="$t('balanceChange.initMoney')" prop="initMoney" align="center">
          <template slot-scope="{row}">
            <span>{{ row.initMoney|balance }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('balanceChange.changeMoney')" prop="changeMoney" align="center">
          <template slot-scope="{row}">
            <span>{{ row.changeMoney|balance }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('balanceChange.dnedMoney')" prop="dnedMoney" align="center">
          <template slot-scope="{row}">
            <span>{{ row.dnedMoney|balance }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('balanceChange.balanceChange')" prop="balanceChangeType" align="center">
          <template slot-scope="{row}">
            <span>{{ $t('balanceChange.changeTypes')[row.balanceChangeType-1].name }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('balanceChange.createdAt')" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createdAt|formatDate('y-M-d') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('balanceChange.remark')" prop="remark" align="center">
          <template slot-scope="{row}">
            <span>{{ row.remark }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="totalLog>0" :total="totalLog" :page.sync="listLogQuery.pageNo" :limit.sync="listLogQuery.pageSize" @pagination="log" />
    </el-dialog>
    <el-dialog width="80%" :title="$t('page.player.setParent')" :visible.sync="setParentDialogVisible">
      <el-input v-model="parentListQuery.username" :placeholder="$t('page.player.username')" style="width: 200px;" class="filter-item" @keyup.enter.native="handleParentFilter" />
      <el-input v-model="parentListQuery.invitationCode" :placeholder="$t('page.player.invitationCode')" style="width: 200px;" class="filter-item" @keyup.enter.native="handleParentFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleParentFilter">
        {{ $t('table.search') }}
      </el-button>
      <el-table
        :key="parentTableKey"
        v-loading="parentListLoading"
        :data="parentList"
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
        <el-table-column :label="$t('page.player.username')" align="center">
          <template slot-scope="{row}">
            <span>{{ row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('page.player.superiorName')" align="center">
          <template slot-scope="{row}">
            <span>{{ row.superiorName }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('page.player.invitationCode')" align="center">
          <template slot-scope="{row}">
            <span>{{ row.invitationCode }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('page.player.sub')" align="center">
          <template slot-scope="{row}">
            <span>{{ row.directlySubordinateNum }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('page.player.group')" align="center">
          <template slot-scope="{row}">
            <span>{{ row.groupSize }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('page.player.accountType')" align="center">
          <template slot-scope="{row}">
            <span>{{ $t('form.player.accountTypes')[row.accountType-1].name }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('page.player.vipLevel')" align="center">
          <template slot-scope="{row}">
            <span>{{ row.vipLevel }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('page.player.status')" align="center">
          <template slot-scope="{row}">
            <span>{{ $t('form.statusOper')[row.status-1].name }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.actions')" align="center" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="chooseParent(row)">{{$t('button.choose')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="parentTotal>0" :total="parentTotal" :page.sync="parentListQuery.pageNo" :limit.sync="parentListQuery.pageSize" @pagination="getParentList" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="setParentDialogVisible = false">
          {{ $t('table.cancel') }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog width="80%" :title="$t('form.info')" :visible.sync="infoDialogVisible">
      <el-form ref="infoForm" :visible.sync="infoDialogVisible" :model="temp" label-position="left" label-width="100px">
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.username')" prop="username">
              {{ temp.username }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.superiorName')" prop="superiorName">
              {{ temp.superiorName }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.invitationCode')" prop="invitationCode">
              {{ temp.invitationCode }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.accountType')" prop="accountType">
              {{ temp.accountType!=null?$t('form.player.accountTypes')[temp.accountType-1].name:'' }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.sub')" prop="directlySubordinateNum">
              {{ temp.directlySubordinateNum }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="email" prop="eMail">
              {{ temp.eMail }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.vipLevel')" prop="level">
              {{ temp.levelStr }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.group')" prop="groupSize">
              {{ temp.groupSize }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.phone')" prop="phone">
              {{ temp.phone }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.balance')" prop="balance">
              {{ temp.balance|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.version')" prop="level">
              {{ temp.version }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.cumulativeReflect')" prop="cumulativeReflect">
              {{ temp.cumulativeReflect|balance }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.reflectTimes')" prop="reflectTimes">
              {{ temp.reflectTimes }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.maxReflect')" prop="maxReflect">
              {{ temp.maxReflect|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.firstReflect')" prop="firstReflect">
              {{ temp.firstReflect|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.cumulativeTopUp')" prop="cumulativeTopUp">
              {{ temp.cumulativeTopUp|balance }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.topUpTimes')" prop="reflectTimes">
              {{ temp.topUpTimes }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.maxTopUp')" prop="maxReflect">
              {{ temp.maxTopUp|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.firstTopUp')" prop="reflectTimes">
              {{ temp.firstTopUp }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.firstTopUpTime')" prop="maxReflect">
              {{ temp.firstTopUpTime|balance }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.onLineTopUp')" prop="firstReflect">
              {{ temp.onLineTopUp|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.offlineTopUp')" prop="cumulativeTopUp">
              {{ temp.offlineTopUp|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.artificialAdd')" prop="artificialAdd">
              {{ temp.artificialAdd }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.artificialSubtract')" prop="artificialSubtract">
              {{ temp.artificialSubtract|balance }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.cumulativeWinning')" prop="cumulativeWinning">
              {{ temp.cumulativeWinning|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.accumulativeBet')" prop="accumulativeBet">
              {{ temp.accumulativeBet|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.cumulativeBackWater')" prop="cumulativeBackWater">
              {{ temp.cumulativeBackWater }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.promoteIncome')" prop="promoteIncome">
              {{ temp.promoteIncome|balance }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.cumulativeQr')" prop="cumulativeQr">
              {{ temp.cumulativeQr|balance }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('page.player.needQr')" prop="needQr">
              {{ temp.needQr|balance }}
            </el-form-item>
          </el-col>
          <!--<el-col :span="6">-->
            <!--<el-form-item :label="$t('page.player.')" prop="reflectTimes">-->
              <!--&lt;!&ndash;{{ temp.reflectTimes }}&ndash;&gt;-->
            <!--</el-form-item>-->
          <!--</el-col>-->
          <!--<el-col :span="6">-->
            <!--<el-form-item :label="$t('page.player.')" prop="maxReflect">-->
              <!--&lt;!&ndash;{{ temp.maxReflect|balance }}&ndash;&gt;-->
            <!--</el-form-item>-->
          <!--</el-col>-->
        </el-row>
        <!--<el-row>-->
          <!--<el-col :span="6">-->
            <!--<el-form-item :label="$t('page.player.')" prop="firstReflect">-->
              <!--&lt;!&ndash;{{ temp.firstReflect|balance }}&ndash;&gt;-->
            <!--</el-form-item>-->
          <!--</el-col>-->
        <!--</el-row>-->
      </el-form>
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
      showAdd: false,
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
      temp: {
        id: 1,
        username: '',
        password: '',
        nickname: '',
        roleId: ''
      },
      tempError: {
      },
      dialogFormVisible: false,
      dialogStatus: 0,
      rules: {
        username: [{ required: true, message: this.$t('form.player.requireUserName'), trigger: 'blur' }],
        editPwd: [{ required: true, message: this.$t('form.player.requirePwd'), trigger: 'blur' }]
      },
      roles: [],
      setParentDialogVisible: false,
      parentTableKey: 99,
      parentList: [],
      parentTotal: 0,
      parentListLoading: false,
      parentListQuery: {
        pageNo: 1,
        pageSize: 20,
        playerId: 0
      },
      infoDialogVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    playerInfo(row) {
      this.infoDialogVisible = true
      request({
        url: 'ball/player/info',
        method: 'post',
        params: {
          playerId: row.id
        }
      }).then((response) => {
        if (response.code === 200) {
          this.temp = response.data
        }
      })
    },
    witchEditShow(witch) {
      this.showEdit = false
      this.showEditPwd = false
      this.showEditPayPwd = false
      this.showEditAddBalance = false
      this.showEditCaptchaPass = false
      this.showAdd = false
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
          this.playerInfo(row)
          break
        case 'a8':
          this.log(row)
          break
      }
    },
    changeStatus(row) {
      MessageBox.confirm(this.$t('form.player.statusConfirm' + (row.status % 2)), this.$t('tips.importentTitle'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
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
              message: this.$t('messages.successEdit'),
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
    chooseParent(row) {
      if (row.id == this.temp.id) {
        this.$message({
          message: this.$t('form.player.cannotSelf'),
          type: 'error',
          duration: 2 * 1000
        })
        return
      }
      this.temp.superiorId = row.id
      this.temp.superiorName = row.username
      this.setParentDialogVisible = false
    },
    getParentList() {
      this.parentListLoading = true
      this.setParentDialogVisible = true
      const _this = this
      request({
        url: 'ball/player',
        method: 'post',
        params: _this.parentListQuery
      }).then((response) => {
        if (response.code === 200) {
          this.parentList = response.data.results
          this.parentTotal = response.data.totalCount
        }
        this.parentListLoading = false
      }).catch(() => {
        this.parentListLoading = false
      })
    },
    handleParentFilter() {
      this.parentListQuery.pageNo = 1
      this.getParentList()
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
      this.dialogStatus = 0
      this.dialogFormVisible = true
      this.showAdd = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
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
                message: this.$t('messages.successAdd'),
                type: 'success',
                duration: 3 * 1000
              })
            }
          }, (response) => {
            // 处理表单错误提示
            response.data.forEach((item) => {
              this.$set(this.tempError, item.name, this.$t('form.player.' + item.msgKey))
            })
          })
        }
      })
    },
    handleUpdate(row, witch) {
      this.temp = Object.assign({}, row) // copy obj
      this.witchEditShow(witch)
      this.dialogStatus = 1
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
              if (tempData.witch == 4) {
                this.list.splice(index, 1, response.data)
              } else {
                this.list.splice(index, 1, this.temp)
              }
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
          url: 'ball/player/del?id=' + ids,
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
