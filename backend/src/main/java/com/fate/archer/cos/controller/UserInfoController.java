package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.UserInfo;
import com.fate.archer.cos.service.IAnnexOrderService;
import com.fate.archer.cos.service.IOrderInfoService;
import com.fate.archer.cos.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/user-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoController {

    private final IUserInfoService userInfoService;

    private final IOrderInfoService orderInfoService;

    private final IAnnexOrderService annexOrderService;

    /**
     * 查询用户信息
     *
     * @param page     分页对象
     * @param userInfo 用户信息
     * @return 用户信息列表
     */
    @Log("查询用户信息")
    @GetMapping("/page")
    public R page(Page page, UserInfo userInfo) {
        return R.ok(userInfoService.getUserPage(page, userInfo));
    }

    /**
     * 根据用户编号获取交易记录、账户余额
     *
     * @param userCode 用户编号
     * @return 交易记录、账户余额
     */
    @GetMapping("/trade")
    public R getUserTradeByCode(@RequestParam("userCode") String userCode) {
        return R.ok(userInfoService.getUserTradeByCode(userCode));
    }

    /**
     * 根据用户编号获取用户详细信息
     *
     * @param userCode 用户编号
     * @return 用户详细信息
     */
    @GetMapping("/info/{code}")
    public R getUserInfoByCode(@PathVariable("code") String userCode) {
        return R.ok(userInfoService.getUserInfoByCode(userCode));
    }

    /**
     * 用户列表
     *
     * @param account 账户名称
     * @return 用户列表
     */
    @GetMapping("/userList")
    public R getUserList(@RequestParam(value = "account", required = false) String account) {
        return R.ok(userInfoService.list(Wrappers.<UserInfo>lambdaQuery().like(account != null, UserInfo::getAccount, account)));
    }

    /**
     * 添加用户
     *
     * @param userInfo 用户信息
     * @return 返回结果
     */
    @Log("添加用户")
    @PostMapping
    public R save(UserInfo userInfo) {
        return R.ok(userInfoService.save(userInfo));
    }

    /**
     * 修改用户
     *
     * @param userInfo 用户信息
     * @return 返回结果
     */
    @Log("修改用户")
    @PutMapping
    public R edit(UserInfo userInfo) {
        return R.ok(userInfoService.updateById(userInfo));
    }

    /**
     * 删除用户
     *
     * @param ids 用户信息Id
     * @return 返回结果
     */
    @Log("删除用户")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(userInfoService.removeByIds(ids));
    }

    /**
     * 更新用户基本信息（所属校院、性别）
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    @PutMapping("/base")
    public R updateUserBaseInfo(UserInfo userInfo) {
        return R.ok(userInfoService.update(Wrappers.<UserInfo>lambdaUpdate()
                .set(UserInfo::getSex, userInfo.getSex()).set(UserInfo::getSchool, userInfo.getSchool())
                .set(UserInfo::getSchoolName, userInfo.getSchoolName())
                .eq(UserInfo::getId, userInfo.getId())));
    }

    /**
     * 普通用户验证邮箱地址
     *
     * @param mail     邮箱地址
     * @param userCode 用户编号
     * @return 验证结果
     */
    @Log("普通用户验证邮箱地址")
    @GetMapping("/verify/mail")
    public R mailVerify(String mail, String userCode) throws Exception {
        return R.ok(userInfoService.mailVerify(mail, userCode));
    }

    /**
     * 验证识别码更换邮箱
     *
     * @param mail       邮箱地址
     * @param verifyCode 验证码
     * @param userCode   用户编号
     * @return 返回结果
     */
    @Log("验证识别码更换邮箱")
    @GetMapping("/check/mail")
    public R checkMailVerify(String mail, String verifyCode, String userCode) throws Exception {
        return R.ok(userInfoService.checkMailVerify(mail, verifyCode, userCode));
    }

    /**
     * 普通用户验证手机号码
     *
     * @param mobile   手机号码
     * @param userCode 用户编号
     * @return 验证结果
     */
    @Log("普通用户验证手机号码")
    @GetMapping("/verify/mobile")
    public R mobileVerify(String mobile, String userCode) throws Exception {
        return R.ok(userInfoService.mobileVerify(mobile, userCode));
    }

    /**
     * 验证识别码更换手机号码
     *
     * @param mobile     手机号码
     * @param verifyCode 验证码
     * @param userCode   用户编号
     * @return 返回结果
     */
    @Log("验证识别码更换手机号码")
    @GetMapping("/check/mobile")
    public R checkMobileVerify(String mobile, String verifyCode, String userCode) throws Exception {
        return R.ok(userInfoService.checkMobileVerify(mobile, verifyCode, userCode));
    }

    /**
     * 申请为邀请人
     *
     * @param code 用户编号
     * @return 结果
     */
    @GetMapping("/inviter/{userCode}")
    public R applicationInviter(@PathVariable("userCode") String code) throws Exception {
        return R.ok(userInfoService.applicationInviter(code));
    }

    /**
     * 我的邀请人信息
     *
     * @param code 用户编号
     * @return 邀请人信息
     */
    @GetMapping("/user/inviter/{userCode}")
    public R selectInviterMeUserInfo(@PathVariable("userCode") String code) throws Exception {
        return R.ok(userInfoService.selectInviterMeUserInfo(code));
    }

    /**
     * 我邀请的用户信息
     *
     * @param userCode 用户编号
     * @return 用户列表
     */
    @GetMapping("/inviter/list/{userCode}")
    public R selectInvitedUser(@PathVariable("userCode") String userCode) {
        return R.ok(userInfoService.selectInvitedUser(userCode));
    }

    /**
     * 门户端查询订单信息
     *
     * @param page     分页对象
     * @param userCode 用户编号
     * @return 订单列表
     */
    @GetMapping("/order/page")
    public R selectOrderListByUser(Page page, String userCode) {
        return R.ok(orderInfoService.orderListByUser(page, userCode));
    }

    /**
     * 订单详情
     *
     * @param orderCode 订单编号
     * @return 订单详情
     */
    @GetMapping("/order/detail/{orderCode}")
    public R orderDetailByCode(@PathVariable("orderCode") String orderCode) {
        if (orderCode.contains("AORD")) {
            // 附件订单
            return R.ok(annexOrderService.annexOrderInfoByCode(orderCode));
        } else if (orderCode.contains("ORD")) {
            // 成品订单
            return R.ok(orderInfoService.orderInfoByCode(orderCode));
        } else {
            return R.ok();
        }
    }

}
