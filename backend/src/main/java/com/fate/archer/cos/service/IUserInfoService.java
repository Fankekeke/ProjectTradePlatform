package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.archer.cos.entity.WechatUserInfo;
import com.fate.archer.cos.entity.vo.InviterUserVo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IUserInfoService extends IService<UserInfo> {

    /**
     * 分页获取用户信息
     *
     * @param page     分页对象
     * @param userInfo 用户信息
     * @return 用户信息
     */
    IPage<LinkedHashMap<String, Object>> getUserPage(Page page, UserInfo userInfo);

    /**
     * 根据用户编号获取交易记录、账户余额
     *
     * @param userCode 用户编号
     * @return
     */
    LinkedHashMap<String, Object> getUserTradeByCode(String userCode);

    /**
     * 用户提现申请后账户余额清空
     *
     * @param userCode 用户编号
     */
    void balanceClean(String userCode);

    /**
     * 根据用户编号获取用户详细信息
     *
     * @param userCode 用户编号
     * @return 用户详细信息
     */
    LinkedHashMap<String, Object> getUserInfoByCode(String userCode);

    /**
     * 用户扫描微信二维码登录
     *
     * @param wechatUser 微信用户
     * @return 用户信息
     */
    UserInfo wxCallBack(WechatUserInfo wechatUser) throws Exception;

    /**
     * 用户登录后操作
     *
     * @param userInfo 用户信息
     * @param request  请求
     * @throws Exception 异常
     */
    void loginCallBack(UserInfo userInfo, HttpServletRequest request) throws Exception;

    /**
     * 用户绑定手机号码
     *
     * @param userCode 用户编号
     * @param phone    手机号码
     * @return 返回结果
     */
    Boolean smsBind(String userCode, String phone);

    /**
     * 用户绑定邮箱
     *
     * @param userCode 用户编号
     * @param email    邮箱地址
     * @return 返回结果
     */
    Boolean emailBind(String userCode, String email);

    /**
     * 普通用户验证邮箱地址
     *
     * @param mail     邮箱地址
     * @param userCode 用户编号
     * @return 结果
     * @throws Exception 异常
     */
    Boolean mailVerify(String mail, String userCode) throws Exception;

    /**
     * 验证识别码更换邮箱
     *
     * @param mail       邮箱地址
     * @param verifyCode 验证码
     * @param userCode   用户编号
     * @return 返回结果
     * @throws Exception 异常
     */
    R checkMailVerify(String mail, String verifyCode, String userCode) throws Exception;

    /**
     * 普通用户验证手机号码
     *
     * @param mobile   手机号码
     * @param userCode 用户编号
     * @return 结果
     * @throws Exception 异常
     */
    Boolean mobileVerify(String mobile, String userCode) throws Exception;

    /**
     * 验证识别码更换手机号码
     *
     * @param mobile     手机号码
     * @param verifyCode 验证码
     * @param userCode   用户编号
     * @return 返回结果
     * @throws Exception 异常
     */
    R checkMobileVerify(String mobile, String verifyCode, String userCode) throws Exception;

    /**
     * 申请为邀请人
     *
     * @param userCode 用户编号
     * @return 结果
     * @throws Exception 异常
     */
    Boolean applicationInviter(String userCode) throws Exception;

    /**
     * 我的邀请人信息
     *
     * @param userCode 用户编号
     * @return 邀请人信息
     * @throws Exception 异常
     */
    InviterUserVo selectInviterMeUserInfo(String userCode) throws Exception;

    /**
     * 我邀请的用户信息
     *
     * @param userCode 用户编号
     * @return 用户信息
     */
    List<InviterUserVo> selectInvitedUser(String userCode);

}
