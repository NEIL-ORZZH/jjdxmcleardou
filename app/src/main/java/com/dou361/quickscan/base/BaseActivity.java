package com.dou361.quickscan.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.dou361.note.view.ViewUtils;
import com.dou361.quickscan.R;
import com.dou361.quickscan.bean.EventTypeBean;
import com.dou361.quickscan.dialogs.ProgressDialogFragment;
import com.dou361.quickscan.utils.SystemBarTintManager;
import com.dou361.quickscan.utils.UIElementsHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


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
 * 创建日期：2015/12/1 22:59
 * <p/>
 * 描 述：基类
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public abstract class BaseActivity extends FragmentActivity {

    /**
     * 屏幕的宽度
     */
    protected int mScreenWidth;
    /**
     * 屏幕的高度
     */
    protected int mScreenHeight;
    /**
     * 屏幕的密度
     */
    protected float mDensity;
    /**
     * 屏上下文
     */
    protected Activity mActivity;
    /**
     * 屏上下文
     */
    protected Context mContext;
    /**
     * 定位处理业务的类
     */
    protected String LogName;
    /**
     * 进度条对话框
     */
    private ProgressDialogFragment mProgressDialogFragment;
    /**
     * 记录处于前台的Activity
     */
    private static BaseActivity mForegroundActivity;
    /**
     * 记录所有活动的Activity
     */
    private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
    /**
     * 判断当前Activity是否是显示true为onResume()之后 false为onPause()之后
     */
    protected boolean mFgState;
    protected String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
        LogName = this.getClass().getSimpleName();


        init();
        /**
         * 只有继承了BaseActivity，Activity才会加入集合管理中
         */
        addActivity(this);
        /**
         * 事件bus，注册监听
         */
        EventBus.getDefault().register(this);
        initView();
        initViewData();
        initListener();
    }

    @Override
    public void setContentView(int layoutResID) {
//        applyKitKatTranslucency(layoutResID);
        super.setContentView(layoutResID);
        ViewUtils.inject(this);
    }

    @Override
    public void onBackPressed() {
        /** 点击返回键时关闭当前Activity */
        finishActivity(this);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        mForegroundActivity = this;
        mActivity = this;
        mContext = this;
        mFgState = true;
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        mForegroundActivity = null;
        mFgState = false;
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        /** 销毁Activity时设置空布局，释放原有的布局，（也可以一个一个成员变量释放）减少资源占用 */
        setContentView(R.layout.activity_destroy);
        /** 主动调用gc回收 */
        System.gc();
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t) {
            //this may crash if registration did not go through. just be safe
        }
    }

    /**
     * 初始化对象
     */
    protected void init() {
        mActivity = this;
        mContext = this;
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 设置View对应的数据
     */
    protected void initViewData() {
    }

    /**
     * 初始化监听器
     */
    protected void initListener() {
    }

    /**
     * 关闭所有Activity
     */
    public static void finishAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 添加页面
     */
    public static void addActivity(BaseActivity activity) {
        for (int i = 0; i < mActivities.size(); i++) {
            if (mActivities.get(i) == activity) {
                mActivities.remove(i);
                break;
            }
        }
        mActivities.add(activity);
    }

    /**
     * 添加页面
     */
    public static void finishActivity(BaseActivity activity) {
        for (int i = 0; i < mActivities.size(); i++) {
            if (mActivities.get(i) == activity) {
                activity.finish();
                mActivities.remove(i);
                break;
            }
        }

    }

    /**
     * 关闭所有Activity，除了参数传递的Activity
     */
    public static void finishAll(BaseActivity except) {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            if (activity != except)
                activity.finish();
        }
    }

    /**
     * 是否有启动的Activity
     */
    public static boolean hasActivity() {
        return mActivities.size() > 0;
    }

    /**
     * 是否有其他启动的Activity除掉当前的
     */
    public static boolean hasElseActivity(BaseActivity activity) {
        int i = 0;
        for (; i < mActivities.size(); i++) {
            if (mActivities.get(i) == activity) {
                break;
            }
        }
        return mActivities.size() != i;
    }

    /**
     * 获取当前处于前台的activity
     */
    public static BaseActivity getForegroundActivity() {
        return mForegroundActivity;
    }

    /**
     * 获取当前处于栈顶的activity，无论其是否处于前台
     */
    public static BaseActivity getCurrentActivity() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        if (copy.size() > 0) {
            return copy.get(copy.size() - 1);
        }
        return null;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void showDialogLoading() {
        showDialogLoading(null);
    }

    public void showDialogLoading(String msg) {
        if (mProgressDialogFragment == null) {
            mProgressDialogFragment = ProgressDialogFragment.newInstance(0,
                    null);
        }
        if (msg != null) {
            mProgressDialogFragment.setMessage(msg);
        }
        mProgressDialogFragment.show(getFragmentManager(), "");

    }

    public void dismissDialogLoading() {
        if (mProgressDialogFragment != null) {
            mProgressDialogFragment.dismiss();
        }
    }


    /**
     * 状态栏一体化
     * Apply KitKat specific translucency.
     */
    private void applyKitKatTranslucency(int layoutResID) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.blue);
//            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//            LayoutInflater.from(mContext).inflate(layoutResID, null).setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
//        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
                SystemBarTintManager mTintManager = new SystemBarTintManager(this);
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setNavigationBarTintEnabled(true);
                mTintManager.setTintDrawable(UIElementsHelper
                        .getGeneralActionBarBackground(this));
                getActionBar().setBackgroundDrawable(
                        UIElementsHelper.getGeneralActionBarBackground(this));

            }
        } catch (Exception e) {

        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        try {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        } catch (Exception e) {
        }
    }

    /**
     * =====================================EventBus管理============================================
     */
    @Subscribe
    public void onEvent(String event) {
    }

    @Subscribe
    public void onEventMainThread(EventTypeBean event) {
    }

    @Subscribe
    public void onEventBackgroundThread(String event) {
    }

    @Subscribe
    public void onEventBusAsync(String event) {
    }
    /** =====================================EventBus管理============================================  */


}
