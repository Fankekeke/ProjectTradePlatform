package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.cos.dao.*;
import com.fate.archer.cos.entity.*;
import com.fate.archer.cos.entity.vo.IncomeDetailVo;
import com.fate.archer.cos.service.IRateInfoService;
import com.fate.archer.cos.service.ITeamInfoService;
import com.fate.archer.cos.service.IUserInfoService;
import com.fate.archer.cos.service.IWealthAssignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WealthAssignServiceImpl extends ServiceImpl<WealthAssignMapper, WealthAssign> implements IWealthAssignService {

    private final IUserInfoService userInfoService;

    private final ITeamInfoService teamInfoService;

    private final IRateInfoService rateInfoService;

    private final AnnexInfoMapper annexInfoMapper;

    /**
     * 附件订单佣金分配
     *
     * @param annexOrder 附件订单信息
     * @return 返回结果
     * @throws Exception 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean wealthAssignAnnex(AnnexOrder annexOrder) throws Exception {
        // 获取附件信息
        AnnexInfo annexInfo = annexInfoMapper.selectOne(Wrappers.<AnnexInfo>lambdaQuery().eq(AnnexInfo::getCode, annexOrder.getAnnexCode()));
        if (annexInfo == null) {
            throw new FebsException("附件信息不存在！");
        }

        // 佣金分配比率
        Map<String, BigDecimal> rateMap = new HashMap<String, BigDecimal>() {
            {
                put("developer", new BigDecimal("74"));
                put("platform", new BigDecimal("26"));
            }
        };
        List<RateInfo> rateInfoList = rateInfoService.list(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getIsInvite, 2).eq(RateInfo::getFlag, 2));

        rateMap.forEach((key, value) -> {
            rateInfoList.stream().filter(e -> key.equals(e.getRole())).findFirst().ifPresent(rateInfo -> rateMap.put(key, rateInfo.getRate()));
        });

        WealthAssign wealthAssign = new WealthAssign();
        // 分款金额
        wealthAssign.setWealth(annexOrder.getPrice());
        // 订单编号
        wealthAssign.setOrderCode(annexOrder.getCode());
        // 开发者ID
        wealthAssign.setDevelopera(annexInfo.getUserId());
        wealthAssign.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 平台收益
        BigDecimal platformWealth = wealthAssign.getWealth().multiply(rateMap.get("platform")).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        wealthAssign.setPlatformBalance(platformWealth);
        // 开发者收益
        BigDecimal developerWealth = wealthAssign.getWealth().multiply(rateMap.get("developer")).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        wealthAssign.setDeveloperBalance(developerWealth);

        return this.save(wealthAssign);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean wealthAssign(WealthAssign wealthAssign) {
        // 获取分配比例
        RateInfo rateInfoDeveloper;
        RateInfo rateInfoPlatform;
        RateInfo rateInfoInvite;
        // 是否存在邀请人
        if (wealthAssign.getInviteCode() != null) {
            rateInfoDeveloper = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "developer")
                    .eq(RateInfo::getFlag, 2).eq(RateInfo::getIsInvite, 1));
            rateInfoPlatform = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "platform")
                    .eq(RateInfo::getFlag, 2).eq(RateInfo::getIsInvite, 1));
            rateInfoInvite = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "invite")
                    .eq(RateInfo::getFlag, 2).eq(RateInfo::getIsInvite, 1));
            // 邀请人分配
            wealthAssign.setInviteBalance(rateInfoInvite.getRate().multiply(wealthAssign.getWealth()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
            UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, wealthAssign.getInviteCode()));
            userInfoService.update(Wrappers.<UserInfo>lambdaUpdate()
                    .set(UserInfo::getBalance, userInfo.getBalance().add(wealthAssign.getInviteBalance()))
                    .eq(UserInfo::getCode, wealthAssign.getInviteCode()));
        } else {
            rateInfoDeveloper = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "developer")
                    .eq(RateInfo::getFlag, 2).eq(RateInfo::getIsInvite, 0));
            rateInfoPlatform = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "platform")
                    .eq(RateInfo::getFlag, 2).eq(RateInfo::getIsInvite, 0));
        }
        // 开发者分配
        wealthAssign.setDeveloperBalance(rateInfoDeveloper.getRate().multiply(wealthAssign.getWealth()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getId, wealthAssign.getDevelopera()));
        teamInfoService.update(Wrappers.<TeamInfo>lambdaUpdate()
                .set(TeamInfo::getBalance, teamInfo.getBalance().add(wealthAssign.getDeveloperBalance()))
                .eq(TeamInfo::getId, wealthAssign.getDevelopera()));
        // 平台分配
        wealthAssign.setPlatformBalance(rateInfoPlatform.getRate().multiply(wealthAssign.getWealth()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
        wealthAssign.setCreateDate(DateUtil.formatDateTime(new Date()));
        return this.save(wealthAssign);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean wealthAssignCustomize(WealthAssign wealthAssign) {
        // 获取分配比例
        RateInfo rateInfoDeveloper;
        RateInfo rateInfoPlatform;
        RateInfo rateInfoInvite;
        // 是否存在邀请人
        if (wealthAssign.getInviteCode() != null) {
            rateInfoDeveloper = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "developer")
                    .eq(RateInfo::getFlag, 1).eq(RateInfo::getIsInvite, 1));
            rateInfoPlatform = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "platform")
                    .eq(RateInfo::getFlag, 1).eq(RateInfo::getIsInvite, 1));
            rateInfoInvite = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "invite")
                    .eq(RateInfo::getFlag, 1).eq(RateInfo::getIsInvite, 1));
            // 邀请人分配
            wealthAssign.setInviteBalance(rateInfoInvite.getRate().multiply(wealthAssign.getWealth()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
            UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, wealthAssign.getInviteCode()));
            userInfoService.update(Wrappers.<UserInfo>lambdaUpdate()
                    .set(UserInfo::getBalance, userInfo.getBalance().add(wealthAssign.getInviteBalance()))
                    .eq(UserInfo::getCode, wealthAssign.getInviteCode()));
        } else {
            rateInfoDeveloper = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "developer")
                    .eq(RateInfo::getFlag, 1).eq(RateInfo::getIsInvite, 0));
            rateInfoPlatform = rateInfoService.getOne(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, "platform")
                    .eq(RateInfo::getFlag, 1).eq(RateInfo::getIsInvite, 0));
        }
        // 开发者分配
        wealthAssign.setDeveloperBalance(rateInfoDeveloper.getRate().multiply(wealthAssign.getWealth()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
        int developerNum = 0;
        if (wealthAssign.getDevelopera() != null) {
            developerNum++;
        }

        if (wealthAssign.getDeveloperb() != null) {
            developerNum++;
        }

        if (wealthAssign.getDeveloperb() != null) {
            developerNum++;
        }

        if (wealthAssign.getDevelopera() != null) {
            TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getId, wealthAssign.getDevelopera()));
            teamInfoService.update(Wrappers.<TeamInfo>lambdaUpdate()
                    .set(TeamInfo::getBalance, teamInfo.getBalance().add(wealthAssign.getDeveloperBalance().divide(new BigDecimal(developerNum), 2, BigDecimal.ROUND_HALF_UP)))
                    .eq(TeamInfo::getId, wealthAssign.getDevelopera()));
        }
        if (wealthAssign.getDeveloperb() != null) {
            TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getId, wealthAssign.getDeveloperb()));
            teamInfoService.update(Wrappers.<TeamInfo>lambdaUpdate()
                    .set(TeamInfo::getBalance, teamInfo.getBalance().add(wealthAssign.getDeveloperBalance().divide(new BigDecimal(developerNum), 2, BigDecimal.ROUND_HALF_UP)))
                    .eq(TeamInfo::getId, wealthAssign.getDeveloperb()));
        }
        if (wealthAssign.getDeveloperb() != null) {
            TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getId, wealthAssign.getDeveloperc()));
            teamInfoService.update(Wrappers.<TeamInfo>lambdaUpdate()
                    .set(TeamInfo::getBalance, teamInfo.getBalance().add(wealthAssign.getDeveloperBalance().divide(new BigDecimal(developerNum), 2, BigDecimal.ROUND_HALF_UP)))
                    .eq(TeamInfo::getId, wealthAssign.getDeveloperc()));
        }
        // 平台分配
        wealthAssign.setPlatformBalance(rateInfoPlatform.getRate().multiply(wealthAssign.getWealth()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
        wealthAssign.setCreateDate(DateUtil.formatDateTime(new Date()));
        return this.save(wealthAssign);
    }

    /**
     * 邀请人查看收益记录
     *
     * @param userCode 邀请人编号
     * @return 收益记录
     * @throws Exception 异常
     */
    @Override
    public List<IncomeDetailVo> selectWealthPartByInvite(String userCode) throws Exception {
        // 获取用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, userCode));
        if (userInfo == null) {
            throw new FebsException("无用户信息！");
        }

        // 被邀请人信息
        List<UserInfo> userInfoList = userInfoService.list(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getInviteUser, userCode));
        if (CollectionUtils.isEmpty(userInfoList)) {
            return Collections.emptyList();
        }

        // 收益记录
        List<WealthAssign> wealthAssignList = this.list(Wrappers.<WealthAssign>lambdaQuery().eq(WealthAssign::getInviteCode, userCode));
        if (CollectionUtils.isEmpty(wealthAssignList)) {
            return Collections.emptyList();
        }

        List<String> orderCodes = wealthAssignList.stream().map(WealthAssign::getOrderCode).distinct().collect(Collectors.toList());
        // 成品收益
        List<IncomeDetailVo> finshedProductWealthList = baseMapper.selectFinshedProductWealthPartByInvite(orderCodes, BusinessConstant.ROLE_INVITE_FLAG);

        // 定制收益
        List<IncomeDetailVo> customWealthList = baseMapper.selectFinshedProductWealthPartByInvite(orderCodes, BusinessConstant.ROLE_INVITE_FLAG);

        return ListUtils.union(customWealthList, finshedProductWealthList);
    }

    /**
     * 开发查看收益记录
     *
     * @param teamId 开发者ID
     * @return 收益记录
     * @throws Exception 异常
     */
    @Override
    public List<IncomeDetailVo> selectWealthPartByDeveloper(Integer teamId) throws Exception {
        TeamInfo teamInfo = teamInfoService.getById(teamId);
        if (teamInfo == null) {
            throw new FebsException("该开发者不存在！");
        }
        // 获取收益信息
        List<WealthAssign> wealthAssignList = this.list(Wrappers.<WealthAssign>lambdaQuery().or().eq(WealthAssign::getDevelopera, teamId)
                .or().eq(WealthAssign::getDeveloperb, teamId).or().eq(WealthAssign::getDeveloperc, teamId));
        if (CollectionUtils.isEmpty(wealthAssignList)) {
            return Collections.emptyList();
        }

        // 获取订单编号
        List<String> orderCodes = wealthAssignList.stream().map(WealthAssign::getOrderCode).collect(Collectors.toList());
        // 附件收益
        List<IncomeDetailVo> annexWealthList = baseMapper.selectAnnexWealthPartByInvite(orderCodes, BusinessConstant.ROLE_DEVELOPER_FLAG);

        // 成品收益
        List<IncomeDetailVo> finshedProductWealthList = baseMapper.selectFinshedProductWealthPartByInvite(orderCodes, BusinessConstant.ROLE_DEVELOPER_FLAG);

        // 定制收益
        List<IncomeDetailVo> customWealthList = baseMapper.selectCustomWealthList(orderCodes, BusinessConstant.ROLE_DEVELOPER_FLAG);
        return ListUtils.union(ListUtils.union(finshedProductWealthList, annexWealthList), customWealthList);
    }

    /**
     * 平台查看收益记录
     *
     * @return 收益记录
     * @throws Exception 异常
     */
    @Override
    public List<IncomeDetailVo> selectWealthPartByPlatform() throws Exception {
        List<WealthAssign> wealthAssignList = this.list();
        if (CollectionUtils.isEmpty(wealthAssignList)) {
            return Collections.emptyList();
        }

        // 获取订单编号
        List<String> orderCodes = wealthAssignList.stream().map(WealthAssign::getOrderCode).collect(Collectors.toList());

        // 附件收益
        List<IncomeDetailVo> annexWealthList = baseMapper.selectAnnexWealthPartByInvite(orderCodes, BusinessConstant.ROLE_SYSTEM_FLAG);

        // 成品收益
        List<IncomeDetailVo> finshedProductWealthList = baseMapper.selectFinshedProductWealthPartByInvite(orderCodes, BusinessConstant.ROLE_SYSTEM_FLAG);

        // 定制收益
        List<IncomeDetailVo> customWealthList = baseMapper.selectCustomWealthList(orderCodes, BusinessConstant.ROLE_SYSTEM_FLAG);
        return ListUtils.union(ListUtils.union(finshedProductWealthList, annexWealthList), customWealthList);
    }

}
