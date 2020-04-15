<template>
  <div class="app-container">
    <ve-line :extend="chartExtend" :data="chartData" :settings="chartSettings" />

    <div class="filter-container">
      <el-button-group>
        <el-button :loading="downloadLoading" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
        <el-button @click="select30">最近30日</el-button>
        <el-button @click="click15">最近15日</el-button>
        <el-button @click="click7">最近7日</el-button>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="dateChanged"
        />
      </el-button-group>

    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="日期" prop="day" />

      <el-table-column align="center" label="订单数" prop="orders" />

      <el-table-column align="center" label="成交商品数" prop="products" />

      <el-table-column align="center" label="成交额" prop="amount" />

      <el-table-column align="center" label="商品浏览量" prop="goodsViews" />

      <el-table-column align="center" label="虚拟商品浏览量" prop="virtualGoodsViews" />

      <el-table-column align="center" label="非虚拟商品浏览量" prop="nonVirtualGoodsViews" />

      <el-table-column align="center" label="小店访问人数" prop="platformViews" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="params.page" :limit.sync="params.limit" @pagination="getStat" />

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>
  </div>
</template>

<script>
import { statComplex } from '@/api/stat'
import VeLine from 'v-charts/lib/line'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  components: { VeLine, BackToTop, Pagination },
  data() {
    return {
      chartData: {},
      chartSettings: {},
      chartExtend: {},
      list: [],
      total: 0,
      listLoading: true,
      dateRange: undefined,
      params: {
        page: 1,
        limit: 20,
        days: 14,
        start: undefined,
        end: undefined
      },
      downloadLoading: false
    }
  },
  created() {
    this.getStat()
  },
  methods: {
    getStat() {
      this.listLoading = true
      statComplex(this.params).then(response => {
        this.chartData = response.data.data.chartData
        this.chartSettings = {
          labelMap: {
            'orders': '订单数',
            'products': '成交商品数',
            'amount': '成交额',
            'goodsViews': '商品浏览量',
            'virtualGoodsViews': '虚拟商品浏览量',
            'nonVirtualGoodsViews': '非虚拟商品浏览量',
            'platformViews': '小店访问人数'
          }
        }
        this.chartExtend = {
          xAxis: { boundaryGap: true }
        }
        this.list = response.data.data.listData.data.list
        this.total = response.data.data.listData.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    click7() {
      this.params.days = 6
      this.params.start = undefined
      this.params.end = undefined
      this.getStat()
    },
    click15() {
      this.params.days = 14
      this.params.start = undefined
      this.params.end = undefined
      this.getStat()
    },
    select30() {
      this.params.days = 29
      this.params.start = undefined
      this.params.end = undefined
      this.getStat()
    },
    dateChanged() {
      this.params.days = undefined
      this.params.start = this.dateRange[0].getTime()
      this.params.end = this.dateRange[1].getTime()
      this.getStat()
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['日期', '订单数', '成交商品数', '成交额', '商品浏览量', '虚拟商品浏览量', '非虚拟商品浏览量', '小店访问人数']
        const filterVal = ['day', 'orders', 'products', 'amount', 'goodsViews', 'virtualGoodsViews', 'nonVirtualGoodsViews', 'platformViews']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '平台统计')
        this.downloadLoading = false
      })
    }
  }

}
</script>
