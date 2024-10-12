package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.TeamInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface TeamInfoMapper extends BaseMapper<TeamInfo> {

    /**
     * 分页查询团队信息
     *
     * @param page     分页对象
     * @param teamInfo 团队信息
     * @return 团队信息列表
     */
    IPage<LinkedHashMap<String, Object>> getTeamByPage(Page page, @Param("teamInfo") TeamInfo teamInfo);

    /**
     * 查询未绑定团队的账户
     *
     * @return 查询未绑定团队的账户
     */
    List<LinkedHashMap<String, Object>> teamUnbound();

    /**
     * 查询团队信息
     *
     * @param account 账户信息
     * @return 团队信息
     */
    List<LinkedHashMap<String, Object>> teamList(@Param("account") String account);

    /**
     * 根据ID获取团队成员信息
     *
     * @param ids 成员Ids
     * @return 团队成员信息
     */
    List<LinkedHashMap<String, Object>> getTeamUserInfo(@Param("ids") List<String> ids);

    /**
     * 获取所有管理员信息消息通知
     *
     * @param roleId 角色Id
     * @return 信息消息通知
     */
    List<LinkedHashMap<String, Object>> administrator(@Param("roleId") Integer roleId);
}
