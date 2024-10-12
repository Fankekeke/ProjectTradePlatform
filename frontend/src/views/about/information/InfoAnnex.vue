<template>
  <a-modal v-model="show" title="附件信息" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px">
      <a-table :columns="annexColumns" :data-source="annexData">
        <template slot="operation" slot-scope="text, record">
          <a-popover title="" trigger="click" placement="left">
            <template slot="content">
              <div style="font-size: 13px;margin-top: 12px;width: 430px">
                <a-row style="padding-left: 16px;padding-right: 16px;">
                  <a-col :span="12"><b>附件编号：</b>
                    <a-tooltip>
                      <template slot="title">
                        {{ record.code }}
                      </template>
                      {{ record.code.slice(0, 8) }} ...
                    </a-tooltip>
                  </a-col>
                  <a-col :span="12"><b>附件名称：</b>
                    <a-tooltip>
                      <template slot="title">
                        {{ record.annexName }}
                      </template>
                      {{ record.annexName.slice(0, 8) }} ...
                    </a-tooltip>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 16px;padding-right: 16px;">
                  <a-col :span="12"><b>附件类型：</b>
                    <span v-if="record.annexType == 1">论文</span>
                    <span v-if="record.annexType == 2">PPT</span>
                    <span v-if="record.annexType == 3">任务书</span>
                    <span v-if="record.annexType == 4">开题报告</span>
                  </a-col>
                  <a-col :span="12"><b>附件价格：</b>￥{{ record.annexPrice }}</a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 16px;padding-right: 16px;">
                  <a-col :span="12"><b>上传时间：</b>
                    <a-tooltip>
                      <template slot="title">
                        {{ record.uploadDate }}
                      </template>
                      {{ record.uploadDate.slice(0, 10) }} ...
                    </a-tooltip>
                  </a-col>
                  <a-col :span="12"><b>审核状态：</b>
                    <a-tag v-if="record.status == 1">正在审核</a-tag>
                    <a-tag v-if="record.status == 2" color="green">审核通过</a-tag>
                    <a-tag v-if="record.status == 3" color="red">不通过</a-tag>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 16px;padding-right: 16px;" v-if="record.pic !== null">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">图册</span></a-col>
                  <a-popover title="" v-for="(item, index) in record.pic.split(',')" :key="index">
                    <template slot="content">
                      <a-avatar shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/' + item"/>
                    </template>
                    <a-avatar shape="square" size="large" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/' + item" style="margin-right: 10px"/>
                  </a-popover>
                </a-row>
              </div>
            </template>
            <a-icon type="cloud" title="查 看" style="margin-right: 15px"></a-icon>
          </a-popover>
        </template>
      </a-table>
    </div>
  </a-modal>
</template>

<script>
import {mapState} from "vuex"

export default {
  name: 'InfoAnnex',
  props: {
    annexShow: {
      type: Boolean,
      default: false
    },
    annexData: {
      type: Array
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.annexShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      annexColumns: [{
        title: '附件名称',
        dataIndex: 'annexName',
        ellipsis: true
      }, {
        title: '附件类型',
        dataIndex: 'annexType',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="cyan">论文</a-tag>
            case 2:
              return <a-tag color="cyan">PPT</a-tag>
            case 3:
              return <a-tag color="cyan">任务书</a-tag>
            case 4:
              return <a-tag color="cyan">开题报告</a-tag>
            default:
              return text
          }
        }
      }, {
        title: '上传时间',
        dataIndex: 'uploadDate',
        ellipsis: true
      }, {
        title: '当前状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag>正在审核</a-tag>
            case 2:
              return <a-tag color="green">审核通过</a-tag>
            case 3:
              return <a-tag color="red">不通过</a-tag>
            default:
              return text
          }
        }
      }, {
        title: '详情',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  methods: {
    onClose() {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
