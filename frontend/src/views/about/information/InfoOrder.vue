<template>
  <a-modal v-model="show" title="订单信息" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px">
      <a-tabs default-active-key="1">
        <a-tab-pane key="1" tab="项目交易记录">
          <a-table :columns="orderColumns" :data-source="orderData">
            <div slot="orderPriceShow" slot-scope="text, record">
              <span>￥ {{record.orderPrice}}</span>
              <span v-if="record.needDeploy == 1"> + 30</span>
            </div>
            <div slot="needDeployShow" slot-scope="text, record">
              <a-icon type="arrow-up" style="color: green" v-if="record.createDate !== null"/>
              <a-icon type="arrow-up" style="color: orange" v-else/>
              <span>{{ record.developerBalance }}元</span>
            </div>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="2" tab="附件交易记录" force-render>
          <a-table :columns="thesisColumns" :data-source="thesisOrderData">
            <div slot="priceShow" slot-scope="text, record">
              <span>￥ {{record.price}}</span>
            </div>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-modal>
</template>

<script>

import {mapState} from "vuex"

export default {
  name: 'InfoOrder',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Array
    },
    thesisOrderData: {
      type: Array
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderShow
      },
      set: function () {
      }
    }
  },
  data() {
    return {
      orderColumns: [{
        title: '订单编号',
        dataIndex: 'code',
        ellipsis: true
      }, {
        title: '交易价格',
        dataIndex: 'orderPrice',
        scopedSlots: {customRender: 'orderPriceShow'},
      }, {
        title: '交易时间',
        dataIndex: 'orderTime',
        ellipsis: true
      }, {
        title: '个人收益',
        dataIndex: 'needDeploy',
        scopedSlots: {customRender: 'needDeployShow'}
      }, {
        title: '所属项目',
        dataIndex: 'proName',
        ellipsis: true
      }],
      thesisColumns: [{
        title: '订单编号',
        dataIndex: 'code',
        ellipsis: true
      }, {
        title: '下单时间',
        dataIndex: 'orderTime',
        ellipsis: true
      },  {
        title: '附件名称',
        dataIndex: 'annexName',
        ellipsis: true
      }, {
        title: '价 格',
        dataIndex: 'price',
        scopedSlots: {customRender: 'priceShow'}
      }],
      customNot: {
        developer: 75,
        platform: 25
      },
      customNotInvite: {
        developer: 70,
        platform: 20,
        invite: 10,
      },
      custom: {
        developer: 80,
        platform: 20
      },
      customInvite: {
        developer: 75,
        platform: 17,
        invite: 8,
      }
    }
  },
  mounted() {
    this.rateInfo()
  },
  watch: {
    orderShow: function (value) {
      if (value) {
        this.init()
      }
    }
  },
  methods: {
    init () {
      // 非定制项目
      this.orderData.forEach(item => {
        let orderPrice = item.orderPrice + (item.needDeploy == 1 ? 30 : 0)
        if (item.createDate == null) {
          if (item.agent !== null) {
            item.developerBalance = (orderPrice * (this.customNotInvite.developer * 0.01)).toFixed(2)
          } else {
            item.developerBalance = (orderPrice * (this.customNot.developer * 0.01)).toFixed(2)
          }
        }
      })
    },
    rateInfo() {
      this.$get('/cos/rate-info/rate').then((r) => {
        r.data.customInvite.forEach(item => {
          switch (item.role) {
            case 'developer':
              this.customInvite.developer = item.rate
              break
            case 'platform':
              this.customInvite.platform = item.rate
              break
            case 'invite':
              this.customInvite.invite = item.rate
              break
          }
        })
        r.data.custom.forEach(item => {
          switch (item.role) {
            case 'developer':
              this.custom.developer = item.rate
              break
            case 'platform':
              this.custom.platform = item.rate
              break
          }
        })
        r.data.customNot.forEach(item => {
          switch (item.role) {
            case 'developer':
              this.customNot.developer = item.rate
              break
            case 'platform':
              this.customNot.platform = item.rate
              break
          }
        })
        r.data.customNotInvite.forEach(item => {
          switch (item.role) {
            case 'developer':
              this.customNotInvite.developer = item.rate
              break
            case 'platform':
              this.customNotInvite.platform = item.rate
              break
            case 'invite':
              this.customNotInvite.invite = item.rate
              break
          }
        })
      })
    },
    onClose() {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
