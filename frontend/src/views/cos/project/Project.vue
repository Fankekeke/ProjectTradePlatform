  <template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="产品名称"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.proName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="技术方案"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.proTechnology"/>
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
        <a-button type="primary" ghost @click="add" v-hasPermission="['user:add']">新增</a-button>
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

        <!-- 产品名称插槽 -->
        <template slot="proNameShow" slot-scope="text, record">
          <template>
            <a-badge status="success" v-show="record.delFlag === 0"/>
            <a-badge status="processing" v-show="record.delFlag === 1"/>
            <a @click="view(record)">{{ record.proName }}</a>
          </template>
        </template>

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

        <template slot="email" slot-scope="text, record">
          <a-popover placement="topLeft">
            <template slot="content">
              <div>{{text}}</div>
            </template>
            <p style="width: 150px;margin-bottom: 0">{{text}}</p>
          </a-popover>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-dropdown placement="bottomCenter">
            <a class="ant-dropdown-link" @click="e => e.preventDefault()">
              操作 <a-icon type="down" />
            </a>
            <a-menu slot="overlay" style="font-size: 12px">
              <a-menu-item key="1" v-hasPermission="['user:update']" @click="edit(record)"> <a-icon type="setting" />修改项目</a-menu-item>
              <a-menu-item key="3" @click="generateOrder(record)"> <a-icon type="switcher" />生成订单</a-menu-item>
              <a-menu-item key="4" v-show="record.delFlag === 0" @click="projectSwitch(record.id, 1)"> <a-icon type="minus-square"/>下 架</a-menu-item>
              <a-menu-item key="5" v-show="record.delFlag === 1" @click="projectSwitch(record.id, 0)"> <a-icon type="plus-square"/>上 架</a-menu-item>
            </a-menu>
            <a-button style="margin-left: 8px"> Button <a-icon type="down" /> </a-button>
          </a-dropdown>
          <a-badge v-hasNoPermission="['user:update','user:view']" status="warning" text="无权限"></a-badge>
        </template>
      </a-table>
    </div>
    <!-- 项目信息查看 -->
    <user-info
      :userInfoData="userInfo.data"
      :userInfoVisiable="userInfo.visiable"
      @close="handleUserInfoClose">
    </user-info>
    <!-- 新增项目 -->
    <user-add
      @close="handleUserAddClose"
      @success="handleUserAddSuccess"
      :userAddVisiable="userAdd.visiable">
    </user-add>
    <!-- 修改项目 -->
    <user-edit
      ref="userEdit"
      @close="handleUserEditClose"
      @success="handleUserEditSuccess"
      :userEditVisiable="userEdit.visiable">
    </user-edit>
  </a-card>
</template>

<script>
  import DeptInputTree from '../../system/dept//DeptInputTree'
  import RangeDate from '@/components/datetime/RangeDate'
  import UserInfo from './ProjectInfo'
  import UserAdd from './ProjectAdd'
  import UserEdit from './ProjectEdit'

  export default {
    name: 'User',
    components: { UserAdd, UserEdit, UserInfo, DeptInputTree, RangeDate},
    data() {
      return {
        advanced: false,
        userInfo: {
          visiable: false,
          data: {}
        },
        userAdd: {
          visiable: false
        },
        userEdit: {
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
        }
      }
    },
    computed: {
      columns() {
        let {sortedInfo, filteredInfo} = this
        sortedInfo = sortedInfo || {}
        filteredInfo = filteredInfo || {}
        return [{
          title: '产品名称',
          dataIndex: 'proName',
          ellipsis: true,
          scopedSlots: { customRender: 'proNameShow' },
        }, {
          title: '产品介绍',
          dataIndex: 'proIntroduce',
          ellipsis: true,
          scopedSlots: { customRender: 'proIntroduceShow' },
        }, {
          title: '价格',
          dataIndex: 'proPrice',
          customRender: (text, row, index) => {
            return "￥" + text
          },
        }, {
          title: '技术方案',
          dataIndex: 'proTechnology',
          ellipsis: true,
          scopedSlots: { customRender: 'proTechnologyShow' },
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
        }, {
          title: '项目分类',
          dataIndex: 'proSort',
          scopedSlots: { customRender: 'proSortShow' },
        }, {
          title: '访问量',
          dataIndex: 'visits',
          customRender: (text, row, index) => {
            return <a-tag color="pink">{text}℃</a-tag>
          }
        }, {
          title: '成交量',
          dataIndex: 'turnover',
          customRender: (text, row, index) => {
            return <a-tag color="green">{text}单</a-tag>
          }
        }, {
          title: '发布人',
          dataIndex: 'releaseUser',
        }, {
            title: '操作',
            dataIndex: 'operation',
            scopedSlots: {customRender: 'operation'}
          }
        ]
      }
    },
    mounted() {
      this.fetch()
    },
    methods: {
      /**
       * 订单生成
       * @param project
       */
      generateOrder(project) {
        let OrderInfo = {proCode: project.code, orderPrice: project.proPrice}
        this.$post('/cos/order-info/orderSaveBySystem', {
          ...OrderInfo
        }).then((r) => {
          this.$message.success('生成订单成功')
          this.orderInfoShow(r.data.data)
        }).catch(() => {
          // this.$message.warning('发生错误，请联系管理员')
        })
      },
      /**
       * 上下架
       */
      projectSwitch(id, type) {
        this.$put('/cos/project-info/switch', {
          id: id, type: type
        }).then((r) => {
          this.$message.success('操作成功')
          this.fetch()
        })
      },
      /**
       * 订单生成成功
       * @param orderInfo
       */
      orderInfoShow(orderInfo) {
        this.$success({
          title: '生成订单成功',
          // JSX support
          content: this.$createElement('div', {}, [
              this.$createElement('br'),
              this.$createElement('p', {},[
                this.$createElement('b','订单编号：'),
                this.$createElement('span', orderInfo.code)
              ]),
              this.$createElement('p', {},[
                this.$createElement('b','付款金额：'),
                this.$createElement('span', orderInfo.orderPrice.toFixed(2))
              ]),
              this.$createElement('p', {},[
                this.$createElement('b','下单时间：'),
                this.$createElement('span', orderInfo.orderTime)
              ]),
              this.$createElement('p', {},[
                this.$createElement('b','支付方式：'),
                this.$createElement('span', '平台下单')
              ]),
              this.$createElement('p', {},[
                this.$createElement('b','兑换码：'),
                this.$createElement('span', orderInfo.exchangeCode)
              ]),
              this.$createElement('p', {},[
                this.$createElement('b','兑换有效时间：'),
                this.$createElement('span', orderInfo.effectiveTime)
              ]),
            ])
        });
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
      view(record) {
        this.userInfo.data = record
        this.userInfo.visiable = true
      },
      add() {
        this.userAdd.visiable = true
      },
      handleUserAddClose() {
        this.userAdd.visiable = false
      },
      handleUserAddSuccess() {
        this.userAdd.visiable = false
        this.$message.success('新增项目成功')
        this.search()
      },
      edit(record) {
        this.$refs.userEdit.setFormValues(record)
        this.userEdit.visiable = true
      },
      handleUserEditClose() {
        this.userEdit.visiable = false
      },
      handleUserEditSuccess() {
        this.userEdit.visiable = false
        this.$message.success('修改项目成功')
        this.search()
      },
      handleUserInfoClose() {
        this.userInfo.visiable = false
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
              ids.push(that.dataSource[key].id)
            }
            that.$delete('/cos/project-info/'+ids,).then(() => {
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
        // 清空部门树选择
        this.$refs.deptTree.reset()
        // 清空时间选择
        if (this.advanced) {
          this.$refs.createTime.reset()
        }
        this.fetch()
      },
      handleTableChange(pagination, filters, sorter) {
        // 将这三个参数赋值给Vue data，用于后续使用
        this.paginationInfo = pagination
        this.filteredInfo = filters
        this.sortedInfo = sorter

        this.userInfo.visiable = false
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
        this.$get('/cos/project-info/page', {
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
