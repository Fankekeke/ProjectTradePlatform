<template>
  <a-modal
    title="定制信息"
    v-model="show"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="1100"
    @cancel="handleCancelClick">
    <div style="font-size: 13px" v-if="customData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目流程</span></a-col>
        <a-col>
          <a-steps :current="customData.status" type="navigation" size="small" style="box-shadow: 0px -1px 0 0 #e8e8e8 inset">
            <a-step title="待审核" description="" />
            <a-step title="沟通中" description="" />
            <a-step title="已接单" description="" />
            <a-step title="已完成" description="" />
            <a-step title="部署完成" description="" />
            <a-step title="审核结果" description="" />
            <a-step title="佣金分配" description="" />
          </a-steps>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row>
        <a-col :span="8">
          <a-result :title="customData.startDate">
            <template #icon>
              开始时间
            </template>
          </a-result>
        </a-col>
        <a-col :span="8">
          <a-result v-if="customData.lessDay >= 0" :title="customData.lessDay + '天'">
            <template #icon>
            </template>
          </a-result>
          <a-result v-else :title="'已超时' + Math.abs(customData.lessDay) + '天'">
            <template #icon>
            </template>
          </a-result>
        </a-col>
        <a-col :span="8">
          <a-result :title="customData.endDate">
            <template #icon>
              结束时间
            </template>
          </a-result>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基本信息</span></a-col>
        <a-col :span="6"><b>项目名称：</b>{{ customData.proName }}</a-col>
        <a-col :span="6"><b>定制编号：</b>{{ customData.code !== null ? customData.code : '- -' }}</a-col>
        <a-col :span="6"><b>联系方式：</b>{{ customData.contact !== null ? customData.contact : '- -' }}</a-col>
        <a-col :span="6"><b>申请时间：</b>{{ customData.createDate !== null ? customData.createDate : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>定制价格：</b>{{ customData.amount !== null ? ('￥' + customData.amount) : '- -' }}</a-col>
        <a-col :span="6"><b>定 金：</b>{{ customData.deposit !== null ? ('￥' + customData.deposit) : '- -' }}</a-col>
        <a-col :span="6"><b>开始时间：</b>{{ customData.startDate !== null ? customData.startDate : '- -' }}</a-col>
        <a-col :span="6"><b>项目周期：</b>{{ customData.cycle !== null ? (customData.cycle + '天') : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">支付流程</span></a-col>
        <a-col :span="6"><b>定金状态：</b>
          <span v-if="customData.frontStatus == 1" style="color: green">已支付</span>
          <span v-else style="color: red">未支付</span>
        </a-col>
        <a-col :span="6"><b>支付方式：</b>{{ customData.frontType !== null ? customData.frontType : '- -' }}</a-col>
        <a-col :span="6"><b>定金金额：</b>{{ customData.frontMoney !== null ? ('￥' + customData.frontMoney) : '- -' }}</a-col>
        <a-col :span="6"><b>支付时间：</b>{{ customData.frontDate !== null ? customData.frontDate : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>尾款状态：</b>
          <span v-if="customData.finalStatus == 1" style="color: green">已支付</span>
          <span v-else style="color: red">未支付</span>
        </a-col>
        <a-col :span="6"><b>支付方式：</b>{{ customData.finalType !== null ? customData.finalType : '- -' }}</a-col>
        <a-col :span="6"><b>尾款金额：</b>{{ customData.finalMoney !== null ? ('￥' + customData.finalMoney) : '- -' }}</a-col>
        <a-col :span="6"><b>支付时间：</b>{{ customData.finalDate !== null ? customData.finalDate : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">技术要求</span></a-col>
        <a-col :span="24" v-html="">{{ customData.skills }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">功能模块</span></a-col>
        <a-col :span="24">{{ customData.module }}</a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="(customData.technician !== null && customData !== '') || developerFlag">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">开发者</span></a-col>
        <a-col :span="12">
          <a-select style="width: 100%" v-model="developer" @change="developerChange" mode="multiple" option-label-prop="label">
            <a-select-option v-for="(item, index) in developerSpare" :key="index" :value="item.id" :label="item.players">
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
        </a-col>
        <a-col :span="6" :offset="1">
          <a-button type="primary" :disabled="developer.length === 0" @click="developerBind">
            更新
          </a-button>
        </a-col>
        <a-col :span="24" style="margin-top: 25px">
          <a-row>
            <a-col :span="8" v-for="(item, index) in developerInfo" :key="index">
              <div style="padding-right: 15px;padding-bottom: 10px">
                <a-card hoverable>
<!--                  <template slot="actions" class="ant-card-actions">-->
<!--                    <a-icon key="check" type="check" style="color: green" v-if="item.status == 0"/>-->
<!--                    <a-icon key="close" type="close" style="color: red" v-if="item.status == 0"/>-->
<!--                    <a-icon key="minus-square" type="minus-square" style="color: red" v-if="item.status == 1"/>-->
<!--                    <a-icon key="plus-square" type="plus-square" v-if="item.status == 1 && index == developer.length - 1"/>-->
<!--                  </template>-->
                  <a-card-meta :title="item.players" :description="item.remarks !== null ? '[' + item.deptName + ']  ' + item.remarks : '[' + item.deptName + ']'">
                    <a-avatar
                      slot="avatar"
                      :src="'http://127.0.0.1:9528/imagesWeb/' + item.avatar"
                    />
                  </a-card-meta>
                </a-card>
              </div>
            </a-col>
          </a-row>
        </a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-else>
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目分配</span></a-col>
        <a-result title="未对此定制项目分配开发者！">
          <template #icon>
            <a-icon type="frown" theme="twoTone" />
          </template>
          <template #extra>
            <a-button type="primary" @click="developerAdd">
              添 加
            </a-button>
          </template>
        </a-result>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px" v-if="customData.thesisCode !== null">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="customData.thesisCode !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">定制论文</span></a-col>
        <a-col :span="6"><b>论文名称：</b>{{ customData.thesisName }}</a-col>
        <a-col :span="6"><b>字数要求：</b>{{ customData.wordCount }}字</a-col>
        <a-col :span="6"><b>查重率：</b>{{ customData.duplicate }}%</a-col>
        <a-col :span="6"><b>论文价格：</b>￥{{ customData.price }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="customData.thesisCode !== null">
        <a-col :span="6"><b>论文周期：</b>{{ customData.thesisCycle }}天</a-col>
        <a-col :span="6"><b>论文分配人：</b>
          <a-popover>
            <template slot="content">
              <a-avatar shape="square" :size="120" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/'+customData.avatar" />
            </template>
            <a>{{customData.players}}</a>
          </a-popover>
        </a-col>
        <a-col :span="6">
          <a-popconfirm
            title="是否要删除论文？"
            ok-text="是"
            cancel-text="否"
            @confirm="revokeThesis">
            <a-button type="danger" size="small">删除</a-button>
          </a-popconfirm>
        </a-col>
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
import moment from 'moment'

export default {
  name: 'CustomInfo',
  props: {
    customShow: {
      type: Boolean,
      default: false
    },
    customData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.customShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      userData: null,
      developer: [],
      developerInfo: [],
      developerSpare: [],
      developerFlag: false
    }
  },
  mounted() {
    this.getTeamList()
  },
  methods: {
    revokeThesis () {
      this.$post('/cos/customize-info/revokeThesis', {
        customCode: this.customData.code
      }).then((r) => {
        this.customData.thesisCode = null
        this.$message.success('删除论文成功')
      })
    },
    developerBind () {
      this.$put('/cos/customize-info/developer/bind', {
        ids: this.developer.join(','), customCode: this.customData.code
      }).then((r) => {
        this.$message.success('分配开发者成功')
      })
    },
    developerChange (value) {
      this.developerInfo = []
      this.developer.forEach(tb1 => {
        this.developerSpare.forEach(tmp => {
          if (tmp.id == tb1) {
            this.developerInfo.push(tmp)
          }
        })
      })
    },
    developerAdd () {
      this.developerFlag = true
    },
    getUserInfo (userCode) {
      this.$get(`/cos/user-info/info/${userCode}`).then((r) => {
        this.userData = r.data
      })
    },
    getTeamList () {
      this.$get('/cos/team-info/teamList').then((r) => {
        this.developerSpare = r.data.data
      })
    },
    handleCancelClick () {
      this.developerFlag = false
      this.developer = []
      this.developerInfo = []
      this.$emit('close')
    }
  },
  watch: {
    customShow: function (data) {
      if (data) {
        this.customData.endDate = moment(this.customData.startDate).add(this.customData.cycle, 'days').format('YYYY-MM-DD')
        if (this.customData.technician !== null) {
          this.developer = this.customData.technician.split(',').map(Number)
          this.developerChange(this.developer)
        }
        let m1 = moment();
        let m2 = moment(this.customData.endDate);
        this.customData.lessDay = m2.diff(m1, 'day')
        this.getUserInfo(this.customData.userCode)
      }
    }
  }
}
</script>

<style scoped>

</style>
