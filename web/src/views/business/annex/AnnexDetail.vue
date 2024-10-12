<template>
  <div v-if="annexInfo != null">
    <a-page-header
      :title="annexInfo.annexName"
      :sub-title="annexInfo.code"
      @back="back"
    />
    <div style="padding: 23px;font-family: SimHei;">
      <a-carousel effect="fade" autoplay style="margin-bottom: 50px">
        <div v-for="(item, index) in annexInfo.pic.split(',')" :key="index">
          <img :src="`${fileHost}` + 'imagesWeb/' + item" style="width: 100%;height: 100%"/>
        </div>
      </a-carousel>
      <a-descriptions bordered>
        <a-descriptions-item label="附件名称">
          {{ annexInfo.annexName }}
        </a-descriptions-item>
        <a-descriptions-item label="附件类型">
          <span v-if="annexInfo.proType == 1">论文</span>
          <span v-if="annexInfo.proType == 2">PPT</span>
          <span v-if="annexInfo.proType == 3">任务书</span>
          <span v-if="annexInfo.proType == 4">开题报告</span>
        </a-descriptions-item>
        <a-descriptions-item label="价格">
          <span style="color: #f5222d">{{ annexInfo.annexPrice }}</span> 元
        </a-descriptions-item>
        <a-descriptions-item label="项目作者">
          {{ annexInfo.authorName }}
        </a-descriptions-item>
        <a-descriptions-item label="发布时间" :span="2">
          {{ annexInfo.uploadDate }}
        </a-descriptions-item>
        <a-descriptions-item label="上架状态" :span="3">
          <a-badge status="processing" text="上架中" />
        </a-descriptions-item>
        <a-descriptions-item v-if="annexInfo.annexType == 1" label="论文字数" :span="3">
          {{ annexInfo.wordCount }}
        </a-descriptions-item>
        <a-descriptions-item label="附件备注">
          {{ annexInfo.remark }}
        </a-descriptions-item>
      </a-descriptions>
    </div>
    <div style="margin-top: 50px">
      <a-tooltip placement="bottomLeft">
        <template slot="title">
          <span>本校交易记录，可在个人信息设置学校信息查看当前学校项目交易记录</span>
        </template>
        <div style="font-size: 16px;font-weight: 600;margin-bottom: 15px"><a-icon type="bars" />
          <span style="margin-left: 5px;font-family: SimHei;letter-spacing: 1px;">交易记录</span>
        </div>
      </a-tooltip>
      <a-result title="未绑定学校信息！" v-if="client.school == null || client.school === ''">
        <template #icon>
          <a-icon type="idcard" theme="twoTone" />
        </template>
        <template #extra>
          <a-button type="primary" @click="$router.push('/personal')">
            去绑定
          </a-button>
        </template>
      </a-result>
      <a-result title="本校暂无交易记录！" v-if="transactionRecord.length === 0">
        <template #icon>
          <a-icon type="smile" theme="twoTone" />
        </template>
      </a-result>
      <div v-else>
        <a-result :title="'本校有'+transactionRecord.length+'条交易信息！'">
          <template #icon>
            <a-icon type="meh" theme="twoTone" />
          </template>
        </a-result>
        <a-list :grid="{ gutter: 16, column: 4 }" :data-source="transactionRecord">
          <a-list-item slot="renderItem" slot-scope="item, index">
            <a-list-item-meta :description="item.tradeDate">
              <a slot="title" href="https://www.antdv.com/">{{ item.account }}</a>
              <a-avatar slot="avatar" :src="item.headImgUrl"/>
            </a-list-item-meta>
          </a-list-item>
        </a-list>
      </div>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: 'AnnexDetail',
  computed: {
    ...mapState({
      user: state => state.account.user,
      client: state => state.account.client,
      fileHost: state => state.account.fileHost
    })
  },
  props: {
    annexCode: {
      type: String
    }
  },
  watch: {
    annexCode: function (val) {
      if (val) {
        this.getDetail(val)
        if (this.client.school) {
          this.getTransactionRecord(val)
        }
      }
    }
  },
  data () {
    return {
      annexInfo: null,
      transactionRecord: []
    }
  },
  methods: {
    getDetail (code) {
      this.$get(`/cos/annex-info/detail/${code}`).then((r) => {
        console.log(r.data.data)
        this.annexInfo = r.data.data
      })
    },
    getTransactionRecord (annexCode) {
      this.$get(`/cos/annex-info/trade/school`, {annexCode, userCode: this.client.code}).then((r) => {
        this.transactionRecord = r.data.data
      })
    },
    back () {
      this.$emit('back')
    }
  }
}
</script>

<style scoped>
  /deep/ .ant-page-header-heading-title {
    letter-spacing: 1px;
    font-family: SimHei;
  }

  .ant-carousel >>> .slick-slide {
    text-align: center;
    height: 460px;
    line-height: 460px;
    overflow: hidden;
  }

  /deep/ .ant-descriptions-item-label {
    width: 126px;
  }
</style>
