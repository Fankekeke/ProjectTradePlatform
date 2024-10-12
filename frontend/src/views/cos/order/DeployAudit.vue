<template>
  <a-modal
    v-model="show"
    title="部署审核"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="1000"
    @cancel="handleCancelClick">
    <div style="font-size: 13px" v-if="show">
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
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>订单编号：</b>{{ orderData.code }}</a-col>
        <a-col :span="8"><b>购买项目：</b>{{ orderData.proName !== null ? orderData.proName : '- -' }}</a-col>
        <a-col :span="8"><b>项目价格：</b>￥{{ orderData.orderPrice !== null ? orderData.orderPrice : '- -' }}</a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 35px">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">审核</span></a-col>
        <a-col>
          <a-textarea
            v-model="remarks"
            placeholder="输入评价信息"
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-col>
        <a-col :span="24" style="line-height: 30px;margin-top: 30px">
          <!--http://82.157.17.18:9528-->
          <!--http://localhost:9528-->
          <a-upload
            name="avatar"
            action="http://localhost:9528/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
            <div v-if="fileList.length < 8">
              <a-icon type="plus" />
              <div class="ant-upload-text">
                Upload
              </div>
            </div>
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <a-row style="margin-top: 35px;padding: 0px 24px">
        <a-col :span="24" style="line-height: 30px">
          <div style="float: right">
            <a-button type="danger" @click="complete(4)">驳回</a-button>
            &nbsp;
            <a-button type="primary" @click="complete(3)">提交</a-button>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'DeployAudit',
  props: {
    auditShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  watch: {
    auditShow: function (val) {
      if (val) {
        this.orderDetailed(this.orderData.code)
      }
    }
  },
  computed: {
    show: {
      get: function () {
        return this.auditShow
      },
      set: function () {
      }
    },
  },
  data() {
    return {
      fileList: [],
      remarks: '',
      previewVisible: false,
      previewImage: '',
      orderInfo: null,
      deployProcesse: [],
      deployEvaluate: null,
      deployProcesseStep: {
        step: 0,
        deploy1: null,
        deploy2: null,
        deploy3: null,
        deploy4: null,
      },
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
        if (this.deployEvaluate !== null) {
          this.remarks = this.deployEvaluate.remark
          if (this.deployEvaluate.evidence !== null) {
            this.proPicInit(this.deployEvaluate.evidence)
          }
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
      })
    },
    handleCancel () {
      this.previewVisible = false
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
    /**
     * 图片展示
     * @param proPic
     */
    proPicInit (proPic) {
      if (proPic !== null && proPic !== '') {
        let imageList = []
        proPic.split(',').forEach((image, index) => {
          // imageList.push({uid: index, name: image, status: 'done', url: 'http://82.157.17.18:9528/imagesWeb/' + image})
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9528/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    complete (status) {
      let imageList = []
      this.fileList.forEach(image => {
        imageList.push(image.response !== undefined ? image.response : image.name)
      })
      this.$post('/cos/deploy-evaluate/audit', {
        orderCode: this.orderInfo.code,
        evidence: imageList.length > 0 ? imageList.join(',') : null,
        remark: this.remarks,
        status: status
      }).then((r) => {
        this.$emit('success')
      })
    },
    handleCancelClick () {
      this.fileList = []
      this.remarks = ''
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
