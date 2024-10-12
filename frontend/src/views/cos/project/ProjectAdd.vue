<template>
  <a-drawer
    title="新增项目"
    :maskClosable="false"
    width=850
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="userAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">

    <a-form :form="form">
      <a-form-item label='产品名称' v-bind="formItemLayout">
        <a-input v-decorator="[
        'proName',
        { rules: [{ required: true, message: '请输入名称!' }] }
        ]"/>
      </a-form-item>
      <a-form-item label='产品价格' v-bind="formItemLayout">
        <a-input-number :min="0" :step="0.1" style="width: 100%" v-decorator="[
        'proPrice'
        ]"/>
      </a-form-item>
      <a-form-item label='技术方案' v-bind="formItemLayout">
        <a-input v-decorator="[
        'proTechnology'
        ]"/>
      </a-form-item>
      <a-form-item label='项目类型' v-bind="formItemLayout">
        <a-select v-decorator="['proType']">
          <a-select-option value="1">仅后台</a-select-option>
          <a-select-option value="2">前后台</a-select-option>
          <a-select-option value="3">小程序</a-select-option>
          <a-select-option value="4">安 卓</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label='项目分类' v-bind="formItemLayout">
        <a-checkbox-group
          v-decorator="['proSort']"
          :options="plainOptions"
          @change="sortChange"
        />
      </a-form-item>
      <a-form-item label='项目作者' v-bind="formItemLayout">
        <a-input v-decorator="['authorName', { rules: [{ required: true, message: '请选择作者!' }] }]" @click="childrenDrawer = true"/>
      </a-form-item>
      <a-form-item label='服务器机' v-bind="formItemLayout">
        <a-input v-decorator="[
        'proDownloadUrl'
        ]"/>
      </a-form-item>
      <a-form-item label='产品介绍' v-bind="formItemLayout">
        <a-textarea :rows="4" v-decorator="[
        'proIntroduce'
        ]"/>
      </a-form-item>
      <br/>
<!--      https://yxblshop.cn:9528/file/fileUploadByVideo/-->
<!--      http://127.0.0.1:9528/file/fileUploadByVideo/-->
      <a-form-item label='上传视频' v-bind="formItemLayout">
        <a-upload
          name="video"
          accept=".mp4"
          action="http://127.0.0.1:9528/file/fileUploadByVideo/"
          :multiple="true"
          :file-list="videoFileList"
          @change="videoHandleChange"
        >
          <a-button :disabled="videoFileList.length >= 1" style="width: 100%"> <a-icon type="upload" /> Upload </a-button>
        </a-upload>
      </a-form-item>
<!--      https://yxblshop.cn:9528/file/fileUploadByZip/-->
<!--      http://127.0.0.1:9528/file/fileUploadByZip/-->
      <a-form-item label='项目文件' v-bind="formItemLayout">
        <a-upload
          name="zip"
          accept=".zip"
          action="http://127.0.0.1:9528/file/fileUploadByZip/"
          :multiple="true"
          :file-list="zipFileList"
          @change="zipHandleChange"
        >
          <a-button :disabled="zipFileList.length >= 1" style="width: 100%"> <a-icon type="upload" /> Upload </a-button>
        </a-upload>
      </a-form-item>
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

    <a-drawer
      title="选择作者"
      width="650"
      :maskClosable="true"
      :closable="false"
      :visible="childrenDrawer"
      @close="onChildrenDrawerClose"
    >
      <a-table :columns="columns" :data-source="players" bordered :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }">
        <template slot="name" slot-scope="text, record">
          <a-popover v-if="record.avatar !== null">
            <template slot="content">
              <a-avatar shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/' + record.avatar" />
<!--              <a-avatar shape="square" :size="132" icon="user" :src="'https://yxblshop.cn:9528/imagesWeb/' + record.avatar" />-->
            </template>
            <a>{{record.players}}</a>
          </a-popover>
          <a v-else>{{ text }}</a>
        </template>
        <template slot="title" slot-scope="currentPageData">
          <a-input-search placeholder="根据昵称筛选" @search="getTeam" />
        </template>
        <template slot="footer" slot-scope="currentPageData">
          <div v-if="selectedRow.players !== undefined">当前选择 <a>{{selectedRow.players}}</a></div>
        </template>
      </a-table>
    </a-drawer>
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
    name: 'UserAdd',
    props: {
      userAddVisiable: {
        default: false
      }
    },
    computed: {
      columns () {
        return [{
          title: '账户名称',
          dataIndex: 'players',
          scopedSlots: { customRender: 'name' }
        },
          {
            title: '部门',
            dataIndex: 'deptName'
          },
          {
            title: '工作时间',
            dataIndex: 'workTime'
          }]
      }
    },
    data () {
      return {
        user: {
          accountCode: '',
          accountPwd: '',
          accountName: '',
          expireDate: '',
          accountStatus: '',
          defaultApp: ''
        },
        dateFormat: 'YYYY/MM/DD',
        loading: false,
        roleData: [],
        deptTreeData: [],
        mockData: [],
        targetKeys: [],
        appInfoData: [],
        roleInfoData: [],
        roleKeys: [],
        menuInfoData: [],
        orgInfoData: [],
        accountInfo: [],
        menuKeys: [],
        formItemLayout,
        defaultPassword: '1234qwer',
        form: this.$form.createForm(this),
        validateStatus: '',
        help: '',
        previewVisible: false,
        previewImage: '',
        videoFileList: [],
        zipFileList: [],
        fileList: [],
        plainOptions: ['Java项目', 'SSM框架', 'SpringBoot框架', '管理系统', 'VUE项目', '企业官网', '后台应用', '移动办公', '小程序配套', '热销产品'],
        childrenDrawer: false,
        players: [],
        selectedRowKeys: [],
        selectedRow: {}
      }
    },
    mounted() {
      this.getTeam('')
    },
    methods: {
      getTeam (account) {
        this.$get('/cos/team-info/teamList', {account}).then((r) => {
          this.players = r.data.data
        })
      },
      onChildrenDrawerClose() {
        this.childrenDrawer = false
      },
      onSelectChange (selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys
        this.selectedRow = selectedRows[0]
        this.form.setFieldsValue({
          authorName: this.selectedRow.players
        })
      },
      sortChange(checkedValues) {
        console.log('checked = ', checkedValues);
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
        // 获取图片List
        let projectImage = []
        this.fileList.forEach(image => {
          projectImage.push(image.response)
        })
        this.form.validateFields((err, values) => {
          values.proVoide = this.videoFileList.length > 0 ? this.videoFileList[0].response : null
          values.proDowbloadZip = this.zipFileList.length > 0 ? this.zipFileList[0].response : null
          values.proPic = projectImage.length > 0 ? projectImage.join(",") : null
          if (!err) {
            values.author = this.selectedRow.id
            this.loading = true
            this.$post('/cos/project-info', {
              ...values
            }).then((r) => {
              this.reset()
              this.$emit('success')
            }).catch(() => {
              this.loading = false
            })

          }
        })
      },
      getMock() {
        const targetKeys = [];
        const mockData = [];
        for (let i = 0; i < 20; i++) {
          const data = {
            key: i.toString(),
            title: `content${i + 1}`,
            description: `description of content${i + 1}`,
            chosen: Math.random() * 2 > 1,
          };
          if (data.chosen) {
            targetKeys.push(data.key);
          }
          mockData.push(data);
        }
        this.mockData = mockData;
        this.targetKeys = targetKeys;
      },
      filterOption(inputValue, option) {
        return option.description.indexOf(inputValue) > -1;
      },
      handleChange(targetKeys, direction, moveKeys) {
        this.targetKeys = targetKeys;
      },
      handleChanges(targetKeys, direction, moveKeys) {
        this.roleKeys = targetKeys;
      },
      handleSearch(dir, value) {
        console.log('search:', dir, value);
      },
      onSelect(keys) {
        this.menuKeys = keys;
        console.log('Trigger Select', keys);
      },
      onCheck(checkedKeys, e) {
        console.log(JSON.stringify(checkedKeys));
      },
    },
    watch: {}
  }
</script>
