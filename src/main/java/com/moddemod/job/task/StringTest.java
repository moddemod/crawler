package com.moddemod.job.task;

public class StringTest {

    public static void main(String[] args) {
        String str = "合肥-高新区    无工作经验    大专    招2人    11-24发布";
        String str1 = "    ";
        System.out.println(str1.length());
        String[] list = str.split("    ");
        for (String ss :
                list) {
            System.out.println(ss);
        }
        System.out.println(list[0]);

    }
}
