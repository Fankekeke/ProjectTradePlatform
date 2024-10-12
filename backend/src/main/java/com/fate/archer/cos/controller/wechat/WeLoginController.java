package com.fate.archer.cos.controller.wechat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.utils.*;
import com.fate.archer.cos.entity.AccessInfo;
import com.fate.archer.cos.entity.UserInfo;
import com.fate.archer.cos.entity.WechatUserInfo;
import com.fate.archer.cos.service.IAccessInfoService;
import com.fate.archer.cos.service.IProjectInfoService;
import com.fate.archer.cos.service.IUserInfoService;
import com.fate.archer.system.controller.LoginController;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FanK
 * 微信授权登陆接口
 */
@RestController
@RequestMapping("/wechat")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeLoginController {

    private final IUserInfoService userInfoService;

    private final IProjectInfoService projectInfoService;

    private final IAccessInfoService accessInfoService;

    private final LoginController loginController;

    @Value("${oauth.wx.appId}")
    private String appId;

    @Value("${oauth.wx.appSecret}")
    private String appSecret;

    @Value("${oauth.wx.http}")
    private String http;

    /**
     * 授权登陆后通过Socket通知前端
     * @param params
     * @return params
     */
    @PostMapping(value = "/sendPost")
    public String sendPost(@RequestBody Map<String, Object> params) {
        if (params == null || params.get("userId") == null) {
            return "error";
        }
        SocketServer.sendMessage(JSONUtil.toJsonStr(params), params.get("userId").toString());
        return "success";
    }

    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        String renderUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(renderUrl);
    }

    @RequestMapping("/callback")
    public void login(AuthCallback callback) throws Exception {
        AuthUser authUser = null;
        AuthRequest authRequest = getAuthRequest();
        AuthResponse response = authRequest.login(callback);
        UserInfo user = new UserInfo();
        if(response.getData() instanceof AuthUser) {
            authUser =  (AuthUser)response.getData();
            //插入数据
            WechatUserInfo wechatUser = new WechatUserInfo();
            wechatUser.setOpenId(authUser.getRawUserInfo().get("openid").toString());
            wechatUser.setNickName(authUser.getRawUserInfo().get("nickname").toString());
            if (authUser.getRawUserInfo().get("sex") == null || "".equals(authUser.getRawUserInfo().get("sex").toString())) {
                wechatUser.setSex(0);
            } else {
                wechatUser.setSex(Integer.parseInt(authUser.getRawUserInfo().get("sex").toString()));
            }
            wechatUser.setHeadImgUrl(authUser.getRawUserInfo().get("headimgurl").toString());
            user = userInfoService.wxCallBack(wechatUser);
        }
        // 添加访问记录
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        accessInfoService.save(new AccessInfo(ip, AddressUtil.getCityInfo(ip), DateUtil.formatDateTime(new Date())));

        // 返回给前端信息
        LinkedHashMap<String, Object> tokenData = new LinkedHashMap<String, Object>();
        tokenData.put("userId", callback.getState());
        tokenData.put("token", loginController.login(user.getOpenId(), BusinessConstant.DEFAULT_PASSWORD, request));
        tokenData.put("userData", user);
        HttpRequest.post("http://127.0.0.1:9528/wechat/sendPost")
                .body(JSONUtil.toJsonStr(tokenData))
                .execute().body();
    }

    /**
     * 获取微信授权请求
     * @return AuthRequest
     */
    private AuthRequest getAuthRequest() {
        return new AuthWeChatOpenRequest(AuthConfig.builder()
                .clientId(appId)
                .clientSecret(appSecret)
                .redirectUri(http)
                .ignoreCheckState(true)
                .build());
    }

}
