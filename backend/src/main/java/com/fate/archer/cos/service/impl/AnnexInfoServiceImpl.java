package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.common.utils.CloneUtils;
import com.fate.archer.common.utils.FebsUtil;
import com.fate.archer.common.utils.FileDownload;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.dao.*;
import com.fate.archer.cos.entity.*;
import com.fate.archer.cos.entity.vo.AnnexDetailVo;
import com.fate.archer.cos.entity.vo.ProductTradeWithSchoolVo;
import com.fate.archer.cos.service.IAnnexInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.archer.cos.service.INotificationService;
import com.fate.archer.cos.service.ITeamInfoService;
import com.fate.archer.system.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnexInfoServiceImpl extends ServiceImpl<AnnexInfoMapper, AnnexInfo> implements IAnnexInfoService {

    private final ITeamInfoService teamInfoService;

    private final INotificationService notificationService;

    private final TeamInfoMapper teamInfoMapper;

    private final UserInfoMapper userInfoMapper;

    private final AnnexOrderMapper annexOrderMapper;

    @Override
    public IPage<LinkedHashMap<String, Object>> page(Page<AnnexInfo> page, AnnexInfo annexInfo) {
        return baseMapper.page(page, annexInfo);
    }

    @Override
    public Boolean annexSave(AnnexInfo annexInfo) {
        // 根据系统用户ID获取teamId
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, annexInfo.getUserId()));
        if (teamInfo != null) {
            annexInfo.setCode("ANX-" + System.currentTimeMillis());
            // 设置附件审核状态
            annexInfo.setStatus(BusinessConstant.ANNEX_AUDIT_STATUS_UNDER_REVIEW);
            // 设置附件启用状态
            annexInfo.setIsEnable(BusinessConstant.ANNEX_ENABLE_STATUS_YES);

            annexInfo.setAnnexAnther(teamInfo.getId());
            annexInfo.setAnnexLocal(annexInfo.getAnnexLocal());
            annexInfo.setUploadDate(DateUtil.formatDateTime(new Date()));
            return this.save(annexInfo);
        } else {
            return false;
        }
    }

    @Override
    public void download(String fileName, HttpServletResponse response) {
        // 下载附件
        FileDownload.download(BusinessConstant.FILEADDRESS + "db/annex/", fileName, response);
    }

    /**
     * 校验用户是否对此附件有下载权限
     *
     * @param annexCode 附件编号
     * @return 校验结果
     * @throws Exception 结果
     */
    public Boolean checkUserAnnex(String annexCode) throws Exception {
        // 获取当前登录用户
        User user = FebsUtil.getCurrentUser();
        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, user.getUsername()));
        if (userInfo == null) {
            throw new FebsException("用户不存在！");
        }

        // 校验用户是否对此附件进行购买
        Integer buyCount = annexOrderMapper.selectCount(Wrappers.<AnnexOrder>lambdaQuery()
                .eq(AnnexOrder::getAnnexCode, annexCode).eq(AnnexOrder::getUserCode, userInfo.getCode()));
        return buyCount > 0;
    }

    @Override
    public Boolean updateStatus(Integer id, Integer status) {
        // 获取附件作者信息
        AnnexInfo annexInfo = this.getOne(Wrappers.<AnnexInfo>lambdaQuery().eq(AnnexInfo::getId, id));
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getId, annexInfo.getAnnexAnther()));
        String content = "";

        // 判断审核状态
        if (status.equals(BusinessConstant.ANNEX_AUDIT_STATUS_PASS)) {
            content = "您上传的附件《" + annexInfo.getAnnexName() + "》 已通过管理员审核！😀";
        } else {
            content = "您上传的附件《" + annexInfo.getAnnexName() + "》 未通过管理员审核！😥，请修改后调整";
        }

        // 消息通知
        notificationService.setNotification("《" + annexInfo.getAnnexName() + "》附件审核", content, Integer.parseInt(teamInfo.getAdminUser().toString()), BusinessConstant.NOTIFICATION);
        return this.update(Wrappers.<AnnexInfo>lambdaUpdate().set(AnnexInfo::getStatus, status).eq(AnnexInfo::getId, id));
    }

    /**
     * 门户端附件查询
     *
     * @param page     分页对象
     * @param sortType 排序方式
     * @param key      关键字
     * @return 附件列表
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectAnnexPage(Page<AnnexInfo> page, Integer sortType, String key) {
        return baseMapper.selectAnnexPage(page, sortType, key);
    }

    /**
     * 附件详情
     *
     * @param annexCode 附件编号
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public AnnexDetailVo selectAnnexDetail(String annexCode) throws Exception {
        AnnexInfo annexInfo = this.getOne(Wrappers.<AnnexInfo>lambdaQuery().eq(AnnexInfo::getCode, annexCode));
        if (annexInfo == null) {
            throw new FebsException("附件信息不存在！");
        }
        AnnexDetailVo annexDetailVo = CloneUtils.clone(annexInfo, AnnexDetailVo.class);

        // 附件作者
        if (annexInfo.getAnnexAnther() != null) {
            TeamInfo teamInfo = teamInfoMapper.selectById(annexInfo.getAnnexAnther());
            if (teamInfo != null) {
                annexDetailVo.setAuthorName(teamInfo.getPlayers());
            }
        }
        return annexDetailVo;
    }

    /**
     * 根据用户学校信息获取附件交易情况
     *
     * @param userCode  用户编号
     * @param annexCode 附件编号
     * @return 附件交易情况
     * @throws Exception 异常
     */
    @Override
    public List<ProductTradeWithSchoolVo> selectAnnexTradeByUser(String userCode, String annexCode) throws Exception {
        if (StrUtil.isBlank(userCode) || StrUtil.isBlank(annexCode)) {
            throw new FebsException("参数校验失败！");
        }

        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, userCode));
        if (userInfo == null) {
            throw new FebsException("用户信息不存在！");
        }
        if (StrUtil.isBlank(userInfo.getSchool())) {
            throw new FebsException("未绑定学校信息！");
        }

        // 此学校的用户
        List<UserInfo> userInfoList = userInfoMapper.selectList(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getSchool, userInfo.getSchool()));
        if (CollectionUtils.isEmpty(userInfoList)) {
            return Collections.emptyList();
        }
        List<String> userCodeList = userInfoList.stream().map(UserInfo::getCode).collect(Collectors.toList());

        // 获取附件订单
        List<AnnexOrder> orderList = annexOrderMapper.selectList(Wrappers.<AnnexOrder>lambdaQuery().eq(AnnexOrder::getAnnexCode, annexCode).in(AnnexOrder::getUserCode, userCodeList));
        return orderList.stream().map(e -> new ProductTradeWithSchoolVo(userInfo.getSchoolName(), e.getOrderTime(), userInfo.getAccount(), userInfo.getHeadImgUrl())).collect(Collectors.toList());
    }
}
