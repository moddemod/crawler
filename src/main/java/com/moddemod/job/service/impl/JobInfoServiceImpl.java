package com.moddemod.job.service.impl;

import com.moddemod.job.dao.JobInfoDao;
import com.moddemod.job.pojo.JobInfo;
import com.moddemod.job.service.JobInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoDao jobInfoDao;

    @Override
    @Transient
    public void save(JobInfo jobInfo) {
        // 根据url和发布时间查询数据
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());

        // 执行查询
        List<JobInfo> list = this.findJobInfo(param);

        // 如果查询为空，就新增或更新数据库
        if (list.size() == 0) {
            this.jobInfoDao.saveAndFlush(jobInfo);
        }

    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        // 设置查询条件
        Example<JobInfo> example = Example.of(jobInfo);

        // 执行查询
        List<JobInfo> list = this.jobInfoDao.findAll(example);
        return list;
    }
}
