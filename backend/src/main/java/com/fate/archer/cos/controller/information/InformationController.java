package com.fate.archer.cos.controller.information;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.exception.RedisConnectException;
import com.fate.archer.common.query.Producer;
import com.fate.archer.common.service.RedisService;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.dto.query.AccountVerify;
import com.fate.archer.cos.entity.AnnexInfo;
import com.fate.archer.cos.entity.MessageTip;
import com.fate.archer.cos.entity.ProjectInfo;
import com.fate.archer.cos.entity.TeamInfo;
import com.fate.archer.cos.service.*;
import com.fate.archer.system.domain.User;
import com.fate.archer.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.jms.Destination;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/cos/information")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InformationController {

    private final ITeamInfoService teamInfoService;

    private final IProjectInfoService projectInfoService;

    private final IOrderInfoService orderInfoService;

    private final IAnnexOrderService annexOrderService;

    private final IAnnexInfoService annexInfoService;

    private final ICustomizeInfoService customizeInfoService;

    private final UserService userService;

    private final RedisService redisService;

    private final Producer producer;

    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 进入个人信息页面
     * @param userId
     * @return
     */
    @Log("进入个人信息页面")
    @GetMapping("/home")
    public R home(@RequestParam("userId") Integer userId) {
        // 是否绑定手机号码
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, userId));
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, userId));
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("mobile", !StrUtil.hasBlank(user.getMobile())); // 手机验证
                put("mail", !StrUtil.hasBlank(user.getEmail())); // 邮箱验证
                put("team", teamInfo); // 团队信息
                put("user", userService.findDetail(user.getUsername())); // 团队信息
            }
        };
        if (teamInfo != null) {
            // 项目信息
            List<ProjectInfo> projectList = projectInfoService.list(Wrappers.<ProjectInfo>lambdaQuery().eq(ProjectInfo::getAuthor, teamInfo.getId()));
            // 项目订单信息
            List<LinkedHashMap<String, Object>> orderList = orderInfoService.orderListByDevelopers(teamInfo.getId());
            // 论文订单信息
            List<LinkedHashMap<String, Object>> thesisOrderList = annexOrderService.thesisOrderList(teamInfo.getId());
            // 上传附件信息
            List<AnnexInfo> annexList = annexInfoService.list(Wrappers.<AnnexInfo>lambdaQuery().eq(AnnexInfo::getAnnexAnther, teamInfo.getId()).eq(AnnexInfo::getStatus, 2));
            // 获取定制信息
            List<LinkedHashMap<String, Object>> customizeInfoList = customizeInfoService.customizeInfoBuild(teamInfo.getId(), null);
            result.put("project", projectList);
            result.put("order", orderList);
            result.put("thesisOrder", thesisOrderList);
            result.put("annex", annexList);
            result.put("customize", customizeInfoList);
        }
        return R.ok(result);
    }

    /**
     * 根据开发者ID获取任务
     * @param userId
     * @return
     */
    @GetMapping("/task")
    public R taskList(@RequestParam("userId") Integer userId) {
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, userId));
        return teamInfo != null ? R.ok(customizeInfoService.customizeInfoBuild(teamInfo.getId(), null)) : null;
    }

    /**
     * 根据开发者ID获取订单
     * @param userId
     * @return
     */
    @GetMapping("/order")
    public R orderList(@RequestParam("userId") Integer userId) {
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, userId));
        return teamInfo != null ? R.ok(orderInfoService.orderListByDevelopers(teamInfo.getId())) : null;
    }

    /**
     * 系统用户验证手机号码
     * @param mobile
     * @param userId
     * @return
     * @throws Exception
     */
    @Log("系统用户验证手机号码")
    @GetMapping("/verify/mobile")
    public R mobileVerify(@RequestParam("mobile") String mobile, @RequestParam("userId") Integer userId) throws Exception {
        // 判断手机号码是否已被注册
        Integer count = userService.count(Wrappers.<User>lambdaQuery().eq(User::getMobile, mobile));
        if (count > 0) {
            return R.error("该手机号码已被注册");
        } else {
            // 绘制随机字符
            String random = "";
            for (int i = 1; i <= 50; i++) {
                random = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
            }
            //1：将随机生成的验证码放入redis中
            redisService.set(mobile, random);
            User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, userId));
            // 将验证对象放置消息队列
            AccountVerify accountVerify = new AccountVerify(user.getUsername(), mobile, 1, random, 2);
            Destination destination = new ActiveMQQueue("assist.queue");
            producer.sendMessage(destination, JSONUtil.toJsonStr(accountVerify));
            return R.ok(true);
        }
    }

    /**
     * 系统用户验证邮箱地址
     * @param mail
     * @param userId
     * @return
     * @throws Exception
     */
    @Log("系统用户验证邮箱地址")
    @GetMapping("/verify/mail")
    public R mailVerify(@RequestParam("mail") String mail, @RequestParam("userId") Integer userId) throws Exception {
        // 判断邮箱是否已经注册
        Integer count = userService.count(Wrappers.<User>lambdaQuery().eq(User::getEmail, mail));
        if (count > 0) {
            return R.error("该邮箱地址已被注册");
        } else {
            // 绘制随机字符
            String random = "";
            for (int i = 1; i <= 50; i++) {
                random = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
            }
            //1：将随机生成的验证码放入redis中
            redisService.set(mail, random);
            User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, userId));
            // 将验证对象放置消息队列
            AccountVerify accountVerify = new AccountVerify(user.getUsername(), mail, 2, random, 2);
            Destination destination = new ActiveMQQueue("assist.queue");
            producer.sendMessage(destination, JSONUtil.toJsonStr(accountVerify));
            return R.ok(true);
        }
    }

    /**
     * 验证识别码更换邮箱
     * @param mail
     * @param verifyCode
     * @param userId
     * @return
     * @throws Exception
     */
    @Log("验证识别码更换邮箱")
    @GetMapping("/check/mail")
    public R checkMailVerify(@RequestParam("mail") String mail, @RequestParam("verifyCode") String verifyCode, @RequestParam("userId") Integer userId) throws Exception {
        //1:获取redis里面的验证码信息
        String code = redisService.get(mail);
        if (code != null) {
            //2:判断验证码是否正确
            if (!StringUtils.isEmpty(verifyCode)) {
                verifyCode = verifyCode.toLowerCase();
                code = code.toLowerCase();
                if (verifyCode.equals(code)) {
                    redisService.del(mail);
                    // 修改用户绑定邮箱
                    return R.ok(userService.update(Wrappers.<User>lambdaUpdate().set(User::getEmail, mail).eq(User::getUserId, userId)));
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
     * 验证识别码更换手机
     * @param mobile
     * @param verifyCode
     * @param userId
     * @return
     * @throws Exception
     */
    @Log("验证识别码更换手机")
    @GetMapping("/check/mobile")
    public R checkMobileVerify(@RequestParam("mobile") String mobile, @RequestParam("verifyCode") String verifyCode, @RequestParam("userId") Integer userId) throws Exception {
        //1:获取redis里面的验证码信息
        String code = redisService.get(mobile);
        if (code != null) {
            //2:判断验证码是否正确
            if (!StringUtils.isEmpty(verifyCode)) {
                verifyCode = verifyCode.toLowerCase();
                code = code.toLowerCase();
                if (verifyCode.equals(code)) {
                    // 修改用户绑定手机
                    return R.ok(userService.update(Wrappers.<User>lambdaUpdate().set(User::getMobile, mobile).eq(User::getUserId, userId)));
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


}
