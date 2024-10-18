<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="发送方式"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select
                  :allowClear="true"
                  v-model="queryParams.sendType"
                  style="width: 100%">
                  <a-select-option value="1">邮 箱</a-select-option>
                  <a-select-option value="2">短 信</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="消息类型"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select
                  :allowClear="true"
                  v-model="queryParams.messageType"
                  style="width: 100%">
                  <a-select-option value="1">付款通知</a-select-option>
                  <a-select-option value="2">账户验证</a-select-option>
                  <a-select-option value="3">账户验证</a-select-option>
                  <a-select-option value="4">提现通知</a-select-option>
                  <a-select-option value="5">日常通知</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="发送时间"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-range-picker @change="onPickerChange" />
              </a-form-item>
            </a-col>
            <template v-if="advanced">
            </template>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
             <a @click="toggleAdvanced" style="margin-left: 8px">
              {{ advanced ? '收起' : '展开' }}
              <a-icon :type="advanced ? 'up' : 'down'"/>
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
<!--      <div class="operator">-->
<!--        <a-button @click="batchDelete">删除</a-button>-->
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
        <template slot="operation" slot-scope="text, record">
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
        </template>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'messageTip',
  components: {RangeDate},
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
        title: '发送时间',
        dataIndex: 'sendDate',
        ellipsis: true
      }, {
        title: '发送账户',
        dataIndex: 'user',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        },
        ellipsis: true
      }, {
        title: '发送类型',
        dataIndex: 'sendType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="cyan">邮箱</a-tag>
            case 2:
              return <a-tag color="cyan">邮箱</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '消息类型',
        dataIndex: 'messageType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="cyan">付款通知</a-tag>
            case 2:
              return <a-tag color="cyan">账户验证</a-tag>
            case 3:
              return <a-tag color="cyan">订单通知</a-tag>
            case 4:
              return <a-tag color="cyan">提现通知</a-tag>
            case 5:
              return <a-tag color="cyan">日常通知</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="green">成功</a-tag>
            case 2:
              return <a-tag color="red">失败</a-tag>
            default:
              return '- -'
          }
        }
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    onPickerChange (date, dateString) {
      if (date.length !== 0) {
        this.queryParams.startDate = moment(date[0]).format('YYYY-MM-DD')
        this.queryParams.endDate = moment(date[1]).format('YYYY-MM-DD')
      } else {
        delete this.queryParams.startDate
        delete this.queryParams.endDate
      }
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
          that.$delete('/cos/message-tip/' + ids).then(() => {
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
      this.$get('/cos/message-tip/page', {
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
