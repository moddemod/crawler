package com.moddemod.job.task;

import com.moddemod.job.pojo.JobInfo;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    private String url = "https://search.51job.com/list/150200,000000,0000,00,9,99,Java%2B%25E9%25AB%2598%25E7%25BA%25A7,2,1.html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";
    @Override
    public void process(Page page) {
        List<Selectable> list = page.getHtml().css("div#resultList div.el").nodes();

        if (list.size() == 0) {
            // 如果列表为空，表示这是招聘详情页，获取招聘信息，保存数据
            this.saveJobInfo(page);
        } else {
            // 如果不为空，表示这是列表页，解析出详情页的url地址放到任务列队列中
            for (Selectable selectable :
                    list) {
                // 获取url地址
                String jobInfoUrl = selectable.links().toString();
                page.addTargetRequest(jobInfoUrl);
            }

            // 获取下一页url
            String bkUrl = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            page.addTargetRequest(bkUrl);

        }
    }

    /**
     * 解析页面，获取招聘的详情信息
     * @param page
     */
    private void saveJobInfo(Page page) {
        // 创建招聘详情对象
        JobInfo jobInfo = new JobInfo();

        // 解析页面
        Html html = page.getHtml();
        // 获取对象，封装到对象中
        jobInfo.setUrl(page.getUrl().toString());

//        合肥    3-4年经验    本科    招2人    11-24发布
        String text = Jsoup.parse(html.css("p.ltype", "text").toString()).text();
        String[] list = text.split("    ");

        jobInfo.setCompanyAddr(list[0]);
        jobInfo.setTime(list[4].replace("发布", ""));
        jobInfo.setCompanyInfo(html.css("div.tmsg", "text").toString());
        jobInfo.setCompanyName(html.css("p.cname a", "text").toString());
        jobInfo.setJobAddr(Jsoup.parse(html.css("div.bmsg").nodes().get(1).toString()).text());
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        jobInfo.setJobName(html.css("div.cn h1", "text").toString());
        String salaryInfo = Jsoup.parse(html.css("div.cn strong").toString()).text();
        Integer[] salary = SalaryUtils.getSalary(salaryInfo);
        jobInfo.setSalaryMax(salary[1]);
        jobInfo.setSalaryMin(salary[0]);

        // 保存数据
        page.putField("jobInfo", jobInfo);
    }

    private Site site = Site.me()
            .setCharset("gbk")     // 设置编码
            .setTimeOut(10*1000)    // 设置超时时间
            .setRetrySleepTime(3000)    // 设置重试的间隔时间
            .setRetryTimes(3);          // 设置重试的次数
    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private SpringDatePipeline springDatePipeline;
    // 任务启动后等待多久执行方法
    // 每隔多久执行方法
    @Scheduled(initialDelay = 1000, fixedDelay = 100*1000)
    public void process() {
        Spider.create(new JobProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .addPipeline(this.springDatePipeline)
                .run();
    }
}
