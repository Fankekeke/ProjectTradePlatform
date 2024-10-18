<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="附件名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.annexName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="附件类型"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.annexType" allowClear>
                  <a-select-option value="1">论文</a-select-option>
                  <a-select-option value="2">PPT</a-select-option>
                  <a-select-option value="3">任务书</a-select-option>
                  <a-select-option value="4">开题报告</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="支付方式"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.paymentType" allowClear>
                  <a-select-option value="1">支付宝</a-select-option>
                  <a-select-option value="2">微信</a-select-option>
                  <a-select-option value="3">平台下单</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="6" :sm="24" >
                <a-form-item
                  label="用 户"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-input v-model="queryParams.account"/>
                </a-form-item>
              </a-col>
            </template>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
            <a @click="toggleAdvanced" style="margin-left: 8px">
              {{advanced ? '收起' : '展开'}}
              <a-icon :type="advanced ? 'up' : 'down'" />
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button @click="batchDelete">删除</a-button>
      </div>
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
        <!-- 附件名称插槽 -->
        <template slot="annexNameShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.annexName }}
              </template>
              {{ record.annexName.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="cloud" @click="view(record)" title="查 看" style="margin-right: 15px"></a-icon>
        </template>
      </a-table>
    </div>
    <annex-info
      @close="handleAnnexViewClose"
      :annexShow="annexView.show"
      :annexData="annexView.data">
    </annex-info>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import {mapState} from 'vuex'
import moment from 'moment'
import AnnexInfo from './AnnexInfo'
import SingleChoice from "../../../components/project/SingleChoice";
moment.locale('zh-cn')

export default {
  name: 'Annex',
  components: {SingleChoice, AnnexInfo, RangeDate},
  data () {
    return {
      advanced: false,
      annexView: {
        show: false,
        data: null
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '购买人',
        dataIndex: 'account'
      }, {
        title: '订单编号',
        dataIndex: 'code'
      }, {
        title: '附件名称',
        dataIndex: 'annexName',
        scopedSlots: { customRender: 'annexNameShow' }
      }, {
        title: '附件类型',
        dataIndex: 'annexType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="cyan">论文</a-tag>
            case 2:
              return <a-tag color="cyan">PPT</a-tag>
            case 3:
              return <a-tag color="cyan">任务书</a-tag>
            case 4:
              return <a-tag color="cyan">开题报告</a-tag>
            default:
              return text
          }
        }
      }, {
        title: '价格',
        dataIndex: 'price',
        customRender: (text, row, index) => {
          return '￥' + text
        }
      }, {
        title: '支付方式',
        dataIndex: 'paymentType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag>支付宝</a-tag>
            case 2:
              return <a-tag>微信</a-tag>
            case 3:
              return <a-tag>平台下单</a-tag>
            default:
              return text
          }
        }
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
      this.annexView.show = true
      this.annexView.data = row
    },
    handleAnnexViewClose () {
      this.annexView.show = false
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
          that.$delete('/cos/annex-order/' + ids).then(() => {
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
      if (params.annexType === undefined) {
        delete params.annexType
      }
      if (params.paymentType === undefined) {
        delete params.paymentType
      }
      this.$get('/cos/annex-order/page', {
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
