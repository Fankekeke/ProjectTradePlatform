<template>
  <a-modal
    v-model="show"
    :centered="true"
    :keyboard="false"
    :footer="null"
    :width="1000"
    @cancel="handleCancleClick">
    <div style="font-size: 13px" v-if="userInfoData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="6"><b>项目名称：</b>{{ userInfoData.proName }}</a-col>
        <a-col :span="6"><b>项目价格：</b>{{ userInfoData.proPrice !== undefined ? userInfoData.proPrice.toFixed(2) : '- -' }}</a-col>
        <a-col :span="6"><b>服务地址：</b>{{ userInfoData.proDownloadUrl !== null ? userInfoData.proDownloadUrl : '- -' }}</a-col>
        <a-col :span="6"><b>上传用户：</b>{{ userInfoData.releaseUser !== null ? userInfoData.releaseUser : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="6"><b>项目类型：</b>{{ type }}</a-col>
        <a-col :span="6"><b>访问量：</b>{{ userInfoData.visits }} 次</a-col>
        <a-col :span="6"><b>成交量：</b>{{ userInfoData.turnover }} 单</a-col>
        <a-col :span="6"><b>作者：</b>{{ userInfoData.authorName }} </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="6"><b>上传时间：</b>{{ userInfoData.releaseTime }}</a-col>
        <a-col :span="6"><b>项目编号：</b>{{ userInfoData.code }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col :span="24"><b>项目分类：</b>{{ userInfoData.proSort }}</a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;line-height: 20px">
        <a-col style="margin-top: 30px"><a-divider /></a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 10px">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">技术方案</span></a-col>
        <a-col :span="24">{{ userInfoData.proTechnology }}</a-col>
      </a-row>

      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 35px">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目介绍</span></a-col>
        <a-col :span="24" style="line-height: 30px">{{ userInfoData.proIntroduce }}</a-col>
      </a-row>

      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 35px">
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

      <a-row v-if="videoUrl !== ''" style="padding-left: 24px;padding-right: 24px;margin-top: 35px">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">视频文件</span></a-col>
        <a-col :span="24" style="line-height: 30px">
          <div>
            <a-button-group>
              <a-button type="primary" icon="video-camera" @click="initVideo"/>
              <a-button type="primary" icon="cloud-download" @click="dowbloadVideo"/>
            </a-button-group>
          </div>
          <br/>
          <video id="myVideo" class="video-js" style="width: 900px; height: 510px;" v-if="videoPlay">
            <source :src="videoUrl" type="video/mp4">
          </video>
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
export default {
  name: 'ProjectInfo',
  data() {
    return {
      videoUrl: '',
      videoPlay: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
    }
  },
  props: {
    userInfoVisiable: {
      require: true,
      default: false
    },
    userInfoData: {
      require: true
    }
  },
  watch: {
    userInfoData: function (val) {
      this.videoUrl = ''
      if(val.proVoide !== null) {
        this.videoUrl = 'http://localhost:9528/videoWeb/' + val.proVoide
        // this.videoUrl = 'https://yxblshop.cn:9528/videoWeb/' + val.proVoide
      }
      if(val.proPic !== null) {
        let fileList = []
        val.proPic.split(',').forEach((image, index) => {
          fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
          // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
        })
        this.fileList = fileList;
      }
    }
  },
  computed: {
    show: {
      get: function () {
        return this.userInfoVisiable
      },
      set: function () {
      }
    },
    type () {
      switch (this.userInfoData.proType) {
        case 1:
          return '仅后台'
        case 2:
          return '前后台'
        case 3:
          return '小程序'
        case 4:
          return '安卓'
        default:
          return '- -'
      }
    }
  },
  methods: {
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    },
    handleCancleClick () {
      this.videoUrl = ''
      this.videoPlay = false
      this.$emit('close')
    },
    handleCancel() {
      this.previewVisible = false;
    },
    dowbloadVideo() {
      window.open(this.videoUrl,'target');
    },
    initVideo() {
      this.videoPlay = true
      //初始化视频方法
      setTimeout(() => {
        let myPlayer = this.$video('myVideo', {
          //确定播放器是否具有用户可以与之交互的控件。没有控件，启动视频播放的唯一方法是使用autoplay属性或通过Player API。
          controls: true,
          //自动播放属性,muted:静音播放
          autoplay: "muted",
          //建议浏览器是否应在<video>加载元素后立即开始下载视频数据。
          preload: "auto",
          //设置视频播放器的显示宽度（以像素为单位）
          width: "800px",
          //设置视频播放器的显示高度（以像素为单位）
          height: "350px"
        });
      }, 200)
    }
  }
}
</script>
<style lang="less" scoped>

</style>
