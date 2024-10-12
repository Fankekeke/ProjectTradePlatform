<template>
  <a-card :bordered="false" class="card-area" style="width: 1200px;margin: 15px auto 0;">
    <a-alert message="欢迎你的加入" type="success" show-icon style="margin-bottom: 20px"/>
    <a-row :gutter="20">
      <a-col :span="6" v-for="(item, index) in teamList" :key="index" style="margin-bottom: 30px">
        <a-card hoverable :bordered="true">
          <img
            slot="cover"
            alt="example"
            :src="item.img"/>
          <a-card-meta :title="item.name">
            <template slot="description">
              {{ item.content }}
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'Team',
  data () {
    return {
      teamList: [{
        img: 'https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png',
        name: '斗佛',
        content: '---------------'
      }, {
        img: 'https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png',
        name: '小南',
        content: '---------------'
      }, {
        img: 'https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png',
        name: '喜宝',
        content: '---------------'
      }, {
        img: 'https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png',
        name: '裴总',
        content: '---------------'
      }, {
        img: 'https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png',
        name: '文松',
        content: '---------------'
      }, {
        img: 'https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png',
        name: '悲伤的橘子树',
        content: '---------------'
      }]
    }
  }
}
</script>

<style scoped>
  /deep/ .ant-card-meta-title {
    letter-spacing: 1px;
  }
</style>
