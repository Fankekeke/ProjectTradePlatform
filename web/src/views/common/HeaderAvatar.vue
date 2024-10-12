<template>
  <div>
    <div style="display: inline-block; height: 100%; vertical-align: initial">
      <span style="cursor: pointer">
        <a-popover placement="bottom">
          <template slot="content">
            <div style="font-size: 12px">
              <a-menu slot="overlay">
                <a-menu-item key="0" style="font-size: 13px">
                  <a @click="openPersonal"><a-icon type="user" />个人信息</a>
                </a-menu-item>
                <a-menu-item key="1" style="font-size: 13px">
                  <a @click="openMessage"><a-icon type="message" />我的消息</a>
                </a-menu-item>
                <a-menu-item key="2" style="font-size: 13px">
                  <a @click="openOrder"><a-icon type="desktop" />我的订单</a>
                </a-menu-item>
                <a-menu-item key="3" style="font-size: 13px">
                  <a @click="openApartment"><a-icon type="apartment" />申请邀请人</a>
                </a-menu-item>
                <a-menu-item key="4" style="font-size: 13px">
                  <a @click="openBalance"><a-icon type="wallet" />余额统计</a>
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="5" style="font-size: 13px">
                  <a @click="logout"><a-icon type="export" style="color: red"/>退出登陆</a>
                </a-menu-item>
              </a-menu>
            </div>
          </template>
          <a-badge dot>
            <a-avatar class="avatar" shape="square" :src="client.headImgUrl" icon="user"/>
          </a-badge>
        </a-popover>
      </span>
    </div>
<!--    <a-dropdown style="display: inline-block; height: 100%; vertical-align: initial">-->
<!--      <span style="cursor: pointer">-->
<!--        <a-avatar class="avatar" size="small" shape="square" :src="client.headImgUrl"/>-->
<!--        <span class="curr-user">{{client.account}}</span>-->
<!--      </span>-->
<!--      <a-menu style="width: 150px" slot="overlay">-->
<!--        <a-menu-item @click="openProfile">-->
<!--          <a-icon type="user"/>-->
<!--          <span>个人中心</span>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item @click="updatePassword">-->
<!--          <a-icon type="key"/>-->
<!--          <span>密码修改</span>-->
<!--        </a-menu-item>-->
<!--        <a-menu-divider></a-menu-divider>-->
<!--        <a-menu-item @click="handleSettingClick">-->
<!--          <a-icon type="setting"/>-->
<!--          <span>系统定制</span>-->
<!--        </a-menu-item>-->
<!--        <a-menu-divider></a-menu-divider>-->
<!--        <a-menu-item @click="logout">-->
<!--          <a-icon type="logout"/>-->
<!--          <span>退出登录</span>-->
<!--        </a-menu-item>-->
<!--      </a-menu>-->
<!--    </a-dropdown>-->
    <update-password
      @success="handleUpdate"
      @cancel="handleCancelUpdate"
      :user="user"
      :updatePasswordModelVisible="updatePasswordModelVisible">
    </update-password>
  </div>
</template>

<script>
import { mapMutations, mapState } from 'vuex'
import UpdatePassword from '../personal/UpdatePassword'

export default {
  name: 'HeaderAvatar',
  components: {UpdatePassword},
  data () {
    return {
      updatePasswordModelVisible: false
    }
  },
  computed: {
    ...mapState({
      settingBar: state => state.setting.settingBar.opened,
      user: state => state.account.user,
      client: state => state.account.client
    }),
    avatar () {
      return `static/avatar/${this.user.avatar}`
    }
  },
  methods: {
    handleSettingClick () {
      this.setSettingBar(!this.settingBar)
    },
    openPersonal () {
      this.$router.push('/personal')
    },
    openMessage () {
      this.$router.push('/message')
    },
    openOrder () {
      this.$router.push('/order')
    },
    openApartment () {
      this.$router.push('/apartment')
    },
    openBalance () {
      this.$router.push('/balance')
    },
    openProfile () {
      this.$router.push('/profile')
    },
    updatePassword () {
      this.updatePasswordModelVisible = true
    },
    handleCancelUpdate () {
      this.updatePasswordModelVisible = false
    },
    handleUpdate () {
      this.updatePasswordModelVisible = false
      this.$message.success('更新密码成功，请重新登录系统')
      setTimeout(() => {
        this.logout()
      }, 1500)
    },
    logout () {
      this.$get(`logout/${this.user.id}`).then(() => {
        return new Promise((resolve, reject) => {
          this.$db.clear()
          location.reload()
        })
      }).catch(() => {
        this.$message.error('退出系统失败')
      })
    },
    ...mapMutations({setSettingBar: 'setting/setSettingBar'})
  }
}
</script>

<style lang="less" scoped>
  .ant-avatar-sm {
    width: 30px;
    height: 30px;
  }
  .avatar {
    color: #1890ff;
    background: hsla(0, 0%, 100%, .85);
    vertical-align: middle;
  }
  .curr-user {
    font-weight: 550;
    margin-left: 6px
  }
</style>
