package com.dou361.quickscan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dou361.quickscan.bean.SPTagName;


public class SPUtils {

    /**
     * 使用单例模式来创建共享对象的变量
     */
    private static SharedPreferences mSharedPreferences;
    /**
     * 自定义创建共享对象的变量
     */
    private static SharedPreferences mCustomSP;
    /**
     * 共享文件的名称
     */
    public static final String SP_NAME = "config";

    /**
     * 初始化SharedPreferences
     */
    private static void init(Context context) {
        init(context, SP_NAME);
    }

    /**
     * 初始化SharedPreferences
     */
    private static void init(Context context, String spName) {
        if (SP_NAME.equals(spName)) {
            /** 默认的对象 */
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(spName,
                        Context.MODE_MULTI_PROCESS);
            }
        } else {
            if (!(mCustomSP != null && spName.equals(mCustomSP.getString(spName,"")))) {
                /** 同一个对象 */
            } else {
                /** 不同对象 */
                mCustomSP = context.getSharedPreferences(spName,
                        Context.MODE_MULTI_PROCESS);
                mCustomSP.edit().putString(spName, spName).commit();
            }
        }
    }

    /**
     * 添加二级Tag，该方法需要在打印日志的类中获得类对象，比较方便的是在类的父类中获取即可
     */
    public static synchronized SPTagName spTagName(String spName) {
        SPTagName mTagName = new SPTagName();
        mTagName.setSpName(spName);
        return mTagName;
    }

    /**
     * 添加键的内容
     */
    public static void putData(Context context, String key, Object data) {
        putData(context, SP_NAME, key, data);
    }

    /**
     * 获得键的内容
     */
    public static Object getData(Context context, String key, Object defValue) {
        return getData(context, SP_NAME, key, defValue);
    }

    /**
     * 添加键的内容
     */
    public static void putData(Context context, String spName, String key, Object data) {
        String type = data.getClass().getSimpleName();
        init(context, spName);
        if (SP_NAME.equals(spName)) {
            if ("Integer".equals(type)) {
                mSharedPreferences.edit().putInt(key, (Integer) data).commit();
            } else if ("Boolean".equals(type)) {
                mSharedPreferences.edit().putBoolean(key, (Boolean) data).commit();
            } else if ("String".equals(type)) {
                mSharedPreferences.edit().putString(key, (String) data).commit();
            } else if ("Float".equals(type)) {
                mSharedPreferences.edit().putFloat(key, (Float) data).commit();
            } else if ("Long".equals(type)) {
                mSharedPreferences.edit().putLong(key, (Long) data).commit();
            }
        } else {
            if ("Integer".equals(type)) {
                mCustomSP.edit().putInt(key, (Integer) data).commit();
            } else if ("Boolean".equals(type)) {
                mCustomSP.edit().putBoolean(key, (Boolean) data).commit();
            } else if ("String".equals(type)) {
                mCustomSP.edit().putString(key, (String) data).commit();
            } else if ("Float".equals(type)) {
                mCustomSP.edit().putFloat(key, (Float) data).commit();
            } else if ("Long".equals(type)) {
                mCustomSP.edit().putLong(key, (Long) data).commit();
            }
        }
    }

    /**
     * 获得键的内容
     */
    public static Object getData(Context context, String spName, String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        init(context, spName);
        if (SP_NAME.equals(spName)) {
            if ("Integer".equals(type)) {
                return mSharedPreferences.getInt(key, (Integer) defValue);
            } else if ("Boolean".equals(type)) {
                return mSharedPreferences.getBoolean(key, (Boolean) defValue);
            } else if ("String".equals(type)) {
                return mSharedPreferences.getString(key, (String) defValue);
            } else if ("Float".equals(type)) {
                return mSharedPreferences.getFloat(key, (Float) defValue);
            } else if ("Long".equals(type)) {
                return mSharedPreferences.getLong(key, (Long) defValue);
            }
        } else {
            if ("Integer".equals(type)) {
                return mCustomSP.getInt(key, (Integer) defValue);
            } else if ("Boolean".equals(type)) {
                return mCustomSP.getBoolean(key, (Boolean) defValue);
            } else if ("String".equals(type)) {
                return mCustomSP.getString(key, (String) defValue);
            } else if ("Float".equals(type)) {
                return mCustomSP.getFloat(key, (Float) defValue);
            } else if ("Long".equals(type)) {
                return mCustomSP.getLong(key, (Long) defValue);
            }
        }
        return null;
    }

    /**
     * 删除某个键的内容
     */
    public static void remove(Context context, String key) {
        init(context);
        mSharedPreferences.edit().remove(key).commit();
    }

    /**
     * 清空所有的键值对
     */
    public static void clearAll(Context context) {
        init(context);
        mSharedPreferences.edit().clear().commit();
    }

    /**
     * 删除某个键的内容
     */
    public static void remove(Context context, String spName, String key) {
        init(context, spName);
        mCustomSP.edit().remove(key).commit();
    }

    /**
     * 清空所有的键值对
     */
    public static void clearAll(Context context, String spName) {
        init(context, spName);
        mCustomSP.edit().clear().commit();
    }

}
