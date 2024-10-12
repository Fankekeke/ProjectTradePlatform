package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fate.archer.common.authentication.JWTToken;
import com.fate.archer.common.authentication.JWTUtil;
import com.fate.archer.common.domain.ActiveUser;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.domain.FebsConstant;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.common.properties.FebsProperties;
import com.fate.archer.common.query.Producer;
import com.fate.archer.common.service.RedisService;
import com.fate.archer.common.utils.*;
import com.fate.archer.cos.dto.query.AccountVerify;
import com.fate.archer.cos.entity.UserInfo;
import com.fate.archer.cos.dao.UserInfoMapper;
import com.fate.archer.cos.entity.WechatUserInfo;
import com.fate.archer.cos.entity.vo.InviterUserVo;
import com.fate.archer.cos.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.archer.system.domain.User;
import com.fate.archer.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    private final FebsProperties febsProperties;

    private final UserService userService;

    private final RedisService redisService;

    private final Producer producer;

    private final ObjectMapper mapper;

    @Override
    public IPage<LinkedHashMap<String, Object>> getUserPage(Page page, UserInfo userInfo) {
        return baseMapper.getUserPage(page, userInfo);
    }

    @Override
    public LinkedHashMap<String, Object> getUserTradeByCode(String userCode) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 交易记录
        result.put("trade", baseMapper.trade(userCode));
        // 余额记录
        result.put("balance", baseMapper.balance(userCode));
        // 邀请记录
        result.put("invite", baseMapper.invite(userCode));
        return result;
    }

    @Override
    public void balanceClean(String userCode) {
        this.update(Wrappers.<UserInfo>lambdaUpdate().set(UserInfo::getBalance, 0).eq(UserInfo::getCode, userCode));
    }

    @Override
    public LinkedHashMap<String, Object> getUserInfoByCode(String userCode) {
        return baseMapper.getUserInfoByCode(userCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo wxCallBack(WechatUserInfo wechatUser) throws Exception {
        // 根据openId获取用户信息
        UserInfo user = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, wechatUser.getOpenId()));

        if (user != null) {
            // 查看是否有系统账户
            User systemUser = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getOpenId()));
            if (systemUser == null) {
                this.userRegisterBySystem(user.getOpenId());
            }
            // 更新上次登陆时间
            this.update(Wrappers.<UserInfo>lambdaUpdate().set(UserInfo::getLastLoginDate, DateUtil.formatDateTime(new Date())).eq(UserInfo::getCode, user.getCode()));
            return user;
        } else {
            // 添加用户信息
            UserInfo userData = new UserInfo();
            userData.setCode("AK-" + System.currentTimeMillis());
            userData.setAccount(wechatUser.getNickName());
            userData.setSex(wechatUser.getSex());
            userData.setHeadImgUrl(wechatUser.getHeadImgUrl());
            userData.setOpenId(wechatUser.getOpenId());
            userData.setLastLoginDate(DateUtil.formatDateTime(new Date()));
            this.save(userData);
            this.userRegisterBySystem(userData.getOpenId());
            return userData;
        }
    }

    /**
     * 注册系统用户
     * @param openId 微信openId
     * @throws Exception 异常
     */
    public void userRegisterBySystem(String openId) throws Exception {
        this.userService.regist(openId, BusinessConstant.DEFAULT_PASSWORD);
    }

    public String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request) throws Exception {
        String ip = IPUtil.getIpAddr(request);
        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());
        activeUser.setLoginAddress(AddressUtil.getCityInfo(ip));

        // zset 存储登录用户，score 为过期时间戳
        this.redisService.zadd(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, Double.valueOf(token.getExipreAt()), mapper.writeValueAsString(activeUser));
        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        this.redisService.set(FebsConstant.TOKEN_CACHE_PREFIX + token.getToken() + StringPool.DOT + ip, token.getToken(), febsProperties.getShiro().getJwtTimeOut() * 1000);

        return activeUser.getId();
    }

    @Override
    public void loginCallBack(UserInfo userInfo, HttpServletRequest request) throws Exception {
        // 修改用户登录时间
        this.update(Wrappers.<UserInfo>lambdaUpdate().set(UserInfo::getLastLoginDate, DateUtil.formatDateTime(new Date())).eq(UserInfo::getId, userInfo.getId()));
        // 保存token到redis
        String token = FebsUtil.encryptToken(JWTUtil.sign(userInfo.getAccount(), userInfo.getOpenId()));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(febsProperties.getShiro().getJwtTimeOut());
        String expireTimeStr = com.fate.archer.common.utils.DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        User user = new User();
        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);
        user.setUsername(userInfo.getAccount());
    }

    @Override
    public Boolean smsBind(String userCode, String phone) {
        // 验证手机号码合法性
        return FebsUtil.isPhoneNumber(phone);
    }

    @Override
    public Boolean emailBind(String userCode, String email) {
        return FebsUtil.checkEmailFormat(email);
    }

    /**
     * 普通用户验证邮箱地址
     * @param mail 邮箱地址
     * @param userCode 用户编号
     * @return 验证结果
     */
    @Override
    public Boolean mailVerify(String mail, String userCode) throws Exception {
        // 判断邮箱地址是否被使用
        int count = this.count(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getEmail, mail));

        if (count > 0) {
            throw new FebsException("该邮箱已被使用！");
        }

        // 绘制随机字符
        String random = "";
        for (int i = 1; i <= 50; i++) {
            random = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        }
        //1：将随机生成的验证码放入redis中
        redisService.set(mail, random);
        UserInfo user = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, userCode));
        // 将验证对象放置消息队列
        AccountVerify accountVerify = new AccountVerify(user.getAccount(), mail, 2, random, 2);
        Destination destination = new ActiveMQQueue("assist.queue");
        producer.sendMessage(destination, JSONUtil.toJsonStr(accountVerify));
        return true;
    }

    /**
     * 验证识别码更换邮箱
     * @param mail 邮箱地址
     * @param verifyCode 验证码
     * @param userCode 用户编号
     * @return 返回结果
     */
    @Override
    public R checkMailVerify(String mail, String verifyCode, String userCode) throws Exception {
        // 获取redis里面的验证码信息
        String code = redisService.get(mail);
        if (code != null) {
            // 判断验证码是否正确
            if (!StringUtils.isEmpty(verifyCode)) {
                verifyCode = verifyCode.toLowerCase();
                code = code.toLowerCase();
                if (verifyCode.equals(code)) {
                    redisService.del(mail);
                    // 修改用户绑定邮箱
                    return R.ok(this.update(Wrappers.<UserInfo>lambdaUpdate().set(UserInfo::getEmail, mail).eq(UserInfo::getCode, userCode)));
                } else {
                    return R.error("验证码错误！");
                }
            } else {
                return R.error("验证码为空！");
            }
        } else {
            return R.error("出现异常，请稍后在试！");
        }
    }

    /**
     * 普通用户验证手机号码
     * @param mobile 手机号码
     * @param userCode 用户编号
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public Boolean mobileVerify(String mobile, String userCode) throws Exception {
        // 判断手机号码是否被使用
        int count = this.count(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getPhone, mobile));

        if (count > 0) {
            throw new FebsException("该手机号码已被使用！");
        }

        // 绘制随机字符
        String random = "";
        for (int i = 1; i <= 50; i++) {
            random = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        }
        //1：将随机生成的验证码放入redis中
        redisService.set(mobile, random);
        UserInfo user = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, userCode));
        // 将验证对象放置消息队列
        AccountVerify accountVerify = new AccountVerify(user.getAccount(), mobile, 1, random, 2);
        Destination destination = new ActiveMQQueue("assist.queue");
        producer.sendMessage(destination, JSONUtil.toJsonStr(accountVerify));
        return true;
    }

    /**
     * 验证识别码更换手机号码
     * @param mobile 手机号码
     * @param verifyCode 验证码
     * @param userCode 用户编号
     * @return 返回结果
     * @throws Exception 异常
     */
    @Override
    public R checkMobileVerify(String mobile, String verifyCode, String userCode) throws Exception {
        // 获取redis里面的验证码信息
        String code = redisService.get(mobile);
        if (code != null) {
            // 判断验证码是否正确
            if (!StringUtils.isEmpty(verifyCode)) {
                verifyCode = verifyCode.toLowerCase();
                code = code.toLowerCase();
                if (verifyCode.equals(code)) {
                    redisService.del(mobile);
                    // 修改用户绑定手机号码
                    return R.ok(this.update(Wrappers.<UserInfo>lambdaUpdate().set(UserInfo::getPhone, mobile).eq(UserInfo::getCode, userCode)));
                } else {
                    return R.error("验证码错误！");
                }
            } else {
                return R.error("验证码为空！");
            }
        } else {
            return R.error("出现异常，请稍后在试！");
        }
    }

    /**
     * 申请为邀请人
     *
     * @param userCode 用户编号
     * @return 结果
     */
    @Override
    public Boolean applicationInviter(String userCode) throws Exception {
        // 获取用户信息
        UserInfo userInfo = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, userCode));
        if (userInfo == null) {
            throw new FebsException("用户信息不存在！");
        }

        if (userInfo.getInvite() == 1) {
            throw new FebsException("当前已经是邀请人~");
        }

        if (StrUtil.isBlank(userInfo.getPhone())) {
            throw new FebsException("请先完成手机号码验证~");
        }

        return this.update(Wrappers.<UserInfo>lambdaUpdate().set(UserInfo::getInvite, BusinessConstant.USER_TYPE_INVITE_YES).eq(UserInfo::getId, userInfo.getId()));
    }

    /**
     * 我的邀请人信息
     *
     * @param userCode 用户编号
     * @return 邀请人信息
     * @throws Exception 异常
     */
    @Override
    public InviterUserVo selectInviterMeUserInfo(String userCode) throws Exception {
        // 获取用户信息
        UserInfo userInfo = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, userCode));
        if (userInfo == null) {
            throw new FebsException("用户信息不存在！");
        }
        if (StrUtil.isBlank(userInfo.getInviteUser())) {
            return null;
        }
        UserInfo inviteUser = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, userInfo.getInviteUser()));
        return CloneUtils.clone(inviteUser, InviterUserVo.class);
    }

    /**
     * 我邀请的用户信息
     *
     * @param userCode 用户编号
     * @return 用户信息
     */
    @Override
    public List<InviterUserVo> selectInvitedUser(String userCode) {
        List<UserInfo> userList = this.list(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getInviteUser, userCode));
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyList();
        }

        return CloneUtils.convertToTargetList(userList, InviterUserVo.class);
    }

}
