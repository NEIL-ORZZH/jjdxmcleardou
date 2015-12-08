package com.dou361.quickscan.ui;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import com.dou361.note.view.annotation.ViewInject;
import com.dou361.quickscan.R;
import com.dou361.quickscan.base.BaseSwipeBackActivity;
import com.dou361.quickscan.utils.AppUtil;

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
 * 创建日期：2015/12/1 21:53
 * <p/>
 * 描 述：关于我们
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class AboutActivity extends BaseSwipeBackActivity {

    @ViewInject(R.id.subVersion)
    TextView subVersion;

    @Override
    protected void initView() {
        setContentView(R.layout.about);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("关于");
        TextView tv = (TextView) findViewById(R.id.app_information);
        Linkify.addLinks(tv, Linkify.ALL);

        subVersion.setText("V" + AppUtil.getVersion(mContext));

    }

}
