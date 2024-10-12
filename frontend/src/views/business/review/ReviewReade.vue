<template>
  <a-modal
    v-model="show"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="1000"
    @cancel="handleCancleClick">
    <div style="font-size: 13px" v-if="reviewData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">申请信息</span></a-col>
        <a-col :span="6"><b>用户编号：</b>{{ reviewData.userCode }}</a-col>
        <a-col :span="6"><b>邮 箱：</b>{{ reviewData.email !== null ? reviewData.email : '- -' }}</a-col>
        <a-col :span="6"><b>申请人：</b>{{ reviewData.account !== null ? reviewData.account.substring(0, 1) + '***' + reviewData.account.substr(reviewData.account.length - 1, 1) : '- -' }}</a-col>
        <a-col :span="6"><b>申请金额：</b>{{ reviewData.balance !== null ? '￥' + reviewData.balance : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="6"><b>申请时间：</b>{{ reviewData.date }}</a-col>
        <a-col :span="6"><b>支付方式：</b>
          <a-tag v-if="reviewData.tradeType == 1" color="orange">支付宝</a-tag>
          <a-tag v-if="reviewData.tradeType == 2" color="blue">微信</a-tag>
          <a-tag v-if="reviewData.tradeType == 3" color="green">其他</a-tag>
        </a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 15px">
        <a-col :span="12">
          <a-row>
            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">收款码</span></a-col>
            <a-col :span="24" style="line-height: 30px">
              <a-popover v-if="reviewData.receiving !== null">
                <template slot="content">
                  <a-avatar shape="square" :size="240" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/'+reviewData.receiving" />
                </template>
                <a-avatar shape="square" :size="100" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/'+reviewData.receiving" />
              </a-popover>
            </a-col>
          </a-row>
        </a-col>
        <a-col :span="12">
          <a-row>
            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备注</span></a-col>
            <a-col :span="24" style="line-height: 30px">{{ reviewData.remarks !== null && reviewData.remarks !== '' ? reviewData.remarks : ' - - ' }}</a-col>
          </a-row>
        </a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 15px">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">转账完成</span></a-col>
        <a-col :span="24" style="line-height: 30px;">
          <!--http://82.157.17.18:9528-->
          <!--http://localhost:9528-->
          <a-upload
            :disabled="reviewData.review !== 2"
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
      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 35px" v-if="reviewData.review == 3">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单完成</span></a-col>
        <a-col :span="24" style="line-height: 30px">
          {{reviewData.finishDate}}交易完成，请及时查看申请报酬<span style="color: red">￥{{reviewData.balance}}</span>
        </a-col>
      </a-row>
      <a-row style="margin-top: 35px;padding: 0px 24px" v-if="reviewData.review == 2">
        <a-col :span="24" style="line-height: 30px">
          <a-button type="primary" style="float: right" @click="orderComplete">完成提交</a-button>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from "moment";

export default {
  name: 'ReviewReade',
  props: {
    reviewVisiable: {
      require: true,
      default: false
    },
    reviewData: {
      require: true
    }
  },
  data () {
    return {
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  watch: {
    reviewVisiable: function (val) {
      if (val) {
        if (this.reviewData.tradeResult !== null) {
          this.proPicInit(this.reviewData.tradeResult)
        }
      }
    }
  },
  computed: {
    show: {
      get: function () {
        return this.reviewVisiable
      },
      set: function () {
      }
    }
  },
  methods: {
    handleCancel () {
      this.previewVisible = false
    },
    orderComplete () {
      if (this.fileList.length !== 0) {
        let imageList = []
        this.fileList.forEach(image => {
          imageList.push(image.response !== undefined ? image.response : image.name)
        })
        this.$put('/cos/take-review', {
          id: this.reviewData.id, tradeResult: imageList.length > 0 ? imageList.join(',') : null, review: 3, finishDate: moment().format('YYYY-MM-DD HH:mm:ss')
        }).then((r) => {
          this.loading = false
          this.$emit('success')
        })
      } else {
        this.$message.warning('需要上传完成图片')
      }
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
    handleCancleClick () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
