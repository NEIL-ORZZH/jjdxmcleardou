package com.dou361.quickscan.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.dou361.note.view.ViewUtils;
import com.dou361.note.view.annotation.ViewInject;
import com.dou361.quickscan.R;
import com.dou361.quickscan.adapter.GuideActivityAdapter;
import com.dou361.quickscan.base.BaseActivity;

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
 * 创建日期：2015/11/17 11:57
 * <p/>
 * 描 述：开启应用的向导页面
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class GuideActivity extends BaseActivity implements
        OnPageChangeListener, OnClickListener {

    /**
     * 装载向导图片的View
     */
    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;
    /**
     * 进入主页面按钮
     */
    @ViewInject(R.id.btn_experience)
    private Button btn_experience;
    /**
     * 图片列表
     */
    private List<View> imageLists;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guide);
        ViewUtils.inject(this);
        view_pager.setOnPageChangeListener(this);
        btn_experience.setOnClickListener(this);
        TypedArray icons = mContext.getResources().obtainTypedArray(R.array.guide_picture);
        imageLists = new ArrayList<View>();
        for (int i = 0; i < icons.length(); i++) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ScaleType.FIT_XY);
            iv.setImageDrawable(icons.getDrawable(i));
            imageLists.add(iv);
        }
        /** 如果导航页面只有一页，显示体验按钮 */
        if (imageLists.size() == 1) {
            btn_experience.setVisibility(View.VISIBLE);
        }
        GuideActivityAdapter adapter = new GuideActivityAdapter(imageLists);
        view_pager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_experience:
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                onBackPressed();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        /** 最后一张 */
        if (position == imageLists.size() - 1) {
            btn_experience.setVisibility(View.VISIBLE);
        } else {
            btn_experience.setVisibility(View.GONE);
        }
    }

}
