package com.dou361.quickscan.utils;

import android.util.Log;

import com.dou361.quickscan.bean.LogTagName;

import java.util.List;

public class LogUtils {

    /**
     * 日志输出级别NONE
     */
    public static final int LEVEL_NONE = 0;
    /**
     * 日志输出级别V
     */
    public static final int LEVEL_VERBOSE = 1;
    /**
     * 日志输出级别D
     */
    public static final int LEVEL_DEBUG = 2;
    /**
     * 日志输出级别I
     */
    public static final int LEVEL_INFO = 3;
    /**
     * 日志输出级别W
     */
    public static final int LEVEL_WARN = 4;
    /**
     * 日志输出级别E
     */
    public static final int LEVEL_ERROR = 5;

    /**
     * 日志输出时的TAG
     */
    private static String mTag = "dou361";
    /**
     * 当前日志输出级别
     */
    private static int mDebuggable = LEVEL_ERROR;
    /**
     * 当前日志是否输出
     */
    private static boolean mIsPrintLog = true;

    /**
     * 用于记时的变量
     */
    private static long mTimestamp = 0;
    /**
     * 写文件的锁对象
     */
    private static final Object mLogLock = new Object();

    /**
     * 根据当前级别mDebuggable的形式输出LOG，并且打印异常信息
     */
    public static void log(String msg, Throwable e) {
        if (mIsPrintLog) {
            switch (mDebuggable) {
                case LEVEL_NONE:
                    Log.d(mTag, msg, e);
                    break;
                case LEVEL_VERBOSE:
                    Log.v(mTag, msg, e);
                    break;
                case LEVEL_DEBUG:
                    Log.d(mTag, msg, e);
                    break;
                case LEVEL_INFO:
                    Log.i(mTag, msg, e);
                    break;
                case LEVEL_WARN:
                    Log.w(mTag, msg, e);
                    break;
                case LEVEL_ERROR:
                    Log.e(mTag, msg, e);
                    break;
            }
        }
    }

    /**
     * 添加二级Tag，该方法需要在打印日志的类中获得类对象，比较方便的是在类的父类中获取即可
     */
    public static synchronized LogTagName tag(String tag) {
        LogTagName mTagName = new LogTagName();
        mTagName.setSecondTag(tag + "|<----->|>>");
        return mTagName;
    }

    /**
     * 根据当前级别mDebuggable的形式输出LOG
     */
    public static void log(String msg) {
        log(msg, null);
    }

    /**
     * 根据当前级别mDebuggable的形式输出LOG
     */
    public static void log(Throwable e) {
        log("", e);
    }

    /**
     * 输出msg信息,附带时间戳，用于输出一个时间段起始点 @param msg 需要输出的msg
     */
    public static void logStart(String msg) {
        mTimestamp = System.currentTimeMillis();
        log("[Started：" + mTimestamp + "]" + msg);
    }

    /**
     * 输出msg信息,附带时间戳，用于输出一个时间段结束点* @param msg 需要输出的msg
     */
    public static void logElapsed(String msg) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - mTimestamp;
        mTimestamp = currentTime;
        log("[Elapsed：" + elapsedTime + "]" + msg);
    }

    /**
     * 打印list
     */
    public static <T> void printList(List<T> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        int size = list.size();
        log("---begin---");
        for (int i = 0; i < size; i++) {
            log(i + ":" + list.get(i).toString());
        }
        log("---end---");
    }

    /**
     * 打印array
     */
    public static <T> void printArray(T[] array) {
        if (array == null || array.length < 1) {
            return;
        }
        int length = array.length;
        log("---begin---");
        for (int i = 0; i < length; i++) {
            log(i + ":" + array[i].toString());
        }
        log("---end---");
    }
}
