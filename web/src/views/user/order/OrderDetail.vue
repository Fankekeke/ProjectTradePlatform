<template>
  <div>
    <a-skeleton v-if="loading" active :paragraph="{ rows: 6 }" style="margin-top: 50px;"/>
    <div v-if="!loading && orderInfo != null">
      <a-page-header
        :title="orderInfo.code"
        :sub-title="orderInfo.proName"
        @back="back"
      />
      <div style="padding: 23px;font-family: SimHei;">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span>
            <a-icon type="cloud-download" style="font-size: 18px;" @click="downloadResource()"/>
          </a-col>
          <a-col :span="8"><b>订单编号：</b>{{ orderInfo.code }}</a-col>
          <a-col :span="8"><b>购买项目：</b>{{ orderInfo.proName !== null ? orderInfo.proName : '- -' }}</a-col>
          <a-col :span="8"><b>项目价格：</b>￥{{ orderInfo.orderPrice !== null ? orderInfo.orderPrice : '- -' }}</a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
          <a-col :span="8"><b>下单时间：</b>{{ orderInfo.orderTime !== null ? orderInfo.orderTime : '- -' }}</a-col>
          <a-col :span="8"><b>支付方式：</b>
            <span v-if="orderInfo.paymentType === 1">支付宝</span>
            <span v-if="orderInfo.paymentType === 2">微信</span>
            <span v-if="orderInfo.paymentType === 3">平台下单</span>

          </a-col>
          <a-col :span="8"><b>收货人：</b>{{ orderInfo.account !== null ? orderInfo.account : '其他客户' }}</a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
          <a-col :span="8"><b>订单状态：</b>
            <span v-if="orderInfo.orderStatus === -1">已取消</span>
            <span v-if="orderInfo.orderStatus === 0">待支付</span>
            <span v-if="orderInfo.orderStatus === 1">已支付</span>
          </a-col>
          <a-col :span="8"><b>需要部署：</b>
            <b v-if="orderInfo.needDeploy === 0" style="color: red">否</b>
            <b v-if="orderInfo.needDeploy === 1" style="color: green">是</b>
          </a-col>
          <a-col :span="8"><b>价格明细：</b>
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
        <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
          <a-col style="margin-top: 30px">
            <a-divider/>
          </a-col>
        </a-row>
        <div style="padding: 0px 24px" v-if="deployProcesse !== null && deployProcesse.length !== 0">
          <a-row :gutter="20">
            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">部署流程</span>
            </a-col>
            <a-col>
              <a-steps :current="deployProcesseStep.step" style="margin-top: 50px">
                <a-popover slot="progressDot" slot-scope="{ index, status, prefixCls }">
                  <template slot="content">
                  <span
                    v-if="index === 0">付款成功：{{
                      deployProcesseStep.deploy1.operateTime !== null ? deployProcesseStep.deploy1.operateTime : ''
                    }}</span>
                    <div v-if="index === 1">
                      <div style="display: inline-block;margin: 5px" v-if="deployProcesseStep.deploy2.operatePic !== null"
                           v-for="(item, index) in deployProcesseStep.deploy2.operatePic.split(',')" :key="index">
                        <a-avatar shape="square" :size="120" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/'+item"/>
                      </div>
                      <div v-else>
                        该订单未部署完成
                      </div>
                    </div>
                    <div v-if="index === 2">
                      <div v-if="deployProcesseStep.step === 2">
                      <span
                        style="color: red">审核未通过</span> - <span>{{
                          deployProcesseStep.deploy2.operateTime !== null ? deployProcesseStep.deploy2.operateTime : ''
                        }}</span>
                      </div>
                      <div v-if="deployProcesseStep.step === 3">
                      <span
                        style="color: green">审核通过</span> - <span>{{
                          deployProcesseStep.deploy3.operateTime !== null ? deployProcesseStep.deploy3.operateTime : ''
                        }}</span>
                      </div>
                    </div>
                    <span v-if="index === 3">分配完成</span>
                  </template>
                  <span :class="`${prefixCls}-icon-dot`"/>
                </a-popover>
                <a-step title="付款成功"
                        :description="deployProcesseStep.deploy1.operateTime !== null ? deployProcesseStep.deploy1.operateTime : ''"/>
                <a-step title="部署完成"
                        :description="deployProcesseStep.deploy2.operateTime !== null ? deployProcesseStep.deploy2.operateTime : ''"/>
                <a-step title="审核结果" description=""/>
                <a-step title="佣金分配" description=""/>
              </a-steps>
            </a-col>
          </a-row>
        </div>
        <div v-if="deployProcesseStep.step === 0 && orderInfo.needDeploy === 1">
          <a-result title="等待开发者联系并进行项目部署！" >
            <template #icon>
              <a-icon type="smile" theme="twoTone" />
            </template>
          </a-result>
        </div>
        <div v-else>
          <a-result v-if="evaluation == null" title="对此订单进行评价！" >
            <template #icon>
              <a-icon type="smile" theme="twoTone" />
            </template>
            <template #extra>
              <a-button type="primary" @click="evaluateFlag = true">
                评价
              </a-button>
            </template>
          </a-result>
        </div>
        <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
          <a-col style="margin-top: 30px">
            <a-divider/>
          </a-col>
        </a-row>
        <div v-if="evaluation !== null">
          <a-row style="padding-left: 24px;padding-right: 24px;">
            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单评价</span>
            </a-col>
            <a-col :span="6"><b>评价时间：</b>{{ evaluation.date }}</a-col>
          </a-row>
          <br/>
          <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
            <a-col :span="24"><b>评分：</b>
              <a-rate :default-value="evaluation.score" disabled/>
            </a-col>
          </a-row>
          <br/>
          <br/>
          <a-row style="padding-left: 24px;padding-right: 24px;">
            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">评价内容</span>
            </a-col>
            <a-col :span="24" style="line-height: 30px">{{ evaluation.content }}</a-col>
            <a-col :span="24" style="line-height: 30px;margin-top: 20px">
              <a-upload
                list-type="picture-card"
                :file-list="fileList"
                @preview="handlePreview"
                @change="picHandleChange">
              </a-upload>
              <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                <img alt="example" style="width: 100%" :src="previewImage" />
              </a-modal>
            </a-col>
          </a-row>
        </div>
        <evaluate v-if="evaluateFlag" :evaluateFlag="evaluateFlag" :orderInfo="orderInfo" @back="evaluateFlag = false" @complete="evaluateComplete"/>
      </div>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import Evaluate from '../evaluate/Evaluate'

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'OrderDetail',
  components: {Evaluate},
  computed: {
    ...mapState({
      user: state => state.account.user,
      client: state => state.account.client,
      fileHost: state => state.account.fileHost
    })
  },
  props: {
    orderCode: {
      type: String
    }
  },
  watch: {
    orderCode: function (val) {
      if (val) {
        this.orderDetailed(val)
      }
    }
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
        deploy4: null
      },
      evaluateFlag: false,
      loading: false
    }
  },
  methods: {
    orderCheck (orderCode) {
      let reg = RegExp(/AORD-/)
      return reg.test(orderCode)
    },
    downloadResource (orderCode) {
      let downloadUrl = this.orderCheck(orderCode) ? '/cos/annex-order/download' : '/cos/order-info/download'
      this.$post(downloadUrl, {orderCode, userCode: this.client.code}).then(() => {})
    },
    evaluateComplete () {
      this.evaluateFlag = false
      this.orderDetailed(this.orderCode)
    },
    orderDetailed (code) {
      this.loading = true
      this.$get(`/cos/order-info/detailed/${code}`).then((r) => {
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
          if (this.evaluation.pic !== null) {
            let fileList = []
            this.evaluation.pic.split(',').forEach((image, index) => {
              fileList.push({uid: index, name: image, status: 'done', url: this.fileHost + 'imagesWeb/' + image})
            })
            this.fileList = fileList
          }
        }
        setTimeout(() => {
          this.loading = false
        }, 200)
      })
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    handleCancel () {
      this.previewVisible = false
    },
    getDetail (orderCode) {
      this.$get(`/cos/user-info/order/detail/${orderCode}`).then((r) => {
        this.orderInfo = r.data
      })
    },
    back () {
      this.$emit('back')
    }
  }
}
</script>

<style scoped>
  /deep/ .ant-page-header-heading-title {
    letter-spacing: 1px;
    font-family: SimHei;
  }

  .ant-carousel >>> .slick-slide {
    text-align: center;
    height: 460px;
    line-height: 460px;
    overflow: hidden;
  }

  /deep/ .ant-descriptions-item-label {
    width: 126px;
  }
</style>
