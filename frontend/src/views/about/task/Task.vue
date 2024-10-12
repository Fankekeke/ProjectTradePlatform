<template style="height: 100%">
  <div style="width: 100%">
    <a-radio-group v-model="value" @change="onChange" style="margin-top: 30px">
      <a-radio-button value="a">
        定制项目
      </a-radio-button>
      <a-radio-button value="b">
        部署项目
      </a-radio-button>
      <a-radio-button value="c" v-if="currentUser.roleId == 74">
        加时审核
      </a-radio-button>
    </a-radio-group>
    <div style="background:#ECECEC; padding:30px;font-size: 13px;height: 68vh;margin-top: 20px">
      <a-skeleton active :loading="loading">
        <a-row :gutter="20" v-if="value === 'a'">
          <a-col :span="8" v-for="(item, index) in customizeList" :key="index">
            <a-card :bordered="false" style="margin-bottom: 20px">
              <a-popover slot="extra" title="定制要求" placement="leftTop">
                <template slot="content">
                  <div style="width: 400px;font-size: 12px">
                    <a-row>
                      <a-col style="margin-bottom: 15px"><span style="font-size: 13px;font-weight: 650;color: #000c17">技术要求</span>
                      </a-col>
                      <a-col :span="24" v-html="">{{ item.skills }}</a-col>
                    </a-row>
                    <br/>
                    <a-row>
                      <a-col style="margin-bottom: 15px"><span style="font-size: 13px;font-weight: 650;color: #000c17">系统模块</span>
                      </a-col>
                      <a-col :span="24" v-html="">{{ item.module }}</a-col>
                    </a-row>
                  </div>
                </template>
                <a style="font-size: 13px">定制要求</a>
              </a-popover>
              <span slot="title" style="font-size: 13px">
              <a-badge status="success" v-if="item.status === 5"/>
              <a-badge status="processing" v-else/>
              {{ item.proName }}
            </span>
              <template slot="actions" class="ant-card-actions">
                <a-icon key="check" type="check" @click="taskUploadOpen(item)" v-if="item.status === 2"/>
                <a-icon key="code" type="code" @click="taskDeployOpen(item)" v-if="item.status === 3"/>
                <a-popover title="申请加时" placement="bottom" trigger="click" v-if="item.status === 2">
                  <template slot="content">
                    <a-row :gutter="20">
                      <a-col :span="17">
                        <a-input-number
                          v-model="days"
                          style="width: 100%"
                          :min="1"
                          :max="15"
                          :formatter="value => `${value}天`"
                          :parser="value => value.replace('天', '')"
                        />
                      </a-col>
                      <a-col :span="7">
                        <a-button type="primary" @click="overTimeApply(item)">
                          申请
                        </a-button>
                      </a-col>
                    </a-row>
                  </template>
                  <a-icon key="warning" type="warning"/>
                </a-popover>
                <a-icon key="ellipsis" type="ellipsis" @click="taskViewOpen(item)"/>
              </template>
              <div style="font-size: 13px">
                <a-row :gutter="8">
                  <a-col :span="8"><b>项目价格：</b>￥ {{ item.amount }}</a-col>
                  <a-col :span="8"><b>当前状态：</b>
                    <span v-if="item.status == 0">待审核</span>
                    <span v-if="item.status == 1">沟通中</span>
                    <span v-if="item.status == 2">已接单</span>
                    <span v-if="item.status == 3">已完成</span>
                    <span v-if="item.status == 4">部署完成</span>
                    <span v-if="item.status == 5">审核完成</span>
                    <span v-if="item.status == 6">审核不通过</span>
                  </a-col>
                  <a-col :span="8"><b>项目周期：</b>{{ item.cycle }}天</a-col>
                  <br/>
                  <br/>
                  <a-col :span="8"><b>开始时间：</b>
                    {{ item.startDate }}
                  </a-col>
                  <a-col :span="8"><b>结束时间：</b>
                    {{ item.endDate }}
                  </a-col>
                </a-row>
                <!--              <div style="margin-top: 20px" v-if="item.proPic !== null">-->
                <!--                <a-avatar style="margin-right: 10px" shape="square" :size="64" icon="user" v-for="(i, d) in item.proPic.split(',')" :src="'http://127.0.0.1:9528/imagesWeb/' + i" :key="d"/>-->
                <!--              </div>-->
              </div>
            </a-card>
          </a-col>
          <a-col :span="10" :offset="7" v-if="customizeList.length === 0" style="margin-top: 100px">
            <a-card>
              <a-result title="未分配定制任务！">
                <template #icon>
                  <a-icon type="meh" theme="twoTone" />
                </template>
              </a-result>
            </a-card>
          </a-col>
        </a-row>
        <a-row :gutter="20" v-if="value === 'b'">
          <a-col :span="8" v-for="(item, index) in orderList" :key="index">
            <a-card :bordered="false" style="margin-bottom: 20px">
              <span slot="title" style="font-size: 13px">
                <a-badge status="success" v-if="item.needDeploy === 0 || item.status === 3"/>
                <a-badge status="processing" v-else/>
                {{ item.code }}
              </span>
              <span slot="extra" style="font-size: 13px">《{{ item.proName }}》</span>
              <template slot="actions" class="ant-card-actions">
                <a-icon key="check" type="check" @click="orderDeployOpen(item)" v-if="item.needDeploy === 1 && (item.status === 1 || item.status === 4 || item.status === null)"/>
                <a-icon key="ellipsis" type="ellipsis" @click="orderViewOpen(item)"/>
              </template>
              <div style="font-size: 13px">
                <a-row :gutter="8">
                  <a-col :span="8"><b>订单价格：</b>
                    <a-tooltip>
                      <template slot="title">
                        <p>项目价格：￥{{ item.orderPrice }}</p>
                        <p v-if="item.needDeploy === 1">部署价格：￥30</p>
                      </template>
                      ￥ <span>{{ item.orderPrice }}</span>
                      <span v-if="item.needDeploy === 1"> + <span style="color: green">30</span></span>
                    </a-tooltip>
                  </a-col>
                  <a-col :span="8"><b>需要部署：</b>
                    <span style="color: red" v-if="item.needDeploy === 0">否</span>
                    <span style="color: green" v-if="item.needDeploy === 1">是</span>
                  </a-col>
                  <a-col :span="8"><b>订单时间：</b>
                    <a-tooltip>
                      <template slot="title">
                        {{ item.orderTime }}
                      </template>
                      {{ item.orderTime.slice(0, 10) }}...
                    </a-tooltip>
                  </a-col>
                  <br/>
                  <br/>
                  <a-col :span="8"><b>审核状态：</b>
                    <a-tag v-if="item.status === 1 || item.status === null">部署中...</a-tag>
                    <a-tag v-if="item.status === 2">部署完成...</a-tag>
                    <a-tag v-if="item.status === 3">审核通过...</a-tag>
                    <a-tag v-if="item.status === 4">未通过...</a-tag>
                  </a-col>
                </a-row>
              </div>
            </a-card>
          </a-col>
          <a-col :span="10" :offset="7" v-if="orderList.length === 0" style="margin-top: 100px">
            <a-card>
              <a-result title="没有订单信息！">
                <template #icon>
                  <a-icon type="meh" theme="twoTone" />
                </template>
              </a-result>
            </a-card>
          </a-col>
        </a-row>
        <a-row :gutter="20" v-if="value === 'c'">
          <a-col :span="8" v-for="(item, index) in customizeOvertimeList" :key="index">
            <a-card :bordered="false" style="margin-bottom: 20px">
            <span slot="title" style="font-size: 13px">
              <a-badge status="success" v-if="item.status === 2"/>
              <a-badge status="processing" v-if="item.status === 1"/>
              <a-badge status="warning" v-if="item.status === 3"/>
              《{{ item.proName }}》加时申请
            </span>
              <template slot="actions" class="ant-card-actions">
                <a-icon v-if="item.status === 1" key="check-circle" type="check-circle" style="color: green" @click="overTimeCheck(item, 2)"/>
                <a-icon v-if="item.status === 1" key="close-circle" type="close-circle" style="color: red" @click="overTimeCheck(item, 3)"/>
                <a-icon v-if="item.status !== 1" key="check-circle" type="check-circle" style="cursor:not-allowed"/>
                <a-icon v-if="item.status !== 1" key="close-circle" type="close-circle" style="cursor:not-allowed"/>
              </template>
              <div style="font-size: 13px">
                <a-row :gutter="8">
                  <a-col :span="8"><b>订单编号：</b>
                    <a-tooltip placement="topLeft">
                      <template slot="title">
                        <span>{{ item.customizeCode }}</span>
                      </template>
                      {{ item.customizeCode.slice(0, 8) }}...
                    </a-tooltip>
                  </a-col>
                  <a-col :span="8"><b>加时天数：</b>{{ item.overtimeDay }}天</a-col>
                  <a-col :span="8"><b>申请日期：</b>
                    <a-tooltip placement="topLeft">
                      <template slot="title">
                        <span>{{ item.createDate }}</span>
                      </template>
                      {{ item.createDate.slice(0, 8) }}...
                    </a-tooltip>
                  </a-col>
                </a-row>
                <br/>
                <a-row :gutter="8">
                  <a-col :span="8"><b>开始时间：</b>{{ item.startDate }}</a-col>
                  <a-col :span="8"><b>定制天数：</b>{{ item.cycle }}天</a-col>
                  <a-col :span="8"><b>申请人：</b>{{ item.players }}</a-col>
                </a-row>
              </div>
            </a-card>
          </a-col>
        </a-row>
      </a-skeleton>
    </div>
    <task-view
      :taskShow="task.show"
      :taskData="task.data"
      @close="taskViewClose">
    </task-view>
    <task-upload
      :taskShow="taskUpload.show"
      :taskData="taskUpload.data"
      @finish="taskUploadFinish"
      @close="taskUploadClose">
    </task-upload>
    <deploy-completed
      :taskShow="taskDeploy.show"
      :taskData="taskDeploy.data"
      @finish="deployFinish"
      @close="deployClose">
    </deploy-completed>
    <deploy-over
      :taskShow="orderDeploy.show"
      :taskData="orderDeploy.data"
      @finish="orderDeployFinish"
      @close="orderDeployClose">
    </deploy-over>
    <order-task-view
      :taskShow="orderView.show"
      :taskData="orderView.data"
      @close="orderViewClose">
    </order-task-view>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
import TaskView from './TaskView'
import TaskUpload from "./TaskUpload";
import DeployCompleted from "./DeployCompleted";
import DeployOver from "./DeployOver";
import OrderTaskView from "./OrderTaskView";

export default {
  name: 'Task',
  components: {OrderTaskView, DeployOver, DeployCompleted, TaskUpload, TaskView},
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
  },
  data() {
    return {
      loading: false,
      value: 'a',
      days: 5,
      customizeList: [],
      customizeOvertimeList: [],
      orderList: [],
      taskUpload: {
        show: false,
        data: null
      },
      orderView: {
        show: false,
        data: null
      },
      orderDeploy: {
        show: false,
        data: null
      },
      taskDeploy: {
        show: false,
        data: null
      },
      task: {
        show: false,
        data: null
      }
    }
  },
  mounted() {
    this.getTask()
  },
  methods: {
    getOverTimeList() {
      this.loading = true
      this.$get('/cos/customize-overtime/list').then((r) => {
        this.customizeOvertimeList = r.data.data
        setTimeout(() => {
          this.loading = false
        }, 300)
      })
    },
    overTimeCheck (row, status) {
      this.$post('/cos/customize-overtime/check', {
        customizeCode: row.customizeCode,
        overtimeDay: row.overtimeDay,
        proposer: row.proposer,
        status: status
      }).then((r) => {
        this.$message.success('审核成功')
        this.getOverTimeList()
      })
    },
    overTimeApply (row) {
      if (this.days !== null) {
        this.$post('/cos/customize-overtime/apply', {
          customizeCode: row.code,
          overtimeDay: this.days,
          proposer: this.currentUser.userId
        }).then((r) => {
          this.$message.success('申请成功')
        })
      } else {
        this.$message.error('请填写申请天数')
      }
    },
    getTask () {
      this.loading = true
      this.$get('/cos/information/task', {
        userId: this.currentUser.userId
      }).then((r) => {
        this.customizeList = r.data !== null ? r.data.data : []
        this.customizeList.forEach(item => {
          item.endDate = moment(item.startDate).add(item.cycle, 'days').format('YYYY-MM-DD')
        })
        setTimeout(() => {
          this.loading = false
        }, 300)
      })
    },
    getOrder () {
      this.loading = true
      this.$get('/cos/information/order', {
        userId: this.currentUser.userId
      }).then((r) => {
        this.orderList = r.data !== null ? r.data.data : []
        setTimeout(() => {
          this.loading = false
        }, 300)
      })
    },
    orderViewOpen (row) {
      this.orderView.data = row
      this.orderView.show = true
    },
    orderDeployOpen (row) {
      this.orderDeploy.data = row
      this.orderDeploy.show = true
    },
    taskViewOpen (row) {
      this.task.data = row
      this.task.show = true
    },
    taskViewClose () {
      this.task.show = false
    },
    taskUploadFinish () {
      this.taskUpload.show = false
      this.$message.success('上传成功！')
      this.getTask()
    },
    taskUploadClose () {
      this.taskUpload.show = false
    },
    deployFinish () {
      this.taskDeploy.show = false
      this.$message.success('提交成功！')
      this.getTask()
    },
    deployClose () {
      this.taskDeploy.show = false
    },
    orderDeployFinish () {
      this.orderDeploy.show = false
      this.$message.success('提交成功！')
      this.getOrder()
    },
    orderViewClose () {
      this.orderView.show = false
    },
    orderDeployClose () {
      this.orderDeploy.show = false
    },
    taskDeployOpen (row) {
      this.taskDeploy.data = row
      this.taskDeploy.show = true
    },
    taskUploadOpen (row) {
      this.taskUpload.data = row
      this.taskUpload.show = true
    },
    onChange (e) {
      if (e.target.value === 'a') {
        this.getTask()
      } else if (e.target.value === 'b') {
        this.getOrder()
      } else if (e.target.value === 'c') {
        this.getOverTimeList()
      }
    }
  }
}
</script>

<style scoped>

</style>
