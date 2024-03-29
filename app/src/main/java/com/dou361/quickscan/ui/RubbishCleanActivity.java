package com.dou361.quickscan.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.note.view.annotation.ViewInject;
import com.dou361.note.view.annotation.event.OnClick;
import com.dou361.quickscan.R;
import com.dou361.quickscan.adapter.RublishMemoryAdapter;
import com.dou361.quickscan.base.BaseSwipeBackActivity;
import com.dou361.quickscan.model.CacheListItem;
import com.dou361.quickscan.model.StorageSize;
import com.dou361.quickscan.service.CleanerService;
import com.dou361.quickscan.utils.StorageUtil;
import com.dou361.quickscan.utils.SystemBarTintManager;
import com.dou361.quickscan.utils.UIElementsHelper;
import com.dou361.quickscan.widget.textcounter.CounterView;
import com.dou361.quickscan.widget.textcounter.formatters.DecimalFormatter;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;
import java.util.List;
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
 * 创建日期：2015/12/1 21:55
 * <p/>
 * 描 述：垃圾清理
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class RubbishCleanActivity extends BaseSwipeBackActivity implements OnDismissCallback, CleanerService.OnActionListener {

    ActionBar ab;
    protected static final int SCANING = 5;

    protected static final int SCAN_FINIFSH = 6;
    protected static final int PROCESS_MAX = 8;
    protected static final int PROCESS_PROCESS = 9;

    private static final int INITIAL_DELAY_MILLIS = 300;
    SwingBottomInAnimationAdapter swingBottomInAnimationAdapter;
    Resources res;
    int ptotal = 0;
    int pprocess = 0;


    private CleanerService mCleanerService;

    private boolean mAlreadyScanned = false;
    private boolean mAlreadyCleaned = false;

    @ViewInject(R.id.listview)
    ListView mListView;

    @ViewInject(R.id.empty)
    TextView mEmptyView;

    @ViewInject(R.id.header)
    RelativeLayout header;


    @ViewInject(R.id.textCounter)
    CounterView textCounter;
    @ViewInject(R.id.sufix)
    TextView sufix;

    @ViewInject(R.id.progressBar)
    View mProgressBar;
    @ViewInject(R.id.progressBarText)
    TextView mProgressBarText;

    RublishMemoryAdapter rublishMemoryAdapter;

    List<CacheListItem> mCacheListItem = new ArrayList<>();

    @ViewInject(R.id.bottom_lin)
    LinearLayout bottom_lin;

    @ViewInject(R.id.clear_button)
    Button clearButton;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCleanerService = ((CleanerService.CleanerServiceBinder) service).getService();
            mCleanerService.setOnActionListener(RubbishCleanActivity.this);

            //  updateStorageUsage();

            if (!mCleanerService.isScanning() && !mAlreadyScanned) {
                mCleanerService.scanCache();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCleanerService.setOnActionListener(null);
            mCleanerService = null;
        }
    };


    @Override
    protected void initView() {
        setContentView(R.layout.activity_rublish_clean);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //     applyKitKatTranslucency();

//        StikkyHeaderBuilder.stickTo(mListView).setHeader(header)
//                .minHeightHeaderPixel(0).build();
        res = getResources();


        int footerHeight = mContext.getResources().getDimensionPixelSize(R.dimen.footer_height);

        mListView.setEmptyView(mEmptyView);
        rublishMemoryAdapter = new RublishMemoryAdapter(mContext, mCacheListItem);
        mListView.setAdapter(rublishMemoryAdapter);
        mListView.setOnItemClickListener(rublishMemoryAdapter);
        mListView.setOnScrollListener(new QuickReturnListViewOnScrollListener(QuickReturnType.FOOTER, null, 0, bottom_lin, footerHeight));
        bindService(new Intent(mContext, CleanerService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
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

    @Override
    public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] ints) {

    }

    @Override
    public void onScanStarted(Context context) {
        mProgressBarText.setText(R.string.scanning);
        showProgressBar(true);
    }

    @Override
    public void onScanProgressUpdated(Context context, int current, int max) {
        mProgressBarText.setText(getString(R.string.scanning_m_of_n, current, max));

    }

    @Override
    public void onScanCompleted(Context context, List<CacheListItem> apps) {
        showProgressBar(false);
        mCacheListItem.clear();
        mCacheListItem.addAll(apps);
        rublishMemoryAdapter.notifyDataSetChanged();
        header.setVisibility(View.GONE);
        if (apps.size() > 0) {
            header.setVisibility(View.VISIBLE);
            bottom_lin.setVisibility(View.VISIBLE);

            long medMemory = mCleanerService != null ? mCleanerService.getCacheSize() : 0;

            StorageSize mStorageSize = StorageUtil.convertStorageSize(medMemory);
            textCounter.setAutoFormat(false);
            textCounter.setFormatter(new DecimalFormatter());
            textCounter.setAutoStart(false);
            textCounter.setStartValue(0f);
            textCounter.setEndValue(mStorageSize.value);
            textCounter.setIncrement(5f); // the amount the number increments at each time interval
            textCounter.setTimeInterval(50); // the time interval (ms) at which the text changes
            sufix.setText(mStorageSize.suffix);
            //  textCounter.setSuffix(mStorageSize.suffix);
            textCounter.start();
        } else {
            header.setVisibility(View.GONE);
            bottom_lin.setVisibility(View.GONE);
        }

        if (!mAlreadyScanned) {
            mAlreadyScanned = true;

        }


    }

    @Override
    public void onCleanStarted(Context context) {
        if (isProgressBarVisible()) {
            showProgressBar(false);
        }

        if (!RubbishCleanActivity.this.isFinishing()) {
            showDialogLoading();
        }
    }

    @Override
    public void onCleanCompleted(Context context, long cacheSize) {
        dismissDialogLoading();
        Toast.makeText(context, context.getString(R.string.cleaned, Formatter.formatShortFileSize(
                mContext, cacheSize)), Toast.LENGTH_LONG).show();
        header.setVisibility(View.GONE);
        bottom_lin.setVisibility(View.GONE);
        mCacheListItem.clear();
        rublishMemoryAdapter.notifyDataSetChanged();
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


    @OnClick(R.id.clear_button)
    public void clear_buttonClick(View v) {

        if (mCleanerService != null && !mCleanerService.isScanning() &&
                !mCleanerService.isCleaning() && mCleanerService.getCacheSize() > 0) {
            mAlreadyCleaned = false;

            mCleanerService.cleanCache();
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


    private boolean isProgressBarVisible() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.startAnimation(AnimationUtils.loadAnimation(
                    mContext, android.R.anim.fade_out));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

}
