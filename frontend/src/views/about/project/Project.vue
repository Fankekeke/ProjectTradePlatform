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
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.proName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="当前状态"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.proStatus" allowClear>
                  <a-select-option value="1">正在审核</a-select-option>
                  <a-select-option value="2">审核通过</a-select-option>
                  <a-select-option value="3">不通过</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="项目类型"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.proType" allowClear>
                  <a-select-option value="1">仅后台</a-select-option>
                  <a-select-option value="2">前后台</a-select-option>
                  <a-select-option value="3">小程序</a-select-option>
                  <a-select-option value="4">安卓</a-select-option>
                </a-select>
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
        <a-button type="danger" ghost @click="uploadAnnex">上传附件</a-button>
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
        <!-- 产品介绍插槽 -->
        <template slot="proIntroduceShow" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              {{ record.proIntroduce }}
            </template>
            {{ record.proIntroduce.slice(0, 15) }} ...
          </a-tooltip>
        </template>

        <!-- 技术方案插槽 -->
        <template slot="proTechnologyShow" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              {{ record.proTechnology }}
            </template>
            {{ record.proTechnology.slice(0, 15) }} ...
          </a-tooltip>
        </template>

        <!-- 项目分类插槽 -->
        <template slot="proSortShow" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              {{ record.proSort }}
            </template>
            {{ record.proSort.slice(0, 10) }} ...
          </a-tooltip>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="cloud" @click="view(record)" title="查 看" style="margin-right: 15px"></a-icon>
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
        </template>
      </a-table>
    </div>
    <!-- 新增项目 -->
    <project-add
      v-if="projectAdd.visiable"
      @close="handleProjectAddClose"
      @success="handleProjectAddSuccess"
      :projectAddVisiable="projectAdd.visiable">
    </project-add>
    <!-- 修改项目 -->
    <project-edit
      ref="projectEdit"
      @close="handleProjectEditClose"
      @success="handleProjectEditSuccess"
      :projectEditVisiable="projectEdit.visiable">
    </project-edit>
    <!-- 项目详情 -->
    <project-audit
      v-if="projectView.show"
      :projectShow="projectView.show"
      :projectData="projectView.data"
      @audit="handleProjectViewAudit"
      @close="handleProjectViewClose">
    </project-audit>
    <thesis-upload
      v-if="thesisAdd.visiable"
      @close="handleThesisAddClose"
      @success="handleThesisAddSuccess"
      :thesisAddVisiable="thesisAdd.visiable">
    </thesis-upload>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import ProjectAdd from './ProjectAdd'
import ProjectEdit from './ProjectEdit'
import {mapState} from 'vuex'
import moment from 'moment'
import ProjectAudit from './ProjectAudit'
import ThesisUpload from "./ThesisUpload";
moment.locale('zh-cn')

export default {
  name: 'Project',
  components: {ThesisUpload, ProjectAudit, ProjectAdd, ProjectEdit, RangeDate},
  data () {
    return {
      advanced: false,
      projectAdd: {
        visiable: false
      },
      projectEdit: {
        visiable: false
      },
      projectView: {
        show: false,
        data: null
      },
      thesisAdd: {
        visiable: false
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
        ellipsis: true
      }, {
        title: '项目分类',
        dataIndex: 'proSort',
        scopedSlots: { customRender: 'proSortShow' },
        ellipsis: true
      }, {
        title: '当前状态',
        dataIndex: 'proStatus',
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
        dataIndex: 'releaseTime',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        },
        ellipsis: true
      }, {
        title: '项目介绍',
        dataIndex: 'proIntroduce',
        scopedSlots: { customRender: 'proIntroduceShow' },
        ellipsis: true
      }, {
        title: '项目技术',
        dataIndex: 'proTechnology',
        scopedSlots: { customRender: 'proTechnologyShow' },
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
    uploadAnnex () {
      this.thesisAdd.visiable = true
    },
    handleThesisAddClose () {
      this.thesisAdd.visiable = false
    },
    handleThesisAddSuccess () {
      this.thesisAdd.visiable = false
      this.$message.success('上传附件成功')
      this.search()
    },
    view (row) {
      this.projectView.show = true
      this.projectView.data = row
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.projectAdd.visiable = true
    },
    handleProjectAddClose () {
      this.projectAdd.visiable = false
    },
    handleProjectAddSuccess () {
      this.projectAdd.visiable = false
      this.$message.success('新增项目成功')
      this.search()
    },
    edit (record) {
      this.$refs.projectEdit.setFormValues(record)
      this.projectEdit.visiable = true
    },
    handleProjectEditClose () {
      this.projectEdit.visiable = false
    },
    handleProjectViewClose () {
      this.projectView.show = false
    },
    handleProjectViewAudit () {
      this.projectView.show = false
      this.$message.success('项目审核完成')
      this.search()
    },
    handleProjectEditSuccess () {
      this.projectEdit.visiable = false
      this.$message.success('修改项目成功')
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
          that.$delete('/cos/project-apply/' + ids).then(() => {
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
      if (this.currentUser.roleId != 74) {
        params.author = this.currentUser.userId
      }
      if (params.proStatus === undefined) {
        delete params.proStatus
      }
      if (params.proType === undefined) {
        delete params.proType
      }
      this.$get('/cos/project-apply/page', {
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
