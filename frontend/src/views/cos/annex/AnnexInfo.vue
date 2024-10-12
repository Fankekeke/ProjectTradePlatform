<template>
  <a-modal v-model="show" title="订单详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="annexData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>附件名称：</b>
          <a-tooltip>
            <template slot="title">
              {{ annexData.annexName }}
            </template>
            {{ annexData.annexName.slice(0, 8) }} ...
          </a-tooltip>
        </a-col>
        <a-col :span="8"><b>附件类型：</b>
          <span v-if="annexData.annexType == 1">论文 [{{ annexData.wordCount }}字]</span>
          <span v-if="annexData.annexType == 2">PPT</span>
          <span v-if="annexData.annexType == 3">任务书</span>
          <span v-if="annexData.annexType == 4">开题报告</span>
        </a-col>
        <a-col :span="8"><b>购买人：</b>
          {{ annexData.account }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>订单编号：</b>{{ annexData.code }}</a-col>
        <a-col :span="8"><b>订单价格：</b>{{ annexData.price !== null ? ('￥' + annexData.price) : '- -' }}</a-col>
        <a-col :span="8"><b>支付方式：</b>
          <a-tag v-if="annexData.paymentType == 1">
            支付宝
          </a-tag>
          <a-tag v-if="annexData.paymentType == 2">
            微信
          </a-tag>
          <a-tag v-if="annexData.paymentType == 3">
            平台下单
          </a-tag>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>作 者：</b>{{ annexData.players }}</a-col>
        <a-col :span="8"><b>下单时间：</b>{{ annexData.orderTime !== null ? annexData.orderTime : '- -' }}</a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备 注</span></a-col>
        <a-col :span="24">{{ annexData.remark }}</a-col>
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
  name: "AnnexInfo",
  props: {
    annexShow: {
      type: Boolean,
      default: false
    },
    annexData: {
      type: Object
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
  data() {
    return {
      loading: false
    }
  },
  watch: {
    annexShow: function (value) {

    }
  },
  methods: {
    handleCancel () {
      this.previewVisible = false;
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
