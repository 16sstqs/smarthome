package com.fudan.smarthome.Utils;

import android.nfc.Tag;
import android.util.Log;

public class TimeStringToMillisecondUtil {
    private static final String TAG = "MillisecondUtil";

    public static int getMillisecond(String timeString) {
        int base = 1000;
        if (timeString.contains("分")) {
            base *= 60;
            base *= solve(timeString);
        } else if (timeString.contains("秒")) {
            base *= solve(timeString);
        }
        Log.i(TAG, "指令延迟执行时间：" + base);
        return base;
    }

    private static int solve(String s) {
        if (s == null || "".equals(s)) {
            return 0;
        }
        int i = s.indexOf("万");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            int r = solve(s.substring(i + 1));
            return l * 10000 + r;
        }
        i = s.indexOf("千");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            int r = solve(s.substring(i + 1));
            return l * 1000 + r;
        }
        i = s.indexOf("百");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            int r = solve(s.substring(i + 1));
            return l * 100 + r;
        }
        i = s.indexOf("十");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            if (l == 0) {
                l = 1;
            }
            int r = solve(s.substring(i + 1));
            return l * 10 + r;
        }
        i = s.indexOf("零");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            int r = solve(s.substring(i + 1));
            return l + r;
        }
        i = 0;
        switch (s.charAt(0)) {
            case '九':
                return 9;
            case '八':
                return 8;
            case '七':
                return 7;
            case '六':
                return 6;
            case '五':
                return 5;
            case '四':
                return 4;
            case '三':
                return 3;
            case '二':
                return 2;
            case '一':
                return 1;
        }
        return 0;
    }
}
