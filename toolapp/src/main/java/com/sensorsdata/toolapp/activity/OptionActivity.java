package com.sensorsdata.toolapp.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.fragment.BridgeOptionFragment;
import com.sensorsdata.toolapp.fragment.NativeOptionFragment;
import com.sensorsdata.toolapp.fragment.VisualizedOptionFragment;
import com.sensorsdata.toolapp.tool.Utils;

public class OptionActivity extends AppCompatActivity implements View.OnClickListener {
    //根据不同的 code，展示不同布局
    private int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("SDK 设置");
        }
        setContentView(R.layout.activity_option);
        //根据type决定展示哪个布局
        code = getIntent().getIntExtra(Utils.PAGE_TYPE,-1);
        initFragment(code);
        checkSDKState();
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

    //根据不同的type，设置不同fragment
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
    }

    /**
     * 根据编号，设置页面
     * @param code 页面 code
     */
    private void initFragment(int code){
        Fragment fragment = null;
        switch (code){
            case Utils.SDK_NATIVE:
               fragment=new NativeOptionFragment();
                break;
            case Utils.SDK_BRIDGE:
                fragment=new BridgeOptionFragment();
                break;
            case Utils.SDK_VISUALIZED:
                fragment=new VisualizedOptionFragment();
                break;
        }
        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.setting_content,fragment)   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
    }

    /**
     * 检查 SDK 初始化状态
     */
    private void checkSDKState(){
        if (Utils.SDKIsInit()){
            Toast.makeText(this,"SDK 已初始化，后续设置无效",Toast.LENGTH_LONG).show();
        }
    }

}
