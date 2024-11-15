package com.fate.archer.cos.controller.business;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.query.Producer;
import com.fate.archer.common.service.RedisService;
import com.fate.archer.common.utils.FileUtils;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.dto.query.AccountVerify;
import com.fate.archer.cos.entity.Evaluation;
import com.fate.archer.cos.entity.OrderInfo;
import com.fate.archer.cos.entity.SysSchool;
import com.fate.archer.cos.entity.UserInfo;
import com.fate.archer.cos.service.*;
import com.fate.archer.system.domain.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Date;

@Controller
@RequestMapping("/business")
@AllArgsConstructor
public class UserBusinessController {

    private final ISysSchoolService sysSchoolService;

    private final IUserInfoService userInfoService;

    private final IOrderInfoService orderInfoService;

    private final IAnnexOrderService annexOrderService;

    private final IEvaluationService evaluationService;

    private final RedisService redisService;

    private final Producer producer;

    /**
     * 学校信息联想
     *
     * @param key 关键字
     * @return 结果
     */
    @ResponseBody
    @GetMapping("/united/{key}")
    public R schoolSel(@PathVariable("key") String key) {
        return R.ok(sysSchoolService.schoolUnited(key));
    }

    /**
     * 更新用户基本信息
     *
     * @param session session
     * @param request 请求
     * @return 结果
     */
    @ResponseBody
    @PutMapping("/user/data")
    public R userInfoEdit(HttpSession session, HttpServletRequest request) {
        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user != null) {
            if (!StrUtil.hasEmpty(request.getParameter("sex")) || !StrUtil.hasEmpty(request.getParameter("school"))) {
                // 获取院校名称
                String schoolName = sysSchoolService.getSchoolById(Integer.valueOf(request.getParameter("school"))).getName();
                userInfoService.update(Wrappers.<UserInfo>lambdaUpdate().set(UserInfo::getSchool, request.getParameter("school"))
                        .set(UserInfo::getSchoolName, schoolName)
                        .set(UserInfo::getSex, request.getParameter("sex")).eq(UserInfo::getId, user.getId()));
                // 更新session
                session.setAttribute("user", userInfoService.getById(user.getId()));
                return R.ok(true);
            }
        }
        return R.ok(false);
    }

    /**
     * 发送手机验证码
     *
     * @param phone 手机号码
     * @return 结果
     * @throws Exception 异常
     */
    @ResponseBody
    @PostMapping("/sendVerify")
    public R sendVerifyCode(@RequestParam("phone") String phone) throws Exception {
        // 判断手机号码是否已被注册
        int count = userInfoService.count(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getPhone, phone));
        if (count > 0) {
            return R.error("该手机号码已被注册");
        } else {
            // 绘制随机字符
            String random = "";
            for (int i = 1; i <= 50; i++) {
                random = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
            }
            //1：将随机生成的验证码放入redis中
            redisService.set(phone, random);
            // 将验证对象放置消息队列
            AccountVerify accountVerify = new AccountVerify("用户", phone, 1, random, 2);
            Destination destination = new ActiveMQQueue("assist.queue");
            producer.sendMessage(destination, JSONUtil.toJsonStr(accountVerify));
            return R.ok(true);
        }
    }

    /**
     * 绑定手机号码
     *
     * @param mobile     手机号码
     * @param verifyCode 验证码
     * @param session    session
     * @return 结果
     * @throws Exception 异常
     */
    @ResponseBody
    @PostMapping("/check/mobile")
    public R checkMobileVerify(@RequestParam("mobile") String mobile, @RequestParam("verifyCode") String verifyCode, HttpSession session) throws Exception {
        UserInfo user = (UserInfo) session.getAttribute("user");
        //1:获取redis里面的验证码信息
        String code = redisService.get(mobile);
        if (code != null) {
            //2:判断验证码是否正确
            if (!StringUtils.isEmpty(verifyCode)) {
                verifyCode = verifyCode.toLowerCase();
                code = code.toLowerCase();
                if (verifyCode.equals(code)) {
                    redisService.del(mobile);
                    userInfoService.update(Wrappers.<UserInfo>lambdaUpdate().set(UserInfo::getPhone, mobile).eq(UserInfo::getCode, user.getCode()));
                    // 更新session
                    session.setAttribute("user", userInfoService.getById(user.getId()));
                    // 修改用户绑定手机
                    return R.ok(true);
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
     * 用户历史订单
     *
     * @param pageNo   当前页数
     * @param userCode 用户编号
     * @return 结果
     */
    @ResponseBody
    @GetMapping("/orderHistory")
    public R orderHistoryByUserCodePage(@RequestParam("pageNo") Integer pageNo, @RequestParam("userCode") String userCode) {
        Page<OrderInfo> page = new Page<>();
        page.setCurrent(pageNo);
        return R.ok(orderInfoService.page(page, Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getUserCode, userCode)));
    }

    /**
     * 根据订单编号获取详细信息
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @ResponseBody
    @GetMapping("/orderInfoByCode")
    public R orderInfoByCode(@RequestParam("orderCode") String orderCode) {
        if (orderCode.contains(BusinessConstant.ANNEX_ORDER)) {
            return R.ok(annexOrderService.annexOrderInfoByCode(orderCode));
        } else if (orderCode.contains(BusinessConstant.COMMON_ORDER)) {
            return R.ok(orderInfoService.orderInfoByCode(orderCode));
        } else {
            return R.ok();
        }
    }

    /**
     * 添加订单评价
     *
     * @param orderCode 订单编号
     * @param score     评价分数
     * @param content   内容
     * @param pic       图片
     * @param session   session
     * @return 结果
     */
    @ResponseBody
    @PostMapping("/evaluationOrder")
    public R evaluationOrder(@RequestParam("orderCode") String orderCode, @RequestParam("score") BigDecimal score,
                             @RequestParam("content") String content, @RequestParam(value = "pic", required = false) String pic, HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute("user");
        Evaluation evaluation = new Evaluation();
        evaluation.setContent(content);
        evaluation.setDate(DateUtil.formatDateTime(new Date()));
        evaluation.setOrderCode(orderCode);
        evaluation.setPic(pic);
        evaluation.setScore(score);
        evaluation.setDeleteFlag(0);
        evaluation.setUserCode(user.getCode());
        return R.ok(evaluationService.save(evaluation));
    }

    /**
     * 文件
     *
     * @param file 文件
     * @return 结果
     */
    @ResponseBody
    @PostMapping("/fileUpload")
    public String upload(@RequestParam("avatar") MultipartFile file) {
        // 1定义要上传文件 的存放路径
        String localPath = "" + BusinessConstant.FILEADDRESS + "db/image";
        // 2获得文件名字
        String fileName = file.getOriginalFilename();
        // 2上传失败提示
        String warning = "";
        String newFileName = FileUtils.upload(file, localPath, fileName);
        if (newFileName != null) {
            //上传成功
            warning = newFileName;
        } else {
            warning = "上传失败";
        }
        System.out.println(warning);
        return warning;
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param response 对象
     */
    @ResponseBody
    @GetMapping("download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(Files.newInputStream(new File(BusinessConstant.FILEADDRESS + "file/" + fileName).toPath()));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 附件下载
     *
     * @param fileName 文件名称
     * @param response 返回
     */
    @ResponseBody
    @GetMapping("downloadAnnex/{fileName}")
    public void downloadAnnex(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(Files.newInputStream(new File(BusinessConstant.ANNEXADDRESS + "" + fileName).toPath()));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
