<template>
  <a-drawer
    title="新增定制项目"
    :maskClosable="false"
    width=750
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="customAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="项目名称">
            <a-input v-decorator="['proName',{rules: [{ required: true, message: '请填写项目名称' }]}]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="办理用户">
            <a-select show-search :filter-option="filterOption" v-decorator="['userCode',{rules: [{ required: true, message: '请选择办理用户' }]}]">
              <a-select-option v-for="(item, index) in adminUserData" :key="index" :value="item.code">{{ item.account }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label="定制价格">
            <a-input-number
              style="width: 100%"
              :formatter="value => `￥ ${value}`"
              :parser="value => value.replace('￥', '')"
              v-decorator="['amount',{rules: [{ required: true, message: '请填写定制价格' }]}]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="项目周期（天）">
            <a-input-number
              style="width: 100%"
              :min="0"
              :max="100"
              :formatter="value => `${value}天`"
              :parser="value => value.replace('天', '')"
              v-decorator="['cycle',{rules: [{ required: true, message: '请填写项目周期' }]}]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="预计项目开始时间">
            <a-date-picker style="width: 100%" v-decorator="['startDate',{rules: [{ required: true, message: '预计项目开始时间' }]}]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="论 文">
            <a-radio-group @change="onSwitchChange" v-decorator="['thesis']">
              <a-radio-button value="0">
                不需要
              </a-radio-button>
              <a-radio-button value="1">
                需要
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="20">
        <a-col :span="24">
          <a-form-item label="技术要求">
            <a-textarea
              v-decorator="['skills',{rules: [{ required: true, message: '请填写技术要求' }]}]"
              placeholder=""
              :auto-size="{ minRows: 3, maxRows: 6 }"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="功能模块">
            <a-textarea
              v-decorator="['module',{rules: [{ required: true, message: '请填写功能模块' }]}]"
              placeholder=""
              :auto-size="{ minRows: 3, maxRows: 6 }"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备 注">
            <a-textarea
              v-decorator="['remarks']"
              placeholder=""
              :auto-size="{ minRows: 3, maxRows: 6 }"
            />
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
    <a-drawer
      title="论文定制"
      width="600"
      :closable="false"
      :visible="thesisDrawer"
      @close="thesisDrawer = false"
    >
      <a-form layout="vertical">
        <a-form-item label="论文名称">
          <a-input v-model="thesisData.thesisName"/>
        </a-form-item>
        <a-row type="flex" justify="space-between">
          <a-col :span="6">
            <a-form-item label="字数要求">
              <a-input-number v-model="thesisData.wordCount"/>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="查重率">
              <a-input-number
                v-model="thesisData.duplicate"
                :min="0"
                :max="100"
                :formatter="value => `${value}%`"
                :parser="value => value.replace('%', '')"
              />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="论文价格(元)">
              <a-input-number v-model="thesisData.price"/>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="项目周期(天)">
              <a-input-number v-model="thesisData.cycle"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="论文分配人（一人）">
          <a-select v-model="thesisData.technician" show-search :filter-option="filterOption" mode="multiple" option-label-prop="label">
            <a-select-option v-for="(item, index) in players" :key="index" :value="item.id" :label="item.players">
              <a-row>
                <a-col :span="4">
                  <a-avatar style="margin-right: 20px" shape="square" :size="40" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/' + item.avatar" />
                </a-col>
                <a-col :span="20">
                  <a-row>
                    <a-col><span style="font-weight: 600">{{item.players}}</span></a-col>
                    <a-col><span style="font-size: 10px">{{item.remarks !== null ? item.remarks : '- -'}}</span></a-col>
                  </a-row>
                </a-col>
              </a-row>
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <div class="drawer-bootom-button">
        <a-popconfirm title="确定放弃编辑？" @confirm="thesisClose" okText="确定" cancelText="取消">
          <a-button style="margin-right: .8rem">不需要</a-button>
        </a-popconfirm>
        <a-button @click="thesisSubmit" type="primary">提交</a-button>
      </div>
    </a-drawer>
  </a-drawer>
</template>
<script>
import moment from 'moment'
moment.locale('zh-cn')
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'CustomAdd',
  props: {
    customAddVisiable: {
      default: false
    }
  },
  data () {
    return {
      custom: {},
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      adminUserData: [],
      players: [],
      thesisDrawer: false,
      thesisData: {
        thesisName: '',
        wordCount: 8000,
        duplicate: 15,
        price: 0,
        cycle: 10,
        technician: []
      }
    }
  },
  mounted() {
    this.getTeam('')
    this.getUserList()
  },
  methods: {
    setFormValues ({...custom}) {
      this.custom = custom
      let fields = ['userCode', 'proName', 'skills', 'module', 'contact', 'thesis']
      let obj = {}
      Object.keys(custom).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = custom[key]
        }
        if (key === 'thesis') {
          obj[key] = custom[key].toString()
        }
      })
      // this.$get(`/user/id/${obj['adminUser']}`).then((r) => {
      //   this.adminUserData = [r.data.data]
      //   obj['adminUser'] = obj['adminUser']
      // })
      this.form.setFieldsValue(obj)
    },
    getTeam (account) {
      this.$get('/cos/team-info/teamList', {account}).then((r) => {
        this.players = r.data.data
      })
    },
    thesisSubmit() {
      if (this.thesisData.thesisName !== '' && this.thesisData.technician.length !== 0) {
        this.thesisDrawer = false
      } else {
        this.$message.error('请完整填写')
      }
    },
    thesisClose() {
      this.thesisDrawer = false
      this.form.setFieldsValue({'thesis': '0'})
    },
    onSwitchChange(val) {
      if (val.target.value === '1') {
        this.thesisDrawer = true
      }
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    },
    getUserList () {
      this.$get('/cos/user-info/userList').then((r) => {
        this.adminUserData = r.data.data
      })
    },
    handleCancel() {
      this.previewVisible = false;
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    thesisReset () {
      this.thesisData.thesisName = ''
      this.thesisData.wordCount = 8000
      this.thesisData.duplicate = 15
      this.thesisData.price = 0
      this.thesisData.cycle = 10
      this.thesisData.technician = []
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          values.code = this.custom.code
          values.contact = this.custom.contact
          if (values.startDate) {
            values.startDate = moment(values.startDate).format('YYYY-MM-DD')
          }
          // 添加论文信息
          if (values.thesis == 1) {
            values.thesisName = this.thesisData.thesisName
            values.wordCount = this.thesisData.wordCount
            values.duplicate = this.thesisData.duplicate
            values.price = this.thesisData.price
            values.thesisCycle = this.thesisData.cycle
            values.technician = this.thesisData.technician[0]
          }
          this.$post('/cos/custom-application/made', {
            ...values
          }).then((r) => {
            this.reset()
            this.thesisReset()
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
