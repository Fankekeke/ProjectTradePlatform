<template>
  <a-card :bordered="false" class="card-area" style="width: 1200px;margin: 15px auto 0;">
    <div v-show="!detailFlag">
      <a-breadcrumb style="letter-spacing: 1px">
        <a-breadcrumb-item>
          <a @click="$router.push('/')">首页</a>
        </a-breadcrumb-item>
        <a-breadcrumb-item>我的订单</a-breadcrumb-item>
      </a-breadcrumb>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               style="margin-top: 50px;font-family: SimHei;"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="orderList"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="proNameLabel" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              {{ record.proName }}
            </template>
            {{ record.proName.slice(0, 8) }} ...
          </a-tooltip>
        </template>
        <template slot="orderPriceLabel" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              {{ record.orderPrice }}
            </template>
            {{ record.orderPrice }} <span v-if="record.needDeploy === 1" style="color: #52c41a">+ 30</span>
          </a-tooltip>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon v-if="orderCheck(record.code)" type="folder-open" theme="twoTone" twoToneColor="#4a9ff5" title="下 载" @click="downloadResource(record.code)"/>
          <a-icon v-else type="control" theme="twoTone" twoToneColor="#4a9ff5" title="详 情" @click="detailInit(record.code)"></a-icon>
        </template>
      </a-table>
    </div>
    <order-detail v-show="detailFlag" @back="detailFlag = false" :orderCode="orderCode"/>
  </a-card>
</template>

<script>
import {mapState} from 'vuex'
import OrderDetail from './OrderDetail'

export default {
  name: 'Order',
  components: {OrderDetail},
  computed: {
    ...mapState({
      user: state => state.account.user,
      client: state => state.account.client
    }),
    columns () {
      return [{
        title: '购买服务',
        dataIndex: 'proName',
        scopedSlots: { customRender: 'proNameLabel' }
      }, {
        title: '订单编号',
        dataIndex: 'code'
      }, {
        title: '价格',
        dataIndex: 'orderPrice',
        scopedSlots: { customRender: 'orderPriceLabel' },
        width: 130
      }, {
        title: '下单时间',
        dataIndex: 'orderTime',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '支付方式',
        dataIndex: 'paymentType',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        },
        width: 130
      }, {
        title: '订单状态',
        dataIndex: 'orderStatus',
        width: 130
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'},
        width: 80
      }]
    }
  },
  data () {
    return {
      orderList: [],
      detailFlag: false,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      orderCode: null,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      }
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    orderCheck (orderCode) {
      let reg = RegExp(/AORD-/)
      return reg.test(orderCode)
    },
    downloadResource (orderCode) {
      let downloadUrl = this.orderCheck(orderCode) ? '/cos/annex-order/download' : '/cos/order-info/download'
      this.$post(downloadUrl, {orderCode, userCode: this.client.code}).then(() => {})
    },
    detailInit (orderCode) {
      this.orderCode = orderCode
      this.detailFlag = true
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/bulletin-info/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      params.size = this.pagination.defaultPageSize
      params.current = this.pagination.defaultCurrent
      // params.userCode = this.client.code
      params.userCode = 'AK-1621770978182'
      this.$get(`/cos/user-info/order/page`, {...params}).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.orderList = data.records
        this.pagination = pagination
      })
    }
  }
}
</script>

<style scoped>

</style>
