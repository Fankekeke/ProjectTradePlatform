<template>
  <a-popover placement="bottomRight" :overlayStyle="{marginTop: '20px'}" trigger="click">
    <template slot="content" style="margin-top: 50px">
      <div style="width: 350px;">
        <a-tabs default-active-key="1" :tabBarStyle="{textAlign: 'center',}">
          <a-tab-pane key="1" :tab="'通知(' + t.length + ')'" :style="{ height: '280px', overflowY: 'auto' }">
            <a-empty :description="false" v-if="t.length === 0"/>
            <a-list item-layout="horizontal" :data-source="t">
              <a-list-item slot="renderItem" slot-scope="item, index">
                <a-list-item-meta :description="item.content">
                  <a slot="title">{{ item.title }}</a>
                </a-list-item-meta>
              </a-list-item>
            </a-list>
          </a-tab-pane>
          <a-tab-pane key="2" :tab="'公告(' + g.length + ')'" :style="{ height: '280px', overflowY: 'auto' }">
            <a-empty :description="false" v-if="g.length === 0"/>
            <a-list item-layout="horizontal" :data-source="g">
              <a-list-item slot="renderItem" slot-scope="item, index">
                <a-list-item-meta :description="item.content">
                  <a slot="title">{{ item.title }}</a>
                </a-list-item-meta>
              </a-list-item>
            </a-list>
          </a-tab-pane>
          <a-tab-pane key="3" :tab="'任务(' + r.length + ')'" :style="{ height: '280px', overflowY: 'auto' }">
            <a-empty :description="false" v-if="r.length === 0"/>
            <a-list item-layout="horizontal" :data-source="r">
              <a-list-item slot="renderItem" slot-scope="item, index">
                <a-list-item-meta :description="item.content">
                  <a slot="title">{{ item.title }}</a>
                </a-list-item-meta>
              </a-list-item>
            </a-list>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <a-badge :count="count">
      <a-icon type="inbox" style="font-size: 22px"/>
    </a-badge>
  </a-popover>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: 'Notification',
  computed: {
    ...mapState({
      user: state => state.account.user
    })
  },
  data() {
    return {
      count: 0,
      t: [],
      g: [],
      r: []
    }
  },
  mounted() {
    this.notificationByType()
  },
  methods: {
    notificationByType() {
      this.$get('/cos/notification/notificationByType', {userId: this.user.userId}).then((r) => {
        this.count = r.data.count
        this.t = r.data.t
        this.g = r.data.g
        this.r = r.data.r
      })
    }
  }
}
</script>

<style scoped>
  /deep/ .ant-list-item-meta-description {
    font-size: 12px;
  }
</style>
