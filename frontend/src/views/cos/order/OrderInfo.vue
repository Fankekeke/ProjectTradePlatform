<template>
  <a-modal
    v-model="show"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="1000"
    @cancel="handleCancelClick">
    <div style="font-size: 13px" v-if="orderData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>订单编号：</b>{{ orderData.code }}</a-col>
        <a-col :span="8"><b>购买项目：</b>{{ orderData.proName !== null ? orderData.proName : '- -' }}</a-col>
        <a-col :span="8"><b>项目价格：</b>￥{{ orderData.orderPrice !== null ? orderData.orderPrice : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="8"><b>下单时间：</b>{{ orderData.orderTime !== null ? orderData.orderTime : '- -' }}</a-col>
        <a-col :span="8"><b>支付方式：</b>
          <span v-if="orderData.paymentType === 1">支付宝</span>
          <span v-if="orderData.paymentType === 2">微信</span>
          <span v-if="orderData.paymentType === 3">平台下单</span>

        </a-col>
        <a-col :span="8"><b>收货人：</b>{{ orderData.account !== null ? orderData.account : '其他客户' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="8"><b>订单状态：</b>
          <span v-if="orderData.orderStatus === -1">已取消</span>
          <span v-if="orderData.orderStatus === 0">待支付</span>
          <span v-if="orderData.orderStatus === 1">已支付</span>
        </a-col>
        <a-col :span="8"><b>是否过期：</b>
          <span v-if="orderData.isExpired === 0" style="color: green">未过期</span>
          <span v-if="orderData.isExpired === 1" style="color:red;">已过期</span>
        </a-col>
        <a-col :span="8"><b>兑换码：</b>{{orderData.exchangeCode}}</a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <div style="padding: 0px 24px" v-if="deployProcesse !== null && deployProcesse.length !== 0">
        <a-row :gutter="20">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">部署流程</span></a-col>
          <a-col>
            <a-steps :current="deployProcesseStep.step" style="margin-top: 50px">
              <a-popover slot="progressDot" slot-scope="{ index, status, prefixCls }">
                <template slot="content">
                  <span v-if="index === 0">付款成功：{{deployProcesseStep.deploy1.operateTime !== null ? deployProcesseStep.deploy1.operateTime : ''}}</span>
                  <span v-if="index === 1">
                    <div style="display: inline-block;margin: 5px" v-if="deployProcesseStep.deploy2.operatePic !== null" v-for="(item, index) in deployProcesseStep.deploy2.operatePic.split(',')" :key="index">
                      <a-avatar shape="square" :size="120" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/'+item"/>
                    </div>
                    <div v-else>
                      该订单未部署完成
                    </div>
                  </span>
                  <span v-if="index === 2">
                    <div v-if="deployProcesseStep.step === 2">
                      <span style="color: red">审核未通过</span> - <span>{{deployProcesseStep.deploy2.operateTime !== null ? deployProcesseStep.deploy2.operateTime : ''}}</span>
                    </div>
                    <div v-if="deployProcesseStep.step === 3">
                      <span style="color: green">审核通过</span> - <span>{{deployProcesseStep.deploy3.operateTime !== null ? deployProcesseStep.deploy3.operateTime : ''}}</span>
                    </div>
                  </span>
                  <span v-if="index === 3">分配完成</span>
                </template>
                <span :class="`${prefixCls}-icon-dot`" />
              </a-popover>
              <a-step title="付款成功" :description="deployProcesseStep.deploy1.operateTime !== null ? deployProcesseStep.deploy1.operateTime : ''" />
              <a-step title="部署完成" :description="deployProcesseStep.deploy2.operateTime !== null ? deployProcesseStep.deploy2.operateTime : ''" />
              <a-step title="审核结果" description="" />
              <a-step title="佣金分配" description="" />
            </a-steps>
          </a-col>
        </a-row>
      </div>
      <br/>
      <br/>
      <div style="padding: 0px 24px" v-if="orderInfo !== null">
        <a-row :gutter="20">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">分成
          <a-tooltip>
              <template slot="title">
                <p>项目价格：￥{{ orderInfo.orderPrice }}</p>
                <p v-if="orderInfo.needDeploy === 1">部署价格：￥30</p>
              </template>
              『 ￥ <span>{{ orderInfo.orderPrice }}</span>
              <span v-if="orderInfo.needDeploy === 1"> + <span style="color: green">30</span></span> 』
          </a-tooltip>
          </span>
          </a-col>
          <a-col :span="8">
            <a-card hoverable style="margin: 0 auto;text-align: center">
              <a-result title="开发者收益">
                <template #icon>
                  <a-icon type="idcard" theme="twoTone" />
                </template>
              </a-result>
              <a-card-meta :title="'￥'+orderInfo.inviteUser !== null ? '￥'+(rate.xcRateY.developer * orderInfo.allPrice / 100).toFixed(2) : '￥'+(rate.xcRateN.developer * orderInfo.allPrice / 100).toFixed(2)">
                <template slot="description">
                  <a-popover v-if="orderInfo.players !== null">
                    <template slot="content">
                      <a-avatar shape="square" :size="120" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/'+orderInfo.avatar" />
                    </template>
                    <a>{{orderInfo.players}}</a>
                  </a-popover>
                  <span v-else> - - </span>
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
          <a-col :span="8">
            <a-card hoverable style="margin: 0 auto;text-align: center">
              <a-result title="平台提成">
                <template #icon>
                  <a-icon type="money-collect" theme="twoTone" />
                </template>
              </a-result>
              <a-card-meta :title="orderInfo.inviteUser !== null ? '￥'+(rate.xcRateY.platform * orderInfo.allPrice / 100).toFixed(2) : '￥'+(rate.xcRateN.platform * orderInfo.allPrice / 100).toFixed(2)">
                <template slot="description">
                  &nbsp;
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
          <a-col :span="8" v-if="orderInfo.inviteUser !== null">
            <a-card hoverable style="margin: 0 auto;text-align: center">
              <a-result title="邀请人">
                <template #icon>
                  <a-icon type="usb" theme="twoTone" />
                </template>
              </a-result>
              <a-card-meta :title="'￥'+(rate.xcRateY.invite * orderInfo.allPrice / 100).toFixed(2)">
                <template slot="description">
                  {{orderInfo.inviteUser}}
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
        </a-row>
      </div>
      <br/>
      <br/>
      <div v-if="evaluation !== null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单评价</span></a-col>
          <a-col :span="6"><b>评价时间：</b>{{ evaluation.date }}</a-col>
          <a-col :span="6"><b>需要部署：</b>
            <b v-if="orderInfo.needDeploy === 0" style="color: red">否</b>
            <b v-if="orderInfo.needDeploy === 1" style="color: green">是</b>
          </a-col>
          <a-col :span="6"><b>价格明细：</b>
            <a-tooltip>
              <template slot="title">
                <p>项目价格：￥{{ orderInfo.orderPrice }}</p>
                <p v-if="orderInfo.needDeploy === 1">部署价格：￥30</p>
              </template>
              ￥ <span>{{ orderInfo.orderPrice }}</span>
              <span v-if="orderInfo.needDeploy === 1"> + <span style="color: green">30</span></span>
            </a-tooltip>
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
          <a-col :span="24"><b>评分：</b>
            <a-rate :default-value="evaluation.score" disabled />
          </a-col>
        </a-row>
        <br/>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">评价内容</span></a-col>
          <a-col :span="24" style="line-height: 30px">{{ evaluation.content }}</a-col>
          <a-col :span="24" style="line-height: 30px;margin-top: 20px">
            <a-upload
              list-type="picture-card"
              :file-list="fileList">
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-col>
        </a-row>
      </div>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: "OrderInfo",
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  watch: {
    orderShow: function (val) {
      if (val) {
        this.orderDetailed(this.orderData.code)
      }
    }
  },
  computed: {
    show: {
      get: function () {
        return this.orderShow
      },
      set: function () {
      }
    },
  },
  data () {
    return {
      orderInfo: null,
      evaluation: null,
      deployProcesse: [],
      deployEvaluate: null,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      deployProcesseStep: {
        step: 0,
        deploy1: null,
        deploy2: null,
        deploy3: null,
        deploy4: null,
      },
      rate: {
        xcRateY: {
          developer: 0,
          platform: 0,
          invite: 0
        },
        xcRateN: {
          developer: 0,
          platform: 0
        },
      }
    }
  },
  mounted() {
    this.getRate('invite')
    this.getRate('developer')
    this.getRate('platform')
  },
  methods: {
    orderDetailed (code) {
      this.$get('/cos/order-info/detailed', {orderCode: code}).then((r) => {
        this.orderInfo = r.data.orderInfo
        this.orderInfo.allPrice = this.orderInfo.orderPrice
        if (this.orderInfo.needDeploy === 1) {
          this.orderInfo.allPrice = this.orderInfo.orderPrice + 30
        }
        this.evaluation = r.data.evaluation
        this.deployProcesse = r.data.deployProcesse
        if (this.deployProcesse !== null && this.deployProcesse.length !== 0) {
          this.deployProcesse.forEach(item => {
            if (item.currentStatus === 1) {
              this.deployProcesseStep.step = item.status - 1
              if (item.status === 3) {
                this.deployProcesseStep.step = 3
              }
              if (item.status === 4) {
                this.deployProcesseStep.step = 2
              }
            }
            switch (item.status) {
              case 1:
                this.deployProcesseStep.deploy1 = item
                break
              case 2:
                this.deployProcesseStep.deploy2 = item
                break
              case 3:
                this.deployProcesseStep.deploy3 = item
                break
              case 4:
                this.deployProcesseStep.deploy4 = item
                break
            }
          })
        }
        this.deployEvaluate = r.data.deployEvaluate
        if (this.evaluation !== null) {
          if(this.evaluation.pic !== null) {
            let fileList = []
            this.evaluation.pic.split(',').forEach((image, index) => {
              fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
              // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
            })
            this.fileList = fileList;
          }
        }
      })
    },
    getRate (role) {
      this.$get('/cos/rate-info/getRateByRole', {role: role}).then((r) => {
        if (role === 'invite') {
          r.data.data.forEach(item => {
            if (item.flag === 2) {
              this.rate.xcRateY.invite = item.rate
            }
          })
        }
        if (role === 'developer') {
          r.data.data.forEach(item => {
            if (item.flag === 2) {
              switch (item.isInvite) {
                case 1:
                  this.rate.xcRateY.developer = item.rate
                  break
                case 2:
                  this.rate.xcRateN.developer = item.rate
                  break
              }
            }
          })
        }
        if (role === 'platform') {
          r.data.data.forEach(item => {
            if (item.flag === 2) {
              switch (item.isInvite) {
                case 1:
                  this.rate.xcRateY.platform = item.rate
                  break
                case 2:
                  this.rate.xcRateN.platform = item.rate
                  break
              }
            }
          })
        }
      })
    },
    handleCancel() {
      this.previewVisible = false;
    },
    handleCancelClick () {
      this.evaluation = null
      this.orderInfo = null
      this.fileList = []
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
