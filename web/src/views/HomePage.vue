<template>
  <div :class="[multipage === true ? 'multi-page':'single-page', 'not-menu-page', 'home-page']">
    <div id="indexPage">
      <div style="width: 1200px;margin: 0 auto;height: 520px">
        <div class="home-banner-content-wrapper wow" style="width: 600px;float: left;padding-top: 150px">
          <h1 style="font-size: 38px;line-height: 46px;margin-bottom: 16px;font-weight: 400;color: rgba(0,0,0,.85);">用心制作，一键呈现</h1>
          <p style="font-size: 16px;line-height: 46px;margin-bottom: 16px;font-weight: 400;">为您提供专业的云上毕设服务，满足不同行业的个性化需求</p>
        </div>
        <div class="wrapper" style="text-align: right;width: 600px;float: right;">
          <iframe style="width: 100%; height: 530px;" id="tempHtml" ref="tempHtml" frameborder="0" src="/static/img/svg.html"></iframe>
        </div>
      </div>
      <div style="width: 1200px;margin: 0 auto">
        <a-divider orientation="left">
          <span style="font-size: 23px;letter-spacing: 2px">产品展示</span>
        </a-divider>
        <a-row :gutter="30" style="margin-top: 50px">
          <a-col :span="8" v-for="(item, index) in projectList" :key="index" style="margin-bottom: 30px">
            <a-card hoverable :bordered="false">
              <img
                style="height: 210px;object-fit: cover"
                slot="cover"
                alt="example"
                :src="`${fileHost}` + 'imagesWeb/' + item.proPic.split(',')[0]"
              />
              <a-card-meta :title="item.proName">
                <template slot="description">
                  <span style="font-size: 13px;text-align: center;line-height: 35px;letter-spacing: 1px">
                    {{ item.proIntroduce.slice(0, 45) }}
                  </span>
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
        </a-row>
        <a-divider orientation="left" style="margin-top: 120px">
          <span style="font-size: 23px;letter-spacing: 2px">我们团队</span>
        </a-divider>
        <a-row :gutter="30" style="margin-top: 50px">
          <a-col :span="6">
            <a-card hoverable :bordered="false">
              <img
                style="height: 150px;object-fit: cover"
                slot="cover"
                alt="example"
                src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
              />
              <a-card-meta title="悲伤的橘子树">
                <template slot="description">
                  <span style="font-size: 13px;line-height: 24px;letter-spacing: 1px">
                    当你选择了一种语言，意味着你还选择了一组技术、一个社区
                  </span>
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
          <a-col :span="6">
            <a-card hoverable :bordered="false">
              <img
                style="height: 150px;object-fit: cover"
                slot="cover"
                alt="example"
                src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
              />
              <a-card-meta title="斗佛Uncle">
                <template slot="description">
                  <span style="font-size: 13px;line-height: 24px;letter-spacing: 1px">
                    用代码行数来测评软件开发进度，就相对于用重量来计算飞机建造进度。
                  </span>
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
        </a-row>
        <a-divider orientation="left" style="margin-top: 120px">
          <span style="font-size: 23px;letter-spacing: 2px">技术支持</span>
        </a-divider>
        <a-row :gutter="15" style="margin: 50px 0px">
          <a-col :span="2">
            <img src="/static/logo/ant.png" width="60" height="60" alt="Ant Design"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/idea.jpg" width="60" height="60" alt="IDEA"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/html.jpg" width="60" height="60" alt="HTML"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/css.png" width="60" height="60" alt="CSS"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/javascript.png" width="60" height="60" alt="JavaScript"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/vue.png" width="60" height="60" alt="Vue"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/layui.jpg" width="60" height="60" alt="Layui"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/java.png" width="60" height="60" alt="Java"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/spring.png" width="60" height="60" alt="Spring"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/mybatis.png" width="60" height="60" alt="MyBatis"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/rabbitmq.png" width="60" height="60" alt="RabbitMQ"/>
          </a-col>
          <a-col :span="2">
            <img src="/static/logo/swagger.png" width="60" height="60" alt="Swagger"/>
          </a-col>
        </a-row>
      </div>
    </div>
  </div>
</template>
<script>
import HeadInfo from '@/views/common/HeadInfo'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'HomePage',
  components: {HeadInfo},
  data () {
    return {
      projectList: []
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user,
      fileHost: state => state.account.fileHost
    })
  },
  methods: {
    projectTop () {
      this.$get('/cos/project-info/top').then((r) => {
        this.projectList = r.data.data
      })
    }
  },
  mounted () {
    this.projectTop()
  }
}
</script>

<style scoped>
  /deep/ .ant-card-meta-title {
    text-align: center;
    letter-spacing: 1px;
  }
</style>
