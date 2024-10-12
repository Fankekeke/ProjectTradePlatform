<template>
  <a-modal v-model="show" title="上传附件" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='附件名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'annexName',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='附件类型' v-bind="formItemLayout">
            <a-select v-decorator="['annexType', { rules: [{ required: true, message: '请选择项目类型!' }] }]"
                      @change="annexTypeChange">
              <a-select-option value="1">论文</a-select-option>
              <a-select-option value="2">PPT</a-select-option>
              <a-select-option value="3">任务书</a-select-option>
              <a-select-option value="4">开题报告</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='附件价格' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
              'annexPrice', { rules: [{ required: true, message: '请填写价格!' }] }
              ]"
                            :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                            :parser="value => value.replace(/\￥\s?|(,*)/g, '')"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="wordCountShow">
          <a-form-item label='论文字数' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
              'wordCount', { rules: [{ required: true, message: '请选择论文字数!' }] }
              ]"
                            :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='备 注' v-bind="formItemLayout">
            <a-textarea :rows="4" v-decorator="[
            'remark'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <!--      https://yxblshop.cn:9528/file/fileUploadByAnnex/-->
          <!--      http://127.0.0.1:9528/file/fileUploadByAnnex/-->
          <a-form-item label='上传附件' v-bind="formItemLayout">
            <a-upload-dragger
              :disabled="annexFileList.length >= 1"
              name="annex"
              :multiple="false"
              action="http://127.0.0.1:9528/file/fileUploadByAnnex/"
              :file-list="annexFileList"
              @change="annexHandleChange"
            >
              <p class="ant-upload-drag-icon">
                <a-icon type="folder"/>
              </p>
              <p class="ant-upload-text">
                单击或拖动文件到该区域进行上传
              </p>
              <p class="ant-upload-hint">
                支持单次或批量上传。 严禁上传公司资料或其他的文件
              </p>
            </a-upload-dragger>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <!--      https://yxblshop.cn:9528/file/fileUpload/-->
          <!--      http://127.0.0.1:9528/file/fileUpload/-->
          <a-form-item label='附件图册' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9528/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 8">
                <a-icon type="plus"/>
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage"/>
            </a-modal>
          </a-form-item>
        </a-col>
        <a-col :span="12"></a-col>
      </a-row>

    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from "vuex";

function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
}

const formItemLayout = {
  labelCol: {span: 24},
  wrapperCol: {span: 24}
}
export default {
  name: 'ThesisUpload',
  props: {
    thesisAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.thesisAddVisiable
      },
      set: function () {
      }
    }
  },
  data() {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      previewVisible: false,
      previewImage: '',
      annexFileList: [],
      fileList: [],
      loading: false,
      wordCountShow: false
    }
  },
  methods: {
    annexTypeChange(value) {
      if (value == 1) {
        this.wordCountShow = true
      } else {
        this.wordCountShow = false
      }
    },
    annexHandleChange(info) {
      let fileList = [...info.fileList]
      fileList = fileList.slice(-2)
      fileList = fileList.map(file => {
        if (file.response) {
          file.url = file.response.url
        }
        return file
      });
      this.annexFileList = fileList
    },
    reset() {
      this.loading = false
      this.form.resetFields()
    },
    onClose() {
      this.reset()
      this.$emit('close')
    },
    picHandleChange({fileList}) {
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
    handleSubmit() {
      // 获取图片List
      let thesisImage = []
      this.fileList.forEach(image => {
        thesisImage.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.annexLocal = this.annexFileList.length > 0 ? this.annexFileList[0].response : null
        values.pic = thesisImage.length > 0 ? thesisImage.join(",") : null
        if (!err) {
          values.userId = this.currentUser.userId
          this.loading = true
          this.$post('/cos/annex-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
