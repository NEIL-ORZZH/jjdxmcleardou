package com.dou361.quickscan.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.dou361.note.view.annotation.ViewInject;
import com.dou361.quickscan.R;
import com.dou361.quickscan.base.BaseSwipeBackActivity;
import com.dou361.quickscan.fragment.AutoStartFragment;
import com.dou361.quickscan.fragment.WeakFragmentPagerAdapter;
import com.dou361.quickscan.utils.SystemBarTintManager;
import com.dou361.quickscan.utils.UIElementsHelper;
import com.dou361.quickscan.views.SlidingTab;

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
 * 创建日期：2015/12/1 21:54
 * <p/>
 * 描 述：自启软件
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class AutoStartManageActivity extends BaseSwipeBackActivity {

    ActionBar ab;


    Resources res;


    @ViewInject(R.id.tabs)
    SlidingTab tabs;

    @ViewInject(R.id.pagerFragmentTask)
    ViewPager pager;

    private MyPagerAdapter adapter;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_autostart_manage);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setHomeButtonEnabled(true);
        //  applyKitKatTranslucency();


        res = getResources();
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                        .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
        setTabsValue();

    }


    private void setTabsValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setTextColor(Color.parseColor("#ffffff"));
        tabs.setIndicatorColor(Color.parseColor("#ffffff"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ffffff"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);

    }


    /**
     * Apply KitKat specific translucency.
     */

    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);
            // mTintManager.setTintColor(0xF00099CC);

            mTintManager.setTintDrawable(UIElementsHelper
                    .getGeneralActionBarBackground(this));


            getActionBar().setBackgroundDrawable(
                    UIElementsHelper.getGeneralActionBarBackground(this));

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class MyPagerAdapter extends WeakFragmentPagerAdapter {

        private final String[] TITLES = {"普通软件", "系统核心软件"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            // return SuperAwesomeCardFragment.newInstance(position);return
            AutoStartFragment fragment = new AutoStartFragment();
            Bundle bundle = new Bundle();

            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            saveFragment(fragment);

            return fragment;

        }

    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
