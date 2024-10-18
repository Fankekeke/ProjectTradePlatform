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
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.paymentType">
                  <a-select-option value="-1">全部</a-select-option>
                  <a-select-option value="1">支付宝</a-select-option>
                  <a-select-option value="2">微信</a-select-option>
                  <a-select-option value="3">平台下单</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="账 户"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
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
      <!--      <div class="operator">-->
      <!--        <a-button @click="batchDelete">佣金分配</a-button>-->
      <!--      </div>-->
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
              {{ record.content }}
            </template>
            {{ record.content.slice(0, 8) }} ...
          </a-tooltip>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="file-search" @click="view(record)" title="查 看"></a-icon>
        </template>
      </a-table>
    </div>
    <evaluation-info :evaluation-show="evaluationView.show" :evaluation-data="evaluationView.data" @close="evaluationView.show = false"></evaluation-info>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import EvaluationInfo from './EvaluationInfo'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'evaluation',
  components: {RangeDate, EvaluationInfo},
  data () {
    return {
      advanced: false,
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      evaluationView: {
        data: {},
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
    columns () {
      return [{
        title: '项目名称',
        dataIndex: 'proName',
        width: 150,
        ellipsis: true
      }, {
        title: '项目类型',
        dataIndex: 'proType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="cyan">仅后台</a-tag>
            case 2:
              return <a-tag color="cyan">前后台</a-tag>
            case 3:
              return <a-tag color="cyan">小程序</a-tag>
            case 4:
              return <a-tag color="cyan">安卓</a-tag>
            default:
              return text
          }
        },
        width: 150
      }, {
        title: '购买人',
        dataIndex: 'account',
        width: 150,
        ellipsis: true
      }, {
        title: '支付方式',
        dataIndex: 'paymentType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="cyan">支付宝</a-tag>
            case 2:
              return <a-tag color="cyan">微信</a-tag>
            case 3:
              return <a-tag color="cyan">平台下单</a-tag>
            default:
              return text
          }
        },
        width: 130
      }, {
        title: '价格',
        dataIndex: 'orderPrice',
        customRender: (text, row, index) => {
          if (row.needDeploy !== null && row.needDeploy === 1) {
            return '￥' + (30 + row.orderPrice)
          } else {
            return '￥' + row.orderPrice
          }
        },
        width: 130
      }, {
        title: '评分',
        dataIndex: 'score',
        customRender: (text, row, index) => {
          return <a-rate default-value={text} disabled />
        }
      }, {
        title: '评价内容',
        dataIndex: 'content',
        scopedSlots: { customRender: 'contentShow' },
        ellipsis: true
      }, {
        title: '评价日期',
        dataIndex: 'date',
        ellipsis: true
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    view (row) {
      this.evaluationView.show = true
      this.evaluationView.data = row
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
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
          that.$delete('/cos/evaluation/' + ids).then(() => {
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
      this.$get('/cos/evaluation/page', {
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
