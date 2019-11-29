package com.moddemod.job.task;

import com.moddemod.job.pojo.JobInfo;
import com.moddemod.job.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDatePipeline implements Pipeline {

    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        // 获取封装好的对象
        JobInfo jobInfo = resultItems.get("jobInfo");

        if (jobInfo != null) {
            this.jobInfoService.save(jobInfo);
        }

    }
}
