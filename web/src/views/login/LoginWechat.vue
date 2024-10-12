<template>
  <a-card :bordered="false" hoverable style="margin-top: 130px;background-color: #f9f9f9">
    <div style="text-align: left;font-size: 14px;margin-bottom: 30px"><b>'猿'形毕露</b> 微信扫一扫登录</div>
    <div class="login">
      <div class="main_wx">
        <wxlogin
          v-if="appid && redirect_uri"
          :appid="appid"
          scope="snsapi_login"
          :self_redirect="'true'"
          :redirect_uri="redirect_uri"
          :href="href"
          :state="state"
        ></wxlogin>
      </div>
    </div>
  </a-card>
</template>

<script>
import {mapMutations} from 'vuex'
import wxlogin from 'vue-wxlogin'

export default {
  name: 'Login',
  components: { wxlogin },
  data () {
    return {
      loading: false,
      error: '',
      appid: 'wx033d0145ace6a85b',
      redirect_uri: 'http://iuwsfs.natappfree.cc/wechat/callback',
      state: new Date().getTime() + Math.random().toString(36).substr(2, 15),
      href: 'data:text/css;base64,LmltcG93ZXJCb3ggLnFyY29kZSB7CiBib3JkZXI6IG5vbmU7Cn0='
    }
  },
  computed: {
    systemName () {
      return this.$store.state.setting.systemName
    },
    copyright () {
      return this.$store.state.setting.copyright
    }
  },
  created () {
    this.$db.clear()
    this.$router.options.routes = []
    this.socketInit()
  },
  methods: {
    socketInit () {
      if (typeof (WebSocket) === 'undefined') {
        this.$message.error('您的浏览器不支持WebSocket！')
        return false
      }
      let wsname = 'ws://localhost:9528/socketServer/' + this.state
      this.ws = new WebSocket(wsname)
      // 连接成功触发
      this.ws.onopen = (evt) => {
      }
      // 这个是接收后台发送信息的方法
      this.ws.onmessage = (evt) => {
        var data = JSON.parse(evt.data)
        this.saveLoginData(data.token.data)
        this.setClient(data.userData)
        setTimeout(() => {
          this.loading = false
        }, 500)
        this.$router.push('/')
        // 在这里判断后台给的用户信息和token  并进行相应的操作
      }
    },
    doLogin () {
      if (this.activeKey === '1') {
        // 用户名密码登录
        this.form.validateFields(['name', 'password'], (errors, values) => {
          if (!errors) {
            this.loading = true
            let name = this.form.getFieldValue('name')
            let password = this.form.getFieldValue('password')
            this.$post('login', {
              username: name,
              password: password
            }).then((r) => {
              let data = r.data.data
              this.saveLoginData(data)
              setTimeout(() => {
                this.loading = false
              }, 500)
              this.$router.push('/')
            }).catch((e) => {
              console.error(e)
              setTimeout(() => {
                this.loading = false
              }, 500)
            })
          }
        })
      }
      if (this.activeKey === '2') {
        // 手机验证码登录
        this.$message.warning('暂未开发')
      }
    },
    regist () {
      this.$emit('regist', 'Regist')
    },
    getCaptcha () {
      this.$message.warning('暂未开发')
    },
    handleTabsChange (val) {
      this.activeKey = val
    },
    ...mapMutations({
      setToken: 'account/setToken',
      setExpireTime: 'account/setExpireTime',
      setPermissions: 'account/setPermissions',
      setRoles: 'account/setRoles',
      setUser: 'account/setUser',
      setClient: 'account/setClient',
      setTheme: 'setting/setTheme',
      setLayout: 'setting/setLayout',
      setMultipage: 'setting/setMultipage',
      fixSiderbar: 'setting/fixSiderbar',
      fixHeader: 'setting/fixHeader',
      setColor: 'setting/setColor'
    }),
    saveLoginData (data) {
      this.setToken(data.token)
      this.setExpireTime(data.exipreTime)
      this.setUser(data.user)
      this.setPermissions(data.permissions)
      this.setRoles(data.roles)
      this.setTheme(data.config.theme)
      this.setLayout(data.config.layout)
      this.setMultipage(data.config.multiPage === '1')
      this.fixSiderbar(data.config.fixSiderbar === '1')
      this.fixHeader(data.config.fixHeader === '1')
      this.setColor(data.config.color)
    }
  }
}
</script>

<style scoped>
  /deep/ .impowerBox .qrcode {
  border: none !important;
}
</style>

<style lang="less" scoped>
  .login {
    .icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }
  }
</style>
