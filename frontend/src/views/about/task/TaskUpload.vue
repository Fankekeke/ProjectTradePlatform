<template>
  <a-modal v-model="show" @cancel="onClose" title="定制完成" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
      <a-button @click="onUpload">
        上传
      </a-button>
    </template>
    <div style="font-size: 13px">
      <a-form layout="vertical">
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-item label='项目文件' v-bind="formItemLayout">
              <a-upload-dragger
                name="zip"
                accept=".zip"
                action="http://127.0.0.1:9528/file/fileUploadByZip/"
                :multiple="true"
                :file-list="customFileList"
                @change="customHandleChange"
              >
                <p class="ant-upload-drag-icon">
                  <a-icon type="inbox" />
                </p>
                <p class="ant-upload-text">
                  单击或拖动文件到该区域进行上传
                </p>
                <p class="ant-upload-hint">
                  支持单次上传。 严禁上传公司资料或其他乐队文件
                </p>
              </a-upload-dragger>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label='备注' v-bind="formItemLayout">
              <a-textarea
                v-model="remark"
                placeholder="Controlled autosize"
                :auto-size="{ minRows: 3, maxRows: 5 }"
              />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label='项目图册' v-bind="formItemLayout">
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
import {mapState} from 'vuex'

const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'TaskUpload',
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
      remark: '',
      customFileList: [],
      fileList: [],
      previewVisible: false,
      previewImage: '',
    }
  },
  methods: {
    customHandleChange (info) {
      let fileList = [...info.fileList];
      fileList = fileList.slice(-2);
      fileList = fileList.map(file => {
        if (file.response) {
          file.url = file.response.url;
        }
        return file;
      });
      this.customFileList = fileList;
    },
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
    onUpload () {
      // 获取图片List
      let projectImage = []
      this.fileList.forEach(image => {
        projectImage.push(image.response)
      })
      let values = {customizeCode: this.taskData.code, remarks: this.remark}
      values.local = this.customFileList.length > 0 ? this.customFileList[0].response : null
      values.pic = projectImage.length > 0 ? projectImage.join(",") : null
      values.proposer = this.currentUser.userId
      if (values.local !== null && values.pic !== null) {
        this.$post('/cos/customize-ledger/finish', {
          ...values
        }).then((r) => {
          this.reset()
          this.$emit('finish')
        }).catch(() => {
          this.$message.warning('出现异常，请稍后再试！')
        })
      } else {
        this.$message.warning('请完整填写！')
      }
    },
    reset () {
      this.remark = ''
      this.customFileList = []
      this.fileList = []
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
