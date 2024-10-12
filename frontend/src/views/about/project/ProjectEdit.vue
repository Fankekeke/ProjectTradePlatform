<template>
  <a-modal v-model="show" title="上传项目" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='产品名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'proName',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='项目类型' v-bind="formItemLayout">
            <a-select v-decorator="['proType', { rules: [{ required: true, message: '请选择项目类型!' }] }]">
              <a-select-option value="1">仅后台</a-select-option>
              <a-select-option value="2">前后台</a-select-option>
              <a-select-option value="3">小程序</a-select-option>
              <a-select-option value="4">安 卓</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='技术方案' v-bind="formItemLayout">
            <a-input v-decorator="[
            'proTechnology', { rules: [{ required: true, message: '请选择技术方案!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='项目分类' v-bind="formItemLayout">
            <a-checkbox-group
              style="line-height: 35px;"
              v-decorator="['proSort', { rules: [{ required: true, message: '请选择项目分类!' }] }]"
              :options="plainOptions"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='产品介绍' v-bind="formItemLayout">
            <a-textarea :rows="4" v-decorator="[
            'proIntroduce', { rules: [{ required: true, message: '请选择产品介绍!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <!--      https://yxblshop.cn:9528/file/fileUploadByVideo/-->
          <!--      http://127.0.0.1:9528/file/fileUploadByVideo/-->
          <a-form-item label='上传视频' v-bind="formItemLayout">
            <a-upload-dragger
              :disabled="videoFileList.length >= 1"
              name="video"
              accept=".mp4"
              :multiple="false"
              action="http://127.0.0.1:9528/file/fileUploadByVideo/"
              :file-list="videoFileList"
              @change="videoHandleChange"
            >
              <p class="ant-upload-drag-icon">
                <a-icon type="video-camera" />
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
          <!--      https://yxblshop.cn:9528/file/fileUploadByZip/-->
          <!--      http://127.0.0.1:9528/file/fileUploadByZip/-->
          <a-form-item label='项目文件' v-bind="formItemLayout">
            <a-upload-dragger
              :disabled="zipFileList.length >= 1"
              name="zip"
              accept=".zip"
              :multiple="false"
              action="http://127.0.0.1:9528/file/fileUploadByZip/"
              :file-list="zipFileList"
              @change="zipHandleChange"
            >
              <p class="ant-upload-drag-icon">
                <a-icon type="file-zip"/>
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
          <a-form-item label='产品图册' v-bind="formItemLayout">
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
import {mapState} from 'vuex'

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
  name: 'ProjectEdit',
  props: {
    projectEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.projectEditVisiable
      },
      set: function () {
      }
    }
  },
  data() {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      previewVisible: false,
      previewImage: '',
      videoFileList: [],
      zipFileList: [],
      fileList: [],
      plainOptions: ['Java项目', 'SSM框架', 'SpringBoot框架', '管理系统', 'VUE项目', '企业官网', '后台应用', '移动办公', '小程序配套', '热销产品'],
      loading: false
    }
  },
  methods: {
    picInit(pic) {
      if (pic !== null && pic !== '') {
        let imageList = []
        pic.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9528/imagesWeb/' + image})
          // imageList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
        })
        this.fileList = imageList;
      }
    },
    setFormValues({...project}) {
      this.rowId = project.id
      let fields = ['proName', 'proType', 'proTechnology', 'proIntroduce', 'deployTime']
      let obj = {}
      Object.keys(project).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = project[key]
        }
        if (key === "proType") {
          obj[key] = project[key].toString()
        }
        if (key === "proPic") {
          this.fileList = []
          this.picInit(project["proPic"])
        }
        if (key === "proVoide") {
          this.videoFileList = []
          this.proVideoInit(project["proVoide"])
        }
        if (key === "proDowbloadZip") {
          this.zipFileList = []
          this.proZipInit(project["proDowbloadZip"])
        }
        if (key === "proSort" && project[key] !== null) {
          this.form.getFieldDecorator('proSort')
          this.form.setFieldsValue({'proSort': project[key].split(',')})
        }
      })
      this.form.setFieldsValue(obj)
    },
    videoHandleChange(info) {
      let fileList = [...info.fileList];
      // 1. Limit the number of uploaded files
      //    Only to show two recent uploaded files, and old ones will be replaced by the new
      fileList = fileList.slice(-2);
      // 2. read from response and show file link
      fileList = fileList.map(file => {
        if (file.response) {
          // Component will show file.url as link
          file.url = file.response.url;
        }
        return file;
      });
      this.videoFileList = fileList;
    },
    /**
     * 视频文件展示
     * @param proVideo
     */
    proVideoInit(proVideo) {
      if (proVideo !== null && proVideo !== '') {
        this.videoFileList.push({
          uid: 1,
          name: proVideo,
          status: 'done',
          url: 'http://127.0.0.1:9528/videoWeb/' + proVideo
        })
        // this.videoFileList.push({uid: 1, name: proVideo, status: 'done', url: 'https://yxblshop.cn:9528/videoWeb/'+proVideo})
      }
    },
    /**
     * 项目文件
     * @param proZip
     */
    proZipInit(proZip) {
      if (proZip !== null && proZip !== '') {
        this.zipFileList.push({uid: 1, name: proZip, status: 'done'})
      }
    },
    zipHandleChange(info) {
      let fileList = [...info.fileList];
      // 1. Limit the number of uploaded files
      //    Only to show two recent uploaded files, and old ones will be replaced by the new
      fileList = fileList.slice(-2);
      // 2. read from response and show file link
      fileList = fileList.map(file => {
        if (file.response) {
          // Component will show file.url as link
          file.url = file.response.url;
        }
        return file;
      });
      this.zipFileList = fileList;
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
      let projectImage = []
      this.fileList.forEach(image => {
        projectImage.push(image.response !== undefined ? image.response : image.name)
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.proVoide = this.videoFileList.length > 0 ? this.videoFileList[0].response : null
        values.proDowbloadZip = this.zipFileList.length > 0 ? this.zipFileList[0].response : null
        values.proPic = projectImage.length > 0 ? projectImage.join(",") : null
        if (!err) {
          values.author = this.currentUser.userId
          this.loading = true
          this.$put('/cos/project-apply', {
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
