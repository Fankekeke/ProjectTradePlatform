package com.fate.archer.job.service;

import com.fate.archer.common.domain.QueryRequest;
import com.fate.archer.job.domain.Job;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface JobService extends IService<Job> {

    Job findJob(Long jobId);

    IPage<Job> findJobs(QueryRequest request, Job job);

    void createJob(Job job);

    void updateJob(Job job);

    void deleteJobs(String[] jobIds);

    int updateBatch(String jobIds, String status);

    void run(String jobIds);

    void pause(String jobIds);

    void resume(String jobIds);

}
