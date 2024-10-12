<template>
  <a-modal v-model="show" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="taskData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目流程</span></a-col>
        <a-col>
          <a-steps :current="taskData.status" type="navigation" size="small" style="box-shadow: 0px -1px 0 0 #e8e8e8 inset">
            <a-step title="待审核" description="" />
            <a-step title="沟通中" description="" />
            <a-step title="已接单" description="" />
            <a-step title="已完成" description="" />
            <a-step title="部署完成" description="" />
            <a-step title="审核结果" description="" />
            <a-step title="佣金分配" description="" />
          </a-steps>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row>
        <a-col :span="8">
          <a-result :title="taskData.startDate">
            <template #icon>
              开始时间
            </template>
          </a-result>
        </a-col>
        <a-col :span="8">
          <a-result v-if="taskData.lessDay >= 0" :title="taskData.lessDay + '天'">
            <template #icon>
            </template>
          </a-result>
          <a-result v-else :title="'已超时' + Math.abs(taskData.lessDay) + '天'">
            <template #icon>
            </template>
          </a-result>
        </a-col>
        <a-col :span="8">
          <a-result :title="taskData.endDate">
            <template #icon>
              结束时间
            </template>
          </a-result>
        </a-col>
      </a-row>
      <a-calendar :defaultValue="taskData.defaultDate">
        <ul slot="dateCellRender" slot-scope="value" class="events">
          <li v-for="item in getListData(value)" :key="item.content">
            <a-badge :status="item.type" :text="item.content" />
          </li>
        </ul>
      </a-calendar>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="taskData.status > 2">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">项目完成</span></a-col>
        <a-col :span="6"><b>上传人：</b>{{ taskData.uploadPlayers }}</a-col>
        <a-col :span="6"><b>完成时间：</b>{{ taskData.completeDate !== null ? taskData.completeDate : '- -' }}</a-col>
        <a-col :span="6"><b>文件名称：</b>{{ taskData.local !== null ? taskData.local : '- -' }}</a-col>
        <a-col :span="6"><b>备注：</b>{{ taskData.completeRemarks !== null ? taskData.completeRemarks : '- -' }}</a-col>
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
        <a-col :span="6"><b>部署人：</b>{{ taskData.deployPlayers }}</a-col>
        <a-col :span="6"><b>部署时间：</b>{{ taskData.deployDate !== null ? taskData.deployDate : '- -' }}</a-col>
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
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基本信息</span></a-col>
        <a-col :span="6"><b>项目名称：</b>{{ taskData.proName }}</a-col>
        <a-col :span="6"><b>定制编号：</b>{{ taskData.code !== null ? taskData.code : '- -' }}</a-col>
        <a-col :span="6"><b>联系方式：</b>{{ taskData.contact !== null ? taskData.contact : '- -' }}</a-col>
        <a-col :span="6"><b>申请时间：</b>{{ taskData.createDate !== null ? taskData.createDate : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>定制价格：</b>{{ taskData.amount !== null ? ('￥' + taskData.amount) : '- -' }}</a-col>
        <a-col :span="6"><b>定 金：</b>{{ taskData.deposit !== null ? ('￥' + taskData.deposit) : '- -' }}</a-col>
        <a-col :span="6"><b>开始时间：</b>{{ taskData.startDate !== null ? taskData.startDate : '- -' }}</a-col>
        <a-col :span="6"><b>项目周期：</b>{{ taskData.cycle !== null ? (taskData.cycle + '天') : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">技术要求</span></a-col>
        <a-col :span="24" v-html="">{{ taskData.skills }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">功能模块</span></a-col>
        <a-col :span="24">{{ taskData.module }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">开发者</span></a-col>
        <a-col :span="24" style="margin-top: 5px">
          <a-row>
            <a-col :span="8" v-for="(item, index) in developerInfo" :key="index">
              <div style="padding-right: 15px;padding-bottom: 10px">
                <a-card hoverable>
                  <a-card-meta :title="item.players" :description="item.remarks !== null ? '[' + item.deptName + ']  ' + item.remarks : '[' + item.deptName + ']'">
                    <a-avatar
                      slot="avatar"
                      :src="'http://127.0.0.1:9528/imagesWeb/' + item.avatar"
                    />
                  </a-card-meta>
                </a-card>
              </div>
            </a-col>
          </a-row>
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
import moment from "moment";

export default {
  name: 'TaskView',
  props: {
    taskShow: {
      type: Boolean,
      default: false
    },
    taskData: {
      type: Object
    }
  },
  watch: {
    taskShow: function (data) {
      if (data) {
        this.taskData.defaultDate = moment(this.taskData.startDate)
        this.taskData.endDate = moment(this.taskData.startDate).add(this.taskData.cycle, 'days').format('YYYY-MM-DD')
        let m1 = moment();
        let m2 = moment(this.taskData.endDate);
        this.taskData.lessDay = m2.diff(m1, 'day')
        if (this.taskData.technician !== null) {
          this.developer = this.taskData.technician.split(',').map(Number)
          this.developerChange(this.developer)
        }
        if(this.taskData.pic !== null) {
          let fileList = []
          this.taskData.pic.split(',').forEach((image, index) => {
            fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
            // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
          })
          this.fileList = fileList;
        }
        if(this.taskData.deployPic !== null) {
          let fileList = []
          this.taskData.deployPic.split(',').forEach((image, index) => {
            fileList.push({uid: index, name: image, status: 'done', url: 'http://localhost:9528/imagesWeb/'+image})
            // fileList.push({uid: index, name: image, status: 'done', url: 'https://yxblshop.cn:9528/imagesWeb/'+image})
          })
          this.fileDeployList = fileList
        }
      }
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
  data () {
    return {
      fileDeployList: [],
      previewDeployVisible: false,
      previewDeployImage: '',
      fileList: [],
      previewVisible: false,
      previewImage: '',
      developerInfo: [],
      developer: [],
      developerSpare: []
    }
  },
  mounted() {
    this.getTeamList()
  },
  methods: {
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
    getTeamList () {
      this.$get('/cos/team-info/teamList').then((r) => {
        this.developerSpare = r.data.data
      })
    },
    developerChange (value) {
      this.developerInfo = []
      this.developer.forEach(tb1 => {
        this.developerSpare.forEach(tmp => {
          if (tmp.id == tb1) {
            this.developerInfo.push(tmp)
          }
        })
      })
    },
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr),
        beginDate = new Date(beginDateStr),
        endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    getListData(value) {
      let listData = []
      this.taskData.endDate = moment(this.taskData.startDate).add(this.taskData.cycle, 'days').format('YYYY-MM-DD')
      // if (moment(value).format('YYYY-MM-DD') === this.taskData.startDate) {
      //   listData.push({type: 'success', content: '【定制开始】'+ this.taskData.proName +'.'})
      // }
      // if (moment(value).format('YYYY-MM-DD') === this.taskData.endDate) {
      //   listData.push({type: 'warning', content: '【定制结束】'+ this.taskData.proName +'.'})
      // }
      if (this.isDuringDate(this.taskData.startDate, this.taskData.endDate, moment(value).format('YYYY-MM-DD'))) {
        listData.push({type: 'success', content: '✔'})
      }
      return listData || []
    },
    getMonthData(value) {
      if (value.month() === 8) {
        return 1394;
      }
    },
    onClose() {
      this.fileList = []
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
