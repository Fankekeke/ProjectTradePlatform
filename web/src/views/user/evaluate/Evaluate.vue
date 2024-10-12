<template>
  <a-modal v-model="show" :title="'订单评价 ' + orderInfo.code" @ok="evaluateCheck" @cancel="handleClose">
    <a-form layout="vertical" :form="form">
      <a-form-item label="评价分数" v-bind="formItemLayout">
        <a-rate v-decorator="['score']"/>
      </a-form-item>
      <a-form-item label="评价图片" v-bind="formItemLayout">
        <a-upload
          name="avatar"
          action="http://127.0.0.1:9528/file/fileUpload/"
          list-type="picture-card"
          :file-list="fileList"
          @preview="handlePreview"
          @change="picHandleChange"
        >
          <div v-if="fileList.length < 2">
            <a-icon type="plus" />
          </div>
        </a-upload>
        <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
          <img alt="example" style="width: 100%" :src="previewImage" />
        </a-modal>
      </a-form-item>
      <a-form-item label="评价内容" v-bind="formItemLayout">
        <a-textarea
          v-decorator="['content']"
          placeholder="填写评价内容"
          :auto-size="{ minRows: 6, maxRows: 8 }"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'Evaluate',
  props: {
    orderInfo: {
      type: Object
    },
    evaluateFlag: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    show: {
      get: function () {
        return this.evaluateFlag
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      previewVisible: false,
      previewImage: '',
      fileList: []
    }
  },
  methods: {
    evaluateCheck () {
      this.form.validateFields((err, values) => {
        if (!err) {
          if (values.score === undefined) {
            this.$message.warning('请填写评价分数！')
          }
          values.orderCode = this.orderInfo.code
          let evaluateImages = []
          this.fileList.forEach(image => {
            evaluateImages.push(image.response)
          })
          values.pic = evaluateImages.length > 0 ? evaluateImages.join(',') : null
          this.$post('/cos/evaluation', {...values}).then(() => {
            this.$message.success('评价成功！')
            this.handleOk()
          })
        }
      })
    },
    handleOk () {
      this.$emit('complete')
    },
    handleClose () {
      this.$emit('back')
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    }
  }
}
</script>

<style scoped>
  /deep/ .ant-form-item-label {
    font-family: SimHei;
    font-weight: 600;
  }
</style>
