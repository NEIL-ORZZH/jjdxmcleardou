package com.dou361.quickscan.bean;

import android.content.Context;

import com.dou361.quickscan.utils.SPUtils;

import java.io.Serializable;

/**
 * ========================================
 * <p/>
 * 版 权：深圳市晶网电子科技有限公司 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2015/11/11
 * <p/>
 * 描 述：共享文件的SPUtils的包装类，打印信息中包含其对应的类名称
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class SPTagName implements Serializable {

    /**
     *
     */
    private final long serialVersionUID = -765691622218959750L;

    /**
     * 自定义共享文件名，相当于新开一个
     */
    private String spName;

    public void setSpName(String spName) {
        this.spName = spName;
    }

    /**
     * 添加键的内容
     */
    public void putData(Context context, String key, Object value) {
        SPUtils.putData(context, spName, key, value);
    }

    /**
     * 获得键的内容
     */
    public Object getData(Context context, String key,
                          Object defValue) {
        return SPUtils.getData(context, spName, key, defValue);
    }

    /**
     * 删除某个键的内容
     */
    public void remove(Context context, String key) {
        SPUtils.remove(context, spName, key);
    }

    /**
     * 清空所有的键值对
     */
    public void clearAll(Context context) {
        SPUtils.clearAll(context, spName);
    }

}
