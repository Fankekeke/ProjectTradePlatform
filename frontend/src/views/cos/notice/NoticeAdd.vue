<template>
  <a-drawer
    title="新增公告"
    :maskClosable="false"
    width="100%"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="userAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-row :gutter="15">
        <a-col :span="8">
          <a-form-item label='公告标题'>
            <a-input v-decorator="[
            'messTitle',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='发帖人'>
            <a-input v-decorator="[
            'messUser',
            { rules: [{ required: true, message: '请输入作者!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='公告类别'>
            <a-select v-decorator="[
              'messType'
              ]">
              <a-select-option value="0">突击事件</a-select-option>
              <a-select-option value="1">正常新闻</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='内容'>
            <editor-bar  v-model="editor.info" :isClear="isClear" @change="change" style="width: 100%"></editor-bar>
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
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import EditorBar from '../../common/EditorBar'
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
    components: {
      AFormItem,
      EditorBar
    },
    data () {
      return {
        editor: {
          info: ''
        },
        isClear: false,
        mapId: 'area',
        cardShow: false,
        local: '',
        localData: [],
        childrenDrawer: false,
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
        localPoint: {},
        stayAddress: ''
      }
    },
    mounted() {

    },
    methods: {
      change(val) {
        this.editor.info = val
      },
      reset () {
        this.loading = false
        this.form.resetFields()
      },
      showChildrenDrawer() {
        this.childrenDrawer = true;
      },
      onChildrenDrawerClose() {
        this.childrenDrawer = false
      },
      onClose () {
        this.reset()
        this.$emit('close')
      },
      handleSubmit () {
        this.form.validateFields((err, values) => {
          if (!err) {
            this.loading = true
            values.content = this.editor.info
            this.$post('/cos/notice-info', {
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
    watch: {
      userAddVisiable () {
        this.editor.info = "";
        if (this.userAddVisiable) {
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
