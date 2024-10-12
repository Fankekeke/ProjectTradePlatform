package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页查询订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 订单信息
     */
    IPage<OrderInfo> getOrderInfoByPage(Page page, OrderInfo orderInfo);

    /**
     * 使用兑换码兑换
     *
     * @param code 兑换码
     * @return 订单信息
     */
    OrderInfo getProjectFileByExchangeCode(String code);

    /**
     * 订单详情
     *
     * @param orderCode 订单编号
     * @return 订单详情
     */
    LinkedHashMap<String, Object> orderInfoDetailed(String orderCode);

    /**
     * 用户历史订单
     *
     * @param page     分页对象
     * @param userCode 用户编号
     * @return 用户历史订单
     */
    IPage<OrderInfo> orderHistoryByUserCodePage(Page page, String userCode);

    /**
     * 订单统计-主页
     *
     * @param startDate 开始时间
     * @param endDate   截至时间
     * @param type      类型
     * @return 订单统计
     */
    LinkedHashMap<String, Object> orderStatistics(String startDate, String endDate, Integer type);

    /**
     * 系统主页信息
     *
     * @param roleFlag 角色标识
     * @return 系统主页信息
     */
    LinkedHashMap<String, Object> homePageData(Integer roleFlag);

    /**
     * 根据开发者ID获取订单信息
     *
     * @param teamId 开发者Id
     * @return 订单信息
     */
    List<LinkedHashMap<String, Object>> orderListByDevelopers(Integer teamId);

    /**
     * 根据用户编号分页获取订单信息
     *
     * @param page     分页镀锡
     * @param userCode 用户编号
     * @return 订单信息
     */
    IPage<LinkedHashMap<String, Object>> orderListByUser(Page page, String userCode);

    /**
     * 根据订单编号获取详细信息
     *
     * @param orderCode 订单编号
     * @return 订单详细信息
     */
    LinkedHashMap<String, Object> orderInfoByCode(String orderCode);

    /**
     * 门户端用户下单
     *
     * @param orderInfo 订单信息
     * @return 返回结果
     * @throws Exception 异常
     */
    Boolean placeOrderFinished(OrderInfo orderInfo) throws Exception;

    /**
     * 成品项目下载
     *
     * @param orderCode 订单编号
     * @param userCode  用户编号
     * @param response  response
     * @throws Exception 异常
     */
    void download(String orderCode, String userCode, HttpServletResponse response) throws Exception;
}
