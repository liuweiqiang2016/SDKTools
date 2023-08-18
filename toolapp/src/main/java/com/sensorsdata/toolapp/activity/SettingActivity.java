package com.sensorsdata.toolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.fragment.AboutFragment;
import com.sensorsdata.toolapp.fragment.OpenFragment;
import com.sensorsdata.toolapp.fragment.SettingFragment;
import com.sensorsdata.toolapp.tool.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Fragment fragment = null;
    int code=-1;
    ActionBar actionBar;
    //根据不同的 code，展示不同布局
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar= getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("设置");
        }
        setContentView(R.layout.activity_setting);
        //注册 eventbus
        EventBus.getDefault().register(this);
        initFragment(Utils.SETTING_HOME);
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
    public void onBackPressed() {
       if (code==Utils.SETTING_HOME){
           //若在主页，则返回到上一页
           super.onBackPressed();
       }else {
           //否则返回到主页
           initFragment(Utils.SETTING_HOME);
       }
    }

    @Override
    public void onClick(View view) {
    }

    /**
     * 根据编号，设置页面
     * @param code 页面 code
     */
    private void initFragment(int code){
        if (code==Utils.SETTING_USER){
            Intent intent=new Intent(this,ManualActivity.class);
            startActivity(intent);
            return;
        }
        this.code=code;
        switch (code){
            case Utils.SETTING_HOME:
               fragment=new SettingFragment();
                break;
            case Utils.SETTING_ABOUT:
                fragment=new AboutFragment();
                break;
            case Utils.SETTING_OPEN:
                fragment=new OpenFragment();
                break;
        }
        Log.e("code", "initFragment: "+code);
        setActionBar(code);
        getSupportFragmentManager()    //
                .beginTransaction()
                .replace(R.id.fm_content,fragment)   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
    }

    void setActionBar(int code){
        switch (code){
            case Utils.SETTING_HOME:
                actionBar.setTitle("设置");
                break;
            case Utils.SETTING_ABOUT:
                actionBar.setTitle("关于");
                break;
            case Utils.SETTING_OPEN:
                actionBar.setTitle("开源框架");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity 销毁时，解除注册
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(Integer code){
        initFragment(code);
    }
}
