<template>
  <a-modal v-model="show" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="taskData !== null">
      <div style="padding: 0px 24px;margin-top: 15px" v-if="deployProcesse !== null && deployProcesse.length !== 0">
        <a-row :gutter="20">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">部署流程</span></a-col>
          <a-col>
            <a-steps :current="deployProcesseStep.step" :style="stepStyle" type="navigation" size="small">
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
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>订单编号：</b>{{ taskData.code }}</a-col>
        <a-col :span="8"><b>购买项目：</b>{{ taskData.proName !== null ? taskData.proName : '- -' }}</a-col>
        <a-col :span="8"><b>项目价格：</b>
          <a-tooltip>
            <template slot="title">
              <p>项目价格：￥{{ taskData.orderPrice }}</p>
              <p v-if="taskData.needDeploy === 1">部署价格：￥30</p>
            </template>
            ￥ <span>{{ taskData.orderPrice }}</span>
            <span v-if="taskData.needDeploy === 1"> + <span style="color: green">30</span></span>
          </a-tooltip>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="8"><b>下单时间：</b>{{ taskData.orderTime !== null ? taskData.orderTime : '- -' }}</a-col>
        <a-col :span="8"><b>支付方式：</b>
          <span v-if="taskData.paymentType === 1">支付宝</span>
          <span v-if="taskData.paymentType === 2">微信</span>
          <span v-if="taskData.paymentType === 3">平台下单</span>
        </a-col>
        <a-col :span="8" v-if="orderInfo !== null"><b>邀请人：</b>{{ orderInfo.inviteUser !== null ? orderInfo.inviteUser : '无邀请人' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="deployEvaluate !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">审核评价</span></a-col>
        <a-col :span="8"><b>审核状态：</b>
          <a-tag v-if="deployProcesseStep.step === 2" color="red">未通过</a-tag>
          <a-tag v-if="deployProcesseStep.step === 3" color="green">通过</a-tag>
        </a-col>
        <a-col :span="16"><b>审核状态：</b>
          <a-tooltip>
            <template slot="title">
              {{ deployEvaluate.remark }}
            </template>
            {{ deployEvaluate.remark.slice(0, 20) }}...
          </a-tooltip>
        </a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;">
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
      <br/>
      <div style="padding-left: 24px;padding-right: 24px;" v-if="orderInfo !== null">
        <a-card hoverable style="margin: 0 auto;text-align: center;" v-if="orderInfo !== null">
          <a-result title="开发者收益">
            <template #icon>
              <a-icon type="idcard" theme="twoTone" />
            </template>
          </a-result>
          <a-card-meta :title="'￥'+orderInfo.inviteUser !== null ? '￥'+(rate.xcRateY.developer * orderInfo.allPrice / 100).toFixed(2) : '￥'+(rate.xcRateN.developer * orderInfo.allPrice / 100).toFixed(2)"></a-card-meta>
        </a-card>
      </div>
    </div>
  </a-modal>
</template>

<script>
function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
}
import {mapState} from "vuex";

export default {
  name: 'OrderTaskView',
  props: {
    taskShow: {
      type: Boolean,
      default: false
    },
    taskData: {
      type: Object
    }
  },
  mounted() {
    this.getRate('developer')
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.taskShow
      },
      set: function () {
      }
    }
  },
  watch: {
    taskShow: function (value) {
      if (value) {
        this.orderDetailed(this.taskData.code)
      }
    }
  },
  data () {
    return {
      stepStyle: {
        marginBottom: '20px',
        boxShadow: '0px -1px 0 0 #e8e8e8 inset',
      },
      orderInfo: null,
      deployEvaluate: null,
      deployProcesse: [],
      deployProcesseStep: {
        step: 0,
        deploy1: null,
        deploy2: null,
        deploy3: null,
        deploy4: null,
      },
      fileList: [],
      previewVisible: false,
      previewImage: '',
      rate: {
        xcRateY: {
          developer: 0
        },
        xcRateN: {
          developer: 0
        },
      }
    }
  },
  methods: {
    orderDetailed (code) {
      this.$get('/cos/order-info/detailed', {orderCode: code}).then((r) => {
        this.orderInfo = r.data.orderInfo
        this.orderInfo.allPrice = this.orderInfo.orderPrice
        if (this.orderInfo.needDeploy === 1) {
          this.orderInfo.allPrice = this.orderInfo.orderPrice + 30
        }
        this.deployEvaluate = r.data.deployEvaluate
        if (this.deployEvaluate.evidence !== null) {
          let fileList = []
          this.deployEvaluate.evidence.split(',').forEach((image, index) => {
            fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
            // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
          })
          this.fileList = fileList
        }
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
      })
    },
    getRate (role) {
      this.$get('/cos/rate-info/getRateByRole', {role: role}).then((r) => {
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
      })
    },
    picHandleChange({ fileList }) {
      this.fileList = fileList
    },
    handleCancel() {
      this.previewVisible = false
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    onClose () {
      this.fileList = []
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
