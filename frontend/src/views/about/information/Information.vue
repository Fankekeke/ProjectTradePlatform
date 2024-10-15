<template>
  <div style="font-size: 13px">
    <a-row :gutter="20">
      <a-col :span="16">
        <a-spin :spinning="loading">
          <a-calendar>
            <ul slot="dateCellRender" slot-scope="value" class="events">
              <li v-for="item in getListData(value)" :key="item.content">
                <a-badge :status="item.type" :text="item.content" />
              </li>
            </ul>
            <template slot="monthCellRender" slot-scope="value">
              <div v-if="getMonthData(value)" class="notes-month">
                <section>{{ getMonthData(value) }}</section>
                <span>Backlog number</span>
              </div>
            </template>
          </a-calendar>
        </a-spin>
      </a-col>
      <a-col :span="8">
        <a-spin :spinning="loading">
          <a-card hoverable style="width: 65%;margin: 0 auto;margin-top: 13px;" v-if="user !== null">
            <img
              slot="cover"
              alt="example"
              src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
            />
            <template slot="actions" class="ant-card-actions">
              <a-popover placement="bottomRight" v-if="!mobile" trigger="click">
                <template slot="content">
                  <a-steps v-model="mobileData.mobileCurrent" type="navigation" size="small" :style="stepStyle">
                    <a-step status="process" title="获取验证码" />
                    <a-step status="wait" title="验证" disabled />
                  </a-steps>
                  <a-row :gutter="20" v-if="mobileData.mobileCurrent == 0">
                    <a-col :span="17">
                      <a-input placeholder="输入有效的手机号码" v-model="mobileData.number"/>
                    </a-col>
                    <a-col :span="7">
                      <a-button type="primary" @click="mobileNext">
                        获取
                      </a-button>
                    </a-col>
                  </a-row>
                  <a-row :gutter="20" v-if="mobileData.mobileCurrent == 1">
                    <a-col :span="17">
                      <a-input placeholder="输入有效的验证码"  v-model="mobileData.code"/>
                    </a-col>
                    <a-col :span="7">
                      <a-button type="primary" @click="mobileCheck">
                        验证
                      </a-button>
                    </a-col>
                  </a-row>
                </template>
                <template slot="title">
                  <span style="font-size: 12px">手机号码验证</span>
                </template>
                <a-icon key="mobile" type="mobile" style="color: red"/>
              </a-popover>
              <a-popover title="" v-else>
                <template slot="content">
                  <p style="font-size: 13px">{{user.mobile}}</p>
                </template>
                <a-icon key="mobile" type="mobile" @click="mobileAlert"/>
              </a-popover>
              <a-popover placement="bottomRight" v-if="!mail" trigger="click">
                <template slot="content">
                  <a-steps v-model="mailData.mailCurrent" type="navigation" size="small" :style="stepStyle">
                    <a-step status="process" title="获取验证码" />
                    <a-step status="wait" title="验证" disabled />
                  </a-steps>
                  <a-row :gutter="20" v-if="mailData.mailCurrent == 0">
                    <a-col :span="17">
                      <a-input placeholder="输入有效的邮箱地址" v-model="mailData.number"/>
                    </a-col>
                    <a-col :span="7">
                      <a-button type="primary" @click="mailNext">
                        获取
                      </a-button>
                    </a-col>
                  </a-row>
                  <a-row :gutter="20" v-if="mailData.mailCurrent == 1">
                    <a-col :span="17">
                      <a-input placeholder="输入有效的验证码"  v-model="mailData.code"/>
                    </a-col>
                    <a-col :span="7">
                      <a-button type="primary" @click="mailCheck">
                        验证
                      </a-button>
                    </a-col>
                  </a-row>
                </template>
                <template slot="title">
                  <span style="font-size: 12px">邮箱地址验证</span>
                </template>
                <a-icon key="mail" type="mail" style="color: red"/>
              </a-popover>
              <a-popover title="" v-else>
                <template slot="content">
                  <p style="font-size: 13px">{{user.email}}</p>
                </template>
                <a-icon key="mail" type="mail" @click="mailAlert"/>
              </a-popover>
              <a-popover placement="bottomRight" title="" trigger="click">
                <template slot="content">
                  <a-button type="primary" size="small" @click="infoView = true">我的信息</a-button>
                  <a-button type="primary" size="small" @click="orderView = true">我的订单</a-button>
                  <a-button type="primary" size="small" @click="annexView = true">我的附件</a-button>
                  <a-button type="primary" size="small" @click="projectView = true">我的项目</a-button>
                </template>
                <a-icon key="ellipsis" type="ellipsis"/>
              </a-popover>
            </template>
            <a-card-meta :title="user !== null ? user.username : '- -'" :description="user.roleName + ' | ' + (user.deptName !== null ? user.deptName : '未分配部门')">
              <a-avatar
                slot="avatar"
                :src="user !== null ? ('static/avatar/' + user.avatar) : 'static/avatar/default.jpg'"
              />
            </a-card-meta>
            <a-row style="margin-top: 30px" type="flex" justify="space-around" v-if="team !== null">
              <a-col :span="12">
                <a-statistic
                  title="余额（CHN）"
                  :value="team !== null ? team.balance : '0'"
                  :precision="2"
                  suffix="元"
                >
                </a-statistic>
              </a-col>
              <a-col :span="12">
                <a-statistic title="附件数量" suffix="件" :value="annex.length"></a-statistic>
              </a-col>
              <a-col :span="12" style="margin-top: 20px">
                <a-statistic title="项目交易量" suffix="单" :value="(order.length + thesisOrder.length)"></a-statistic>
              </a-col>
              <a-col :span="12" style="margin-top: 20px">
                <a-statistic title="项目数量" suffix="件" :value="project.length"></a-statistic>
              </a-col>
            </a-row>
            <div v-else>
              <a-result title="请联系管理员加入我们的团队！">
                <template #icon>
                  <a-icon type="smile" theme="twoTone" />
                </template>
                <template #extra>
                  <a-button type="primary">
                    联系
                  </a-button>
                </template>
              </a-result>
            </div>
          </a-card>
        </a-spin>
      </a-col>
    </a-row>
    <info-view
      :infoShow="infoView"
      :userData="user"
      :teamData="team"
      @close="handleInfoViewClose">
    </info-view>
    <info-order
      :orderShow="orderView"
      :orderData="order"
      :thesisOrderData="thesisOrder"
      @close="handleOrderViewClose">
    </info-order>
    <info-annex
      :annexShow="annexView"
      :annexData="annex"
      @close="handleAnnexViewClose">
    </info-annex>
    <info-project
      :projectShow="projectView"
      :projectData="project"
      @close="handleProjectViewClose">
    </info-project>
  </div>
</template>

<script>
import {mapState} from "vuex";
import InfoView from './InfoView'
import InfoOrder from './InfoOrder'
import InfoAnnex from './InfoAnnex'
import InfoProject from "./InfoProject";
import moment from "moment"

export default {
  name: 'Information',
  components: {InfoProject, InfoAnnex, InfoOrder, InfoView},
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
  },
  data () {
    return {
      loading: false,
      projectView: false,
      annexView: false,
      infoView: false,
      orderView: false,
      mail: false,
      mobile: false,
      team: null,
      user: null,
      project: [],
      order: [],
      annex: [],
      thesisOrder: [],
      customize: [],
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
      stepStyle: {
        marginBottom: '15px',
        boxShadow: '0px -1px 0 0 #e8e8e8 inset',
      },
    }
  },
  mounted() {
    this.home()
  },
  methods: {
    handleProjectViewClose () {
      this.projectView = false
    },
    handleAnnexViewClose () {
      this.annexView = false
    },
    handleOrderViewClose () {
      this.orderView = false
    },
    handleInfoViewClose () {
      this.infoView = false
    },
    mobileNext () {
      let reg = /^1[3-9]\d{9}$/
      if (!reg.test(this.mobileData.number)) {
        this.$message.error('手机号码不正确！')
      } else {
        this.$get('/cos/information/verify/mobile', {
          mobile: this.mobileData.number,
          userId: this.currentUser.userId
        }).then((r) => {
          if (r.data.code == 500) {
            this.$message.error('该手机号码已被注册！')
          } else {
            this.$message.success('请注意接收验证码！')
            this.mobileData.mobileCurrent += 1
          }
        })
      }
    },
    mobileCheck () {
      if (this.mobileData.code !== '') {
        this.$get('/cos/information/check/mobile', {
          mobile: this.mobileData.number,
          verifyCode: this.mobileData.code,
          userId: this.currentUser.userId
        }).then((r) => {
          if (r.data.code == 500) {
            this.$message.error(r.data.msg)
          } else {
            this.$message.success('验证成功！')
            this.mobileData.code = ''
            this.mobileData.number = ''
            this.mobileData.mailCurrent = 0
            // 更新用户状态
            this.home()
          }
        })
      } else {
        this.$message.error('验证码不能为空！')
      }
    },
    mailNext () {
      let reg = /^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/
      if (!reg.test(this.mailData.number)) {
        this.$message.error('邮箱地址不正确！')
      } else {
        this.$get('/cos/information/verify/mail', {
          mail: this.mailData.number,
          userId: this.currentUser.userId
        }).then((r) => {
          if (r.data.code == 500) {
            this.$message.error('该邮箱地址已被注册！')
          } else {
            this.$message.success('请注意接收验证码！')
            this.mailData.mailCurrent += 1
          }
        })
      }
    },
    mailCheck () {
      if (this.mailData.code !== '') {
        this.$get('/cos/information/check/mail', {
          mail: this.mailData.number,
          verifyCode: this.mailData.code,
          userId: this.currentUser.userId
        }).then((r) => {
          if (r.data.code == 500) {
            this.$message.error(r.data.msg)
          } else {
            this.$message.success('验证成功！')
            this.mailData.code = ''
            this.mailData.number = ''
            this.mailData.mailCurrent = 0
            // 更新用户状态
            this.home()
          }
        })
      } else {
        this.$message.error('验证码不能为空！')
      }
    },
    mobileAlert () {
      this.$notification.open({
        message: '手机号码验证提示✔',
        description:
          `您的手机号码[${this.user.mobile}]已经验证成功，如需更换请联系管理员😀`
      });
    },
    mailAlert () {
      this.$notification.open({
        message: '邮箱验证提示✔',
        description:
          `您的邮箱地址[${this.user.email}]已经验证成功，如需更换请联系管理员😀`
      });
    },
    home () {
      this.loading = true
      this.$get('/cos/information/home', {
        userId: this.currentUser.userId
      }).then((r) => {
        this.mail = r.data.mail
        this.mobile = r.data.mobile
        this.team = r.data.team
        this.user = r.data.user
        if (r.data.team != null) {
          this.project = r.data.project
          this.order = r.data.order
          this.thesisOrder = r.data.thesisOrder
          this.annex = r.data.annex
          this.customize = r.data.customize
        }
        setTimeout(() => {
          this.loading = false
        }, 200)
      })
    },
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr),
        beginDate = new Date(beginDateStr),
        endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    getListData (value) {
      let listData = []
      this.customize.forEach(item => {
        item.endDate = moment(item.startDate).add(item.cycle, 'days').format('YYYY-MM-DD')
        if (moment(value).format('YYYY-MM-DD') === item.startDate) {
          listData.push({type: 'success', content: '【定制开始】'+ item.proName +'.'})
        }
        if (moment(value).format('YYYY-MM-DD') === item.endDate) {
          listData.push({type: 'warning', content: '【定制结束】'+ item.proName +'.'})
        }
      })
      return listData || []
    },

    getMonthData(value) {
      if (value.month() === 8) {
        return 1394;
      }
    },
  }
}
</script>

<style scoped>
  .events {
    list-style: none;
    margin: 0;
    padding: 0;
  }
  .events .ant-badge-status {
    overflow: hidden;
    white-space: nowrap;
    width: 100%;
    text-overflow: ellipsis;
    font-size: 12px;
  }
  .notes-month {
    text-align: center;
    font-size: 28px;
  }
  .notes-month section {
    font-size: 28px;
  }
</style>
