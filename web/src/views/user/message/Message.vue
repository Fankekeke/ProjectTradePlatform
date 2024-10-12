<template>
  <a-card :bordered="false" class="card-area" style="width: 1200px;margin: 15px auto 0;">
    <a-breadcrumb style="letter-spacing: 1px">
      <a-breadcrumb-item>
        <a @click="$router.push('/')">首页</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>我的消息</a-breadcrumb-item>
    </a-breadcrumb>
    <div style="padding: 30px" v-if="listData.length > 0">
      <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="listData">
        <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
          <template slot="actions">
            <span key="tag">
              <a-icon type="tag" style="margin-right: 8px" />
              {{ item.date }}
            </span>
          </template>
          <a-list-item-meta>
            <a slot="title" class="messageTitle">{{ item.title }}</a>
          </a-list-item-meta>
          <div class="messageContent">
            {{ item.content }}
          </div>
        </a-list-item>
      </a-list>
    </div>
    <div style="height: 500px" v-if="listData.length === 0">
      <a-empty :description="false" style="line-height: 500px"/>
    </div>
  </a-card>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'Message',
  computed: {
    ...mapState({
      user: state => state.account.user,
      client: state => state.account.client
    })
  },
  data () {
    return {
      pagination: {
        pageSize: 5
      },
      listData: []
    }
  },
  mounted () {
    this.getMessage(this.client.code)
  },
  methods: {
    getMessage (userCode) {
      this.$get(`/cos/foreign-notification/list/${userCode}`).then((r) => {
        this.listData = r.data.data
      })
    }
  }
}
</script>

<style scoped>
  .messageTitle {
    font-weight: 600;
    font-family: SimHei;
  }
  .messageContent {
    line-height: 1.8;
    font-family: SimHei;
  }
</style>
