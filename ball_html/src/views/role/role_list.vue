<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button v-if="hasAuth('/ball/group/add')" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
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
      <el-table-column label="角色名"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="昵称"  align="center">-->
      <!--<template slot-scope="{row}">-->
      <!--<span>{{ row.nickname }}</span>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column label="创建时间"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.createdAt|formatDate('y-M-d h:m:s') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间"  align="center">
        <template slot-scope="{row}">
          <span>{{ row.updatedAt|formatDate('y-M-d h:m:s') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" min-width="200px" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.id!=1 && hasAuth('/ball/group/edit')" type="primary" size="mini" @click="handleUpdate(row)">
            {{ $t('table.edit') }}
          </el-button>
          <el-button v-if="row.id!=1 && hasAuth('/ball/group/del')" size="mini" type="danger" @click="handleDelete(row,$index)">
            {{ $t('table.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog width="60%" top="5vh" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 100%; padding-left:50px;padding-right: 50px">
        <el-form-item label="角色名" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="权限">
          <el-tree
            ref="authTree"
            width="100%"
            :data="auths"
            show-checkbox
            node-key="id"
            accordion
            :default-expanded-keys="authsExpanded"
            :default-checked-keys="authsChecked"
            :props="defaultProps"
            :render-content="renderContent"
            @node-expand="handleExpand"
          />
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
        name: ''
      },
      temp: {
        id: 1,
        name: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        name: [{ required: true, message: '角色名必填', trigger: 'blur' }]
      },
      authsExpanded: [9],
      authsChecked: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      auths: this.$t('role')
    }
  },
  created() {
    this.getList()
  },
  mounted() {
    this.changeCss()
  },
  methods: {
    handleExpand() {
      // 节点被展开时触发的事件
      // 因为该函数执行在renderContent函数之前，所以得加this.$nextTick()
      this.$nextTick(() => {
        this.changeCss()
      })
    },
    changeCss() {
      // levelname是上面的最底层节点的名字
      var levelName = document.getElementsByClassName('levelname')
      for (var i = 0; i < levelName.length; i++) {
        // cssFloat 兼容 ie6-8  styleFloat 兼容ie9及标准浏览器
        levelName[i].parentNode.style.cssFloat = 'left'
        // 最底层的节点，包括多选框和名字都让他左浮动
        levelName[i].parentNode.style.styleFloat = 'left'
        // if (i > 0) {
        //   levelName[i].parentNode.style.paddingLeft = '1px'
        // }
      }
    },
    renderContent(h, { node, data, store }) {
      console.log(node)
      let classname = ''
      if (node.level === 3) {
        classname = 'levelname'
      }
      return h('p', { class: classname }, node.label)
    },
    getList() {
      this.listLoading = true
      const _this = this
      request({
        url: 'ball/group',
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
      this.authsChecked = []
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        // console.log(this.temp)
        const checkedAuth = this.$refs['authTree'].getCheckedKeys()
        const checkedAuthHalf = this.$refs['authTree'].getHalfCheckedKeys()
        checkedAuthHalf.forEach(function(item) {
          checkedAuth.push(item)
        })
        if (checkedAuth.length === 0) {
          this.$message({
            message: '请勾选权限~',
            type: 'error',
            duration: 3 * 1000
          })
          return
        }
        this.temp.authsId = checkedAuth
        if (valid) {
          request({
            url: 'ball/group/add',
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
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      const that = this
      request({
        url: 'ball/group/edit?roleId=' + row.id,
        method: 'get'
      }).then((response) => {
        if (response.code === 200) {
          const newCheck = []
          response.data.map((i, n) => {
            const node = that.$refs['authTree'].getNode(i)
            if (node.isLeaf) {
              newCheck.push(i)
            }
          })
          this.authsChecked = newCheck
        }
      })
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      const checkedAuth = this.$refs['authTree'].getCheckedKeys()
      const checkedAuthHalf = this.$refs['authTree'].getHalfCheckedKeys()
      checkedAuthHalf.forEach(function(item) {
        checkedAuth.push(item)
      })
      if (checkedAuth.length === 0) {
        this.$message({
          message: '请勾选权限~',
          type: 'error',
          duration: 3 * 1000
        })
        return
      }
      this.temp.authsId = checkedAuth
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          request({
            url: 'ball/group/edit',
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
          url: 'ball/group/del?id=' + ids,
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
  .el-tree-node__content{
    height: 26px !important;
    line-height: 26px !important;
  }
</style>
