<template>
  <div :class="[multipage === true ? 'multi-page':'single-page', 'not-menu-page', 'home-page']">
    <a-row :gutter="8" class="head-info">
      <a-card class="head-info-card" hoverable :bordered="false">
        <a-col :span="12">
          <div class="head-info-avatar">
            <img alt="头像" :src="avatar">
          </div>
          <div class="head-info-count">
            <div class="head-info-welcome">
              {{welcomeMessage}}
            </div>
            <div class="head-info-desc">
              <p>{{user.deptName ? user.deptName : '暂无部门'}} | {{user.roleName ? user.roleName : '暂无角色'}}</p>
            </div>
            <div class="head-info-time">上次登录时间：{{user.lastLoginTime ? user.lastLoginTime : '第一次访问系统'}}</div>
          </div>
        </a-col>
        <a-col :span="12">
          <div>
            <a-row class="more-info">
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4">
                <head-info title="今日IP" :content="todayIp" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="今日访问" :content="todayVisitCount" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="总访问量" :content="totalVisitCount" :center="false" />
              </a-col>
            </a-row>
          </div>
        </a-col>
      </a-card>
    </a-row>
    <div style="padding: 30px 0px">
      <a-spin :spinning="spinning">
        <a-row :gutter="8">
          <a-col :span="6">
            <a-skeleton :loading="spinning" active>
              <chart-card title="总收益" :total="`￥${allPrice}`">
                <a-tooltip title="总收益" slot="action">
                  <a-icon type="info-circle-o" />
                </a-tooltip>
                <div>
                  <trend v-if="!spinning" :type="yearPriceOnRate>0" term="年同比" style="margin-right: 16px;" :percentage="Math.abs(yearPriceOnRate)"/>
                  <trend v-if="!spinning" :type="monthPriceOnRate>0" term="月环比" style="margin-right: 16px;" :percentage="Math.abs(monthPriceOnRate)"/>
                </div>
                <template slot="footer">月支出<span>{{`￥ ${expensesMonthPrice}`}}</span></template>
              </chart-card>
            </a-skeleton>
          </a-col>
          <a-col :span="6">
            <a-skeleton :loading="spinning" active>
              <chart-card title="年进度" :total="`${yearProgress}%`">
                <a-tooltip title="对比日期为今年的1月1日至来年的1月1日" slot="action">
                  <a-icon type="info-circle-o" />
                </a-tooltip>
                <div>
                  <mini-progress color="rgb(19, 194, 194)" :target="yearProgress" :percentage="yearProgress" height="8px" />
                </div>
                <template slot="footer">已过天数 <span>{{dayPassed}}</span></template>
              </chart-card>
            </a-skeleton>
          </a-col>
          <a-col :span="6">
            <a-skeleton :loading="spinning" active>
              <chart-card title="访问量" :total="totalVisitCount">
                <a-tooltip title="访问量" slot="action">
                  <a-icon type="info-circle-o" />
                </a-tooltip>
                <div>
                  <mini-area :data="miniAreaData" />
                </div>
                <template slot="footer">今日访问量<span> {{ todayVisitCount }}</span></template>
              </chart-card>
            </a-skeleton>
          </a-col>
          <a-col :span="6">
            <a-skeleton :loading="spinning" active>
              <chart-card title="交易量" :total="nonOrderNum + orderNum">
                <a-tooltip title="指标说明" slot="action">
                  <a-icon type="info-circle-o" />
                </a-tooltip>
                <div>
                  <mini-bar :data="nonCustomizedRevenueData" />
                </div>
                <template slot="footer">
                  <trend v-if="!spinning" :type="orderNumMonthRate>0" term="月环比" style="margin-right: 16px;" :percentage="Math.abs(orderNumMonthRate)"/>
                  <trend v-if="!spinning" :type="orderNumDayRate>0" term="日环比" style="margin-right: 16px;" :percentage="Math.abs(orderNumDayRate)"/>
                </template>
              </chart-card>
            </a-skeleton>
          </a-col>
        </a-row>
      </a-spin>
    </div>
    <a-row :gutter="8">
      <a-col :span="8" class="visit-count-wrapper">
        <a-spin :spinning="spinning">
          <div style="background: #ECECEC; padding: 30px">
            <a-card hoverable class="visit-count">
              <apexchart ref="count" type=bar height=200 :options="chartOptions" :series="series" />
            </a-card>
          </div>
          <div style="background: #ECECEC; padding: 30px;margin-top: 10px">
            <a-row :gutter="16">
              <a-col :span="12">
                <a-card hoverable>
                  <p style="font-size: 14px;color: #404040;"><b>本月支出</b></p>
                  <div style="color: green">
                    <a-icon type="arrow-down" />
                    <span style="font-size: 18px;font-weight: 500;"> {{ expensesMonthPrice.toFixed(2) }} </span><b>￥</b>
                  </div>
                </a-card>
              </a-col>
              <a-col :span="12">
                <a-card hoverable>
                  <p style="font-size: 14px;color: #404040;"><b>本月收入</b></p>
                  <div style="color: red">
                    <a-icon type="arrow-up" />
                    <span style="font-size: 18px;font-weight: 500;"> {{ orderAllMonthPrice.toFixed(2) }} </span><b>￥</b>
                  </div>
                </a-card>
              </a-col>
            </a-row>
          </div>
        </a-spin>
      </a-col>
      <a-col :span="16" class="project-wrapper">
        <a-spin :spinning="spinning">
          <div style="background: #ECECEC; padding: 30px">
            <a-card hoverable>
              <div style="height: 397px;padding: 8px 15px;overflow-y: auto;font-family: SimHei">
                <p style="font-size: 14px;color: #404040;font-weight: 600">进行中的项目</p>
                <a-tabs default-active-key="1" size="small" :tabBarStyle="{textAlign: 'right'}">
                  <a-tab-pane key="1" tab="非定制项目">
                    <a-result title="目前没有进行的项目!" v-if="nonProgressTaskData.length === 0">
                      <template #icon>
                        <a-icon type="frown" theme="twoTone" />
                      </template>
                    </a-result>
                    <a-row v-else>
                      <a-tabs
                        default-active-key="0"
                        tab-position="left"
                        :style="{ height: '280px' }"
                      >
                        <a-tab-pane v-for="(item, index) in nonProgressTaskData" :key="index" :tab="item.proName">
                          <a-row style="padding-left: 24px;padding-right: 24px;">
                            <a-col style="margin-bottom: 15px"><span style="font-size: 16px;font-weight: 550;color: #000c17">基本信息</span></a-col>
                            <a-col :span="8"><b class="item_title">项目名称：</b>{{ item.proName }}</a-col>
                            <a-col :span="8"><b class="item_title">订单编号：</b>{{ item.code !== null ? item.code : '- -' }}</a-col>
                            <a-col :span="8"><b class="item_title">联系方式：</b>{{ item.contact !== null ? item.contact : '- -' }}</a-col>
                          </a-row>
                          <br/>
                          <a-row style="padding-left: 24px;padding-right: 24px;">
                            <a-col :span="8"><b class="item_title">下单时间：</b>{{ item.orderTime !== null ? item.orderTime : '- -' }}</a-col>
                            <a-col :span="8"><b class="item_title">价 格：</b>
                              {{ item.orderPrice !== null ? ('￥' + item.orderPrice) : '- -' }}
                              <span v-if="item.needDeploy == 1"> + ￥30</span>
                            </a-col>
                            <a-col :span="8"><b class="item_title">负责人：</b>{{ item.authorName !== null ? item.authorName : '- -' }}</a-col>
                          </a-row>
                        </a-tab-pane>
                      </a-tabs>
                    </a-row>
                  </a-tab-pane>
                  <a-tab-pane key="2" tab="定制项目">
                    <a-result title="目前没有进行的项目!" v-if="progressTaskData.length === 0">
                      <template #icon>
                        <a-icon type="frown" theme="twoTone" />
                      </template>
                    </a-result>
                    <a-row v-else>
                      <a-tabs
                        default-active-key="0"
                        tab-position="left"
                        :style="{ height: '280px' }"
                      >
                        <a-tab-pane v-for="(item, index) in progressTaskData" :key="index" :tab="item.proName">
                          <a-row style="padding-left: 24px;padding-right: 24px;">
                            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基本信息</span></a-col>
                            <a-col :span="8"><b class="item_title">项目名称：</b>{{ item.proName }}</a-col>
                            <a-col :span="8"><b class="item_title">定制编号：</b>{{ item.code !== null ? item.code : '- -' }}</a-col>
                            <a-col :span="8"><b class="item_title">联系方式：</b>{{ item.contact !== null ? item.contact : '- -' }}</a-col>
                          </a-row>
                          <br/>
                          <a-row style="padding-left: 24px;padding-right: 24px;">
                            <a-col :span="8"><b class="item_title">申请时间：</b>{{ item.createDate !== null ? item.createDate : '- -' }}</a-col>
                            <a-col :span="8"><b class="item_title">定制价格：</b>{{ item.amount !== null ? ('￥' + item.amount) : '- -' }}</a-col>
                            <a-col :span="8"><b class="item_title">定 金：</b>{{ item.deposit !== null ? ('￥' + item.deposit) : '- -' }}</a-col>
                          </a-row>
                          <br/>
                          <a-row style="padding-left: 24px;padding-right: 24px;">
                            <a-col :span="8"><b class="item_title">开始时间：</b>{{ item.startDate !== null ? item.startDate : '- -' }}</a-col>
                            <a-col :span="8"><b class="item_title">结束时间：</b>{{ dateAdd(item.startDate, item.cycle) }}</a-col>
                            <a-col :span="8"><b class="item_title">项目周期：</b>{{ item.cycle !== null ? (item.cycle + '天') : '- -' }}</a-col>
                          </a-row>
                          <br/>
                          <a-row style="padding-left: 24px;padding-right: 24px;">
                            <a-col :span="8"><b class="item_title">剩余时间：</b>{{ timeDifference(item.startDate, item.cycle) }}</a-col>
                          </a-row>
                        </a-tab-pane>
                      </a-tabs>
                    </a-row>
                  </a-tab-pane>
                </a-tabs>
              </div>
            </a-card>
          </div>
        </a-spin>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import { ChartCard, MiniArea, MiniBar, MiniProgress, Trend, Bar } from '@/components'
import HeadInfo from '@/views/common/HeadInfo'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'HomePage',
  components: {HeadInfo, Trend, ChartCard, MiniProgress, MiniArea, MiniBar},
  data () {
    return {
      spinning: false,
      series: [],
      chartOptions: {
        chart: {
          toolbar: {
            show: false
          }
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '35%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        fill: {
          opacity: 1

        }
      },
      todayIp: '',
      todayVisitCount: '',
      totalVisitCount: '',
      userRole: '',
      userDept: '',
      lastLoginTime: '',
      welcomeMessage: '',
      miniAreaData: [],
      miniBarData: [],
      yearProgress: 0,
      dayPassed: 0,
      toMonth: {
        income: 0,
        expenses: 0
      },
      nonOrderPrice: 0,
      orderNumMonthRate: 0,
      monthPriceOnRate: 0,
      nonCustomizedRevenueData: [],
      orderNum: 0,
      yearPriceOnRate: 0,
      orderNumDayRate: 0,
      nonOrderMonthPrice: 0,
      orderAllMonthPrice: 0,
      allPrice: 0,
      expensesMonthPrice: 0,
      nonOrderNum: 0,
      orderMonthPrice: 0,
      orderPrice: 0,
      progressTaskData: [],
      nonProgressTaskData: []
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    }),
    avatar () {
      return `static/avatar/${this.user.avatar}`
    }
  },
  methods: {
    dateAdd(startDate, day) {
      return moment(startDate).add(day, 'days').format('YYYY-MM-DD')
    },
    timeDifference(startDate, day) {
      let m1 = moment()
      let m2 = moment(moment(startDate).add(day, 'days').format('YYYY-MM-DD'))
      let time = m2.diff(m1, 'day')
      if (time >= 0) {
        return time + '天'
      } else {
        return '已超时' + Math.abs(time) + '天'
      }
    },
    getHomeData () {
      this.spinning = true
      this.$get("/statistics/home", {roleFlag: 0}).then((r) => {
        this.nonOrderPrice = r.data.nonOrderPrice
        this.orderNumMonthRate = r.data.orderNumMonthRate
        this.monthPriceOnRate = r.data.monthPriceOnRate
        this.nonCustomizedRevenueData = r.data.nonCustomizedRevenueData
        this.orderNum = r.data.orderNum
        this.yearPriceOnRate = r.data.yearPriceOnRate
        this.orderNumDayRate = r.data.orderNumDayRate
        this.nonOrderMonthPrice = r.data.nonOrderMonthPrice
        this.orderAllMonthPrice = r.data.orderAllMonthPrice
        this.allPrice = r.data.allPrice
        this.expensesMonthPrice = r.data.expensesMonthPrice
        this.nonOrderNum = r.data.nonOrderNum
        this.orderMonthPrice = r.data.orderMonthPrice
        this.orderPrice = r.data.orderPrice
        this.progressTaskData = r.data.progressTaskData
        this.nonProgressTaskData = r.data.nonProgressTaskData
        setTimeout(() => {
          this.spinning = false
        }, 500)
      })
    },
    welcome () {
      const date = new Date()
      const hour = date.getHours()
      let time = hour < 6 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour <= 18 ? '下午好' : '晚上好')))
      let welcomeArr = [
        '喝杯咖啡休息下吧☕',
        '要不要和朋友打局LOL',
        '要不要和朋友打局塞尔达',
        '几天没见又更好看了呢😍',
        '今天又写了几个Bug🐞呢',
        '今天在群里吹水了吗',
        '今天吃了什么好吃的呢',
        '今天您微笑了吗😊',
        '今天帮助别人解决问题了吗',
        '准备吃些什么呢',
        '周末要不要去看电影？'
      ]
      let index = Math.floor((Math.random() * welcomeArr.length))
      return `${time}，${this.user.username}，${welcomeArr[index]}`
    },
    statisticsYear () {
      // 设置年进度
      let day = moment().diff(moment().startOf('year'), 'day')
      let dayCount = 0
      for (let i = 1; i < 13; i++) {
        dayCount += moment().month(i).daysInMonth()
      }
      this.dayPassed = day // 已过天数
      this.yearProgress = Math.round(day / dayCount * 100) // 年进度
    }
  },
  mounted () {
    this.getHomeData()
    this.welcomeMessage = this.welcome()
    this.statisticsYear()
    this.$get(`index/${this.user.username}`).then((r) => {
      let data = r.data.data
      this.todayIp = data.todayIp
      this.todayVisitCount = data.todayVisitCount
      this.totalVisitCount = data.totalVisitCount
      let sevenVisitCount = []
      let dateArr = []
      for (let i = 6; i >= 0; i--) {
        let time = moment().subtract(i, 'days').format('MM-DD')
        let contain = false
        this.miniAreaData = data.lastSevenVisitCount
        for (let o of data.lastSevenVisitCount) {
          if (o.days === time) {
            contain = true
            sevenVisitCount.push(o.count)
          }
        }
        if (!contain) {
          sevenVisitCount.push(0)
        }
        dateArr.push(time)
      }
      let sevenUserVistCount = []
      for (let i = 6; i >= 0; i--) {
        let time = moment().subtract(i, 'days').format('MM-DD')
        let contain = false
        for (let o of data.lastSevenUserVisitCount) {
          if (o.days === time) {
            contain = true
            sevenUserVistCount.push(o.count)
          }
        }
        if (!contain) {
          sevenUserVistCount.push(0)
        }
      }
      this.$refs.count.updateSeries([
        {
          name: '您',
          data: sevenUserVistCount
        },
        {
          name: '总数',
          data: sevenVisitCount
        }
      ], true)
      this.$refs.count.updateOptions({
        xaxis: {
          categories: dateArr
        },
        title: {
          text: '近七日系统访问记录',
          align: 'left'
        }
      }, true, true)
    }).catch((r) => {
      console.error(r)
      this.$message.error('获取首页信息失败')
    })
  }
}
</script>
<style lang="less">
  .item_title {
    font-size: 13px;
  }
  .home-page {
    .head-info {
      margin-bottom: .5rem;
      .head-info-card {
        padding: .5rem;
        border-color: #f1f1f1;
        .head-info-avatar {
          display: inline-block;
          float: left;
          margin-right: 1rem;
          img {
            width: 5rem;
            border-radius: 2px;
          }
        }
        .head-info-count {
          display: inline-block;
          float: left;
          .head-info-welcome {
            font-size: 1.05rem;
            margin-bottom: .1rem;
          }
          .head-info-desc {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
            p {
              margin-bottom: 0;
            }
          }
          .head-info-time {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
          }
        }
      }
    }
    .count-info {
      .visit-count-wrapper {
        padding-left: 0 !important;
        .visit-count {
          padding: .5rem;
          border-color: #f1f1f1;
          .ant-card-body {
            padding: .5rem 1rem !important;
          }
        }
      }
      .project-wrapper {
        padding-right: 0 !important;
        .project-card {
          border: none !important;
          .ant-card-head {
            border-left: 1px solid #f1f1f1 !important;
            border-top: 1px solid #f1f1f1 !important;
            border-right: 1px solid #f1f1f1 !important;
          }
          .ant-card-body {
            padding: 0 !important;
            table {
              width: 100%;
              td {
                width: 50%;
                border: 1px solid #f1f1f1;
                padding: .6rem;
                .project-avatar-wrapper {
                  display:inline-block;
                  float:left;
                  margin-right:.7rem;
                  .project-avatar {
                    color: #42b983;
                    background-color: #d6f8b8;
                  }
                }
              }
            }
          }
          .project-detail {
            display:inline-block;
            float:left;
            text-align:left;
            width: 78%;
            .project-name {
              font-size:.9rem;
              margin-top:-2px;
              font-weight:600;
            }
            .project-desc {
              color:rgba(0, 0, 0, 0.45);
              p {
                margin-bottom:0;
                font-size:.6rem;
                white-space:normal;
              }
            }
          }
        }
      }
    }
  }
</style>
