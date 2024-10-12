package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 分页获取用户信息
     *
     * @param page     分页对象
     * @param userInfo 用户信息
     * @return 用户信息列表
     */
    IPage<LinkedHashMap<String, Object>> getUserPage(Page page, @Param("userInfo") UserInfo userInfo);

    /**
     * 根据用户编号获取交易记录
     *
     * @param userCode 用户编号
     * @return 交易记录
     */
    List<LinkedHashMap<String, Object>> trade(@Param("userCode") String userCode);

    /**
     * 根据用户编号获取余额记录
     *
     * @param userCode 用户编号
     * @return 余额记录
     */
    List<LinkedHashMap<String, Object>> balance(@Param("userCode") String userCode);

    /**
     * 根据用户编号获取邀请记录
     *
     * @param userCode 用户编号
     * @return 邀请记录
     */
    List<LinkedHashMap<String, Object>> invite(@Param("userCode") String userCode);

    /**
     * 根据用户编号获取用户详细信息
     *
     * @param userCode 用户编号
     * @return 用户详细信息
     */
    LinkedHashMap<String, Object> getUserInfoByCode(@Param("userCode") String userCode);
}
