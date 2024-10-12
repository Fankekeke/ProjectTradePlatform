package com.fate.archer.cos.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.ThesisInfo;
import com.fate.archer.cos.dao.ThesisInfoMapper;
import com.fate.archer.cos.service.IThesisInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThesisInfoServiceImpl extends ServiceImpl<ThesisInfoMapper, ThesisInfo> implements IThesisInfoService {


    /**
     * 论文定制分页查询
     *
     * @param page       分页对象
     * @param thesisInfo 定制信息
     * @return 结果
     */
    @Override
    public IPage<ThesisInfo> selectThesisInfoPage(Page page, ThesisInfo thesisInfo) {
        return baseMapper.selectThesisInfoPage(page, thesisInfo);
    }

    /**
     * 添加论文定制信息
     *
     * @param thesisInfo 定制信息
     * @return 结果
     */
    @Override
    public Boolean addThesisInfo(ThesisInfo thesisInfo) {
        thesisInfo.setCode("THESIS-" + System.currentTimeMillis());
        return this.save(thesisInfo);
    }

    /**
     * 指定论文分配人
     *
     * @param thesisCode 定制论文编号
     * @param technician 分配人
     * @return 结果
     */
    @Override
    public Boolean assigneeTechnician(String thesisCode, Long technician) {
        return this.update(Wrappers.<ThesisInfo>lambdaUpdate().set(ThesisInfo::getTechnician, technician).eq(ThesisInfo::getCode, thesisCode));
    }
}
