  <template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="所属项目"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.proCode">
                  <a-select-option value="-1">全部</a-select-option>
                  <a-select-option :value="item.code" v-for="(item, index) in projectList" :key="index">{{ item.proName }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="订单状态"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.orderStatus">
                  <a-select-option value="-2">全部</a-select-option>
                  <a-select-option value="-1">已取消</a-select-option>
                  <a-select-option value="0">未支付</a-select-option>
                  <a-select-option value="1">已支付</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="是否过期"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.isExpired">
                  <a-select-option value="-1">全部</a-select-option>
                  <a-select-option value="0">未过期</a-select-option>
                  <a-select-option value="1">已过期</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="支付方式"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.paymentType">
                  <a-select-option value="-1">全部</a-select-option>
                  <a-select-option value="1">支付宝</a-select-option>
                  <a-select-option value="2">微信</a-select-option>
                  <a-select-option value="3">平台下单</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="订单编号"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-input v-model="queryParams.code"/>
                </a-form-item>
              </a-col>
            </template>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
             <a @click="toggleAdvanced" style="margin-left: 8px">
              {{advanced ? '收起' : '展开'}}
              <a-icon :type="advanced ? 'up' : 'down'"/>
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
<!--        <a-button type="primary" ghost @click="add" v-hasPermission="['user:add']">新增</a-button>-->
        <a-button @click="batchDelete" v-hasPermission="['user:delete']">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :rowKey="record => record.id"
               :columns="columns"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="email" slot-scope="text, record">
          <a-popover placement="topLeft">
            <template slot="content">
              <div>{{text}}</div>
            </template>
            <p style="width: 150px;margin-bottom: 0">{{text}}</p>
          </a-popover>
        </template>
        <template slot="orderPriceShow" slot-scope="text, record">
          <a-tooltip>
            <template slot="title">
              <p>
                项目价格：￥{{ record.orderPrice }}&nbsp;&nbsp;&nbsp;&nbsp;
                <span v-if="record.needDeploy === 1">部署价格：￥30</span>
              </p>
            </template>
            <span v-if="record.needDeploy === 0">￥ {{ record.orderPrice }}</span>
            <span v-if="record.needDeploy === 1">￥ {{ (record.orderPrice + 30) }}</span>
          </a-tooltip>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="file-search" @click="view(record)" title="订单详情"></a-icon>
          &nbsp;
          <a-icon v-if="record.deployStatus !== null && record.deployStatus === 1" type="bell" theme="twoTone" twoToneColor="#4a9ff5" @click="urge(record)" title="催一下"></a-icon>
          &nbsp;
          <a-icon v-if="record.deployStatus !== null && (record.deployStatus === 2 || record.deployStatus === 4)" type="apartment" @click="audit(record)" title="部署审核"></a-icon>
        </template>
      </a-table>
    </div>
    <order-info
      :orderData="orderInfo.data"
      :orderShow="orderInfo.visiable"
      @close="handleUserInfoClose">
    </order-info>
    <deploy-audit
      :orderData="orderInfo.data"
      :auditShow="orderInfo.audit"
      @close="handleAuditClose"
      @success="handleAuditSuccess">
    </deploy-audit>
  </a-card>
</template>

<script>
  import DeptInputTree from '../../system/dept//DeptInputTree'
  import RangeDate from '@/components/datetime/RangeDate'
  import OrderInfo from './OrderInfo'
  import DeployAudit from './DeployAudit';

  export default {
    name: 'User',
    components: {DeptInputTree, RangeDate, OrderInfo, DeployAudit},
    data() {
      return {
        advanced: false,
        orderInfo: {
          visiable: false,
          audit: false,
          data: {}
        },
        queryParams: {},
        filteredInfo: null,
        sortedInfo: null,
        paginationInfo: null,
        dataSource: [],
        selectedRowKeys: [],
        loading: false,
        projectList: [],
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
      columns() {
        let {sortedInfo, filteredInfo} = this
        sortedInfo = sortedInfo || {}
        filteredInfo = filteredInfo || {}
        return [{
          title: '订单编号',
          dataIndex: 'code',
        }, {
          title: '购买项目',
          dataIndex: 'proName',
        }, {
          title: '是否过期',
          dataIndex: 'isExpired',
          customRender: (text, row, index) => {
            switch (text) {
              case 0:
                return <a-tag color="green">未过期</a-tag>
              case 1:
                return <a-tag color="red">已过期</a-tag>
              default:
                return text
            }
          },
        }, {
          title: '价格',
          dataIndex: 'orderPrice',
          scopedSlots: { customRender: 'orderPriceShow' },
        }, {
          title: '下单时间',
          dataIndex: 'orderTime',
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
        }, {
          title: '收货人',
          dataIndex: 'account',
          customRender: (text, row, index) => {
            if(text !== null) {
              return text
            }else {
              return '其他客户'
            }
          },
        }, {
          title: '订单状态',
          dataIndex: 'orderStatus',
          customRender: (text, row, index) => {
            switch (text) {
              case -1:
                return <a-tag color="red">已取消</a-tag>
              case 0:
                return <a-tag color="cyan">待支付</a-tag>
              case 1:
                return <a-tag color="green">已支付</a-tag>
              default:
                return text
            }
          },
        },{
          title: '需要部署',
          dataIndex: 'needDeploy',
          customRender: (text, row, index) => {
            switch (text) {
              case 0:
                return <a-tag color="purple">否</a-tag>
              case 1:
                return <a-tag color="pink">是</a-tag>
              default:
                return text
            }
          },
        }, {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: {customRender: 'operation'}
        }]
      }
    },
    mounted() {
      this.fetch()
      this.getProjectList()
    },
    methods: {
      /**
       * 获取项目信息
       */
      getProjectList() {
        this.$get('/cos/project-info/list').then((r) => {
          this.projectList = r.data.data
        })
      },
      onSelectChange(selectedRowKeys) {
        this.selectedRowKeys = selectedRowKeys
      },
      toggleAdvanced() {
        this.advanced = !this.advanced
        if (!this.advanced) {
          this.queryParams.createTimeFrom = ''
          this.queryParams.createTimeTo = ''
        }
      },
      urge(record) {

      },
      audit(record) {
        this.orderInfo.data = record
        this.orderInfo.audit = true
      },
      view(record) {
        this.orderInfo.data = record
        this.orderInfo.visiable = true
      },
      handleUserInfoClose() {
        this.orderInfo.visiable = false
      },
      handleAuditClose() {
        this.orderInfo.audit = false
      },
      handleAuditSuccess() {
        this.orderInfo.audit = false
        this.fetch()
        this.$message.success('操作成功')
      },
      handleTypeChange(value) {
        this.queryParams.accountType = value || ''
      },
      handleDateChange(value) {
        if (value) {
          this.queryParams.createTimeFrom = value[0]
          this.queryParams.createTimeTo = value[1]
        }
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
            let ids = []
            for (let key of that.selectedRowKeys) {
              ids.push(key)
            }
            that.$delete('/cos/order-info/'+ids,).then(() => {
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
      exportExcel() {
        let {sortedInfo, filteredInfo} = this
        let sortField, sortOrder
        // 获取当前列的排序和列的过滤规则
        if (sortedInfo) {
          sortField = sortedInfo.field
          sortOrder = sortedInfo.order
        }
        this.$export('user/excel', {
          sortField: sortField,
          sortOrder: sortOrder,
          ...this.queryParams,
          ...filteredInfo
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
        // // 清空部门树选择
        // this.$refs.deptTree.reset()
        // // 清空时间选择
        // if (this.advanced) {
        //   this.$refs.createTime.reset()
        // }
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
        this.$get('/cos/order-info/page', {
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
    }
  }
</script>
<style lang="less" scoped>
  @import "../../../../static/less/Common";
</style>
