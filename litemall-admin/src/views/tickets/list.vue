<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input ref="idInput" v-model="listQuery.id" clearable class="filter-item" style="width: 200px;" placeholder="请输入门票编号" />
      <el-button v-permission="['GET /admin/tickets/list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="门票编号" prop="id" sortable />

      <el-table-column align="center" label="门票标题" prop="goodsName" />

      <el-table-column align="center" label="门票价格" prop="price" />

      <el-table-column align="center" label="售出时间" prop="createTime" />

      <el-table-column align="center" label="使用时间" prop="useTime" />

      <el-table-column align="center" label="是否已使用" prop="used">
        <template slot-scope="scope">
          <el-tag :type="scope.row.used ? 'success' : 'error' ">{{ scope.row.used ? '已使用' : '未使用' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="!scope.row.used" v-permission="['POST /admin/tickets/use']" type="primary" size="mini" @click="handleUpdate(scope.row)">核销</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

  </div>
</template>

<style>

</style>

<script>
import { listTicket, useTicket } from '@/api/tickets'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'TicketList',
  components: { Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        id: undefined,
        sort: 'create_time',
        order: 'desc'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      this.listQuery.id = this.listQuery.id ? parseInt(this.listQuery.id) : undefined
      listTicket(this.listQuery)
        .then(response => {
          this.list = response.data.data.list
          this.total = response.data.data.total
          this.listLoading = false
          this.$refs['idInput'].focus()
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleUpdate(row) {
      this.$confirm('核销后该门票将无法使用，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        useTicket(row)
          .then((res) => {
            for (const ticket of this.list) {
              if (ticket.id === row.id) {
                ticket.used = true
                ticket.useTime = res.data.data.useTime
                break
              }
            }
            this.$message({
              type: 'success',
              message: '核销成功!'
            })
          })
      })
    }
  }
}
</script>
