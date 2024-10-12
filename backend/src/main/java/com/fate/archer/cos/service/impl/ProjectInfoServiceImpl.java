package com.fate.archer.cos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.common.utils.CloneUtils;
import com.fate.archer.common.utils.FebsUtil;
import com.fate.archer.cos.dao.OrderInfoMapper;
import com.fate.archer.cos.entity.AnnexInfo;
import com.fate.archer.cos.entity.OrderInfo;
import com.fate.archer.cos.entity.vo.ProductDetailVo;
import com.fate.archer.cos.entity.vo.ProductTradeWithSchoolVo;
import com.fate.archer.cos.entity.ProjectInfo;
import com.fate.archer.cos.dao.ProjectInfoMapper;
import com.fate.archer.cos.entity.UserInfo;
import com.fate.archer.cos.service.IOrderInfoService;
import com.fate.archer.cos.service.IProjectInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.archer.cos.service.IUserInfoService;
import com.fate.archer.system.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements IProjectInfoService {

    private final IUserInfoService userInfoService;

    private final OrderInfoMapper orderInfoMapper;

    @Override
    public List<LinkedHashMap<String, Object>> getProInfoTop(String key) {
        return baseMapper.getProInfoTop(key);
    }

    @Override
    public IPage<LinkedHashMap<String, Object>> getProjectPage(Page page, String key, Integer proType) {
        return baseMapper.getProjectPage(page, key, proType);
    }

    @Override
    public LinkedHashMap<String, Object> getProjectById(Integer id) {
        return baseMapper.getProjectById(id);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getProjectLess() {
        return baseMapper.getProjectLess();
    }

    @Override
    public List<LinkedHashMap<String, Object>> getProFuzzyQuery(String proName) {
        return baseMapper.getProFuzzyQuery(proName);
    }

    /**
     * 用户端项目分页查询
     *
     * @param page     分页对象
     * @param sortType 排序方式
     * @param key      关键字
     * @return 项目列表
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectProductList(Page page, Integer sortType, String key) {
        return baseMapper.selectProductList(page, sortType, key);
    }

    /**
     * 项目信息详情
     *
     * @param productCode 项目编号
     * @return 项目信息详情
     */
    @Override
    public ProductDetailVo productDetail(String productCode) throws Exception {
        // 项目信息
        ProjectInfo projectInfo = this.getOne(Wrappers.<ProjectInfo>lambdaQuery().eq(ProjectInfo::getCode, productCode));
        if (projectInfo == null) {
            throw new FebsException("成品项目不存在！");
        }
        // 添加项目访问量
        this.update(Wrappers.<ProjectInfo>lambdaUpdate().set(ProjectInfo::getVisits, projectInfo.getVisits() + 1).eq(ProjectInfo::getId, projectInfo.getId()));
        return CloneUtils.clone(projectInfo, ProductDetailVo.class);
    }

    /**
     * 根据用户学校信息获取项目交易情况
     *
     * @param userCode    用户编号
     * @param productCode 项目编号
     * @return 交易情况
     * @throws Exception 异常
     */
    @Override
    public List<ProductTradeWithSchoolVo> productTradeByUser(String userCode, String productCode) throws Exception {
        if (StrUtil.isBlank(userCode) || StrUtil.isBlank(productCode)) {
            throw new FebsException("参数校验失败！");
        }

        // 获取用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, userCode));
        if (userInfo == null) {
            throw new FebsException("用户不存在！");
        }
        if (StrUtil.isBlank(userInfo.getSchool())) {
            throw new FebsException("未绑定学校信息！");
        }

        // 获取此学校的用户
        List<UserInfo> userInfoList = userInfoService.list(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getSchool, userInfo.getSchool()));
        if (CollectionUtils.isEmpty(userInfoList)) {
            return Collections.emptyList();
        }
        Map<String, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getCode, e -> e));
        List<String> userCodeList = userInfoList.stream().map(UserInfo::getCode).collect(Collectors.toList());

        // 根据项目编号获取订单
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getProCode, productCode).eq(OrderInfo::getOrderStatus, BusinessConstant.FINISHED_ORDER_STATUS_PAID));
        if (CollectionUtils.isEmpty(orderInfoList)) {
            return Collections.emptyList();
        }
        // 重复学校订单
        List<OrderInfo> repeatSchoolOrderList = orderInfoList.stream().filter(e -> userCodeList.contains(e.getUserCode())).collect(Collectors.toList());
        return repeatSchoolOrderList.stream().map(e -> new ProductTradeWithSchoolVo(userInfo.getSchoolName(), e.getOrderTime(), userInfoMap.get(e.getUserCode()).getAccount(), userInfoMap.get(e.getUserCode()).getHeadImgUrl())).collect(Collectors.toList());
    }

    /**
     * 项目成交量更新
     *
     * @param productCode 项目编号
     */
    @Override
    public void projectTurnover(String productCode) {
        ProjectInfo projectInfo = this.getOne(Wrappers.<ProjectInfo>lambdaQuery().eq(ProjectInfo::getCode, productCode));
        if (projectInfo != null) {
            this.update(Wrappers.<ProjectInfo>lambdaUpdate().set(ProjectInfo::getTurnover, projectInfo.getTurnover() + 1).eq(ProjectInfo::getId, projectInfo.getId()));
        }
    }

    /**
     * 校验用户对此项目是否又下载权限
     *
     * @param productCode 项目编号
     * @return 校验结果
     * @throws Exception 异常
     */
    public Boolean checkUserProduct(String productCode) throws Exception {
        // 当前登录用户
        User user = FebsUtil.getCurrentUser();
        // 获取用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, user.getUsername()));
        if (userInfo == null) {
            throw new FebsException("用户不存在！");
        }

        // 判断是否购买过此项目
        Integer buyCount = orderInfoMapper.selectCount(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getProCode, productCode).eq(OrderInfo::getUserCode, userInfo.getCode()));
        return buyCount > 0;
    }

}
