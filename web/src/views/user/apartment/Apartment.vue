<template>
  <a-card :bordered="false" class="card-area" style="width: 1200px;margin: 15px auto 0;">
    <a-breadcrumb style="letter-spacing: 1px">
      <a-breadcrumb-item>
        <a @click="$router.push('/')">首页</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>申请邀请人</a-breadcrumb-item>
    </a-breadcrumb>
    <a-result :title="resultTitle" >
      <template #icon>
        <a-icon type="smile" theme="twoTone" />
      </template>
      <template #extra>
        <a-button v-if="!client.phone" type="primary" @click="$router.push('/personal')">
          验证手机号码
        </a-button>
        <a-button v-else type="primary" @click="partCheck">
          开通
        </a-button>
      </template>
    </a-result>
    <div style="padding: 13px;font-family: SimHei;">
      <a-row v-if="apartmentList.length !== 0">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">我的邀请人</span></a-col>
        <a-list :grid="{ gutter: 16, column: 4 }" item-layout="horizontal" :data-source="apartmentList">
          <a-list-item slot="renderItem" slot-scope="item, index">
            <a-list-item-meta :description="item.schoolName ? item.schoolName : '- -'">
              <a slot="title">{{ item.account }}</a>
              <a-avatar
                slot="avatar"
                :src="item.headImgUrl"/>
            </a-list-item-meta>
          </a-list-item>
        </a-list>
      </a-row>
      <br/>
      <a-row>
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">当前邀请人数</span></a-col>
      </a-row>
      <a-list :grid="{ gutter: 16, column: 4 }" item-layout="horizontal" :data-source="userList">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-list-item-meta :description="item.schoolName ? item.schoolName : '- -'">
            <a slot="title">{{ item.account }}</a>
            <a-avatar
              slot="avatar"
              :src="item.headImgUrl"/>
          </a-list-item-meta>
        </a-list-item>
      </a-list>
    </div>
  </a-card>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'Apartment',
  computed: {
    ...mapState({
      user: state => state.account.user,
      client: state => state.account.client
    })
  },
  data () {
    return {
      userList: [],
      apartmentList: [],
      resultTitle: '申请成为邀请人'
    }
  },
  mounted () {
    this.getInviterMeUserInfo(this.client.code)
    this.getInvitedUser(this.client.code)
  },
  methods: {
    getInviterMeUserInfo (userCode) {
      this.$get(`/cos/user-info/user/inviter/${userCode}`).then((r) => {
        if (r.data.data != null) {
          this.apartmentList = [r.data.data]
        }
      })
    },
    getInvitedUser (userCode) {
      this.$get(`/cos/user-info/inviter/list/${userCode}`).then((r) => {
        this.userList = r.data.data
      })
    },
    partCheck () {
      this.$post(`/cos/user-info/inviter/${this.client.code}`).then(() => {
        this.$message.success('申请成功！')
      })
    }
  }
}
</script>

<style scoped>

</style>
