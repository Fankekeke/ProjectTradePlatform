<template>
  <a-card :bordered="false" class="card-area">
    <a-button style="margin-bottom: 20px" @click="distributeVisible = true">佣金分配</a-button>
    <a-row :gutter="20">
      <a-col :span="12" style="margin-bottom: 20px">
        <a-descriptions bordered>
          <a-descriptions-item label="分配类型" :span="1">
            <b>非定制项目</b>
          </a-descriptions-item>
          <a-descriptions-item label="开发者佣金" :span="1">
            {{customNot.developer}}%
          </a-descriptions-item>
          <a-descriptions-item label="平台抽成" :span="1">
            {{customNot.platform}}%
          </a-descriptions-item>
          <a-descriptions-item label="邀请人" :span="1">
            （无邀请人佣金）
          </a-descriptions-item>
          <a-descriptions-item label="备注" :span="2">
            - -
          </a-descriptions-item>
          <a-descriptions-item label="状态" :span="3">
            <a-badge status="processing" text="Running" />
          </a-descriptions-item>
        </a-descriptions>
      </a-col>
      <a-col :span="12" style="margin-bottom: 20px">
        <a-descriptions bordered>
          <a-descriptions-item label="分配类型" :span="1">
            <b>非定制项目（含邀请人）</b>
          </a-descriptions-item>
          <a-descriptions-item label="开发者佣金" :span="1">
            {{customNotInvite.developer}}%
          </a-descriptions-item>
          <a-descriptions-item label="平台抽成" :span="1">
            {{customNotInvite.platform}}%
          </a-descriptions-item>
          <a-descriptions-item label="邀请人" :span="1">
            {{customNotInvite.invite}}%
          </a-descriptions-item>
          <a-descriptions-item label="备注" :span="2">
            内部人员项目不会参与到开发者佣金里，平台占90%
          </a-descriptions-item>
          <a-descriptions-item label="状态" :span="3">
            <a-badge status="processing" text="Running" />
          </a-descriptions-item>
        </a-descriptions>
      </a-col>
      <a-col :span="12" style="margin-bottom: 20px">
        <a-descriptions bordered>
          <a-descriptions-item label="分配类型" :span="1">
            <b>定制项目</b>
          </a-descriptions-item>
          <a-descriptions-item label="开发者佣金" :span="1">
            {{custom.developer}}%
          </a-descriptions-item>
          <a-descriptions-item label="平台抽成" :span="1">
            {{custom.platform}}%
          </a-descriptions-item>
          <a-descriptions-item label="邀请人" :span="1">
            （无邀请人佣金）
          </a-descriptions-item>
          <a-descriptions-item label="备注" :span="2">
            - -
          </a-descriptions-item>
          <a-descriptions-item label="状态" :span="3">
            <a-badge status="processing" text="Running" />
          </a-descriptions-item>
        </a-descriptions>
      </a-col>
      <a-col :span="12" style="margin-bottom: 20px">
        <a-descriptions bordered>
          <a-descriptions-item label="分配类型" :span="1">
            <b>定制项目（含邀请人）</b>
          </a-descriptions-item>
          <a-descriptions-item label="开发者佣金" :span="1">
            {{customInvite.developer}}%
          </a-descriptions-item>
          <a-descriptions-item label="平台抽成" :span="1">
            {{customInvite.platform}}%
          </a-descriptions-item>
          <a-descriptions-item label="邀请人" :span="1">
            {{customInvite.invite}}%
          </a-descriptions-item>
          <a-descriptions-item label="备注" :span="2">
            开发者完成定制任务，管理员审核完成后佣金发放&nbsp;&nbsp;&nbsp;&nbsp;
          </a-descriptions-item>
          <a-descriptions-item label="状态" :span="3">
            <a-badge status="processing" text="Running" />
          </a-descriptions-item>
        </a-descriptions>
      </a-col>
    </a-row>
    <distribute :distributeVisible="distributeVisible" @close="distributeClose" @edit="distributeEdit"></distribute>
  </a-card>
</template>

<script>
import Distribute from './Distribute'
export default {
  name: 'brokerage',
  data () {
    return {
      distributeVisible: false,
      customNot: {
        developer: 75,
        platform: 25
      },
      customNotInvite: {
        developer: 70,
        platform: 20,
        invite: 10,
      },
      custom: {
        developer: 80,
        platform: 20
      },
      customInvite: {
        developer: 75,
        platform: 17,
        invite: 8,
      },
    }
  },
  mounted() {
    this.rateInfo()
  },
  components: {Distribute},
  methods: {
    rateInfo() {
      this.$get('/cos/rate-info/rate').then((r) => {
        r.data.customInvite.forEach(item => {
          switch (item.role) {
            case 'developer':
              this.customInvite.developer = item.rate
              break
            case 'platform':
              this.customInvite.platform = item.rate
              break
            case 'invite':
              this.customInvite.invite = item.rate
              break
          }
        })
        r.data.custom.forEach(item => {
          switch (item.role) {
            case 'developer':
              this.custom.developer = item.rate
              break
            case 'platform':
              this.custom.platform = item.rate
              break
          }
        })
        r.data.customNot.forEach(item => {
          switch (item.role) {
            case 'developer':
              this.customNot.developer = item.rate
              break
            case 'platform':
              this.customNot.platform = item.rate
              break
          }
        })
        r.data.customNotInvite.forEach(item => {
          switch (item.role) {
            case 'developer':
              this.customNotInvite.developer = item.rate
              break
            case 'platform':
              this.customNotInvite.platform = item.rate
              break
            case 'invite':
              this.customNotInvite.invite = item.rate
              break
          }
        })
      })
    },
    distributeEdit() {
      this.rateInfo()
      this.distributeVisible = false
      this.$message.success('修改成功')
    },
    distributeClose() {
      this.distributeVisible = false
    }
  }
}
</script>

<style scoped>

</style>
