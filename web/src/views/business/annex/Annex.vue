<template>
  <a-card :bordered="false" class="card-area" style="width: 1200px;margin: 15px auto 0;">
    <div v-if="!detailFlag">
      <a-breadcrumb style="letter-spacing: 1px">
        <a-breadcrumb-item>
          <a @click="$router.push('/product')">项目系统</a>
        </a-breadcrumb-item>
        <a-breadcrumb-item>
          <a>论文、任务书</a>
        </a-breadcrumb-item>
      </a-breadcrumb>
      <a-row :gutter="15" style="margin-top: 50px">
        <a-col :span="6">
          <a-radio-group  v-model="queryParams.sortType" default-value="1" button-style="solid" @change="sortChange">
            <a-radio-button value="1">
              综合
            </a-radio-button>
            <a-radio-button value="2">
              销量
            </a-radio-button>
            <a-radio-button value="3">
              价格
            </a-radio-button>
            <a-radio-button value="4">
              新品
            </a-radio-button>
          </a-radio-group>
        </a-col>
        <a-col :span="6">
          <a-input-search placeholder="搜索附件" v-model="queryParams.key" @search="search" />
        </a-col>
      </a-row>
      <a-row :gutter="30" style="margin-top: 50px">
        <a-skeleton active :paragraph="{ rows: 10 }" v-if="loading"/>
        <a-col :span="8" v-if="!loading" v-for="(item, index) in annexList" :key="index" style="margin-bottom: 30px">
          <a-card hoverable :bordered="false" @click="detailInit(item.code)">
            <img
              style="height: 210px;object-fit: cover"
              slot="cover"
              alt="example"
              :src="`${fileHost}` + 'imagesWeb/' + item.pic.split(',')[0]"
            />
            <a-card-meta>
              <template slot="title">
                <span style="font-size: 15px;color: #f5222d;letter-spacing: 0px">￥{{ item.annexPrice }}</span>
                <a-tag color="#108ee9" style="float: right;font-size: 12px">
                  销量：{{ item.sales }}
                </a-tag>
              </template>
              <template slot="description">
                <span style="font-size: 13px;text-align: center;line-height: 35px;letter-spacing: 1px;color: #000c17">
                  {{ item.annexName.slice(0, 38) }}
                </span>
              </template>
            </a-card-meta>
          </a-card>
        </a-col>
        <a-col :span="24">
          <a-empty v-if="!loading && annexList.length === 0" :description="false" style="margin-top: 80px;margin-bottom: 80px"/>
        </a-col>
      </a-row>
      <a-pagination
        :defaultCurrent="pagination.defaultCurrent"
        :defaultPageSize="pagination.defaultPageSize"
        :total="pagination.total"
        @change="onChange"
        style="float: right;margin-top: 50px"/>
    </div>
    <annex-detail v-show="detailFlag" @back="detailFlag = false" :annexCode="annexCode"/>
  </a-card>
</template>

<script>
import AnnexDetail from './AnnexDetail'
import {mapState} from 'vuex'
export default {
  name: 'News',
  components: {AnnexDetail},
  computed: {
    ...mapState({
      fileHost: state => state.account.fileHost
    })
  },
  data () {
    return {
      loading: false,
      annexList: [],
      queryParams: {
        sortType: '1'
      },
      pagination: {
        defaultCurrent: 1,
        defaultPageSize: 6,
        total: 0
      },
      annexCode: '',
      detailFlag: false
    }
  },
  methods: {
    // 项目详情
    detailInit (annexCode) {
      this.annexCode = annexCode
      this.detailFlag = true
    },
    // 排序改变事件
    sortChange () {
      this.search()
    },
    /**
     * 分页查询改变事件
     * @param page
     * @param pageSize
     */
    onChange (page, pageSize) {
      this.pagination.defaultCurrent = page
      this.search()
    },
    search () {
      this.fetch({
        ...this.queryParams
      })
    },
    fetch (params = {}) {
      this.loading = true
      params.size = this.pagination.defaultPageSize
      params.current = this.pagination.defaultCurrent
      this.$get('/cos/annex-info/list/page', {...params}).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.annexList = data.records
        this.pagination = pagination
        setTimeout(() => {
          this.loading = false
        }, 200)
      })
    }
  },
  mounted () {
    this.search()
  }
}
</script>

<style scoped>
  /deep/ .ant-card-meta-title {
    letter-spacing: 1px;
  }
</style>
