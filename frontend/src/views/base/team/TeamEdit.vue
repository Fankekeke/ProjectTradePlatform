<template>
  <a-drawer
    title="修改成员"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="teamEditVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-row :gutter="15">
        <a-col :span="12">
          <a-form-item label="成员名称">
            <a-input v-decorator="['players',{rules: [{ required: true, message: '请填写成员名称' }]}]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="所属人员">
            <a-select show-search :filter-option="filterOption" v-decorator="['adminUser',{rules: [{ required: true, message: '请填写所属人员' }]}]">
              <a-select-option v-for="(item, index) in adminUserData" :key="index" :value="item.userId">{{ item.username }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="工作时间">
            <a-input v-decorator="['workTime']"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="部署时间">
            <a-input v-decorator="['deployTime']"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备 注">
            <a-textarea v-decorator="['remarks']"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <!--      https://yxblshop.cn:9528/file/fileUpload/-->
          <!--      http://127.0.0.1:9528/file/fileUpload/-->
          <a-form-item label='头像'>
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9528/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 1">
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
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>
    </div>
  </a-drawer>
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
import {mapState, mapMutations} from 'vuex'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'TeamEdit',
  props: {
    teamEditVisiable: {
      default: false
    },
    teamTypeData: {
      type: Array
    },
    teamRiskData: {
      type: Array
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      previewVisible: false,
      rowId: '',
      loading: false,
      fileList: [],
      previewImage: '',
      adminUserData: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  methods: {
    ...mapMutations({
      setUser: 'account/setUser'
    }),
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
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
    onClose () {
      this.loading = false
      this.form.resetFields()
      this.$emit('close')
    },
    avatarInit(avatar) {
      if(avatar !== null && avatar !== '') {
        let imageList = []
        avatar.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9528/imagesWeb/'+image})
          // imageList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
        })
        this.fileList = imageList;
      }
    },
    setFormValues ({...team}) {
      this.rowId = team.id
      let fields = ['players', 'adminUser', 'remarks', 'status', 'workTime', 'deployTime']
      let obj = {}
      Object.keys(team).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = team[key]
        }
        if(key === "avatar") {
          this.fileList = []
          this.avatarInit(team["avatar"])
        }
      })
      this.$get(`/user/id/${obj['adminUser']}`).then((r) => {
        this.adminUserData = [r.data.data]
        obj['adminUser'] = obj['adminUser']
      })
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      // 获取图片
      let avatar = []
      this.fileList.forEach(image => {
        avatar.push(image.response !== undefined ? image.response : image.name)
      })
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          let team = this.form.getFieldsValue()
          team.avatar = avatar.length > 0 ? avatar.join(",") : null
          team.id = this.rowId
          this.$put('/cos/team-info', {
            ...team
          }).then((r) => {
            this.loading = false
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  },
  watch: {}
}
</script>
