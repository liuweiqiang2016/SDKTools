package com.sensorsdata.toolapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.analytics.android.sdk.BuildConfig;
import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.tool.Utils;

public class AboutFragment extends Fragment {

    private TextView tv1,tv2;

    public AboutFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        tv1=view.findViewById(R.id.about_app_version);
        tv1.setText("应用版本:"+Utils.getVersionName(getContext()));
        tv2=view.findViewById(R.id.about_sdk_version);
        tv2.setText("SDK 版本:"+ BuildConfig.SDK_VERSION);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }




}
