<template>
  <a-modal
    title="定制信息"
    v-model="show"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="1000"
    @cancel="handleCancelClick">
    <div style="font-size: 13px" v-if="applicationData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基本信息</span></a-col>
        <a-col :span="6"><b>项目名称：</b>{{ applicationData.proName }}</a-col>
        <a-col :span="6"><b>定制编号：</b>{{ applicationData.customizeCode !== null ? applicationData.customizeCode : '- -' }}</a-col>
        <a-col :span="6"><b>联系方式：</b>{{ applicationData.contact !== null ? applicationData.contact : '- -' }}</a-col>
        <a-col :span="6"><b>申请时间：</b>{{ applicationData.createDate !== null ? applicationData.createDate : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">技术要求</span></a-col>
        <a-col :span="24" v-html="">{{ applicationData.skills }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">功能模块</span></a-col>
        <a-col :span="24">{{ applicationData.module }}</a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="userData !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span></a-col>
        <a-col :span="6"><b>用户编号：</b>{{ userData.code }}</a-col>
        <a-col :span="6"><b>账户名称：</b>{{ userData.account !== null ? userData.account : '- -' }}</a-col>
        <a-col :span="6"><b>邮箱地址：</b>{{ userData.email !== null ? userData.email : '- -' }}</a-col>
        <a-col :span="6"><b>手机号码：</b>{{ userData.phone !== null ? userData.phone : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px" v-if="userData !== null">
        <a-col :span="6"><b>毕业院校：</b>{{ userData.schoolName !== null ? userData.schoolName : '- -' }}</a-col>
        <a-col :span="6"><b>所在城市：</b>{{ userData.cityName !== null ? userData.cityName : '- -' }}</a-col>
        <a-col :span="6"><b>注册时间：</b>{{ userData.createDate !== null ? userData.createDate : '- -' }}</a-col>
        <a-col :span="6"><b>邀请人：</b>{{ userData.inviteUserName !== null ? userData.inviteUserName : '- -' }}</a-col>
      </a-row>
      <br/>
      <br/>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'ApplicationInfo',
  props: {
    applicationShow: {
      type: Boolean,
      default: false
    },
    applicationData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.applicationShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      userData: null
    }
  },
  methods: {
    getUserInfo (userCode) {
      this.$get(`/cos/user-info/info/${userCode}`).then((r) => {
        this.userData = r.data
      })
    },
    handleCancelClick () {
      this.$emit('close')
    }
  },
  watch: {
    applicationShow: function (data) {
      if (data) {
        this.getUserInfo(this.applicationData.userCode)
      }
    }
  }
}
</script>

<style scoped>

</style>
