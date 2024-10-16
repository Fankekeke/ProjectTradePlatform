<template>
  <a-drawer
    title="修改公告"
    :maskClosable="false"
    width="100%"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="userEditVisiable"
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
  import {mapState, mapMutations} from 'vuex'
  import EditorBar from '../../common/EditorBar'
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
        editor: {
          info: ''
        },
        isClear: false,
        mapId: 'area',
        cardShow: false,
        localPoint: {},
        stayAddress: '',
        local: '',
        localData: [],
        formItemLayout,
        childrenDrawer: false,
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
        loading: false
      }
    },
    computed: {
      ...mapState({
        currentUser: state => state.account.user
      })
    },
    created() {

    },
    components: {
      EditorBar
    },
    methods: {
      ...mapMutations({
        setUser: 'account/setUser'
      }),
      change(val) {
        this.editor.info = val
      },
      onSearch() {
        let localData = new Array();
        var options = {
          onSearchComplete: (results) =>{
            // 判断状态是否正确
            if (local.getStatus() == BMAP_STATUS_SUCCESS){
              for (var i = 0; i < results.getCurrentNumPois(); i ++){
                if(i==0) {
                  setTimeout(() => {
                    baiduMap.findPoint(results.getPoi(0).point,15);
                  }, 10);
                }
                localData.push(results.getPoi(i));
                if(results.getPoi(i).point != undefined) {
                  baiduMap.localPointAdd(results.getPoi(i));
                }
              }
              this.localData = localData;
              this.cardShow = true;
            }
          }
        };
        var local = new BMap.LocalSearch(baiduMap.rMap(), options);
        local.search(this.local);
      },
      onClose() {
        this.loading = false
        this.form.resetFields()
        this.$emit('close')
      },
      showChildrenDrawer() {
        this.childrenDrawer = true;
      },
      onChildrenDrawerClose() {
        this.childrenDrawer = false
      },
      setFormValues({...user}) {
        this.userId = user.id
        this.editor.info = user.content
        let fields = ['messTitle', 'messUser', 'messType']
        Object.keys(user).forEach((key) => {
          if(key === "messType" && user["messType"] !== null) {
            user["messType"]=user["messType"].toString()
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
        this.form.validateFields((err, values) => {
          if (!err) {
            this.loading = true
            let user = this.form.getFieldsValue()
            user.content = this.editor.info
            user.id = this.userId;
            this.$put('/cos/notice-info', {
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
