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
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.analytics.android.sdk.SAConfigOptions;
import com.sensorsdata.analytics.android.sdk.SensorsAnalyticsAutoTrackEventType;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.activity.ElementActivity;
import com.sensorsdata.toolapp.activity.ViewActivity;
import com.sensorsdata.toolapp.tool.Utils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;


public class VisualizedOptionFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageView iv_sa,iv_h5;
    EditText et_sa,et_h5;
    Switch sw_properties,sw_type,sw_bridge;
    LinearLayout linearLayout;
    Button btn_next;
    String SA_URL="",H5_URL="";
    final int SAURL_CODE=100,H5URL_CODE=101;
    SharedPreferences sp;

    public VisualizedOptionFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vis,container,false);
        sp=getContext().getSharedPreferences(Utils.SP_NAME, Context.MODE_PRIVATE);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void initView(View view){

        linearLayout=view.findViewById(R.id.vis_lin);
        iv_sa=view.findViewById(R.id.vis_iv_saurl);
        iv_sa.setOnClickListener(this);
        iv_h5=view.findViewById(R.id.vis_iv_h5);
        iv_h5.setOnClickListener(this);

        //数据接收地址
        et_sa=view.findViewById(R.id.vis_et_saurl);
        if (!sp.getString(Utils.SP_VISUALIZED_SA_URL,"").trim().equals("")){
            SA_URL=sp.getString(Utils.SP_VISUALIZED_SA_URL,"");
            et_sa.setText(SA_URL);
        }
        et_h5=view.findViewById(R.id.vis_et_h5);
        if (!sp.getString(Utils.SP_VISUALIZED_H5_URL,"").trim().equals("")){
            H5_URL=sp.getString(Utils.SP_VISUALIZED_H5_URL,"");
            et_h5.setText(H5_URL);
        }

        //可视化全埋点自定义属性开关
        sw_properties=view.findViewById(R.id.vis_sw_properties);
        sw_properties.setOnCheckedChangeListener(this);
        sw_properties.setChecked(sp.getBoolean(Utils.SP_VISUALIZED_PROPERTIES_SW,true));

        //打通开关
        sw_bridge=view.findViewById(R.id.vis_sw_bridge);
        sw_bridge.setOnCheckedChangeListener(this);
        sw_bridge.setChecked(sp.getBoolean(Utils.SP_VISUALIZED_BRIDGE_SW,true));

        //页面类型开关
        sw_type=view.findViewById(R.id.vis_sw_type);
        sw_type.setOnCheckedChangeListener(this);
        boolean b=sp.getBoolean(Utils.SP_VISUALIZED_TYPE_SW,true);
        sw_type.setChecked(b);
        //如果是原生类型，H5页面配置信息隐藏
        if (!b){
            linearLayout.setVisibility(View.GONE);
        }
        btn_next=view.findViewById(R.id.vis_btn_next);
        btn_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.vis_btn_next){
            if(SA_URL.trim().equals("")){
                Toast.makeText(getContext(), "数据地址不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            //如果未初始化过 SDK ，才初始化 SA SDK
            if (!Utils.SDKIsInit()){
                initSA();
            }
            if (sw_type.isChecked()){
                //如果页面类型是H5，跳转到H5页面
                Intent intent=new Intent(getContext(), ViewActivity.class);
                intent.putExtra(Utils.PAGE_TYPE,Utils.BRIDGE_WEBVIEW);
                intent.putExtra("url",H5_URL);
                intent.putExtra("visSwitch",true);
                startActivity(intent);
            }else {
                //跳转到原生页面
                Intent intent=new Intent(getContext(), ElementActivity.class);
                intent.putExtra("visSwitch",true);
                startActivity(intent);
            }

        }

        if (view.getId()==R.id.vis_iv_saurl){
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            startActivityForResult(intent, SAURL_CODE);

        }
        if (view.getId()==R.id.vis_iv_h5){
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            startActivityForResult(intent,H5URL_CODE);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor=sp.edit();
        //根据页面类型，决定是否显示打通参数配置页，并记录页面类型状态
        if (compoundButton.getId()==R.id.vis_sw_type){
            editor.putBoolean(Utils.SP_VISUALIZED_TYPE_SW,b);
            editor.apply();
            if (!b){
                if (linearLayout!=null){
                    linearLayout.setVisibility(View.GONE);
                }
            }else {
                if (linearLayout!=null){
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        }
        //记录可视化全埋点自定义属性状态
        if (compoundButton.getId()==R.id.vis_sw_properties){
            editor.putBoolean(Utils.SP_VISUALIZED_PROPERTIES_SW,b);
            editor.apply();
        }
        //记录打通状态
        if (compoundButton.getId()==R.id.vis_sw_bridge){
            editor.putBoolean(Utils.SP_VISUALIZED_BRIDGE_SW,b);
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
                    et_sa.setText(SA_URL);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString(Utils.SP_VISUALIZED_SA_URL,SA_URL);
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
                    et_h5.setText(H5_URL);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString(Utils.SP_VISUALIZED_H5_URL,H5_URL);
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
        saConfigOptions.enableHeatMap(true);
        saConfigOptions.setAutoTrackEventType(SensorsAnalyticsAutoTrackEventType.APP_START|SensorsAnalyticsAutoTrackEventType.APP_CLICK|SensorsAnalyticsAutoTrackEventType.APP_VIEW_SCREEN|SensorsAnalyticsAutoTrackEventType.APP_END);
        if (sw_properties.isChecked()){
            saConfigOptions.enableVisualizedProperties(true);
        }else {
            saConfigOptions.enableVisualizedAutoTrack(true);
        }
        if (sw_bridge.isChecked()){
            saConfigOptions.enableJavaScriptBridge(true);
        }
        SensorsDataAPI.startWithConfigOptions(getContext(),saConfigOptions);
        //开启 Fragment 页面浏览
        SensorsDataAPI.sharedInstance().trackFragmentAppViewScreen();

    }

}
