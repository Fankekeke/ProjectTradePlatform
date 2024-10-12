<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart
      height="254"
      :data="data"
      :forceFit="true"
      :padding="['auto', 'auto', '40', '50']">
      <v-tooltip />
      <v-axis />
      <v-bar :position="position"/>
    </v-chart>
  </div>
</template>

<script>
export default {
  name: 'Bar',
  props: {
    title: {
      type: String,
      default: ''
    },
    data: {
      type: Array,
      default: () => {
        return []
      }
    },
    position: {
      type: String,
      default: 'date*money'
    }
    // scale: {
    //   type: Array,
    //   default: () => {
    //     return [{
    //       dataKey: 'date'
    //     }, {
    //       dataKey: 'money',
    //       min: 1,
    //       max: 22
    //     }]
    //   }
    // },
    // tooltip: {
    //   type: Array,
    //   default: () => {
    //     return [
    //       'date*money',
    //       (name, value) => ({
    //         name: name,
    //         value: value
    //       })
    //     ]
    //   }
    // }
  },
  data () {
    return {
    }
  }
}
</script>
