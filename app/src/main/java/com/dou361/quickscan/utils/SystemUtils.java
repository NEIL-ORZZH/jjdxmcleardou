package com.dou361.quickscan.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * created by jjdxm on 2015-2-15 下午9:58:26 说明
 **/
public class SystemUtils {

    /**
     * 获取android系统版本号
     */
    public static String getOSVersion() {
        String release = Build.VERSION.RELEASE; // android系统版本号
        release = "android" + release;
        return release;
    }

    /**
     * 获得android系统sdk版本号
     */
    public static String getOSVersionSDK() {
        return Build.VERSION.SDK;
    }

    /**
     * 获得android系统sdk版本号
     */
    public static int getOSVersionSDKINT() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 根据packageName获取packageInfo
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        if (null == context) {
            return null;
        }
        if (null == packageName) {
            return null;
        }
        PackageInfo info = null;
        PackageManager manager = context.getPackageManager();
        // 根据packageName获取packageInfo
        try {
            info = manager.getPackageInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return info;
    }

    /**
     * 获取应用包名
     */
    public static String getPackageName(Context context) {
        if (null == context) {
            return null;
        }
        return context.getPackageName();
    }

    /**
     * 获取本应用的VersionCode
     */
    public static int getVersionCode(Context context) {
        PackageInfo info = getPackageInfo(context, getPackageName(context));
        if (info != null) {
            return info.versionCode;
        } else {
            return -1;
        }
    }

    /**
     * 获取本应用的VersionName
     */
    public static String getVersionName(Context context) {
        PackageInfo info = getPackageInfo(context, getPackageName(context));
        if (info != null) {
            return info.versionName;
        } else {
            return null;
        }
    }

}
