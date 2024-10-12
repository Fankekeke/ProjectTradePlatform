<template>
  <a-drawer
    title="新增成员"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="teamAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-form-item label="成员名称" v-bind="formItemLayout">
        <a-input v-decorator="['players',{rules: [{ required: true, message: '请填写成员名称' }]}]"/>
      </a-form-item>
      <a-form-item label="所属人员" v-bind="formItemLayout">
        <a-select show-search :filter-option="filterOption" v-decorator="['adminUser',{rules: [{ required: true, message: '请填写所属人员' }]}]">
          <a-select-option v-for="(item, index) in adminUserData" :key="index" :value="item.userId">{{ item.username }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="工作时间" v-bind="formItemLayout">
        <a-input v-decorator="['workTime']"/>
      </a-form-item>
      <a-form-item label="部署时间" v-bind="formItemLayout">
        <a-input v-decorator="['deployTime']"/>
      </a-form-item>
      <a-form-item label="备 注" v-bind="formItemLayout">
        <a-textarea v-decorator="['remarks']"/>
      </a-form-item>
      <!--      https://yxblshop.cn:9528/file/fileUpload/-->
      <!--      http://127.0.0.1:9528/file/fileUpload/-->
      <a-form-item label='头像' v-bind="formItemLayout">
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
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'TeamAdd',
  props: {
    teamAddVisiable: {
      default: false
    }
  },
  data () {
    return {
      team: {},
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      previewVisible: false,
      fileList: [],
      previewImage: '',
      adminUserData: []
    }
  },
  methods: {
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    },
    getTeamUnbound () {
      this.$get('/cos/team-info/unbound').then((r) => {
        this.adminUserData = r.data.data
      })
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
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
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
          values.avatar = avatar.length > 0 ? avatar.join(",") : null
          this.team = values
          this.$post('/cos/team-info', {
            ...this.team
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  },
  watch: {
    teamAddVisiable: function (val) {
      if (val)
        this.getTeamUnbound()
    }
  }
}
</script>
