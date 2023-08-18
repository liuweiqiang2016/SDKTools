package com.sensorsdata.toolapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.analytics.android.sdk.SAConfigOptions;
import com.sensorsdata.analytics.android.sdk.SensorsAnalyticsAutoTrackEventType;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.activity.ViewActivity;
import com.sensorsdata.toolapp.tool.Utils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;


public class BridgeOptionFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch sw_jellyBean,sw_safe,sw_version,sw_bridge;
    private Button btn_h5;
    private Spinner sp_view;
    private EditText et_saurl,et_h5url;
    private ImageView iv_saurl,iv_h5url;
    private LinearLayout linearLayout;
    String SA_URL="",H5_URL="";
    final int SAURL_CODE=100,H5URL_CODE=101;
    String TAG="SA.S";
    SharedPreferences sp;

    public BridgeOptionFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bridge,container,false); //  此处的布局文件是普通的线性布局（此博客忽略）
        sp=getContext().getSharedPreferences(Utils.SP_NAME, Context.MODE_PRIVATE);
        initViews(view);//初始化控件
        initEvents();//初始化事件
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews(View view){
        iv_saurl=view.findViewById(R.id.iv_saurl);
        iv_h5url=view.findViewById(R.id.iv_h5url);
        linearLayout=view.findViewById(R.id.lin_para);
        sp_view=view.findViewById(R.id.sp_core);
        btn_h5=view.findViewById(R.id.btn_h5);

        //打通开关
        sw_bridge=view.findViewById(R.id.sw_bridge);
        sw_bridge.setOnCheckedChangeListener(this);
        sw_bridge.setChecked(sp.getBoolean(Utils.SP_BRIDGE_H5_SW,true));
        //是否支持 jellyBean 开关
        sw_jellyBean=view.findViewById(R.id.sw_jellyBean);
        sw_jellyBean.setOnCheckedChangeListener(this);
        sw_jellyBean.setChecked(sp.getBoolean(Utils.SP_BRIDGE_JB_SW,false));
        //是否校验数据接收地址
        sw_safe=view.findViewById(R.id.sw_safe);
        sw_safe.setOnCheckedChangeListener(this);
        sw_safe.setChecked(sp.getBoolean(Utils.SP_BRIDGE_CHECK_SW,false));
        //是否是旧版打通
        sw_version=view.findViewById(R.id.sw_version);
        sw_version.setOnCheckedChangeListener(this);
        sw_version.setChecked(sp.getBoolean(Utils.SP_BRIDGE_VERSION_SW,false));
        //原生数据接收地址
        et_saurl=view.findViewById(R.id.et_saurl);
        if (!sp.getString(Utils.SP_BRIDGE_SA_URL,"").trim().equals("")){
            SA_URL=sp.getString(Utils.SP_BRIDGE_SA_URL,"");
            et_saurl.setText(SA_URL);
        }
        //h5 页面地址
        et_h5url=view.findViewById(R.id.et_h5url);
        if (!sp.getString(Utils.SP_BRIDGE_H5_URL,"").trim().equals("")){
            H5_URL=sp.getString(Utils.SP_BRIDGE_H5_URL,"");
            et_h5url.setText(H5_URL);
        }

    }

    private void initEvents(){
        btn_h5.setOnClickListener(this);
        sw_bridge.setOnCheckedChangeListener(this);
        iv_saurl.setOnClickListener(this);
        iv_h5url.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.btn_h5){
            if(H5_URL.trim().equals("")){
                Toast.makeText(getContext(), "H5 页面地址不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            //如果未初始化过 SDK 且开启打通，才初始化 SA SDK
            if (!Utils.SDKIsInit()&&sw_bridge.isChecked()){
                initSA();
            }
            Intent intent=new Intent(getContext(), ViewActivity.class);
            intent.putExtra("isSupportJellyBean",sw_jellyBean.isChecked());
            intent.putExtra("enableVerify",sw_safe.isChecked());
            intent.putExtra("isOldVersion",sw_version.isChecked());
            int code=-1;
            if (sp_view.getSelectedItemPosition()==0){
                code=Utils.BRIDGE_WEBVIEW;
            }else {
                code=Utils.BRIDGE_X5WEBVIEW;
            }
            intent.putExtra(Utils.PAGE_TYPE,code);
            intent.putExtra("url",H5_URL);
            startActivity(intent);
        }

        if (view.getId()==R.id.iv_saurl){
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            startActivityForResult(intent, SAURL_CODE);

        }
        if (view.getId()==R.id.iv_h5url){
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            startActivityForResult(intent,H5URL_CODE);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        System.out.println("状态:"+b);
        SharedPreferences.Editor editor=sp.edit();
        //根据 H5 打通开关是否开启，决定是否显示打通参数配置页
        if (compoundButton.getId()==R.id.sw_bridge){
            editor.putBoolean(Utils.SP_BRIDGE_H5_SW,b);
            editor.apply();
            if (!b){
                linearLayout.setVisibility(View.GONE);
            }else {
                linearLayout.setVisibility(View.VISIBLE);
            }
        }
        if (compoundButton.getId()==R.id.sw_jellyBean){
            editor.putBoolean(Utils.SP_BRIDGE_JB_SW,b);
            editor.apply();
        }
        if (compoundButton.getId()==R.id.sw_safe){
            editor.putBoolean(Utils.SP_BRIDGE_CHECK_SW,b);
            editor.apply();
        }
        if (compoundButton.getId()==R.id.sw_version){
            editor.putBoolean(Utils.SP_BRIDGE_VERSION_SW,b);
            editor.apply();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAURL_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    SA_URL= bundle.getString(CodeUtils.RESULT_STRING);
                    et_saurl.setText(SA_URL);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString(Utils.SP_BRIDGE_SA_URL,SA_URL);
                    editor.apply();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (requestCode == H5URL_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    H5_URL= bundle.getString(CodeUtils.RESULT_STRING);
                    et_h5url.setText(H5_URL);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString(Utils.SP_BRIDGE_H5_URL,H5_URL);
                    editor.apply();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void initSA(){
        SAConfigOptions saConfigOptions=new SAConfigOptions(SA_URL);
        saConfigOptions.enableLog(true);
        //默认新版打通
        if (!sw_version.isChecked()){
            if (sw_jellyBean.isChecked()){
                //支持 jellyBean
                saConfigOptions.enableJavaScriptBridge(true);
            }else {
                //不支持 jellyBean
                saConfigOptions.enableJavaScriptBridge(false);
            }
        }
        SensorsDataAPI.startWithConfigOptions(getContext(),saConfigOptions);

    }
}
