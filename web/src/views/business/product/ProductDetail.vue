<template>
  <div v-if="productInfo != null">
    <a-page-header
      :title="productInfo.proName"
      :sub-title="productInfo.code"
      @back="back"
    />
    <div style="padding: 23px;font-family: SimHei;">
      <a-carousel effect="fade" autoplay style="margin-bottom: 50px">
        <div v-for="(item, index) in productInfo.proPic.split(',')" :key="index">
          <img :src="`${fileHost}` + 'imagesWeb/' + item" style="width: 100%;height: 100%"/>
        </div>
      </a-carousel>
      <a-descriptions bordered>
        <a-descriptions-item label="项目名称">
          {{ productInfo.proName }}
        </a-descriptions-item>
        <a-descriptions-item label="项目类型">
          <span v-if="productInfo.proType == 1">仅后台</span>
          <span v-if="productInfo.proType == 2">前后台</span>
          <span v-if="productInfo.proType == 3">小程序</span>
          <span v-if="productInfo.proType == 4">安卓</span>
        </a-descriptions-item>
        <a-descriptions-item label="价格">
          <span style="color: #f5222d">{{ productInfo.proPrice }}</span> 元
        </a-descriptions-item>
        <a-descriptions-item label="项目作者">
          {{ productInfo.authorName }}
        </a-descriptions-item>
        <a-descriptions-item label="发布时间" :span="2">
          {{ productInfo.releaseTime }}
        </a-descriptions-item>
        <a-descriptions-item label="上架状态" :span="3">
          <a-badge status="processing" text="上架中" />
        </a-descriptions-item>
        <a-descriptions-item label="访问量">
          {{ productInfo.visits }}
        </a-descriptions-item>
        <a-descriptions-item label="销量">
          {{ productInfo.turnover }}单
        </a-descriptions-item>
        <a-descriptions-item label="项目标签">
          {{ productInfo.proSort }}
        </a-descriptions-item>
        <a-descriptions-item label="技术方案" :span="3">
          {{ productInfo.proTechnology }}
        </a-descriptions-item>
        <a-descriptions-item label="项目描述">
          {{ productInfo.proIntroduce }}
        </a-descriptions-item>
      </a-descriptions>
      <div style="margin-top: 50px" v-if="productInfo.videoAddress">
        <div style="font-size: 16px;font-weight: 600;margin-bottom: 15px"><a-icon type="tag" />
          <span style="margin-left: 5px;font-family: SimHei;letter-spacing: 1px;">项目视频</span>
        </div>
        <iframe :src="productInfo.videoAddress" allowfullscreen="allowfullscreen" width="100%" height="500" scrolling="no" frameborder="0" sandbox="allow-top-navigation allow-same-origin allow-forms allow-scripts"></iframe>
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
  </div>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'ProductDetail',
  computed: {
    ...mapState({
      user: state => state.account.user,
      client: state => state.account.client,
      fileHost: state => state.account.fileHost
    })
  },
  props: {
    productCode: {
      type: String
    }
  },
  watch: {
    productCode: function (val) {
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
      productInfo: null,
      transactionRecord: []
    }
  },
  methods: {
    getDetail (code) {
      this.$get(`/cos/project-info/detail/${code}`).then((r) => {
        this.productInfo = r.data.data
      })
    },
    getTransactionRecord (productCode) {
      this.$get(`/cos/project-info/trade/school`, {productCode, userCode: this.client.code}).then((r) => {
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
