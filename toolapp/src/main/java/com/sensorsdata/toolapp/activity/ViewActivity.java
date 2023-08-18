package com.sensorsdata.toolapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sensorsdata.analytics.android.sdk.dialog.SensorsDataDialogUtils;
import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.fragment.LogFragment;
import com.sensorsdata.toolapp.fragment.NativeViewFragment;
import com.sensorsdata.toolapp.fragment.WebFragment;
import com.sensorsdata.toolapp.fragment.X5Fragment;
import com.sensorsdata.toolapp.tool.Utils;

import java.util.ArrayList;
import java.util.List;

//根据不同type动态显示不同布局
public class ViewActivity extends AppCompatActivity {

    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> fragments = new ArrayList<>();
    //装载底部标题
    private List<String> titles = new ArrayList<>();
    private TabLayout tabLayout;
    //根据不同的 code，展示不同布局
    private int code;
    private LogFragment logFragment;
    private WebFragment webFragment;
    private NativeViewFragment nativeViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("调试页面");
        }
//        setTitle("View 页面");
        //根据type决定展示哪个布局
        code = getIntent().getIntExtra(Utils.PAGE_TYPE,-1);
        setFragment(code);
        initView();//初始化布局
    }

    //初始化控件
    private void initView() {
        mViewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.vb_tab);
        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return fragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                //viewpager的底部标题
                return titles.get(position);
            }
        };
        //不要忘记设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                if (titles.get(position).equals("日志")){
                    if (logFragment!=null){
                        logFragment.refreshUI();
                    }
                }
            }
            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
        //实现TabLayout和viewpager联动
        tabLayout.setupWithViewPager(mViewPager);

    }

    //根据不同的 code，设置不同fragment
    private void setFragment(int code) {
        //首先清空fragments和titles
        if (fragments != null) {
            fragments.clear();
        }
        if (titles != null) {
            titles.clear();
        }
        switch (code) {
            case Utils.NATIVE_VIEW:
                nativeViewFragment=new NativeViewFragment();
                fragments.add(nativeViewFragment);
                titles.add("原生SDK");
                logFragment=new LogFragment();
                fragments.add(logFragment);
                titles.add("日志");
                break;
            case Utils.BRIDGE_WEBVIEW:
                webFragment=new WebFragment();
                fragments.add(webFragment);
                titles.add("原生WebView");
                logFragment=new LogFragment();
                fragments.add(logFragment);
                titles.add("日志");
                break;
            case Utils.BRIDGE_X5WEBVIEW:
                fragments.add(new X5Fragment());
                titles.add("腾讯X5WebView");
                logFragment=new LogFragment();
                fragments.add(logFragment);
                titles.add("日志");
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (webFragment!=null&&!webFragment.isIndexPage()){
            webFragment.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //actionbar navigation up 按钮
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "销毁了...................");
    }
}
