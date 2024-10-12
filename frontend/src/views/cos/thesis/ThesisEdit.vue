<template>
  <a-modal v-model="show" title="修改附件" @cancel="onClose" :width="800">
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
          <a-form-item label='附件名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'annexName',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='附件类型' v-bind="formItemLayout">
            <a-select v-decorator="['annexType', { rules: [{ required: true, message: '请选择项目类型!' }] }]" @change="annexTypeChange">
              <a-select-option value="1">论文</a-select-option>
              <a-select-option value="2">PPT</a-select-option>
              <a-select-option value="3">任务书</a-select-option>
              <a-select-option value="4">开题报告</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='附件价格' v-bind="formItemLayout">
            <a-input-number v-decorator="['annexPrice', { rules: [{ required: true, message: '请填写价格!' }] }]" style="width: 100%"
              :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
              :parser="value => value.replace(/\￥\s?|(,*)/g, '')"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="wordCountShow">
          <a-form-item label='论文字数' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="['wordCount', { rules: [{ required: true, message: '请选择论文字数!' }] }]"
              :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='所属项目' v-bind="formItemLayout">
            <a-select v-decorator="['proCode']" mode="multiple" option-label-prop="label">
              <a-select-option v-for="(item, index) in projectList" :key="index" :value="item.code" :label="item.proName">
                <a-row>
                  <a-col :span="4">
                    <a-avatar style="margin-right: 20px" shape="square" :size="40" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/' + item.avatar" />
                  </a-col>
                  <a-col :span="20">
                    <a-row>
                      <a-col><span style="font-weight: 600">{{item.proName}}</span></a-col>
                      <a-col>
                        <span style="font-size: 10px">
                          <a-tag v-if="item.proType == 1">仅后台</a-tag>
                          <a-tag v-if="item.proType == 2">前后台</a-tag>
                          <a-tag v-if="item.proType == 3">小程序</a-tag>
                          <a-tag v-if="item.proType == 4">安卓</a-tag>
                        </span>
                        <span style="font-size: 10px">{{item.authorName !== null ? item.authorName : '无作者'}}</span>
                      </a-col>
                    </a-row>
                  </a-col>
                </a-row>
              </a-select-option>
            </a-select>
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
            <a-upload
              name="annex"
              action="http://127.0.0.1:9528/file/fileUploadByAnnex/"
              :multiple="true"
              :file-list="annexFileList"
              @change="annexHandleChange"
            >
              <a-button :disabled="annexFileList.length >= 1" style="width: 100%"> <a-icon type="upload" /> Upload </a-button>
            </a-upload>
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
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'ThesisEdit',
  props: {
    thesisEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.thesisEditVisiable
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
      annexFileList: [],
      fileList: [],
      loading: false,
      projectList: [],
      wordCountShow: false
    }
  },
  mounted() {
    this.getProjectLess()
  },
  methods: {
    annexTypeChange (value) {
      if (value == 1) {
        this.wordCountShow = true
      } else {
        this.wordCountShow = false
      }
    },
    getProjectLess () {
      this.$get('/cos/project-info/list/less').then((r) => {
        this.projectList = r.data.data
      })
    },
    picInit(pic) {
      if(pic !== null && pic !== '') {
        let imageList = []
        pic.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9528/imagesWeb/' + image})
          // imageList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
        })
        this.fileList = imageList;
      }
    },
    setFormValues ({...annex}) {
      this.rowId = annex.id
      let fields = ['annexName', 'annexType', 'annexPrice', 'wordCount', 'proCode', 'remark']
      let obj = {}
      Object.keys(annex).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = annex[key]
        }
        if(key === "annexType") {
          obj[key] = annex[key].toString()
        }
        if(key === "proCode" && annex['proCode'] !== null) {
          obj[key] = annex['proCode'].split(',')
        }
        if(key === "proCode" && annex['proCode'] === null) {
          delete obj[key]
        }
        if(key === "pic") {
          this.fileList = []
          this.picInit(annex["pic"])
        }
        if(key === "annexLocal") {
          this.annexFileList = []
          this.annexInit(annex["annexLocal"])
        }
      })
      this.form.setFieldsValue(obj)
    },
    /**
     * 附件文件
     * @param annex
     */
    annexInit(annex) {
      if(annex !== null && annex !== '') {
        this.annexFileList.push({uid: 1, name: annex, status: 'done'})
      }
    },
    annexHandleChange(info) {
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
      this.annexFileList = fileList;
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
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
    handleSubmit () {
      // 获取图片List
      let thesisImage = []
      this.fileList.forEach(image => {
        thesisImage.push(image.response !== undefined ? image.response : image.name)
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.annexLocal = this.annexFileList.length > 0 ? this.annexFileList[0].response : null
        values.pic = thesisImage.length > 0 ? thesisImage.join(",") : null
        if (!err) {
          values.author = this.currentUser.userId
          this.loading = true
          this.$put('/cos/annex-info', {
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
