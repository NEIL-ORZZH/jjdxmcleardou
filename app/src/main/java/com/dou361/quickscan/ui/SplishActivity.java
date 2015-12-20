package com.dou361.quickscan.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.dou361.note.view.annotation.ViewInject;
import com.dou361.quickscan.R;
import com.dou361.quickscan.base.BaseActivity;
import com.dou361.quickscan.bean.EventTypeBean;
import com.dou361.quickscan.service.CleanerService;
import com.dou361.quickscan.service.CoreService;
import com.dou361.quickscan.utils.Constants;
import com.dou361.quickscan.utils.LogUtils;
import com.dou361.quickscan.utils.SPUtils;
import com.dou361.quickscan.utils.StatusConfig;
import com.dou361.quickscan.utils.SystemUtils;

import de.greenrobot.event.EventBus;

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
 * 创建日期：2015/12/1 22:01
 * <p/>
 * 描 述：最先启动的activity
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class SplishActivity extends BaseActivity {

    public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";


    /**
     * 加载数据状态参数
     */
    private boolean[] loadDataArrs = new boolean[5];

    @Override
    public void onEventMainThread(EventTypeBean event) {
        if (mFgState) {
            LogUtils.tag(TAG).log("SplashActivity------" + event.what);
            switch (event.what) {
                case StatusConfig.SEND_01:
                    /**
                     * 判断当前的用户是不是第一次使用app，如果是第一次使用那么就跳入到引导界面，如果不是第一次，就跳到主界面
                     */
                    if ((boolean) SPUtils.getData(mContext, Constants.ISFIRST,
                            false)) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        onBackPressed();
                    } else {
                        Intent intent = new Intent(mContext, GuideActivity.class);
                        startActivity(intent);
                        SPUtils.putData(mContext, Constants.ISFIRST,
                                true);
                        onBackPressed();
                    }
                    break;
            }
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_splish);
        startService(new Intent(this, CoreService.class));
        startService(new Intent(this, CleanerService.class));
        if (!(boolean) SPUtils.getData(mContext,
                Constants.ISSHORTCUT, false)) {
            createShortCut();
        }
        /** 应用上一次启动的版本号 */
        int preVersion = (int) SPUtils.getData(mContext,
                Constants.PREVERSION, 0);

        if (preVersion < SystemUtils.getVersionCode(mContext)) {
            /** 如果应用更新版本了，则重新调用向导页面 */
            SPUtils.putData(mContext, Constants.PREVERSION,
                    SystemUtils.getVersionCode(mContext));
            SPUtils.remove(mContext, Constants.ISFIRST);
        }
        goToActivity();
    }

    private void createShortCut() {
        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "一键加速");
        intent.putExtra("duplicate", false);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.short_cut_icon));
        Intent i = new Intent();
        i.setAction("com.yzy.shortcut");
        i.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
        sendBroadcast(intent);
        SPUtils.putData(mContext, Constants.ISSHORTCUT, true);
    }

    /**
     * 发一个延迟消息进行页面跳转
     */
    private void goToActivity() {
        new Thread() {
            public void run() {
                try {
                    /**  模拟延时 */
                    Thread.sleep(1000);
                    /**  发布事件，在后台线程发的事件 */
                    EventBus.getDefault().post(new EventTypeBean(StatusConfig.SEND_01));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
