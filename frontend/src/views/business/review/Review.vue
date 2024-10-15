<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="支付方式"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.tradeType">
                  <a-select-option value="-1">全部</a-select-option>
                  <a-select-option value="1">支付宝</a-select-option>
                  <a-select-option value="2">微信</a-select-option>
                  <a-select-option value="3">其他</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="账 户"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.account"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="contentShow" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              {{ record.remarks }}
            </template>
            {{ record.remarks.slice(0, 8) }} ...
          </a-tooltip>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="property-safety" theme="twoTone" v-if="record.review === 2" @click="view(record)" title="转 账"></a-icon>
          &nbsp;
          <a-icon type="container" theme="twoTone" v-if="record.review === 3" @click="view(record)" title="查 看"></a-icon>
          &nbsp;
          <a-icon type="clock-circle" v-if="record.review === 1" @click="checkAfterTime(record)" title="受 理"/>
        </template>
      </a-table>
    </div>
    <review-reade :reviewVisiable="review.show" :reviewData="review.data" @close="reviewClose" @success="reviewSuccess"></review-reade>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import ReviewReade from "./ReviewReade";
import {mapState} from 'vuex'
import moment from 'moment'

moment.locale('zh-cn')

export default {
  name: 'Review',
  components: {RangeDate, ReviewReade},
  data() {
    return {
      advanced: false,
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      review: {
        data: null,
        show: false
      },
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
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns() {
      return [{
        title: '申请账户',
        dataIndex: 'account',
        width: 150,
        ellipsis: true
      }, {
        title: '交易类型',
        dataIndex: 'tradeType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="cyan">支付宝</a-tag>
            case 2:
              return <a-tag color="cyan">微信</a-tag>
            case 3:
              return <a-tag color="cyan">其他</a-tag>
            default:
              return text
          }
        },
        width: 150
      }, {
        title: '提现金额',
        dataIndex: 'balance',
        customRender: (text, row, index) => {
          return '￥' + row.balance
        },
        width: 130
      }, {
        title: '收款码',
        dataIndex: 'receiving',
        customRender: (text, record, index) => {
          if (!record.receiving) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={180} icon="user" src={ 'http://127.0.0.1:9528/imagesWeb/'+record.receiving } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9528/imagesWeb/'+record.receiving } />
          </a-popover>
        },
      }, {
        title: '备注',
        dataIndex: 'remarks',
        scopedSlots: {customRender: 'contentShow'},
        ellipsis: true
      }, {
        title: '当前状态',
        dataIndex: 'review',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="cyan">审核中</a-tag>
            case 2:
              return <a-tag color="pink">正在交易</a-tag>
            case 3:
              return <a-tag color="green">交易完成</a-tag>
            default:
              return text
          }
        },
        width: 150
      }, {
        title: '申请日期',
        dataIndex: 'date',
        ellipsis: true
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted() {
    this.fetch()
  },
  methods: {
    reviewSuccess () {
      this.$message.success('转账提交成功')
    },
    reviewClose () {
      this.review.data = null
      this.review.show = false
    },
    view(row) {
      this.review.data = row
      this.review.show = true
    },
    checkAfterTime(row) {
      let takeReview = row
      takeReview.review = 2
      this.$put('/cos/take-review', Object.assign({},takeReview)).then((r) => {
        this.$message.success('受理成功')
        this.fetch()
      })
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    handleDeptChange(value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete() {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk() {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/evaluation/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel() {
          that.selectedRowKeys = []
        }
      })
    },
    search() {
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
    reset() {
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
    handleTableChange(pagination, filters, sorter) {
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
    fetch(params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      if (params.sendType === undefined) {
        delete params.sendType
      }
      if (params.messageType === undefined) {
        delete params.messageType
      }
      this.$get('/cos/take-review/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
