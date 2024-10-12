<template>
  <a-modal v-model="show" title="我的信息" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="userData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>用户名称：</b>{{ userData.username }}</a-col>
        <a-col :span="8"><b>所属部门：</b>{{ userData.deptName !== null ? userData.deptName : '- -' }}</a-col>
        <a-col :span="8"><b>账户状态：</b>
          <span v-if="userData.status == 0">禁用</span>
          <span v-if="userData.status == 1">正常</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>邮箱地址：</b>{{ userData.email !== null ? userData.email : '- -' }}</a-col>
        <a-col :span="8"><b>手机号码：</b>{{ userData.mobile !== null ? userData.mobile : '- -' }}</a-col>
        <a-col :span="8"><b>创建时间：</b>{{ userData.createTime !== null ? userData.createTime : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>性 别：</b>
          <span v-if="userData.ssex == 0">男</span>
          <span v-if="userData.ssex == 1">女</span>
          <span v-if="userData.ssex == 2">保密</span>
        </a-col>
        <a-col :span="8"><b>角 色：</b>{{ userData.roleName !== null ? userData.roleName : '- -' }}</a-col>
        <a-col :span="8"><b>上次登陆时间：</b>{{ userData.lastLoginTime !== null ? userData.lastLoginTime : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="teamData !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">团队信息</span></a-col>
        <a-col :span="8"><b>名 称：</b>
          <a-popover>
            <template slot="content">
              <a-avatar shape="square" :size="120" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/'+teamData.avatar" />
            </template>
            <a>{{teamData.players}}</a>
          </a-popover>
        </a-col>
        <a-col :span="8"><b>余 额：</b>{{ teamData.balance !== null ? ('￥ ' + teamData.balance) : '￥ 0' }}</a-col>
        <a-col :span="8"><b>状 态：</b>
          <a-tag color="green" v-if="teamData.status">正常</a-tag>
          <a-tag color="orange" v-else>忙碌</a-tag>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="teamData !== null">
        <a-col :span="8"><b>工作时间：</b>{{ teamData.workTime !== null ? teamData.workTime : '- -' }}</a-col>
        <a-col :span="8"><b>部署时间：</b>{{ teamData.deployTime !== null ? teamData.deployTime : '- -' }}</a-col>
        <a-col :span="8"><b>备 注：</b>{{ teamData.remarks !== null ? teamData.remarks : '- -' }}</a-col>
      </a-row>
      <br/>
    </div>
  </a-modal>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: 'InfoView',
  props: {
    infoShow: {
      type: Boolean,
      default: false
    },
    userData: {
      type: Object
    },
    teamData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.infoShow
      },
      set: function () {
      }
    }
  },
  methods: {
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
