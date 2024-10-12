<template>
  <a-modal
    title="订单信息"
    v-model="show"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="1000"
    @cancel="handleCancelClick">
    <div style="font-size: 13px" v-if="show">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="6"><b>用户编号：</b>{{ evaluationData.userCode }}</a-col>
        <a-col :span="6"><b>订单编号：</b>{{ evaluationData.orderCode !== null ? evaluationData.orderCode : '- -' }}</a-col>
        <a-col :span="6"><b>支付方式：</b>
          <span v-if="evaluationData.paymentType === 1">支付宝</span>
          <span v-if="evaluationData.paymentType === 2">微信</span>
          <span v-if="evaluationData.paymentType === 3">平台下单</span>
        </a-col>
        <a-col :span="6"><b>购买人：</b>{{ evaluationData.account !== null ? evaluationData.account : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="6"><b>项目名称：</b>{{ evaluationData.proName !== null ? evaluationData.proName : '- -' }}</a-col>
        <a-col :span="6"><b>项目类型：</b>
          <span v-if="evaluationData.paymentType === 1">仅后台</span>
          <span v-if="evaluationData.paymentType === 2">前后台</span>
          <span v-if="evaluationData.paymentType === 3">小程序</span>
          <span v-if="evaluationData.paymentType === 4">安 卓</span>
        </a-col>
        <a-col :span="6"><b>作 者：</b>{{ evaluationData.authorName !== null ? evaluationData.authorName : '- -' }}</a-col>
        <a-col :span="6"><b>下单时间：</b>{{ evaluationData.orderTime !== null ? evaluationData.orderTime : '- -' }}</a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">详情</span></a-col>
        <a-col :span="6"><b>评价时间：</b>{{ evaluationData.date }}</a-col>
        <a-col :span="6"><b>需要部署：</b>
          <b v-if="evaluationData.needDeploy === 0" style="color: red">否</b>
          <b v-if="evaluationData.needDeploy === 1" style="color: green">是</b>
        </a-col>
        <a-col :span="6"><b>价格明细：</b>
          <a-tooltip>
            <template slot="title">
              <p>项目价格：￥{{ evaluationData.orderPrice }}</p>
              <p v-if="evaluationData.needDeploy === 1">部署价格：￥30</p>
            </template>
            ￥ <span>{{ evaluationData.orderPrice }}</span>
            <span v-if="evaluationData.needDeploy === 1"> + <span style="color: green">30</span></span>
          </a-tooltip>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="24"><b>评分：</b>
          <a-rate :default-value="evaluationData.score" disabled />
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">评价内容</span></a-col>
        <a-col :span="24" style="line-height: 30px">{{ evaluationData.content }}</a-col>
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
export default {
  name: 'EvaluationInfo',
  props: {
    evaluationShow: {
      type: Boolean,
      default: false
    },
    evaluationData: {
      type: Object
    }
  },
  watch: {
    evaluationShow: function (data) {
      if(this.evaluationData.pic !== null) {
        let fileList = []
        this.evaluationData.pic.split(',').forEach((image, index) => {
          fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
          // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
        })
        this.fileList = fileList;
      }
    }
  },
  computed: {
    show: {
      get: function () {
        return this.evaluationShow
      },
      set: function () {
      }
    }
  },
  data() {
    return {
      fileList: [],
      previewVisible: false,
      previewImage: '',
    }
  },
  methods: {
    picHandleChange({ fileList }) {
      this.fileList = fileList;
    },
    handleCancel() {
      this.previewVisible = false;
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    },
    handleCancelClick () {
      this.$emit('close')
    },
  }
}
</script>

<style scoped>

</style>
