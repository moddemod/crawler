
# 基于WebMagic框架的爬虫

- maven
- WebMagic
    - http://webmagic.io/
- MySQL
- SpringBoot


## 配置

```
# DB Configuration
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://host:port/?useSSL=false&serverTimezone=GMT%2B8
spring.datasource.username=name
spring.datasource.password=password

#JPA Configuration
spring.jpa.database=MySQL
spring.jpa.show-sql=true
```

## MySQL

```mysql
DROP TABLE IF EXISTS `job_info`;
CREATE TABLE `job_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `company_name` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `company_addr` varchar(200) DEFAULT NULL COMMENT '公司联系方式',
  `company_info` text COMMENT '公司信息',
  `job_name` varchar(100) DEFAULT NULL COMMENT '职位信息',
  `job_addr` varchar(50) DEFAULT NULL COMMENT '工作地点',
  `job_info` text COMMENT '职位信息',
  `salary_min` int(10) DEFAULT NULL COMMENT '薪资最小',
  `salary_max` int(10) DEFAULT NULL COMMENT '薪资最大',
  `url` varchar(150) DEFAULT NULL COMMENT '招聘信息详情页',
  `time` varchar(10) DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8 COMMENT='招聘信息';

```

## example


![image](https://github.com/moddemod/images/blob/master/show_crawler.png)