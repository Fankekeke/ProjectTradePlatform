<template>
  <a-card :bordered="false" class="card-area" style="width: 1200px;margin: 15px auto 0;">
    <a-breadcrumb style="letter-spacing: 1px">
      <a-breadcrumb-item>
        <a @click="$router.push('/')">首页</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>余额统计</a-breadcrumb-item>
    </a-breadcrumb>
    <div style="padding: 45px;font-family: SimHei;margin-top: 30px">
      <a-row :gutter="20">
        <a-col :span="3">
          <a-avatar shape="square" :size="64" icon="user" :src="client.headImgUrl"/>
        </a-col>
        <a-col :span="8">
          <a-statistic title="Account Balance (CNY)" :precision="2" :value="client.balance"/>
        </a-col>
      </a-row>
    </div>
    <div style="padding: 23px;font-family: SimHei;margin-top: 20px">
      <a-list :grid="{ gutter: 16, column: 4 }" item-layout="horizontal" :data-source="balanceList" :pagination="pagination">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-list-item-meta :description="item.createDate ? item.createDate : '- -'">
            <a slot="title">{{ item.orderName }} +{{ item.partWealth }}元</a>
            <a-avatar
              slot="avatar"
              :src="item.avatar"/>
          </a-list-item-meta>
        </a-list-item>
      </a-list>
    </div>
  </a-card>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'Balance',
  computed: {
    ...mapState({
      user: state => state.account.user,
      client: state => state.account.client
    })
  },
  data () {
    return {
      balanceList: [],
      pagination: {
        pageSize: 8
      }
    }
  },
  mounted () {
    this.selectWealthPartByInvite('AK-1621770978181')
  },
  methods: {
    selectWealthPartByInvite (userCode) {
      this.$get(`/cos/wealth-assign/wealth/invite/${userCode}`).then((r) => {
        this.balanceList = r.data.data
      })
    }
  }
}
</script>

<style scoped>

</style>
