<template>
  <a-select
    allowClear
    show-search
    :value="value"
    option-label-prop="label"
    placeholder="选择项目"
    :show-arrow="false"
    :filter-option="false"
    :not-found-content="null"
    @search="fetchProject"
    @change="handleChange"
  >
    <a-select-option v-for="(item, index) in data" :key="index" :value="item.code" :label="item.proName">
      <a-row>
        <a-col :span="4">
          <a-avatar style="margin-right: 20px" shape="square" :size="40" icon="user" :src="'http://127.0.0.1:9528/imagesWeb/' + item.avatar" />
        </a-col>
        <a-col :span="20">
          <a-row>
            <a-col><span style="font-weight: 600">{{item.proName}}</span></a-col>
            <a-col>
               <span style="font-size: 10px">
                 <a-tag v-if="item.proType == 1">仅后台</a-tag>
                 <a-tag v-if="item.proType == 2">前后台</a-tag>
                 <a-tag v-if="item.proType == 3">小程序</a-tag>
                 <a-tag v-if="item.proType == 4">安卓</a-tag>
               </span>
              <span style="font-size: 10px">{{item.authorName !== null ? item.authorName : '无作者'}}</span>
            </a-col>
          </a-row>
        </a-col>
      </a-row>
    </a-select-option>
  </a-select>
</template>

<script>
import debounce from 'lodash/debounce'
export default {
  name: 'SingleChoice',
  computed: {
    checked() {
      return this.value;
    }
  },
  model: {
    prop: "value",
    event: "update"
  },
  data() {
    this.lastFetchId = 0;
    this.fetchProject = debounce(this.fetchProject, 800);
    return {
      data: [],
      value: []
    }
  },
  watch: {
    checked: function(newVal, oldVal) {
      this.$emit("update", newVal)
    }
  },
  methods: {
    fetchProject(value) {
      console.log('fetching user', value)
      this.data = []
      if (value === '') {
        value = '系统'
      }
      this.$get(`/cos/project-info/fuzzy/${value}`).then((r) => {
        this.data = r.data.data
      })
    },
    handleChange(value) {
      Object.assign(this, {
        value,
        data: []
      })
    },
  },
}
</script>

<style scoped>

</style>
