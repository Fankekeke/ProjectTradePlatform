<template>
  <div class="antv-chart-mini">
    <div class="chart-wrapper" :style="{ height: 46 }">
      <v-chart :force-fit="true" :height="height" :data="newDate" :padding="[36, 5, 18, 5]">
        <v-tooltip />
        <v-bar position="days*count" />
      </v-chart>
    </div>
  </div>
</template>

<script>
import moment from 'moment'

const tooltip = [
  'days*count',
  (days, count) => ({
    name: days,
    value: count
  })
]

const scale = [{
  dataKey: 'days',
  min: 2
}, {
  dataKey: 'count',
  title: '时间',
  min: 1,
  max: 30
}]

export default {
  name: 'MiniBar',
  props: {
    data: {
      required: true
    }
  },
  computed: {
    newDate () {
      let date = this.data.map(a => a.days)
      let newData = []
      for (let index = 0; index < 10; index++) {
        let days = moment().add(index * -1, 'days').format('YYYY-MM-DD')
        if (!date.includes(days)) {
          newData.push({
            days,
            count: 0
          })
        }
      }
      return [...this.data, ...newData]
    }
  },
  data () {
    return {
      tooltip,
      scale,
      height: 100
    }
  }
}
</script>

<style lang="less" scoped>
  @import "chart";
</style>
