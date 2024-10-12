<template>
  <a-modal v-model="show" @cancel="onClose" title="部署审核" :width="1200">
    <template slot="footer">
      <a-button key="back" type="danger" @click="onAudit(6)">
        驳回
      </a-button>
      <a-button @click="onAudit(5)">
        通过
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="taskData !== null && ledger !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基本信息</span></a-col>
        <a-col :span="6"><b>项目名称：</b>{{ taskData.proName }}</a-col>
        <a-col :span="6"><b>定制编号：</b>{{ taskData.code !== null ? taskData.code : '- -' }}</a-col>
        <a-col :span="6"><b>联系方式：</b>{{ taskData.contact !== null ? taskData.contact : '- -' }}</a-col>
        <a-col :span="6"><b>申请时间：</b>{{ taskData.createDate !== null ? taskData.createDate : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="taskData.status > 2">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目完成</span></a-col>
        <a-col :span="6"><b>上传人：</b>{{ ledger.uploadPlayers }}</a-col>
        <a-col :span="6"><b>完成时间：</b>{{ ledger.completeDate !== null ? ledger.completeDate : '- -' }}</a-col>
        <a-col :span="6"><b>文件名称：</b>{{ ledger.local !== null ? ledger.local : '- -' }}</a-col>
        <a-col :span="6"><b>备注：</b>{{ ledger.completeRemarks !== null ? ledger.completeRemarks : '- -' }}</a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 20px" v-if="taskData.status > 2">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">定制项目图片</span></a-col>
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
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="taskData.status > 3">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">部署完成</span></a-col>
        <a-col :span="6"><b>部署人：</b>{{ ledger.deployPlayers }}</a-col>
        <a-col :span="6"><b>部署时间：</b>{{ ledger.deployDate !== null ? ledger.deployDate : '- -' }}</a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;margin-top: 20px" v-if="taskData.status > 3">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">部署证据</span></a-col>
        <a-col :span="24" style="line-height: 30px">
          <a-upload
            list-type="picture-card"
            @preview="handleDeployPreview"
            :file-list="fileDeployList">
          </a-upload>
          <a-modal :visible="previewDeployVisible" :footer="null" @cancel="handleDeployCancel">
            <img alt="example" style="width: 100%" :src="previewDeployImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
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
import {mapState} from "vuex";

export default {
  name: 'CustomAudit',
  props: {
    taskShow: {
      type: Boolean,
      default: false
    },
    taskData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.taskShow
      },
      set: function () {
      }
    }
  },
  watch: {
    taskShow: function (value) {
      if (value) {
        this.customLedger()
      }
    }
  },
  data () {
    return {
      ledger: null,
      fileDeployList: [],
      previewDeployVisible: false,
      previewDeployImage: '',
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
    customLedger () {
      this.$get('/cos/customize-ledger/ledger', {
        code: this.taskData.code
      }).then((r) => {
        console.log(JSON.stringify(r.data))
        this.ledger = r.data
        if(this.ledger.pic !== null) {
          let fileList = []
          this.ledger.pic.split(',').forEach((image, index) => {
            fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
            // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
          })
          this.fileList = fileList;
        }
        if(this.ledger.deployPic !== null) {
          let fileList = []
          this.ledger.deployPic.split(',').forEach((image, index) => {
            fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
            // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
          })
          this.fileDeployList = fileList
        }
      })
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    handleCancel() {
      this.previewVisible = false
    },
    async handleDeployPreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewDeployImage = file.url || file.preview
      this.previewDeployVisible = true
    },
    handleDeployCancel() {
      this.previewDeployVisible = false
    },
    onAudit (status) {
      this.$post('/cos/customize-info/audit', {
        orderCode: this.taskData.code,
        status: status
      }).then((r) => {
        this.$emit('success')
      })
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
