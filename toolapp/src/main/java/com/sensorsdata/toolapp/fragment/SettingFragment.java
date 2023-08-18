package com.sensorsdata.toolapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.tool.Utils;

import org.greenrobot.eventbus.EventBus;


public class SettingFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    private Switch sw_sdk,sw_config;
    RelativeLayout rl_user,rl_about,rl_open;
    private SharedPreferences sp;

    public SettingFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        sp=getContext().getSharedPreferences(Utils.SP_SETTING, Context.MODE_PRIVATE);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initViews(View view){

        sw_sdk=view.findViewById(R.id.setting_sdk);
        sw_sdk.setChecked(sp.getBoolean(Utils.SP_SETTING_SDK,true));
        sw_sdk.setOnCheckedChangeListener(this);

        sw_config=view.findViewById(R.id.setting_config);
        sw_config.setChecked(sp.getBoolean(Utils.SP_SETTING_CONFIG,true));
        sw_config.setOnCheckedChangeListener(this);

        rl_user=view.findViewById(R.id.setting_user);
        rl_user.setOnClickListener(this);

        rl_about=view.findViewById(R.id.setting_about);
        rl_about.setOnClickListener(this);

        rl_open=view.findViewById(R.id.setting_open);
        rl_open.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //通知 Activity 切换 Fragment
        if (view.getId()==R.id.setting_user){
            EventBus.getDefault().post(Utils.SETTING_USER);
        }
        if (view.getId()==R.id.setting_about){
            EventBus.getDefault().post(Utils.SETTING_ABOUT);
        }
        if (view.getId()==R.id.setting_open){
            EventBus.getDefault().post(Utils.SETTING_OPEN);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId()==R.id.setting_sdk){
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean(Utils.SP_SETTING_SDK,b);
            editor.apply();
        }
        if (compoundButton.getId()==R.id.setting_config){
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean(Utils.SP_SETTING_CONFIG,b);
            editor.apply();
        }
    }
}
