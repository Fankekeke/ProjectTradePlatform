package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.TeamInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ITeamInfoService extends IService<TeamInfo> {

    /**
     * 分页查询团队信息
     * @param page 分页对象
     * @param teamInfo 团队信息
     * @return 团队信息列表
     */
    IPage<LinkedHashMap<String, Object>> getTeamByPage(Page page, TeamInfo teamInfo);

    /**
     * 查询未绑定团队的账户
     * @return 未绑定团队的账户
     */
    List<LinkedHashMap<String, Object>> teamUnbound();

    /**
     * 查询团队信息
     * @param account 账户
     * @return 团队信息
     */
    List<LinkedHashMap<String, Object>> teamList(String account);

    /**
     * 根据ID获取团队成员信息
     * @param ids ID
     * @return 成员信息
     */
    List<LinkedHashMap<String, Object>> getTeamUserInfo(String ids);

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    LinkedHashMap<String, Object> userInfoById(Integer userId);

    /**
     * 获取所有管理员信息消息通知
     * @param roleId 角色Id
     * @return 消息通知
     */
    List<LinkedHashMap<String, Object>> administrator(Integer roleId);

    /**
     * 对开发人员或管理员发送消息
     * @param sendType 发送类型
     * @param account 账户
     * @param message 消息
     * @param messageType 消息类型
     */
    void messageOver(Integer sendType, String account, String message,  Integer messageType);
}
