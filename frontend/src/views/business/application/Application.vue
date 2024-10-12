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
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
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

        <!-- 技术要求插槽 -->
        <template slot="skillsShow" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              {{ record.skills }}
            </template>
            {{ record.skills.slice(0, 25) }} ...
          </a-tooltip>
        </template>

        <!-- 项目模块插槽 -->
        <template slot="moduleShow" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              {{ record.module }}
            </template>
            {{ record.module.slice(0, 25) }} ...
          </a-tooltip>
        </template>

        <template slot="operation" slot-scope="text, record">
          <a-icon type="cloud" @click="view(record)" title="查 看" style="margin-right: 15px"></a-icon>
          <a-icon v-if="record.customizeCode == null || record.customizeCode == ''" type="folder-add" theme="twoTone" twoToneColor="#4a9ff5" @click="add(record)" title="添 加"></a-icon>
        </template>
      </a-table>
    </div>
    <!-- 新增定制订单 -->
    <application-add
      ref="customAdd"
      @close="handleCustomAddClose"
      @success="handleCustomAddSuccess"
      :customAddVisiable="customAdd.visiable">
    </application-add>
    <!-- 定制信息详情 -->
    <application-info
      :applicationShow="applicationView.show"
      :applicationData="applicationView.data"
      @close="applicationClose">
    </application-info>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import ApplicationAdd from './ApplicationAdd'
import {mapState} from 'vuex'
import moment from 'moment'
import ApplicationInfo from './ApplicationInfo'
moment.locale('zh-cn')

export default {
  name: 'Application',
  components: {ApplicationInfo, ApplicationAdd, RangeDate},
  data () {
    return {
      advanced: false,
      customAdd: {
        visiable: false,
        custom: null
      },
      applicationView: {
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
        width: 180
      }, {
        title: '客户',
        dataIndex: 'account',
        width: 180
      }, {
        title: '状态',
        dataIndex: 'customizeCode',
        width: 180,
        customRender: (text, row, index) => {
          if (text !== null && text !== '') {
            return <a-tag color="green">已创建</a-tag>
          } else {
            return <a-tag color="pink">未创建</a-tag>
          }
        }
      }, {
        title: '技术要求',
        dataIndex: 'skills',
        ellipsis: true,
        scopedSlots: { customRender: 'skillsShow' },
      }, {
        title: '功能模块',
        dataIndex: 'module',
        ellipsis: true,
        scopedSlots: { customRender: 'moduleShow' },
      }, {
        title: '联系方式',
        dataIndex: 'contact',
        width: 150
      }, {
        title: '创建时间',
        dataIndex: 'createDate',
        width: 200
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
    applicationClose () {
      this.applicationView.show = false
    },
    view (row) {
      this.applicationView.show = true
      this.applicationView.data = row
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add (row) {
      row.thesis = 0
      this.$refs.customAdd.setFormValues(row)
      this.customAdd.visiable = true
    },
    handleCustomAddClose () {
      this.customAdd.visiable = false
    },
    handleCustomAddSuccess () {
      this.customAdd.visiable = false
      this.$message.success('定制项目生成成功')
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
          that.$delete('/cos/custom-application/' + ids).then(() => {
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
      this.$get('/cos/custom-application/page', {
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
