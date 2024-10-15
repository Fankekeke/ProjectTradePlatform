<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="项目名称"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.proName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="状 态"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select
                  :allowClear="true"
                  v-model="queryParams.status"
                  style="width: 100%">
                  <a-select-option value="-1">全部</a-select-option>
                  <a-select-option value="0">待审核</a-select-option>
                  <a-select-option value="1">沟通中</a-select-option>
                  <a-select-option value="2">已接单</a-select-option>
                  <a-select-option value="3">已完成</a-select-option>
                  <a-select-option value="4">部署完成</a-select-option>
                  <a-select-option value="5">审核完成</a-select-option>
                  <a-select-option value="6">审核不通过</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="订单编号"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.code"/>
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
      <div class="operator">
        <a-button type="primary" ghost @click="add">新增</a-button>
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
        <template slot="operation" slot-scope="text, record">
          <a-icon type="cloud" @click="view(record)" title="查 看" style="margin-right: 15px"></a-icon>
          <a-icon v-if="record.status === 3 || record.status === 4" type="bug" theme="twoTone" twoToneColor="#4a9ff5" @click="audit(record)" title="审核" style="margin-right: 15px"/>
<!--          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>-->
        </template>
      </a-table>
    </div>
    <!-- 新增定制订单 -->
    <custom-add
      @close="handleCustomAddClose"
      @success="handleCustomAddSuccess"
      :customAddVisiable="customAdd.visiable">
    </custom-add>
    <!-- 修改定制订单 -->
    <custom-edit
      ref="customEdit"
      @close="handleCustomEditClose"
      @success="handleCustomEditSuccess"
      :customEditVisiable="customEdit.visiable">
    </custom-edit>
    <custom-info
      :customShow="customView.show"
      :customData="customView.data"
      @close="customClose">
    </custom-info>
    <custom-audit
      :taskShow="customAudit.show"
      :taskData="customAudit.data"
      @close="customAuditClose"
      @success="customAuditSuccess">
    </custom-audit>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import CustomAdd from './CustomAdd'
import CustomEdit from './CustomEdit'
import CustomInfo from './CustomInfo'
import {mapState} from 'vuex'
import moment from 'moment'
import CustomAudit from "./CustomAudit";
moment.locale('zh-cn')

export default {
  name: 'Custom',
  components: {CustomAudit, CustomAdd, CustomEdit, CustomInfo, RangeDate},
  data () {
    return {
      advanced: false,
      customAudit: {
        show: false,
        data: null
      },
      customAdd: {
        visiable: false
      },
      customEdit: {
        visiable: false
      },
      customView: {
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
        title: '项目名称',
        dataIndex: 'proName',
        ellipsis: true
      }, {
        title: '订单编号',
        dataIndex: 'code',
        ellipsis: true
      }, {
        title: '客户',
        dataIndex: 'account',
        ellipsis: true
      }, {
        title: '状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return <a-tag color="pink">待审核</a-tag>
            case 1:
              return <a-tag color="pink">沟通中</a-tag>
            case 2:
              return <a-tag color="orange">已接单</a-tag>
            case 3:
              return <a-tag color="cyan">已完成</a-tag>
            case 4:
              return <a-tag color="cyan">部署完成</a-tag>
            case 5:
              return <a-tag color="green">审核完成</a-tag>
            case 6:
              return <a-tag color="red">审核不通过</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '项目周期',
        dataIndex: 'cycle',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + ' 天'
          } else {
            return '- -'
          }
        }
      }, {
        title: '论文资料',
        dataIndex: 'thesisCode',
        customRender: (text, row, index) => {
          if (text !== null) {
            return <a-tag color="cyan">YES</a-tag>
          } else {
            return <a-tag color="red">NO</a-tag>
          }
        }
      }, {
        title: '创建时间',
        dataIndex: 'createDate',
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
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    audit (row) {
      this.customAudit.data = row
      this.customAudit.show = true
    },
    view (row) {
      this.customView.show = true
      this.customView.data = row
    },
    customAuditClose () {
      this.customAudit.show = false
    },
    customAuditSuccess () {
      this.customAudit.show = false
      this.fetch()
    },
    customClose () {
      this.customView.show = false
      this.fetch()
    },
    add () {
      this.customAdd.visiable = true
    },
    handleCustomAddClose () {
      this.customAdd.visiable = false
    },
    handleCustomAddSuccess () {
      this.customAdd.visiable = false
      this.$message.success('新增成员成功')
      this.search()
    },
    edit (record) {
      this.$refs.customEdit.setFormValues(record)
      this.customEdit.visiable = true
    },
    handleCustomEditClose () {
      this.customEdit.visiable = false
    },
    handleCustomEditSuccess () {
      this.customEdit.visiable = false
      this.$message.success('修改成员成功')
      this.search()
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
          that.$delete('/cos/team-info/' + ids).then(() => {
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
      if (params.status === undefined) {
        delete params.status
      }
      this.$get('/cos/customize-info/page', {
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
