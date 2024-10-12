<template>
  <a-modal
    title="用户信息"
    v-model="show"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="1000"
    @cancel="handleCancelClick">
    <div style="font-size: 13px" v-if="show">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="6"><b>用户编号：</b>{{ userData.code }}</a-col>
        <a-col :span="6"><b>账户名称：</b>{{ userData.account !== null ? userData.account : '- -' }}</a-col>
        <a-col :span="6"><b>邮箱地址：</b>{{ userData.email !== null ? userData.email : '- -' }}</a-col>
        <a-col :span="6"><b>手机号码：</b>{{ userData.phone !== null ? userData.phone : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="6"><b>毕业院校：</b>{{ userData.schoolName !== null ? userData.schoolName : '- -' }}</a-col>
        <a-col :span="6"><b>所在城市：</b>{{ userData.cityName !== null ? userData.cityName : '- -' }}</a-col>
        <a-col :span="6"><b>注册时间：</b>{{ userData.createDate !== null ? userData.createDate : '- -' }}</a-col>
        <a-col :span="6"><b>邀请人：</b>{{ userData.inviteUserName !== null ? userData.inviteUserName : '- -' }}</a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <div style="padding-left: 24px;padding-right: 24px;">
        <div style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">当前余额</span></div>
        <span style="word-break: break-all;white-space: nowrap;color: #000;font-size: 30px">￥ {{userData.balance}}</span>
      </div>
      <br/>
      <br/>
      <div style="padding-left: 24px;padding-right: 24px;">
        <div style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">操作详情</span></div>
        <a-tabs default-active-key="1" size="small">
          <a-tab-pane key="1" tab="账户余额">
            <a-card hoverable :bordered="false" :bodyStyle="bodyStyle" v-if="balanceDataSource.length !== 0">
              <a-table ref="TableInfo"
                       :columns="balanceColumns"
                       :dataSource="balanceDataSource"
                       :pagination="pagination"
                       :loading="loading"
                       :scroll="{ x: 300 }">
                <template slot="orderPriceShow" slot-scope="text, record">
                  <a-tooltip>
                    <template slot="title">
                      <p>
                        项目价格：￥{{ record.orderPrice }}&nbsp;&nbsp;&nbsp;&nbsp;
                        <span v-if="record.needDeploy === 1">部署价格：￥30</span>
                      </p>
                    </template>
                    <span v-if="record.needDeploy === 0">￥ {{ record.orderPrice * xcRate / 100 }}</span>
                    <span v-if="record.needDeploy === 1">￥ {{ (record.orderPrice + 30) * xcRate / 100 }}</span>
                    &nbsp;
                    <a-icon type="arrow-up" style="color: green;font-size: 13px"/>
                  </a-tooltip>
                </template>
              </a-table>
            </a-card>
            <a-card hoverable :bordered="false" v-else>
              <a-result title="未找到相关记录!">
                <template #icon>
                  <a-icon type="frown" theme="twoTone" />
                </template>
              </a-result>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="2" tab="邀请记录">
            <a-card hoverable :bordered="false" :bodyStyle="bodyStyle" v-if="inviteDataSource.length !== 0">
              <a-table ref="TableInfo"
                       :columns="inviteColumns"
                       :dataSource="inviteDataSource"
                       :pagination="pagination"
                       :loading="loading"
                       :scroll="{ x: 300 }">
              </a-table>
            </a-card>
            <a-card hoverable :bordered="false" v-else>
              <a-result title="未找到相关记录!">
                <template #icon>
                  <a-icon type="frown" theme="twoTone" />
                </template>
              </a-result>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="3" tab="交易记录">
            <a-card hoverable :bordered="false" :bodyStyle="bodyStyle" v-if="tradeDataSource.length !== 0">
              <a-table ref="TableInfo"
                       :columns="tradeColumns"
                       :dataSource="tradeDataSource"
                       :pagination="pagination"
                       :loading="loading"
                       :scroll="{ x: 300 }">
                <template slot="orderPriceShow" slot-scope="text, record">
                  <a-tooltip>
                    <template slot="title">
                      <p>项目价格：￥{{ record.orderPrice }}</p>
                      <p v-if="record.needDeploy === 1">部署价格：￥30</p>
                    </template>
                    ￥ <span>{{ record.orderPrice }}</span>
                    <span v-if="record.needDeploy === 1"> + <span style="color: green">30</span></span>
                  </a-tooltip>
                </template>
              </a-table>
            </a-card>
            <a-card hoverable :bordered="false" v-else>
              <a-result title="未找到相关记录!">
                <template #icon>
                  <a-icon type="frown" theme="twoTone" />
                </template>
              </a-result>
            </a-card>
          </a-tab-pane>
        </a-tabs>
      </div>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'UserInfo',
  props: {
    userShow: {
      type: Boolean,
      default: false
    },
    userData: {
      type: Object
    }
  },
  watch: {
    userShow: function (data) {
      if (data) {
        this.getUserTradeByCode()
      }
    }
  },
  computed: {
    show: {
      get: function () {
        return this.userShow
      },
      set: function () {
      }
    },
    balanceColumns () {
      return [{
        title: '账户',
        dataIndex: 'account',
      },{
        title: '项目',
        dataIndex: 'proName',
      },{
        title: '收益',
        dataIndex: 'orderPrice',
        scopedSlots: { customRender: 'orderPriceShow' },
        // customRender: (text, row, index) => {
        //   return '￥' + text
        // }
      },{
        title: '日期',
        dataIndex: 'orderTime',
      },{
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
      }]
    },
    inviteColumns () {
      return [{
        title: '账户编号',
        dataIndex: 'code',
      },{
        title: '账户名称',
        dataIndex: 'account',
      },{
        title: '注册时间',
        dataIndex: 'createDate',
      },{
        title: '性别',
        dataIndex: 'sex',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '男'
            case 1:
              return '女'
            case 2:
              return '保密'
            default:
              return text
          }
        },
      }]
    },
    tradeColumns () {
      return [{
        title: '项目名称',
        dataIndex: 'proName',
      },{
        title: '价格',
        dataIndex: 'orderPrice',
        scopedSlots: { customRender: 'orderPriceShow' },
      },{
        title: '下单时间',
        dataIndex: 'orderTime',
      },{
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
      }]
    }
  },
  data() {
    return {
      bodyStyle: {padding: 0},
      loading: false,
      tradeDataSource: [],
      balanceDataSource: [],
      inviteDataSource: [],
      dzRate: 0,
      xcRate: 0,
      pagination: {
        defaultCurrent: 1,
        defaultPageSize: 5,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
    }
  },
  mounted() {
    this.getRate('invite')
  },
  methods: {
    getRate (role) {
      this.$get('/cos/rate-info/getRateByRole', {role: role}).then((r) => {
        r.data.data.forEach(item => {
          switch (item.flag) {
            case 1:
              this.dzRate = item.rate
              break
            case 2:
              this.xcRate = item.rate
              break
          }
        })
      })
    },
    getUserTradeByCode () {
      this.$get('/cos/user-info/trade', {userCode: this.userData.code}).then((r) => {
        this.tradeDataSource = r.data.trade
        this.balanceDataSource = r.data.balance
        this.inviteDataSource = r.data.invite
      })
    },
    handleCancelClick () {
      this.$emit('close')
    },
  }
}
</script>

<style scoped>

</style>
