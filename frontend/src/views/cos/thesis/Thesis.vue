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
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.annexName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="当前状态"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.status" allowClear>
                  <a-select-option value="1">正在审核</a-select-option>
                  <a-select-option value="2">审核通过</a-select-option>
                  <a-select-option value="3">不通过</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="附件类型"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.annexType" allowClear>
                  <a-select-option value="1">论文</a-select-option>
                  <a-select-option value="2">PPT</a-select-option>
                  <a-select-option value="3">任务书</a-select-option>
                  <a-select-option value="4">开题报告</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="6" :sm="24" >
                <a-form-item
                  label="是否启用"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-select v-model="queryParams.isEnable" allowClear>
                    <a-select-option value="0">下架中</a-select-option>
                    <a-select-option value="1">上架中</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="所属项目"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <single-choice v-model="queryParams.proCode"></single-choice>
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
        <!-- 附件名称插槽 -->
        <template slot="annexNameShow" slot-scope="text, record">
          <template>
            <a-badge status="success" v-show="record.isEnable === 1"/>
            <a-badge status="processing" v-show="record.isEnable === 0"/>
            <a-tooltip>
              <template slot="title">
                {{ record.annexName }}
              </template>
              {{ record.annexName.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon v-if="record.isEnable == 0" type="caret-up" @click="audit(record, 1)" title="上 架" style="margin-right: 15px"></a-icon>
          <a-icon v-if="record.isEnable == 1" type="caret-down" @click="audit(record, 0)" title="下 架" style="margin-right: 15px"></a-icon>
          <a-icon type="cloud" @click="view(record)" title="查 看" style="margin-right: 15px"></a-icon>
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
        </template>
      </a-table>
    </div>
    <!-- 新增附件 -->
    <thesis-add
      v-if="thesisAdd.visiable"
      @close="handleThesisAddClose"
      @success="handleThesisAddSuccess"
      :thesisAddVisiable="thesisAdd.visiable">
    </thesis-add>
    <!-- 修改附件 -->
    <thesis-edit
      ref="thesisEdit"
      @close="handleThesisEditClose"
      @success="handleThesisEditSuccess"
      :thesisEditVisiable="thesisEdit.visiable">
    </thesis-edit>
    <thesis-info
      @close="handleThesisViewClose"
      @audit="handleThesisViewAudit"
      :thesisShow="thesisView.show"
      :thesisData="thesisView.data">
    </thesis-info>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import ThesisAdd from './ThesisAdd'
import ThesisEdit from './ThesisEdit'
import {mapState} from 'vuex'
import moment from 'moment'
import ThesisInfo from './ThesisInfo'
import SingleChoice from "../../../components/project/SingleChoice";
moment.locale('zh-cn')

export default {
  name: 'Thesis',
  components: {SingleChoice, ThesisInfo, ThesisAdd, ThesisEdit, RangeDate},
  data () {
    return {
      advanced: false,
      thesisAdd: {
        visiable: false
      },
      thesisEdit: {
        visiable: false
      },
      thesisView: {
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
        title: '附件名称',
        dataIndex: 'annexName',
        scopedSlots: { customRender: 'annexNameShow' },
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
        title: '作者',
        dataIndex: 'players',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '价格',
        dataIndex: 'annexPrice',
        customRender: (text, row, index) => {
          return '￥' + text
        }
      }, {
        title: '所属项目',
        dataIndex: 'proName'
      }, {
        title: '审核状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag>正在审核</a-tag>
            case 2:
              return <a-tag color="green">审核通过</a-tag>
            case 3:
              return <a-tag color="red">不通过</a-tag>
            default:
              return text
          }
        }
      }, {
        title: '上传时间',
        dataIndex: 'uploadDate',
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
    audit (row, flag) {
      this.$put('/cos/annex-info/putOn', {
        id: row.id,
        flag: flag
      }).then((r) => {
        this.fetch()
      })
    },
    view (row) {
      this.thesisView.show = true
      this.thesisView.data = row
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.thesisAdd.visiable = true
    },
    handleThesisAddClose () {
      this.thesisAdd.visiable = false
    },
    handleThesisAddSuccess () {
      this.thesisAdd.visiable = false
      this.$message.success('新增附件成功')
      this.search()
    },
    edit (record) {
      this.$refs.thesisEdit.setFormValues(record)
      this.thesisEdit.visiable = true
    },
    handleThesisEditClose () {
      this.thesisEdit.visiable = false
    },
    handleThesisViewClose () {
      this.thesisView.show = false
    },
    handleThesisViewAudit () {
      this.thesisView.show = false
      this.$message.success('附件审核完成')
      this.search()
    },
    handleThesisEditSuccess () {
      this.thesisEdit.visiable = false
      this.$message.success('修改附件成功')
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
          that.$delete('/cos/annex-info/' + ids).then(() => {
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
      if (params.annexType === undefined) {
        delete params.annexType
      }
      if (params.isEnable === undefined) {
        delete params.isEnable
      }
      if (params.proCode === undefined) {
        delete params.proCode
      }
      this.$get('/cos/annex-info/page', {
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
