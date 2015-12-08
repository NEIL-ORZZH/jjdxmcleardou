package com.dou361.quickscan.bean;

import android.os.Bundle;

/**
 * ========================================
 * <p/>
 * 版 权：深圳市晶网科技控股有限公司 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2015/11/17
 * <p/>
 * 描 述：事件接收类型bean，根据类型来处理不同事件动作，模拟Message对象
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class EventTypeBean {

    /**
     * 定义回调函数,在EventBus中可以定义四种类型的回调函数：
     * a、onEvent 它和ThreadModel中的PostThread对应，这个也是默认的类型，当使用这种类型时，回调函数和发起事件的函数会在同一个线程中执行
     * b、onEventMainThread，当使用这种类型时，回调函数会在主线程中执行，这个在Android中非常有用，因为在Android中禁止在子线程中修改UI
     * c、onEventBackgroundThread，当使用这种类型时，如果事件发起函数在主线程中执行，那么回调函数另启动一个子线程，如果事件发起函数在子线程执行，那么 回调函数就在这个子线程执行。
     * d、onEventBusAsync，当使用这种类型时，不管事件发起函数在哪里执行，都会另起一个线程去执行回调。
     * */

    /** 事件类型  */
    public int what;
    /** 传递对象  */
    public Object obj;
    /** 传递String参数1  */
    public String str1;
    /** 传递String参数1  */
    public String str2;
    /** 传递int参数1  */
    public int arg1;
    /** 传递int参数1  */
    public int arg2;
    /** 传递String参数1  */
    public String msg1;
    /** 传递int参数1  */
    public int code1;
    /** 传递Bundle参数  */
    Bundle data;

    public EventTypeBean() {
    }

    public EventTypeBean(int what) {
        this.what = what;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
