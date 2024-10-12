<template>
  <a-drawer
    title="修改项目"
    :maskClosable="false"
    width=850
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="userEditVisiable"
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
          v-model="proSort"
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
          <a-button :disabled="videoFileList.length >= 1"> <a-icon type="upload" /> Upload </a-button>
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
  import {mapState, mapMutations} from 'vuex'

  const formItemLayout = {
    labelCol: {span: 3},
    wrapperCol: {span: 18}
  }
  export default {
    name: 'UserEdit',
    props: {
      userEditVisiable: {
        default: false
      }
    },
    data() {
      return {
        formItemLayout,
        account: {},
        form: this.$form.createForm(this),
        deptTreeData: [],
        keychecks: [],
        roleData: [],
        menuKeys: [],
        userDept: [],
        mockData: [],
        targetKeys: [],
        appInfoData: [],
        roleInfoData: [],
        roleKeys: [],
        orgInfoData: [],
        menuInfoData: [],
        userId: '',
        loading: false,
        previewVisible: false,
        previewImage: '',
        videoFileList: [],
        fileList: [],
        zipFileList: [],
        plainOptions: ['Java项目', 'SSM框架', 'SpringBoot框架', '管理系统', 'VUE项目', '企业官网', '后台应用', '移动办公', '小程序配套', '热销产品'],
        proSort: [],
        childrenDrawer: false,
        players: [],
        selectedRowKeys: [],
        selectedRow: {}
      }
    },
    computed: {
      ...mapState({
        currentUser: state => state.account.user
      }),
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
    mounted() {
      this.getTeam('')
    },
    methods: {
      ...mapMutations({
        setUser: 'account/setUser'
      }),
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
      /**
       * 图片展示
       * @param proPic
       */
      proPicInit(proPic) {
        if(proPic !== null && proPic !== '') {
          let imageList = []
          proPic.split(',').forEach((image, index) => {
            imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9528/imagesWeb/'+image})
            // imageList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
          })
          this.fileList = imageList;
        }
      },
      /**
       * 视频文件展示
       * @param proVideo
       */
      proVideoInit(proVideo) {
        if(proVideo !== null && proVideo !== '') {
          this.videoFileList.push({uid: 1, name: proVideo, status: 'done', url: 'http://127.0.0.1:9528/videoWeb/'+proVideo})
          // this.videoFileList.push({uid: 1, name: proVideo, status: 'done', url: 'https://yxblshop.cn:9528/videoWeb/'+proVideo})
        }
      },
      /**
       * 项目文件
       * @param proZip
       */
      proZipInit(proZip) {
        if(proZip !== null && proZip !== '') {
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
      videoHandleChange(info) {
        let fileList = [...info.fileList];
        fileList = fileList.slice(-2);
        fileList = fileList.map(file => {
          if (file.response) {
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
      onClose() {
        this.loading = false
        this.form.resetFields()
        this.$emit('close')
      },
      setFormValues({...user}) {
        this.userId = user.id
        let fields = ['proName', 'proPrice','proTechnology','proDownloadUrl','proIntroduce','proSort','proType', 'authorName']
        Object.keys(user).forEach((key) => {
          if (key === 'author') {
            this.selectedRow.id = user["author"]
          }
          if(key === "proPic") {
            this.fileList = []
            this.proPicInit(user["proPic"])
          }
          if(key === "proVoide") {
            this.videoFileList = []
            this.proVideoInit(user["proVoide"])
          }
          if(key === "proDowbloadZip") {
            this.zipFileList = []
            this.proZipInit(user["proDowbloadZip"])
          }
          if(key === "proSort") {
            this.proSort = user["proSort"].split(',')
          }
          if(key === "proType") {
            user["proType"]=user["proType"].toString()
          }
          if (fields.indexOf(key) !== -1) {
            this.form.getFieldDecorator(key)
            let obj = {}
            obj[key] = user[key]
            this.form.setFieldsValue(obj)
          }
        })
      },
      onDeptChange(value) {
        this.userDept = value
      },
      handleSubmit() {
        // 获取图片List
        let projectImage = []
        this.fileList.forEach(image => {
          projectImage.push(image.response !== undefined ? image.response : image.name)
        })
        this.form.validateFields((err, values) => {
          if (!err) {
            this.loading = true
            let user = this.form.getFieldsValue()
            if(this.videoFileList.length > 0) {
              user.proVoide = this.videoFileList[0].response !== undefined ? this.videoFileList[0].response : this.videoFileList[0].name
            }
            if(this.zipFileList.length > 0) {
              user.proDowbloadZip = this.zipFileList[0].response !== undefined ? this.zipFileList[0].response : this.zipFileList[0].name
            }
            if(this.proSort.length > 0) {
              user.proSort = this.proSort.join(',')
            }
            user.author = this.selectedRow.id
            user.proPic = projectImage.length > 0 ? projectImage.join(",") : null
            user.id = this.userId;
            this.$put('/cos/project-info', {
              ...user
            }).then((r) => {
              this.loading = false
              this.$emit('success')
            }).catch(() => {
              this.loading = false
            })
          }
        })
      },
      filterOption(inputValue, option) {
        return option.description.indexOf(inputValue) > -1;
      },
      handleChange(targetKeys, direction, moveKeys) {
        console.log(targetKeys, direction, moveKeys);
        this.targetKeys = targetKeys;
      },
      handleChanges(targetKeys, direction, moveKeys) {
        this.roleKeys = targetKeys;
      },
      handleSearch(dir, value) {
        console.log('search:', dir, value);
      },
      onSelect(keys) {
        this.menuKeys = keys,
          console.log('Trigger Select', keys);
      },
      onCheck(checkedKeys, e) {
        console.log(JSON.stringify(checkedKeys));
      },
      getMessageByOrgId(orgId) {
        this.$get('/power/org-info/info/' + orgId).then((r) => {
          this.menuKeys = [];
          if (r.data.data != undefined) {
            this.targetKeys = r.data.data;
          }
        })
      },
      getMessageByAccount(accountId) {
        this.$get('/power/role-info/info/' + accountId).then((r) => {
          this.menuKeys = [];
          if (r.data.bindMenuInfo != undefined) {
            r.data.bindMenuInfo.forEach((menu) => {
              this.menuKeys.push(parseInt(menu))
            })
          }
          if (r.data.bindAppInfo != undefined) {
            this.targetKeys = r.data.bindAppInfo;
          }
        })
      }
    },
    watch: {
      userEditVisiable() {
        if (this.userEditVisiable) {
          this.$get('role').then((r) => {
            this.roleData = r.data.rows
          })
          this.$get('dept').then((r) => {
            this.deptTreeData = r.data.rows.children
          })
        }
      }
    }
  }
</script>
