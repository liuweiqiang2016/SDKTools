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


public class NativeOptionFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    private String SERVER_URL;
    private ImageView imageView;
    private EditText editText;
    private Switch sw_auto,sw_encrypt;
    private Button button;
    private SharedPreferences sp;
    final int SAURL_CODE=100;

    public NativeOptionFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_native_setting,container,false); //  此处的布局文件是普通的线性布局（此博客忽略）
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initViews(View view){
        imageView=view.findViewById(R.id.native_iv_saurl);
        imageView.setOnClickListener(this);

        editText=view.findViewById(R.id.native_et_saurl);
        sp=getContext().getSharedPreferences(Utils.SP_NAME, Context.MODE_PRIVATE);
        if (!sp.getString(Utils.SP_NATIVE_SA_URL,"").trim().equals("")){
            SERVER_URL=sp.getString(Utils.SP_NATIVE_SA_URL,"");
            editText.setText(SERVER_URL);
        }

        sw_auto=view.findViewById(R.id.native_sw_auto);
        sw_auto.setChecked(sp.getBoolean(Utils.SP_NATIVE_AUTO,false));
        sw_auto.setOnCheckedChangeListener(this);
        sw_encrypt=view.findViewById(R.id.native_sw_encrypt);
        sw_encrypt.setChecked(sp.getBoolean(Utils.SP_NATIVE_ENCRYPT,false));
        sw_encrypt.setOnCheckedChangeListener(this);

        button=view.findViewById(R.id.native_btn_next);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.native_iv_saurl){
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            startActivityForResult(intent, SAURL_CODE);
        }
        if (view.getId()==R.id.native_btn_next){
            if (!Utils.SDKIsInit()){
                if (SERVER_URL==null||SERVER_URL.trim().equals("")){
                    Toast.makeText(getContext(), "数据地址不能为空!", Toast.LENGTH_LONG).show();
                }else {
                    SAConfigOptions saConfigOptions=new SAConfigOptions(SERVER_URL);
                    saConfigOptions.enableLog(true);
                    if (sw_auto.isChecked()){
                        //开启全埋点
                        saConfigOptions.setAutoTrackEventType(SensorsAnalyticsAutoTrackEventType.APP_START|SensorsAnalyticsAutoTrackEventType.APP_CLICK|SensorsAnalyticsAutoTrackEventType.APP_VIEW_SCREEN|SensorsAnalyticsAutoTrackEventType.APP_END);
                    }
                    if (sw_encrypt.isChecked()){
                        //开启加密
                        saConfigOptions.enableEncrypt(true);
                    }
                    SensorsDataAPI.startWithConfigOptions(getActivity(),saConfigOptions);
                    if (sw_auto.isChecked()){
                        //开启 Fragment 页面浏览
                        SensorsDataAPI.sharedInstance().trackFragmentAppViewScreen();
                    }
                    Intent intent=new Intent(getContext(), ViewActivity.class);
                    intent.putExtra(Utils.PAGE_TYPE,Utils.NATIVE_VIEW);
                    startActivity(intent);
                }
            }else{
                Intent intent=new Intent(getContext(), ViewActivity.class);
                intent.putExtra(Utils.PAGE_TYPE,Utils.NATIVE_VIEW);
                startActivity(intent);
            }



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
                    SERVER_URL= bundle.getString(CodeUtils.RESULT_STRING);
                    editText.setText(SERVER_URL);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString(Utils.SP_NATIVE_SA_URL,SERVER_URL);
                    editor.apply();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId()==R.id.native_sw_auto){
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean(Utils.SP_NATIVE_AUTO,b);
            editor.apply();
        }
        if (compoundButton.getId()==R.id.native_sw_encrypt){
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean(Utils.SP_NATIVE_ENCRYPT,b);
            editor.apply();
        }
    }
}
