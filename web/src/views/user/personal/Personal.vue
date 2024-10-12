<template>
  <a-card :bordered="false" class="card-area" style="width: 1200px;margin: 15px auto 0;">
    <a-breadcrumb style="letter-spacing: 1px">
      <a-breadcrumb-item>
        <a @click="$router.push('/')">首页</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>个人信息</a-breadcrumb-item>
    </a-breadcrumb>
    <a-row :gutter="30" style="margin-top: 30px">
      <a-col :span="8">
        <a-card :hoverable="false" :bordered="false">
          <a-card-meta :title="client.account">
            <a-avatar
              slot="avatar"
              :size="84"
              :src="client.headImgUrl"
            />
            <template slot="description">
              <p style="font-size: 13px">用户编号：{{ client.code }}</p>
              <p style="font-size: 13px">创建时间：{{ client.createDate }}</p>
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
    <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 30px">
      <a-skeleton active v-if="userDataLoading"/>
      <div v-if="!userDataLoading && userInfo != null">
        <a-col style="margin-bottom: 25px"><span style="font-size: 16px;font-weight: 600;color: #000c17;letter-spacing: 1px">基本信息</span></a-col>
        <a-form layout="vertical">
          <a-row :gutter="30">
            <a-col :span="8">
              <a-form-item label="用户编号" v-bind="formItemLayout">
                <a-input disabled placeholder="input placeholder" v-model="userInfo.code"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="账户名称" v-bind="formItemLayout">
                <a-input disabled placeholder="input placeholder"  v-model="userInfo.account"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="所属校院" v-bind="formItemLayout">
                <a-tooltip placement="topRight" title="填写此项可查看学校重复购买情况！">
                  <a-select
                    show-search
                    placeholder="input search text"
                    :value="userInfo.cityName + ' - ' + userInfo.schoolName"
                    :default-active-first-option="false"
                    :show-arrow="false"
                    :filter-option="false"
                    :not-found-content="null"
                    @search="handleSearch"
                    @change="handleChange">
                    <a-select-option v-for="item in schoolList" :key="item.id">
                      {{ item.area }} - {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-tooltip>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="30" style="margin-top: 10px">
            <a-col :span="8">
              <a-form-item label="性 别" v-bind="formItemLayout">
                <a-radio-group v-model="userInfo.sex" button-style="solid">
                  <a-radio-button value="0">
                    男
                  </a-radio-button>
                  <a-radio-button value="1">
                    女
                  </a-radio-button>
                  <a-radio-button value="2">
                    其它
                  </a-radio-button>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-form-item :v-bind="formItemLayout" style="float: right">
            <a-button type="primary" @click="updateUserInfo">
              更新
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-row>
    <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 30px">
      <a-skeleton active v-if="userDataLoading"/>
      <div v-if="!userDataLoading && userInfo != null">
        <a-col style="margin-bottom: 25px">
          <span style="font-size: 16px;font-weight: 600;color: #000c17;letter-spacing: 1px">账号绑定</span>
          <a-tooltip placement="topLeft" title="推送项目状态及收益提成！">
            <a-icon type="question" style="font-weight: 800;"/>
          </a-tooltip>
        </a-col>
        <a-col :span="24" style="letter-spacing: 1px">
          <a-divider />
          <a-row>
            <a-col :span="8">
              <a-icon v-if="mail" type="check-circle" style="color: green"/>
              <a-icon v-else type="exclamation-circle" style="color: orange;"/>
              绑定邮箱
            </a-col>
            <a-col :span="8" v-if="mail">{{ desensitizationEmail(userInfo.email) }}</a-col>
            <a-col :span="8" :offset="8" v-if="!mail">
              <a style="font-size: 12px" v-if="!mail" @click="bindCheck(0)"><a-icon key="mail" type="mail" style="font-size: 12px"/> 绑 定</a>
            </a-col>
          </a-row>
          <a-divider />
          <a-row>
            <a-col :span="8">
              <a-icon v-if="mobile" type="check-circle" style="color: green"/>
              <a-icon v-else type="exclamation-circle" style="color: orange;"/>
              绑定手机
            </a-col>
            <a-col :span="8" v-if="mobile">{{ desensitizationMobile(userInfo.phone) }}</a-col>
            <a-col :span="8" :offset="8" v-if="!mobile">
              <a style="font-size: 12px" v-if="!mobile" @click="bindCheck(1)"><a-icon key="mobile" type="mobile" style="font-size: 12px"/> 绑 定</a>
            </a-col>
          </a-row>
        </a-col>
      </div>
    </a-row>
    <a-modal v-model="visible" :title="checkFlag === 0 ? '邮箱验证' : '手机验证'" :width="450" :centered="true" :footer="null">
      <div v-if="checkFlag === 0">
        <a-steps v-model="mailData.mailCurrent" type="navigation" size="small" :style="stepStyle">
          <a-step status="process" title="获取验证码" />
          <a-step status="wait" title="验证" disabled />
        </a-steps>
        <a-row :gutter="20" v-if="mailData.mailCurrent === 0" style="margin-bottom: 30px;margin-top: 50px">
          <a-col :span="19">
            <a-input placeholder="输入有效的邮箱地址" v-model="mailData.number"/>
          </a-col>
          <a-col :span="5">
            <a-button type="primary" @click="mailNext">
              获取
            </a-button>
          </a-col>
        </a-row>
        <a-row :gutter="20" v-if="mailData.mailCurrent === 1" style="margin-bottom: 30px;margin-top: 50px">
          <a-col :span="19">
            <a-input placeholder="输入有效的验证码"  v-model="mailData.code"/>
          </a-col>
          <a-col :span="5">
            <a-button type="primary" @click="mailCheck">
              验证
            </a-button>
          </a-col>
        </a-row>
      </div>
      <div v-if="checkFlag === 1">
        <a-steps v-model="mobileData.mobileCurrent" type="navigation" size="small" :style="stepStyle">
          <a-step status="process" title="获取验证码" />
          <a-step status="wait" title="验证" disabled />
        </a-steps>
        <a-row :gutter="20" v-if="mobileData.mobileCurrent === 0" style="margin-bottom: 30px;margin-top: 50px">
          <a-col :span="19">
            <a-input placeholder="输入有效的手机号码" v-model="mobileData.number"/>
          </a-col>
          <a-col :span="5">
            <a-button type="primary" @click="mobileNext">
              获取
            </a-button>
          </a-col>
        </a-row>
        <a-row :gutter="20" v-if="mobileData.mobileCurrent === 1" style="margin-bottom: 30px;margin-top: 50px">
          <a-col :span="19">
            <a-input placeholder="输入有效的验证码"  v-model="mobileData.code"/>
          </a-col>
          <a-col :span="5">
            <a-button type="primary" @click="mobileCheck">
              验证
            </a-button>
          </a-col>
        </a-row>
      </div>
    </a-modal>
  </a-card>
</template>

<script>
import {mapState} from 'vuex'

const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'Personal',
  computed: {
    ...mapState({
      user: state => state.account.user,
      client: state => state.account.client
    })
  },
  data () {
    return {
      formItemLayout,
      checkFlag: -1,
      visible: false,
      mail: false,
      mobile: false,
      mobileData: {
        mobileCurrent: 0,
        number: '',
        code: ''
      },
      mailData: {
        mailCurrent: 0,
        number: '',
        code: ''
      },
      userInfo: null,
      schoolList: [],
      userDataLoading: false,
      stepStyle: {
        marginBottom: '35px',
        boxShadow: '0px -1px 0 0 #e8e8e8 inset'
      }
    }
  },
  mounted () {
    this.getUserInfoByCode(this.client.code)
  },
  methods: {
    // 邮箱脱敏
    desensitizationEmail (email) {
      if (email === '') {
        return '- -'
      }
      let str = email.split('@')
      return str[0].substr(0, str[0].length - 2) + ' ***** ' + str[1].substr(2)
    },
    // 手机号码脱敏
    desensitizationMobile (mobile) {
      if (mobile === '') {
        return '- -'
      }
      return mobile.substr(0, 3) + ' **** ' + mobile.substr(7)
    },
    // 更新用户基本信息
    updateUserInfo () {
      let data = {id: this.userInfo.id, sex: this.userInfo.sex, school: this.userInfo.schoolId, schoolName: this.userInfo.schoolName}
      this.$put('/cos/user-info/base', data).then(() => {
        this.getUserInfoByCode(this.client.code)
        this.$message.success('更新成功！')
      })
    },
    // 学校信息远程搜索
    handleSearch (value) {
      if (value !== null && value !== '') {
        this.$get(`/cos/sys-school/united/${value}`).then((r) => {
          this.schoolList = r.data.data
        })
      }
    },
    handleChange (value) {
      if (value !== null && value !== '') {
        let school = this.schoolList.find((e) => {
          return e.id === value
        })
        this.userInfo.cityName = school.area
        this.userInfo.schoolName = school.name
        this.userInfo.schoolId = school.id
      }
    },
    // 根据用户编号获取用户信息
    getUserInfoByCode (userCode) {
      this.userDataLoading = true
      this.$get(`/cos/user-info/info/${userCode}`).then((r) => {
        this.userInfo = r.data
        // 设置性别值为字符串
        if (this.userInfo.sex !== null) {
          this.userInfo.sex = this.userInfo.sex.toString()
        }
        // 判断邮箱是否验证
        this.mail = this.userInfo.email !== null && this.userInfo.email !== ''
        // 判断手机是否验证
        this.mobile = this.userInfo.phone !== null && this.userInfo.phone !== ''
        setTimeout(() => {
          this.userDataLoading = false
        }, 200)
      })
    },
    bindCheck (flag) {
      this.checkFlag = flag
      this.visible = true
    },
    // 手机号码发送验证码
    mobileNext () {
      let reg = /^1[3-9]\d{9}$/
      if (!reg.test(this.mobileData.number)) {
        this.$message.error('手机号码不正确！')
      } else {
        this.$get('/cos/user-info/verify/mobile', {
          mobile: this.mobileData.number,
          userCode: this.client.code
        }).then((r) => {
          if (r.data.code === 500) {
            this.$message.error('该手机号码已被注册！')
          } else {
            this.$message.success('请注意接收验证码！')
            this.mobileData.mobileCurrent += 1
          }
        })
      }
    },
    // 手机号码验证
    mobileCheck () {
      if (this.mobileData.code !== '') {
        this.$get('/cos/user-info/check/mobile', {
          mobile: this.mobileData.number,
          verifyCode: this.mobileData.code,
          userCode: this.client.code
        }).then((r) => {
          if (r.data.code === 500) {
            this.$message.error(r.data.msg)
          } else {
            this.$message.success('验证成功！')
            this.mobileData.code = ''
            this.mobileData.number = ''
            this.mobileData.mailCurrent = 0
            // 更新用户状态
            this.visible = false
            this.mobile = true
            this.getUserInfoByCode(this.client.code)
          }
        })
      } else {
        this.$message.error('验证码不能为空！')
      }
    },
    // 邮箱地址发送验证码
    mailNext () {
      let reg = /^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/
      if (!reg.test(this.mailData.number)) {
        this.$message.error('邮箱地址不正确！')
      } else {
        this.$get('/cos/user-info/verify/mail', {
          mail: this.mailData.number,
          userCode: this.client.code
        }).then((r) => {
          if (r.data.code === 500) {
            this.$message.error('该邮箱地址已被注册！')
          } else {
            this.$message.success('请注意接收验证码！')
            this.mailData.mailCurrent += 1
          }
        })
      }
    },
    // 邮箱地址验证
    mailCheck () {
      if (this.mailData.code !== '') {
        this.$get('/cos/user-info/check/mail', {
          mail: this.mailData.number,
          verifyCode: this.mailData.code,
          userCode: this.client.code
        }).then((r) => {
          if (r.data.code === 500) {
            this.$message.error(r.data.msg)
          } else {
            this.$message.success('验证成功！')
            this.mailData.code = ''
            this.mailData.number = ''
            this.mailData.mailCurrent = 0
            // 更新用户状态
            this.visible = false
            this.mail = true
            this.getUserInfoByCode(this.client.code)
          }
        })
      } else {
        this.$message.error('验证码不能为空！')
      }
    }
  }
}
</script>

<style scoped>

</style>
