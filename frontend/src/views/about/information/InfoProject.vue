<template>
  <a-drawer title="项目信息" :height="fullHeight" placement="bottom" :visible="show" :bodyStyle="bodyStyle" @close="onClose">
    <div style="background:#ECECEC; padding:30px;font-size: 13px;height: 100vh">
      <a-row :gutter="20">
        <a-col :span="8" v-for="(item, index) in projectData" :key="index">
          <a-card :bordered="false">
            <a slot="extra" @click="projectView(item)" style="font-size: 13px">详情</a>
            <span slot="title" style="font-size: 13px">
              <a-badge status="success" v-show="item.delFlag === 0"/>
              <a-badge status="processing" v-show="item.delFlag === 1"/>
              {{ item.proName }}
            </span>
            <div style="font-size: 13px">
              <a-row :gutter="8">
                <a-col :span="8"><b>项目价格：</b>￥ {{ item.proPrice }}</a-col>
                <a-col :span="8"><b>项目类型：</b>
                  <span v-if="item.proType == 1">仅后台</span>
                  <span v-if="item.proType == 2">前后台</span>
                  <span v-if="item.proType == 3">小程序</span>
                  <span v-if="item.proType == 4">安卓</span>
                </a-col>
                <a-col :span="8"><b>作 者：</b>{{ item.authorName }}</a-col>
                <br/>
                <br/>
                <a-col :span="24" v-if="item.proSort !== null">
                  <a-tag v-for="(item, index) in item.proSort.split(',')" :key="index">{{ item }}</a-tag>
                </a-col>
              </a-row>
              <div style="margin-top: 20px" v-if="item.proPic !== null">
                <a-avatar style="margin-right: 10px" shape="square" :size="64" icon="user" v-for="(i, d) in item.proPic.split(',')" :src="'http://127.0.0.1:9528/imagesWeb/' + i" :key="d"/>
              </div>
            </div>
          </a-card>
        </a-col>
        <a-col :span="24" v-if="projectData.length === 0">
          <a-card>
            <a-result title="暂时还没有自己的项目！">
              <template #icon>
                <a-icon type="meh" theme="twoTone" />
              </template>
              <template #extra>
                <a-button type="primary">
                  创建
                </a-button>
              </template>
            </a-result>
          </a-card>
        </a-col>
      </a-row>
    </div>
    <project-info
      :userInfoData="projectInfo.data"
      :userInfoVisiable="projectInfo.visiable"
      @close="handleProjectInfoClose">
    </project-info>
  </a-drawer>
</template>

<script>
import {mapState} from "vuex";
import ProjectInfo from '../../cos/project/ProjectInfo'

export default {
  name: 'InfoProject',
  components: {ProjectInfo},
  props: {
    projectShow: {
      type: Boolean,
      default: false
    },
    projectData: {
      type: Array
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.projectShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      bodyStyle: {padding: 0},
      fullHeight: document.documentElement.clientHeight || document.body.clientHeight,
      projectInfo: {
        data: null,
        visiable: false
      }
    }
  },
  methods: {
    projectView (row) {
      this.projectInfo.data = row
      this.projectInfo.visiable = true
    },
    onClose () {
      this.$emit('close')
    },
    handleProjectInfoClose () {
      this.projectInfo.visiable = false
    }
  }
}
</script>

<style scoped>
  /deep/ .ant-drawer-body {
    padding: 0px;
  }
</style>
