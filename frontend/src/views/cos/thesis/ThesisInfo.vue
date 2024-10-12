<template>
  <a-modal v-model="show" title="附件详情" @cancel="onClose" :width="800">
    <template slot="footer" v-if="thesisData !== null && currentUser.roleId == 74 && thesisData.status != 2">
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
    <div style="font-size: 13px" v-if="thesisData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>附件名称：</b>
          <a-tooltip>
            <template slot="title">
              {{ thesisData.annexName }}
            </template>
            {{ thesisData.annexName.slice(0, 8) }} ...
          </a-tooltip>
        </a-col>
        <a-col :span="8"><b>附件类型：</b>
          <span v-if="thesisData.annexType == 1">论文 [{{ thesisData.wordCount }}字]</span>
          <span v-if="thesisData.annexType == 2">PPT</span>
          <span v-if="thesisData.annexType == 3">任务书</span>
          <span v-if="thesisData.annexType == 4">开题报告</span>
        </a-col>
        <a-col :span="8"><b>审核状态：</b>
          <span v-if="thesisData.status == 1">正在审核</span>
          <span v-if="thesisData.status == 2">审核通过</span>
          <span v-if="thesisData.status == 3">不通过</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>附件编号：</b>{{ thesisData.code }}</a-col>
        <a-col :span="8"><b>配套价格：</b>{{ thesisData.annexPrice !== null ? ('￥' + thesisData.annexPrice) : '- -' }}</a-col>
        <a-col :span="8"><b>是否启用：</b>
          <a-tag color="red" v-if="thesisData.isEnable == 0">
            下架中
          </a-tag>
          <a-tag color="green" v-if="thesisData.isEnable == 1">
            上架中
          </a-tag>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>作 者：</b>{{ thesisData.players }}</a-col>
        <a-col :span="8"><b>上传时间：</b>{{ thesisData.uploadDate !== null ? thesisData.uploadDate : '- -' }}</a-col>
        <a-col :span="8"><b>下载地址：</b><a @click="downLoadAnnex">点我下载</a></a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备 注</span></a-col>
        <a-col :span="24">{{ thesisData.remark }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">所属项目</span></a-col>
        <a-col :span="24" v-if="thesisData.proName !== null">
          <a-tag v-for="(item, index) in thesisData.proName.split(',')" :key="index">{{ item }}</a-tag>
        </a-col>
        <a-col :span="24" v-else>
          <a-result title="未选择所属项目!">
            <template #icon>
              <a-icon type="folder-open" theme="twoTone" />
            </template>
          </a-result>
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
    </div>
  </a-modal>
</template>

<script>
import {mapState} from "vuex";

function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
}
export default {
  name: "ThesisInfo",
  props: {
    thesisShow: {
      type: Boolean,
      default: false
    },
    thesisData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.thesisShow
      },
      set: function () {
      }
    }
  },
  data() {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  watch: {
    thesisShow: function (value) {
      if (value) {
        this.dataInit()
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
    dataInit () {
      if(this.thesisData.pic !== null) {
        let fileList = []
        this.thesisData.pic.split(',').forEach((image, index) => {
          fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
          // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
        })
        this.fileList = fileList;
      }
    },
    downLoadAnnex () {
      if (this.thesisData.annexLocal !== null && this.thesisData.annexLocal !== '上传失败') {
        this.$download('/cos/annex-info/download', {fileName: this.thesisData.annexLocal}, this.thesisData.annexLocal).then((r) => {

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
      this.$put('/cos/annex-info/audit', {
        id: this.thesisData.id,
        status: 3
      }).then((r) => {
        this.loading = false
        this.$emit('audit')
      })
    },
    handleSubmit () {
      this.loading = true
      this.$put('/cos/annex-info/audit', {
        id: this.thesisData.id,
        status: 2
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
