<template>
  <a-modal
    :visible="distributeVisible"
    :confirm-loading="confirmLoading"
    :centered="true"
    :keyboard="false"
    :width="850"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-row>
      <a-col style="margin-bottom: 14px"><span style="font-size: 14px;font-weight: 650;color: #404040">非定制项目</span></a-col>
      <a-col>
        <a-card hoverable style="margin-bottom: 15px">
          <a-form>
            <a-row :gutter="20">
              <a-col :span="8">
                <a-form-item label="开发者收益">
                  <a-input-number
                    style="width: 100%"
                    v-model="customNot.developer"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="平台抽成">
                  <a-input-number
                    style="width: 100%"
                    v-model="customNot.platform"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </a-col>
    </a-row>
    <a-row>
      <a-col style="margin-bottom: 14px"><span style="font-size: 14px;font-weight: 650;color: #404040">非定制项目（含邀请人）</span></a-col>
      <a-col>
        <a-card hoverable style="margin-bottom: 15px">
          <a-form>
            <a-row :gutter="20">
              <a-col :span="8">
                <a-form-item label="开发者收益">
                  <a-input-number
                    style="width: 100%"
                    v-model="customNotInvite.developer"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="平台抽成">
                  <a-input-number
                    style="width: 100%"
                    v-model="customNotInvite.platform"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="邀请人收益">
                  <a-input-number
                    style="width: 100%"
                    v-model="customNotInvite.invite"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </a-col>
    </a-row>
    <a-row>
      <a-col style="margin-bottom: 14px"><span style="font-size: 14px;font-weight: 650;color: #404040">定制项目</span></a-col>
      <a-col>
        <a-card hoverable style="margin-bottom: 15px">
          <a-form>
            <a-row :gutter="20">
              <a-col :span="8">
                <a-form-item label="开发者收益">
                  <a-input-number
                    style="width: 100%"
                    v-model="custom.developer"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="平台抽成">
                  <a-input-number
                    style="width: 100%"
                    v-model="custom.platform"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </a-col>
    </a-row>
    <a-row>
      <a-col style="margin-bottom: 14px"><span style="font-size: 14px;font-weight: 650;color: #404040">定制项目（含邀请人）</span></a-col>
      <a-col>
        <a-card hoverable style="margin-bottom: 15px">
          <a-form>
            <a-row :gutter="20">
              <a-col :span="8">
                <a-form-item label="开发者收益">
                  <a-input-number
                    style="width: 100%"
                    v-model="customInvite.developer"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="平台抽成">
                  <a-input-number
                    style="width: 100%"
                    v-model="customInvite.platform"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="邀请人收益">
                  <a-input-number
                    style="width: 100%"
                    v-model="customInvite.invite"
                    :min="0"
                    :max="100"
                    :formatter="value => `${value}%`"
                    :parser="value => value.replace('%', '')"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </a-col>
    </a-row>
  </a-modal>
</template>

<script>
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 }
}
export default {
  name: 'Distribute',
  props: {
    distributeVisible: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    distributeVisible: function (data) {
      if (data) {
        this.rateInfo()
      }
    }
  },
  data () {
    return {
      formItemLayout,
      formLayout: 'inline',
      confirmLoading: false,
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
    handleOk () {
      this.confirmLoading = true
      let rateList = []
      rateList.push({role: 'developer', flag: 1, isInvite: 2, rate: this.custom.developer})
      rateList.push({role: 'platform', flag: 1, isInvite: 2, rate: this.custom.platform})
      rateList.push({role: 'developer', flag: 1, isInvite: 1, rate: this.customInvite.developer})
      rateList.push({role: 'platform', flag: 1, isInvite: 1, rate: this.customInvite.platform})
      rateList.push({role: 'invite', flag: 1, isInvite: 1, rate: this.customInvite.invite})
      rateList.push({role: 'developer', flag: 2, isInvite: 2, rate: this.customNot.developer})
      rateList.push({role: 'platform', flag: 2, isInvite: 2, rate: this.customNot.platform})
      rateList.push({role: 'developer', flag: 2, isInvite: 1, rate: this.customNotInvite.developer})
      rateList.push({role: 'platform', flag: 2, isInvite: 1, rate: this.customNotInvite.platform})
      rateList.push({role: 'invite', flag: 2, isInvite: 1, rate: this.customNotInvite.invite})
      rateList.forEach((rate, index) => {
        this.$put('/cos/rate-info', rate).then((r) => {
          console.log(index === rateList.length - 1)
          if (index === rateList.length - 1) {
            this.confirmLoading = false
            this.$emit('edit')
          }
        })
      })
    },
    handleCancel () {
      this.$emit("close")
    }
  }
}
</script>

<style scoped>

</style>
