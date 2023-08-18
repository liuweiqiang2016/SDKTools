package com.sensorsdata.toolapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.tool.Utils;

import pub.devrel.easypermissions.EasyPermissions;

/*
* 应用首页
* */
public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    String TAG="SA.S";
    //需要申请的敏感权限
    private String[] permissions={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE
    };

    private ListView listView;
    private RelativeLayout relativeLayout;
    String[] strArr=new String[]{"原生 SDK 相关事件","H5 打通调试","可视化全埋点/App 点击分析"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyPermission();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
            actionBar.setCustomView(actionbarLayout);
            relativeLayout=actionbarLayout.findViewById(R.id.home_setting);
            relativeLayout.setOnClickListener(this);
        }
        setContentView(R.layout.activity_home);
        initListView();
        initData();
    }

    private void initListView(){
        listView=findViewById(R.id.home_lv);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strArr);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    //Activity销毁时，终止该Activity的进程
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("进入后台....");
    }


    //敏感权限申请
    private void applyPermission(){
        if(!EasyPermissions.hasPermissions(this,permissions)){
            EasyPermissions.requestPermissions(this,"拒绝相关权限，app无法正常使用",0,permissions);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this, OptionActivity.class);
        switch(i){
            case 0:
                intent.putExtra(Utils.PAGE_TYPE,Utils.SDK_NATIVE);
                break;
            case 1:
                intent.putExtra(Utils.PAGE_TYPE,Utils.SDK_BRIDGE);
                break;
            case 2:
                intent.putExtra(Utils.PAGE_TYPE,Utils.SDK_VISUALIZED);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.home_setting){
            //进入设置页面
            Intent intent=new Intent(this,SettingActivity.class);
            startActivity(intent);
        }

    }

    private void initData(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //判断是否需要清除历史信息
                Utils.checkSPState(HomeActivity.this);
            }
        }.start();
    }
}
