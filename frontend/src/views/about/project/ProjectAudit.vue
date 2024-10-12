<template>
  <a-modal v-model="show" title="项目详情" @cancel="onClose" :width="800">
    <template slot="footer" v-if="currentUser.roleId == 74 && projectData.proStatus != 2">
      <a-button key="back" @click="turnDown" type="danger" :loading="loading">
        驳回
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        通过
      </a-button>
    </template>
    <template slot="footer" v-else>
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>项目名称：</b>{{ projectData.proName }}</a-col>
        <a-col :span="8"><b>项目类型：</b>
          <span v-if="projectData.proType == 1">仅后台</span>
          <span v-if="projectData.proType == 2">前后台</span>
          <span v-if="projectData.proType == 3">小程序</span>
          <span v-if="projectData.proType == 4">安卓</span>
        </a-col>
        <a-col :span="8"><b>审核状态：</b>
          <span v-if="projectData.proStatus == 1">正在审核</span>
          <span v-if="projectData.proStatus == 2">审核通过</span>
          <span v-if="projectData.proStatus == 3">不通过</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>作 者：</b>{{ projectData.username }}</a-col>
        <a-col :span="8"><b>上传时间：</b>{{ projectData.releaseTime !== null ? projectData.releaseTime : '- -' }}</a-col>
        <a-col :span="8"><b>下载地址：</b><a @click="downLoadZip">点我下载</a></a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">产品介绍</span></a-col>
        <a-col :span="24">{{ projectData.proIntroduce }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">技术方案</span></a-col>
        <a-col :span="24">{{ projectData.proTechnology }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目分类</span></a-col>
        <a-col :span="24">
          <a-tag v-for="(item, index) in projectData.proSort.split(',')" :key="index">{{ item }}</a-tag>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目图册</span></a-col>
        <a-col :span="24" style="line-height: 30px">
          <a-upload
            list-type="picture-card"
            @preview="handlePreview"
            :file-list="fileList">
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <a-row v-if="videoUrl !== ''" style="padding-left: 24px;padding-right: 24px;margin-top: 35px">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">视频文件</span></a-col>
        <a-col :span="24" style="line-height: 30px">
          <div>
            <a-button-group>
              <a-button type="primary" icon="video-camera" @click="initVideo"/>
              <a-button type="primary" icon="cloud-download" @click="downloadVideo"/>
            </a-button-group>
          </div>
          <br/>
          <video id="myVideo" class="video-js" style="width: 700px; height: 395px;" v-if="videoPlay">
            <source :src="videoUrl" type="video/mp4">
          </video>
        </a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 35px" v-if="currentUser.roleId == 74 && projectData.proStatus != 2">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目价格</span></a-col>
        <a-col :span="8" style="line-height: 30px">
          <a-input-number
            style="width: 100%"
            v-model="price"
            :default-value="300"
            :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            :parser="value => value.replace(/\￥\s?|(,*)/g, '')"
          />
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
}
import {mapState} from 'vuex'
export default {
  name: 'ProjectAudit',
  props: {
    projectShow: {
      type: Boolean,
      default: false
    },
    projectData: {
      type: Object
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
  data() {
    return {
      loading: false,
      videoUrl: '',
      videoPlay: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      price: 300
    }
  },
  mounted() {
    this.dataInit()
  },
  methods: {
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    },
    dataInit () {
      if(this.projectData.proVoide !== null) {
        this.videoUrl = 'http://localhost:9528/videoWeb/' + this.projectData.proVoide
        // this.videoUrl = 'https://yxblshop.cn:9528/videoWeb/' + val.proVoide
      }
      if(this.projectData.proPic !== null) {
        let fileList = []
        this.projectData.proPic.split(',').forEach((image, index) => {
          fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
          // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
        })
        this.fileList = fileList;
      }
    },
    initVideo() {
      this.videoPlay = true
      //初始化视频方法
      setTimeout(() => {
        let myPlayer = this.$video('myVideo', {
          //确定播放器是否具有用户可以与之交互的控件。没有控件，启动视频播放的唯一方法是使用autoplay属性或通过Player API。
          controls: true,
          //自动播放属性,muted:静音播放
          autoplay: 'muted',
          //建议浏览器是否应在<video>加载元素后立即开始下载视频数据。
          preload: "auto",
          //设置视频播放器的显示宽度（以像素为单位）
          width: "700px",
          //设置视频播放器的显示高度（以像素为单位）
          height: "395px"
        })
      }, 200)
    },
    downloadVideo () {
      window.open(this.videoUrl,'target')
    },
    downLoadZip () {
      if (this.projectData.proDowbloadZip !== null && this.projectData.proDowbloadZip !== '上传失败') {
        this.$download('/cos/project-apply/download', {fileName: this.projectData.proDowbloadZip}, this.projectData.proDowbloadZip).then((r) => {

        })
      } else {
        this.$message.warning('未上传项目文件')
      }
    },
    handleCancel () {
      this.previewVisible = false;
    },
    onClose () {
      this.$emit('close')
    },
    turnDown () {
      this.loading = true
      this.$put('/cos/project-apply/audit', {
        id: this.projectData.id,
        status: 3,
        price: this.price
      }).then((r) => {
        this.loading = false
        this.$emit('audit')
      })
    },
    handleSubmit () {
      this.loading = true
      this.$put('/cos/project-apply/audit', {
        id: this.projectData.id,
        status: 2,
        price: this.price
      }).then((r) => {
        this.loading = false
        this.$emit('audit')
      })
    }
  }
}
</script>

<style scoped>

</style>
