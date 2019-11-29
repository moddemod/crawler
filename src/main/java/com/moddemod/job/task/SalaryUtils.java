package com.moddemod.job.task;

import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class SalaryUtils {

    public static Integer[] getSalary(String salaryInfo) {

        Integer[] salary = new Integer[2];
        String date = salaryInfo.substring(salaryInfo.length() - 1,salaryInfo.length());
        // 如果是天，则直接乘以240计算
        if (!"月".equals(date) && !"年".equals(date)) {
            salaryInfo = salaryInfo.substring(0, salaryInfo.length() - 2);
            salary[0] = salary[1] = str2Num(salaryInfo, 240);
            return salary;
        }
        String unit = salaryInfo.substring(salaryInfo.length() - 3, salaryInfo.length() - 2);
        String[] salarys = salaryInfo.substring(0, salaryInfo.length() - 3).split("-");
        Float min, max;
        try {
            min = (float)Integer.valueOf(salarys[0]);
            max = (float)Integer.valueOf(salarys[1]);
        } catch (Exception e) {
            min = Float.parseFloat(salarys[0]);
            max = Float.parseFloat(salarys[1]);
        }


        if ("万".equals(unit)) {
               min = min * 10000;
               max = max * 10000;
        } else {
            // 否则就是千
            min = min * 1000;
            max = max * 1000;
        }

        if ("年".equals(date)) {
            salary[0] = (int)(min * 1);
            salary[1] = (int)(max * 1);
            return salary;
        }
        // 否则就是月
        salary[0] = (int)(min * 12);
        salary[1] = (int)(max * 12);

        return salary;
    }

    private static Integer str2Num(String salaryInfo, int num) {
        String[] r = salaryInfo.split("/");
        Integer n = Integer.valueOf(r[0]);
        return n * num;
    }

}
