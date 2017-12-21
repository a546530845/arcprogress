package com.zxg.arcprogress.widget;

import android.content.Context;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/9/27.
 * 千位分隔符
 */
public class DecimalUtil {
    /**
     * 返回千位分隔符的钱
     * @param mMoney
     * @return
     */
    public static String getMoneyDecimal(Object mMoney){
        DecimalFormat df = new DecimalFormat("#,##0.00");
//        DecimalFormat df = new DecimalFormat("#,###.##");
        String m = df.format(mMoney);
        return m;
    }
    public static String getRateDecimal(Object mMoney){
        DecimalFormat df = new DecimalFormat("###0.00");
//        DecimalFormat df = new DecimalFormat("#,###.##");
        String m = df.format(mMoney);
        return m;
    }

    /**
     * 得到保留0位小数
     * @param mMoney
     * @return
     */
    public static String getMoneyDecimalforInteger(double mMoney){
        DecimalFormat df = new DecimalFormat("#,##0");
        String m = df.format(mMoney);
        return m;
    }

    /**
     * 12位数字，如果不够，则以0填补
     * @param mMoney
     * @return
     */
    public static String getPayAmountMoneyDecimal(Object mMoney){
        DecimalFormat df = new DecimalFormat("000000000000");
        String m = df.format(mMoney);
        return m;
    }
    public static boolean isNumber (String mMoney, Context mContext){
        try {
            Double.parseDouble(mMoney);
        }catch (Exception e){
            Toast.makeText(mContext,"金额不是数字类型",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 科学计数法转化为纯数字
     * @param money
     * @return
     */
    public static String getBigDecimal(Object money){
        BigDecimal bd = null;
        try {
            bd = new BigDecimal(money+"");
        }catch (Exception e){
            return "****";
        }

        return getMoneyDecimal_twoPoint(bd);
    }
    public static String getMoneyDecimal_twoPoint(Object mMoney){
        DecimalFormat df = new DecimalFormat("######0.00");
        String m = df.format(mMoney);
        return m;
    }

    /**
     * 科学计数法转化为纯数字
     * @param money
     * @return
     */
    public static BigDecimal getBigDecimal_bigDecimal(Object money){
        BigDecimal bd = null;
        try {
            bd = new BigDecimal(money+"");
        }catch (Exception e){
            return BigDecimal.ZERO;
        }

        return bd;
    }
}
