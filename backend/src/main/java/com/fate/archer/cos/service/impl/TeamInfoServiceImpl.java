package com.fate.archer.cos.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.query.Producer;
import com.fate.archer.cos.dao.WealthAssignMapper;
import com.fate.archer.cos.dto.query.OrderTip;
import com.fate.archer.cos.entity.TeamInfo;
import com.fate.archer.cos.dao.TeamInfoMapper;
import com.fate.archer.cos.service.ITeamInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.archer.cos.service.IWealthAssignService;
import com.fate.archer.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamInfoServiceImpl extends ServiceImpl<TeamInfoMapper, TeamInfo> implements ITeamInfoService {

    private final Producer producer;

    private final UserService userService;

    private final WealthAssignMapper wealthAssignMapper;

    /**
     * 分页查询团队信息
     *
     * @param page
     * @param teamInfo
     * @return
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> getTeamByPage(Page page, TeamInfo teamInfo) {
        return baseMapper.getTeamByPage(page, teamInfo);
    }

    /**
     * 查询未绑定团队的账户
     *
     * @return
     */
    @Override
    public List<LinkedHashMap<String, Object>> teamUnbound() {
        return baseMapper.teamUnbound();
    }

    /**
     * 查询团队信息
     *
     * @param account
     * @return
     */
    @Override
    public List<LinkedHashMap<String, Object>> teamList(String account) {
        return baseMapper.teamList(account);
    }

    /**
     * 根据ID获取团队成员信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<LinkedHashMap<String, Object>> getTeamUserInfo(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return baseMapper.getTeamUserInfo(list);
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public LinkedHashMap<String, Object> userInfoById(Integer userId) {
        TeamInfo teamInfo = getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, userId));
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("userInfo", userService.getById(userId));
                put("teamInfo", teamInfo);
            }
        };
        if (teamInfo != null) {
            // 非定制项目收益统计
            List<LinkedHashMap<String, Object>> nonCustomizedData = wealthAssignMapper.nonCustomized(teamInfo.getId());
            // 定制项目收益统计
            List<LinkedHashMap<String, Object>> customized = wealthAssignMapper.customized(teamInfo.getId());
            // 论文统计
            // 项目统计
        }
        return result;
    }

    @Override
    public List<LinkedHashMap<String, Object>> administrator(Integer roleId) {
        return baseMapper.administrator(roleId);
    }

    @Override
    public void messageOver(Integer sendType, String account, String message, Integer messageType) {
        OrderTip orderTip = new OrderTip(message, sendType, messageType, account);
        Destination destination = new ActiveMQQueue("assist.queue");
        producer.sendMessage(destination, JSONUtil.toJsonStr(orderTip));
    }
}
