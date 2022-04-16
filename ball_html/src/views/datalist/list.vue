<template>
  <div class="mixin-components-container">
    <el-row>
      <div class="filter-container">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
          添加
        </el-button>
        <el-button v-if="user.roles=='admin'" class="filter-item" style="margin-left: 10px;" type="warning" icon="el-icon-setting" @click="handleSetc">
          设置C
        </el-button>
       <span v-if="user.roles=='admin'">当前C值:{{valuec}}</span>
      </div>
    </el-row>
    <el-row>
      <!--请求二维码地址={服务器地址}+/qr/+{id},例如:http://localhost:6777/qr/1-->
    </el-row>
    <el-row>
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
      >
        <el-table-column label="ID" prop="id" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="A" prop="orderNo" align="center">
          <template slot-scope="{row}">
            <span>{{ row.a }}</span>
          </template>
        </el-table-column>
        <el-table-column label="B" prop="orderNo" align="center">
          <template slot-scope="{row}">
            <span>{{ row.b }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="orderNo" align="center">
          <template slot-scope="{row}">
            <span>{{ row.used==0?'未提取':'已提取' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="提取时间" prop="orderNo" align="center">
          <template slot-scope="{row}">
            <span>{{ row.usedTime|formatDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="orderNo" align="center">
          <template slot-scope="{row}">
            <span>{{ row.remark }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" prop="orderNo" align="center">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleUpdate(row)">
              修改
            </el-button>
            <el-button v-if="user.roles=='admin'" size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />
    </el-row>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item v-if="dialogStatus==='create'||user.roles=='admin'" label="A" prop="a">
          <el-input v-model="temp.a" />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='create'||user.roles=='admin'" label="B" prop="b">
          <el-input v-model="temp.b" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="temp.remark" />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='update' && user.roles=='admin' " label="提取状态" prop="used">
          <el-select v-model="temp.used" placeholder="提取状态">
            <el-option
              v-for="item in usedStatus"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
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
    <el-dialog title="设置C" :visible.sync="dialogSetcVisible">
      <el-form ref="dataFormc" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item label="C" prop="c">
          <el-input v-model="temp.c" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogSetcVisible = false">
          {{ $t('table.cancel') }}
        </el-button>
        <el-button type="primary" @click="updateValuec">
          {{ $t('table.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import Pagination from '@/components/Pagination'
import store from '@/store'
import { MessageBox } from 'element-ui'
export default {
  name: 'ComponentChatroom',
  components: { Pagination },
  data() {
    return {
      user: store.getters.user,
      total: 0,
      useCounts: 0,
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改',
        create: '创建'
      },
      temp: {
        name: '',
        type: '',
        amount: 0,
        remark: ''
      },
      list: [
      ],
      listLoading: false,
      tableKey: 0,
      listQuery: {
        pageNo: 1,
        pageSize: 20
      },
      dialogSetcVisible: false,
      serverUrl: '',
      valuec: '',
      usedStatus: [{ name: '未提取', value: 0 }, { name: '已提取', value: 1 }],
      rules: {
        a: [
          {
            required: true,
            message: '必须输入A'
          }
        ],
        b: [
          {
            required: true,
            message: '必须输入B'
          }
        ],
        c: [
          {
            required: true,
            message: '必须输入C'
          }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getvaluec()
    // this.getServerUrl()
  },
  mounted() {
  },
  methods: {
    resetTemp() {
      this.temp = {
        vx: '',
        rooms: ''
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
        if (valid) {
          request({
            url: 'abcdata/addab',
            method: 'post',
            data: this.temp
          }).then((response) => {
            if (response.code === 200) {
              this.dialogFormVisible = false
              // this.list.unshift(response.data)
              this.getList()
              this.$message({
                message: response.msg,
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
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData: function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          const post = {
            id: tempData.id,
            remark: tempData.remark
          }
          let posturl = 'abcdata/edit_user'
          if (this.user.roles === 'admin') {
            posturl = 'abcdata/edit'
            post.a = tempData.a
            post.b = tempData.b
            post.used = tempData.used
          }
          request({
            url: posturl,
            method: 'post',
            data: post
          }).then((response) => {
            if (response.code === 200) {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.dialogFormVisible = false
              this.list.splice(index, 1, this.temp)
              this.$message({
                message: response.msg,
                type: 'success',
                duration: 2 * 1000
              })
            }
          }).catch(() => {
          })
        }
      })
    },
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },
    getList() {
      this.listLoading = true
      const _this = this
      request({
        url: 'abcdata/list',
        params: _this.listQuery,
        method: 'get'
      }).then((response) => {
        if (response.code === 200) {
          this.list = response.data.results
          this.total = response.data.totalCount
          this.useCounts = response.data.useCounts
        }
        this.listLoading = false
      })
    },
    getvaluec() {
      request({
        url: 'abcdata/getc',
        method: 'get'
      }).then((response) => {
        if (response.code === 200) {
          this.valuec = response.data.c
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
          url: 'chatroom/del/' + ids,
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
    handleSetc() {
      this.dialogSetcVisible = true
      this.$nextTick(() => {
        this.$refs['dataFormc'].clearValidate()
      })
      this.temp = Object.assign({}, {'c': this.valuec}) // copy obj
    },
    updateValuec() {
      this.$refs['dataFormc'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          const post = {
            c: tempData.c
          }
          request({
            url: 'abcdata/setc',
            method: 'post',
            data: post
          }).then((response) => {
            if (response.code === 200) {
              this.$message({
                message: response.msg,
                type: 'success',
                duration: 2 * 1000
              })
              this.valuec = post.c
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
  .mixin-components-container {
    background-color: #f0f2f5;
    padding: 30px;
    min-height: calc(100vh - 84px);
  }
  .component-item{
    min-height: 50px;
  }
  h3{
    margin: 0;
    padding: 0;
  }
  .box-card{
    text-align: center;
  }
  .money{
    font-size: 1.2rem;
    color: red;
  }
  .el-col-4{
    padding-bottom: 20px;
  }
  .el-card__body{
    padding: 0px !important;
  }
  .el-col-5{
    width:20%;
  }
  .image-slots{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 400px;
    background: #f5f7fa;
    color: #909399;
  }
  .qr-code{
    padding:40px 53px;
    height: 340px;
    width: 340px;
  }
</style>
