package com.zxg.arcprogress.widget;

/**
 * Author ：zxg on 2017/8/16 16:53
 * email : remotecountry@163.com
 */

public class ProgressMaxUtil {
    /**
     * 获取传入arcprogress最大的progress
     * @param money
     * @return
     */
    public static int getProgressMax(double money){
        int progress_can_max = 10;
        if (money == 0) {
            progress_can_max = 0;
        } else if (money > 0 && money <= 10000) {
            progress_can_max = 10;
        }
        else if (money >= 300000) {
            progress_can_max = 90;
        } else {
            progress_can_max = (int)(10 +  ((money - 10000) / 290000f * 80));
        }
        return  progress_can_max;
    }

}
