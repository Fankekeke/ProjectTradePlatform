package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.common.utils.FileDownload;
import com.fate.archer.cos.dao.UserInfoMapper;
import com.fate.archer.cos.entity.*;
import com.fate.archer.cos.dao.OrderInfoMapper;
import com.fate.archer.cos.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.archer.system.dao.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final IEvaluationService evaluationService;

    private final IDeployProcessService deployProcessService;

    private final IDeployEvaluateService deployEvaluateService;

    private final IProjectInfoService projectInfoService;

    private final UserInfoMapper userInfoMapper;

    @Override
    public IPage<OrderInfo> getOrderInfoByPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        return baseMapper.getOrderInfoByPage(page, orderInfo);
    }

    @Override
    public OrderInfo getProjectFileByExchangeCode(String code) {
        return baseMapper.getProjectFileByExchangeCode(code);
    }

    @Override
    public LinkedHashMap<String, Object> orderInfoDetailed(String orderCode) {
        // 订单信息
        LinkedHashMap<String, Object> orderInfo = baseMapper.orderInfoDetailed(orderCode);
        // 订单评价
        Evaluation evaluation = evaluationService.getOne(Wrappers.<Evaluation>lambdaQuery().eq(Evaluation::getOrderCode, orderCode));
        // 部署流程
        List<DeployProcess> deployProcesses = deployProcessService.list(Wrappers.<DeployProcess>lambdaQuery().eq(DeployProcess::getOrderCode, orderCode));
        // 部署审核
        DeployEvaluate deployEvaluate = deployEvaluateService.getOne(Wrappers.<DeployEvaluate>lambdaQuery().eq(DeployEvaluate::getOrderCode, orderCode));
        return new LinkedHashMap<String, Object>() {
            {
                put("orderInfo", orderInfo);
                put("evaluation", evaluation);
                put("deployProcesse", deployProcesses);
                put("deployEvaluate", deployEvaluate);
            }
        };
    }

    @Override
    public IPage<OrderInfo> orderHistoryByUserCodePage(Page<OrderInfo> page, String userCode) {
        return baseMapper.orderHistoryByUserCodePage(page, userCode);
    }

    @Override
    public LinkedHashMap<String, Object> orderStatistics(String startDate, String endDate, Integer type) {
        List<LinkedHashMap<String, Object>> orderStatisticsData = baseMapper.orderStatistics(startDate, endDate, type);
        orderStatisticsData.forEach(item -> {
            item.put("orderPrice", new BigDecimal(item.get("orderPrice").toString()).add(new BigDecimal(item.get("needDeploy").toString()).multiply(BusinessConstant.NEED_DEPLOY_PRICE)));
        });
        return null;
    }

    @Override
    public LinkedHashMap<String, Object> homePageData(Integer roleFlag) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 近期非定制收益统计
        List<LinkedHashMap<String, Object>> nonCustomizedRevenueData = baseMapper.nonCustomizedRevenue();
        nonCustomizedRevenueData.forEach(item -> {
            item.put("orderPrice", new BigDecimal(item.get("orderPrice").toString()).add(BusinessConstant.NEED_DEPLOY_PRICE.multiply(new BigDecimal(item.get("deploy").toString()))));
        });
        LinkedHashMap<String, Object> allData = baseMapper.moreAll();
        // 总定制交易量
        Integer orderNum = Integer.parseInt(allData.get("num").toString());
        // 总非定制交易量
        Integer nonOrderNum = Integer.parseInt(allData.get("nonNum").toString());
        // 交易量统计
        LinkedHashMap<String, Object> orderNumRateData = baseMapper.orderNumRate();
        // 今日交易量
        int todayNum = Integer.parseInt(orderNumRateData.get("todayNum").toString());
        // 昨日交易量
        int yesterdayNum = Integer.parseInt(orderNumRateData.get("yesterdayNum").toString());
        // 本月交易量
        int currentMonthNum = Integer.parseInt(orderNumRateData.get("currentMonthNum").toString());
        // 上月交易量
        int lastMonthNum = Integer.parseInt(orderNumRateData.get("lastMonthNum").toString());
        // 交易量-月环比
        BigDecimal orderNumMonthRate;
        if (lastMonthNum == 0) {
            orderNumMonthRate = new BigDecimal(100);
        } else {
            orderNumMonthRate = new BigDecimal(((currentMonthNum - lastMonthNum) / lastMonthNum * 100));
        }
        // 交易量-日环比
        BigDecimal orderNumDayRate;
        if (yesterdayNum == 0) {
            orderNumDayRate = new BigDecimal(100);
        } else {
            orderNumDayRate = new BigDecimal(((todayNum - yesterdayNum) / yesterdayNum * 100));
        }
        // 非定制项目总收益
        BigDecimal nonOrderPrice = new BigDecimal(allData.get("nonPrice").toString());
        // 定制项目总收益
        BigDecimal orderPrice = new BigDecimal(allData.get("price").toString());
        // 总收益
        BigDecimal allPrice = nonOrderPrice.add(orderPrice);
        // 本月定制收益
        BigDecimal orderMonthPrice = baseMapper.customIncomeMonth(DateUtil.formatDate(new Date()));
        // 本月非定制收益
        BigDecimal nonOrderMonthPrice = baseMapper.incomeMonth(null, DateUtil.formatDate(new Date()));
        // 本月收入
        BigDecimal orderAllMonthPrice = orderMonthPrice.add(nonOrderMonthPrice);
        // 去年本月定制收益
        BigDecimal lastYearMonthPrice = baseMapper.customIncomeMonth(DateUtil.offset(new Date(), DateField.YEAR, -1).toString());
        // 去年本月非定制收益
        BigDecimal nonLastYearMonthPrice = baseMapper.incomeMonth(null, DateUtil.offset(new Date(), DateField.YEAR, -1).toString());
        // 去年本月总收益
        BigDecimal lastYearMonthAllPrice = lastYearMonthPrice.add(nonLastYearMonthPrice);
        // 上月定制收益
        BigDecimal lastMonthPrice = baseMapper.customIncomeMonth(DateUtil.offset(new Date(), DateField.MONTH, -1).toString());
        // 上月非定制收益
        BigDecimal nonYearMonthPrice = baseMapper.incomeMonth(null, DateUtil.offset(new Date(), DateField.MONTH, -1).toString());
        // 上月总收益
        BigDecimal lastMonthAllPrice = lastMonthPrice.add(nonYearMonthPrice);
        // 收益-年同比
        BigDecimal yearPriceOnRate;
        if (lastYearMonthAllPrice.compareTo(BigDecimal.ZERO) == 0) {
            yearPriceOnRate = new BigDecimal(100);
        } else {
            yearPriceOnRate = orderAllMonthPrice.subtract(lastYearMonthAllPrice).divide(lastYearMonthAllPrice, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            yearPriceOnRate = orderAllMonthPrice.subtract(lastYearMonthAllPrice).divide(lastYearMonthAllPrice, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }
        // 收益-月环比
        BigDecimal monthPriceOnRate;
        if (lastMonthAllPrice.compareTo(BigDecimal.ZERO) == 0) {
            monthPriceOnRate = new BigDecimal(100);
        } else {
            monthPriceOnRate = orderAllMonthPrice.subtract(lastMonthAllPrice).divide(lastMonthAllPrice, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }
        // 本月支出
        BigDecimal expensesMonthPrice = baseMapper.expensesMonth(DateUtil.formatDate(new Date()));

        // 进行中的定制项目
        List<LinkedHashMap<String, Object>> progressTaskData = baseMapper.progressTask(null);
        // 进行中的非定制项目
        List<LinkedHashMap<String, Object>> nonProgressTaskData = baseMapper.nonProgressTask(null);

        // 近期非定制收益统计
        result.put("nonCustomizedRevenueData", nonCustomizedRevenueData);
        // 总定制交易量
        result.put("orderNum", orderNum);
        // 总非定制交易量
        result.put("nonOrderNum", nonOrderNum);
        // 交易量-月环比
        result.put("orderNumMonthRate", orderNumMonthRate);
        // 交易量-日环比
        result.put("orderNumDayRate", orderNumDayRate);
        // 非定制项目总收益
        result.put("nonOrderPrice", nonOrderPrice);
        // 定制项目总收益
        result.put("orderPrice", orderPrice);
        // 总收益
        result.put("allPrice", allPrice);
        // 本月定制收益
        result.put("orderMonthPrice", orderMonthPrice);
        // 本月非定制收益
        result.put("nonOrderMonthPrice", nonOrderMonthPrice);
        // 本月总收入
        result.put("orderAllMonthPrice", orderAllMonthPrice);
        // 收益-年同比
        result.put("yearPriceOnRate", yearPriceOnRate);
        // 收益-月环比
        result.put("monthPriceOnRate", monthPriceOnRate);
        // 本月支出
        result.put("expensesMonthPrice", expensesMonthPrice);

        // 今日交易量
        result.put("todayNum", todayNum);
        // 昨日交易量
        result.put("yesterdayNum", yesterdayNum);
        // 本月交易量
        result.put("currentMonthNum", currentMonthNum);
        // 上月交易量
        result.put("lastMonthNum", lastMonthNum);

        // 去年本月总收益
        result.put("lastYearMonthAllPrice", lastYearMonthAllPrice);
        // 上月总收益
        result.put("lastMonthAllPrice", lastMonthAllPrice);

        // 进行中的定制项目
        result.put("progressTaskData", progressTaskData);
        // 进行中的非定制项目
        result.put("nonProgressTaskData", nonProgressTaskData);

        return result;
    }

    /**
     * 根据开发者ID获取订单信息
     *
     * @param teamId 开发者Id
     * @return 订单信息
     */
    @Override
    public List<LinkedHashMap<String, Object>> orderListByDevelopers(Integer teamId) {
        return baseMapper.orderListByDevelopers(teamId);
    }

    /**
     * 根据用户编号分页获取订单信息
     *
     * @param page     分页镀锡
     * @param userCode 用户编号
     * @return 订单信息
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> orderListByUser(Page<?> page, String userCode) {
        return baseMapper.orderListByUser(page, userCode);
    }

    /**
     * 根据订单编号获取详细信息
     *
     * @param orderCode 订单编号
     * @return 订单详细信息
     */
    @Override
    public LinkedHashMap<String, Object> orderInfoByCode(String orderCode) {
        LinkedHashMap<String, Object> result = baseMapper.orderInfoByCode(orderCode);
        result.put("deployProcess", baseMapper.orderDeployProcess(orderCode));
        result.put("evaluation", evaluationService.getOne(Wrappers.<Evaluation>lambdaQuery().eq(Evaluation::getOrderCode, orderCode)));
        return result;
    }

    /**
     * 门户端用户下单
     *
     * @param orderInfo 订单信息
     * @return 返回结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean placeOrderFinished(OrderInfo orderInfo) throws Exception {
        // 设置订单编号
        orderInfo.setCode("ORD-" + System.currentTimeMillis());

        // 获取邀请信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, orderInfo.getUserCode()));
        if (userInfo == null) {
            throw new FebsException("用户信息不存在！");
        }
        // 设置订单状态
        orderInfo.setOrderStatus(BusinessConstant.FINISHED_ORDER_STATUS_PAID);
        // 下单时间
        orderInfo.setOrderTime(DateUtil.formatDate(new Date()));

        // 订单流程
        deployProcessService.deployProcessSave(orderInfo.getCode());
        // 更新项目成交量
        projectInfoService.projectTurnover(orderInfo.getProCode());
        return this.save(orderInfo);
    }

    /**
     * 成品项目下载
     *
     * @param orderCode 订单编号
     * @param userCode  用户编号
     * @param response  response
     * @throws Exception 异常
     */
    @Override
    public void download(String orderCode, String userCode, HttpServletResponse response) throws Exception {
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        if (orderInfo == null) {
            throw new FebsException("此订单不存在！");
        }

        int count = this.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getUserCode, userCode).eq(OrderInfo::getCode, orderInfo));
        if (count <= 0) {
            throw new FebsException("此用户无此订单信息！");
        }

        // 获取成品项目
        ProjectInfo project = projectInfoService.getOne(Wrappers.<ProjectInfo>lambdaQuery().eq(ProjectInfo::getCode, orderInfo.getProCode()));
        if (project == null) {
            throw new FebsException("成品项目不存在！");
        }

        FileDownload.download(BusinessConstant.FILEADDRESS + "file/", project.getCode(), response);
    }

}
