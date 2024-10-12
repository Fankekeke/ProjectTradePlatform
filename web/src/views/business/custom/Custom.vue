<template>
  <a-card :bordered="false" class="card-area" style="width: 1200px;margin: 15px auto 0;">
    <a-page-header title="项目定制" sub-title="本该如此轻松">
      <template slot="extra">
        <a-button key="1" type="primary">
          联系客服
        </a-button>
      </template>
      <a-row type="flex" style="margin-top: 10px">
        <a-statistic title="Status" value="Pending" />
        <a-statistic
          title="Price"
          prefix="$"
          :value="568.08"
          :style="{
            margin: '0 32px',
          }"
        />
        <a-statistic title="Balance" prefix="$" :value="3345.08" />
      </a-row>
    </a-page-header>
  </a-card>
</template>

<script>
const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'Custom',
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      customInfo: null
    }
  },
  methods: {
  }
}
</script>

<style scoped>
  /deep/ .ant-page-header-heading-title {
    letter-spacing: 1px;
    font-family: SimHei;
  }
</style>
