package com.kd.appelements.add;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Elements {
    //日期、时间、温度、湿度、风向、风速度、降水、气压
    String date;
    String time;
    String wd;
    String max_wd;
    String min_wd;
    String sd;
    String min_sd;
    String fx;
    String fs;
    String js;
    String qy;
    String njd;
    String wea;
    String bright;
    String title;
    int index;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Elements(String title, String date, String time, String wd, String max_wd, String min_wd, String sd, String min_sd, String fx, String fs, String js, String qy) {
        this.date = date;
        this.time = time;
        this.wd = wd;
        this.sd = sd;
        this.max_wd = max_wd;
        this.min_wd = min_wd;
        this.min_sd = min_sd;
        this.fx = fx;
        this.fs = fs;
        this.js = js;
        this.qy = qy;
        this.title = title;

    }

    public Elements(String title, String date, String time, String wd, String max_wd, String min_wd, String sd, String min_sd, String fx, String fs, String js, String qy, String njd) {
        this.date = date;
        this.time = time;
        this.wd = wd;
        this.max_wd = max_wd;
        this.min_wd = min_wd;
        this.min_sd = min_sd;
        this.sd = sd;
        this.fx = fx;
        this.fs = fs;
        this.js = js;
        this.qy = qy;
        this.title = title;
        this.njd = njd;
    }

    public Elements(String date, String time, String wd, String sd, String fx, String fs, String js, String qy, String njd, String wea, String bright, int index) {
        this.date = date;
        this.time = time;
        this.wd = wd;
        this.sd = sd;
        this.fx = fx;
        this.fs = fs;
        this.js = js;
        this.qy = qy;
        this.njd = njd;
        this.wea = wea;
        this.bright = bright;
        this.index = index;

    }

    @Override
    public String toString() {
        return time + "\n"
                + "温度:" + wd + "℃\n"
                + "相对湿度:" + sd + "%\n"
                + "风向:" + fx + "\n"
                + "风速:" + fs + "m/s\n"
                + "降水:" + js + "mm\n"
                + "气压:" + qy + "hPa";
    }

    public String getElementString() {
        return "温度:" + wd + "℃\n"
                + "湿度:" + sd + "%RH\n"
                + "风向:" + fx + "\n"
                + "风速:" + fs + "m/s\n"
                + "降水:" + js + "mm\n"
                + "气压:" + qy + "hPa\n";
    }

    public int getElementindex() {
        return index;
    }

    public String getElementLeftText() {
        return "温度:" + wd + "℃\n"
                + "风速:" + fs + "m/s\n"
                + "雨量:" + js + "mm\n"
               ;
    }
    public String getSevenElementLeftText() {
        return "温度:" + wd + "℃\n"
                + "风速:" + fs + "m/s\n"
                + "雨量:" + js + "mm\n"
                + "能见度:" + njd + "m\n"
                ;
    }
    public String getFiveElementLeftText() {
        return "温度:" + wd + "℃\n"
                + "湿度:" + sd + "%\n"
                + "雨量:" + js + "mm\n"
               ;
    }
    public String getFiveElementRightText() {
        return "风速:" + fs + "m/s\n"
                + "风向:" + fx + "\n"
                +"\n"
                ;
    }
    public String getEightElementLeftText() {
        return "当前湿度:" + sd + "%\n"
                +  "最小湿度:" + min_sd + "%\n"
                + "小时雨量:" + js + "mm\n"
                +"风速:" + fs + "m/s\n";
    }
    public String getEightElementRightText() {
        return "当前温度:" + wd + "℃\n"
                +  "风向:" + fx + "\n"
                +  "最高温度："+max_wd+"℃\n"
                +"最低温度："+min_wd+"℃\n";
    }
    public String getSevenElementRightText() {
        return "相对湿度:" + sd + "%\n"
                + "风向:" + fx + "\n"
                + "气压:" + qy + "hPa\n";
    }
    public String getElementRightText() {
        return "相对湿度:" + sd + "%\n"
                + "风向:" + fx + "\n"
                + "气压:" + qy + "hPa\n\n";
    }
    public String getFourElementRightText() {
        return "风速:" + fs + "m/s\n"
                + "风向:" + fx + "\n";
    }
    public String getFourElementLeftText() {
        return "温度:" + wd + "℃\n"
                + "雨量:" + js + "mm\n";
    }
    public int getbrightness11() {
        int x = 0xccccccff;
        if (!TextUtils.isEmpty(bright)) {
            if (Integer.valueOf(bright) == 1) {
                x = 0xbbbbbbff;
            }
            if (Integer.valueOf(bright) == 2) {
                x = 0xddddddff;
            }
            if (Integer.valueOf(bright) == 3) {
                x = 0xeeeeeeff;
            }
            if (Integer.valueOf(bright) == 4) {
                x = 0xffffffff;
            }
        }
        return x;
    }

    public String getElementWdAndYl() {
        return "温度:" + wd + "℃\n"
                + "雨量:" + js + "mm\n";
    }

    public String getElementFsAndFx() {
        return "风向:" + fx + "\n"
                + "风速:" + fs + "m/s\n";
    }

    public String getElementNjd() {
        return "能见度:" + njd + "m\n";
    }

    //hu
    public String getWea() {
        return wea;
    }

    static int cnt = 0;

    public String getYear() {
        Log.i("TAG", "date:" + date);
        String[] date = this.date.split("-");
        String[] time = this.time.split(":");
        if (cnt <= 10) {
            cnt++;
            if (cnt == 8) {
                int year1 = Integer.valueOf(date[0]);
                int month1 = Integer.valueOf(date[1]) - 1;
                int Day1 = Integer.valueOf(date[2]);
                int hour1 = Integer.valueOf(time[0]);
                int minute1 = Integer.valueOf(time[1]);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year1);
                c.set(Calendar.MONTH, month1);//从0开始，0表是1月，1表示2月依bai次类du推
                c.set(Calendar.DAY_OF_MONTH, Day1);
                c.set(Calendar.HOUR_OF_DAY, hour1);
                c.set(Calendar.MINUTE, minute1);
                c.set(Calendar.SECOND, 0);
                Log.i("TAG_", "系统时间校正=====" + sdf.format(c.getTime()));
            }
        }
        String week = "";
        try {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            String time1 = "";
            for (int i = 0; i < date.length; i++) {
                if (i == date.length - 1) {
                    time1 += date[i];
                } else {
                    time1 += date[i] + "-";
                }
            }
            Date date1 = ft.parse(time1);
            week = getWeek(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }//"语溪小学气象站\n"+
        return date[0] + "年" + date[1] + "月" + date[2] + "日" + " " + week + " " + this.time;
    }

    //根据日期取得星期几

    public static String getWeek(Date date) {

        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;

        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];

    }
}

