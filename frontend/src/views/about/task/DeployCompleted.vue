<template>
  <a-modal v-model="show" @cancel="onClose" title="部署完成证据" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
      <a-button @click="onComplete">
        上传
      </a-button>
    </template>
    <div style="font-size: 13px">
      <a-form layout="vertical">
        <a-row>
          <a-col :span="24">
            <a-form-item label='' v-bind="formItemLayout">
              <a-upload
                name="avatar"
                action="http://127.0.0.1:9528/file/fileUpload/"
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
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
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

const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'DeployCompleted',
  props: {
    taskShow: {
      type: Boolean,
      default: false
    },
    taskData: {
      type: Object
    }
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
  data () {
    return {
      formItemLayout,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
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
    onComplete () {
      // 获取图片List
      let projectImage = []
      this.fileList.forEach(image => {
        projectImage.push(image.response)
      })
      let values = {customizeCode: this.taskData.code, deployUser: this.currentUser.userId}
      values.deployPic = projectImage.length > 0 ? projectImage.join(",") : null
      if (values.deployPic !== null) {
        this.$post('/cos/customize-ledger/complete', {
          ...values
        }).then((r) => {
          this.fileList = []
          this.$emit('finish')
        }).catch(() => {
          this.$message.warning('出现异常，请稍后再试！')
        })
      } else {
        this.$message.warning('请完整填写！')
      }
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
