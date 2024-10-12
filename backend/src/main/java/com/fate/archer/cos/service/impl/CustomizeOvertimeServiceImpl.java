package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.query.Producer;
import com.fate.archer.cos.entity.*;
import com.fate.archer.cos.dao.CustomizeOvertimeMapper;
import com.fate.archer.cos.service.ICustomizeInfoService;
import com.fate.archer.cos.service.ICustomizeOvertimeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.archer.cos.service.INotificationService;
import com.fate.archer.cos.service.ITeamInfoService;
import com.fate.archer.system.domain.User;
import com.fate.archer.system.domain.UserRole;
import com.fate.archer.system.service.UserRoleService;
import com.fate.archer.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomizeOvertimeServiceImpl extends ServiceImpl<CustomizeOvertimeMapper, CustomizeOvertime> implements ICustomizeOvertimeService {

    private final ITeamInfoService teamInfoService;

    private final UserService userService;

    private final INotificationService notificationService;

    private final ICustomizeInfoService customizeInfoService;

    @Override
    public Boolean overTimeApply(CustomizeOvertime customizeOvertime) {
        // 获取项目信息
        CustomizeInfo customizeInfo = customizeInfoService.getOne(Wrappers.<CustomizeInfo>lambdaQuery().eq(CustomizeInfo::getCode, customizeOvertime.getCustomizeCode()));

        // 管理员消息通知
        // 获取所有管理员信息
        List<LinkedHashMap<String, Object>> teamList = teamInfoService.administrator(BusinessConstant.ROLE_ADMIN_ID);
        teamList.forEach(item -> {
            String template = "滴滴滴！这里是猿形毕露网站，{} 您好，{}项目申请加时{}天，请前往平台进行审核，如非本人操作，请忽略本邮件！";
            String str = StrUtil.format(template, item.get("players").toString(), customizeInfo.getProName(), customizeOvertime.getOvertimeDay());
            notificationService.setNotification("《" + customizeInfo.getProName() + "》 加时申请", str, Integer.parseInt(item.get("adminUser").toString()), BusinessConstant.TASK);
            if (item.get("email") != null) {
                teamInfoService.messageOver(BusinessConstant.SEND_TYPE_EMAIL, item.get("email").toString(), str, MessageTip.MESSAGE_ORDER);
            }
            // todo 手机短信发送
            if (item.get("mobile") != null) {
                // teamInfoService.messageOver(1, item.get("mobile").toString(), str, MessageTip.MESSAGE_ORDER);
            }
        });
        customizeOvertime.setStatus(1);
        customizeOvertime.setCreateDate(DateUtil.formatDateTime(new Date()));
        return this.save(customizeOvertime);
    }

    @Override
    public Boolean overTimeCheck(CustomizeOvertime customizeOvertime) {
        // 获取项目信息
        CustomizeInfo customizeInfo = customizeInfoService.getOne(Wrappers.<CustomizeInfo>lambdaQuery().eq(CustomizeInfo::getCode, customizeOvertime.getCustomizeCode()));
        // 获取开发者信息
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, customizeOvertime.getProposer().longValue()));
        // 消息通知
        String template = "滴滴滴！这里是猿形毕露网站，{} 您好，{}项目申请加时{}天，管理员审核{}，如非本人操作，请忽略本邮件！";
        String str = StrUtil.format(template, teamInfo.getPlayers(), customizeInfo.getProName(), customizeOvertime.getOvertimeDay(), customizeOvertime.getStatus() == 2 ? "通过😀" : "未通过😥");
        notificationService.setNotification("《" + customizeInfo.getProName() + "》 加时审核" + (customizeOvertime.getStatus() == 2 ? "通过😀" : "未通过😥") + "", str, customizeOvertime.getProposer(), BusinessConstant.TASK);
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, customizeOvertime.getProposer()));
        if (user.getEmail() != null) {
            teamInfoService.messageOver(2, user.getEmail(), str, MessageTip.MESSAGE_ORDER);
        }
        // 判断审核成功，订单天数修改
        if (customizeOvertime.getStatus() == 2) {
            customizeInfoService.update(Wrappers.<CustomizeInfo>lambdaUpdate().set(CustomizeInfo::getCycle, customizeInfo.getCycle() + customizeOvertime.getOvertimeDay())
                    .eq(CustomizeInfo::getCode, customizeOvertime.getCustomizeCode()));
        }
        // 修改申请状态
        return this.update(Wrappers.<CustomizeOvertime>lambdaUpdate().set(CustomizeOvertime::getStatus, customizeOvertime.getStatus()).eq(CustomizeOvertime::getCustomizeCode, customizeOvertime.getCustomizeCode()));
    }

    @Override
    public List<LinkedHashMap<String, Object>> getOverTimeList() {
        return baseMapper.getOverTimeList();
    }
}
