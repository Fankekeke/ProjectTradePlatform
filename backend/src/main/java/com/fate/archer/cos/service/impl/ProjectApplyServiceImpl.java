package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.ProjectApply;
import com.fate.archer.cos.dao.ProjectApplyMapper;
import com.fate.archer.cos.entity.ProjectInfo;
import com.fate.archer.cos.entity.TeamInfo;
import com.fate.archer.cos.service.IProjectApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.archer.cos.service.IProjectInfoService;
import com.fate.archer.cos.service.ITeamInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectApplyServiceImpl extends ServiceImpl<ProjectApplyMapper, ProjectApply> implements IProjectApplyService {

    private final ITeamInfoService teamInfoService;

    private final IProjectInfoService projectInfoService;

    @Override
    public IPage<LinkedHashMap<String, Object>> projectApplyPage(Page page, ProjectApply projectApply) {
        return baseMapper.projectApplyPage(page, projectApply);
    }

    @Override
    public void buildTheProject(ProjectApply projectApply, BigDecimal price) {
        // 根据用户ID获取TEAM信息
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, projectApply.getAuthor().longValue()));
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setCode("PRO-" + System.currentTimeMillis());
        projectInfo.setAuthor(teamInfo.getId());
        projectInfo.setAuthorName(teamInfo.getPlayers());
        projectInfo.setProName(projectApply.getProName());
        projectInfo.setProIntroduce(projectApply.getProIntroduce());
        projectInfo.setProPic(projectApply.getProPic());
        projectInfo.setProPrice(price);
        projectInfo.setProTechnology(projectApply.getProTechnology());
        projectInfo.setProVoide(projectApply.getProVoide());
        projectInfo.setProDowbloadZip(projectApply.getProDowbloadZip());
        projectInfo.setProType(projectApply.getProType());
        projectInfo.setProSort(projectApply.getProSort());
        projectInfo.setVisits(0);
        projectInfo.setTurnover(0);
        projectInfo.setReleaseTime(DateUtil.formatDateTime(new Date()));
        projectInfo.setDelFlag(0);
        projectInfoService.save(projectInfo);
    }
}
