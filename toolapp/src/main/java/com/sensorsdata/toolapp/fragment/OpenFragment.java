package com.sensorsdata.toolapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.adapter.LibAdapter;
import com.sensorsdata.toolapp.adapter.LogAdapter;
import com.sensorsdata.toolapp.dao.LibInfo;

import java.util.ArrayList;
import java.util.List;


public class OpenFragment extends Fragment  {

    private ListView listView;
    private List<LibInfo> list=new ArrayList<LibInfo>();
    private LibAdapter adapter;


    public OpenFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open,container,false);
        listView=view.findViewById(R.id.open_lv);
        initData();
        adapter=new LibAdapter(getContext(),list);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void initData(){
        list.add(new LibInfo("SensorsData Android SDK","https://github.com/sensorsdata/sa-sdk-android"));
        list.add(new LibInfo("Android-Debug-Database","https://github.com/amitshekhariitbhu/Android-Debug-Database"));
        list.add(new LibInfo("easypermissions","https://github.com/googlesamples/easypermissions"));
        list.add(new LibInfo("android-zxingLibrary","https://github.com/yipianfengye/android-zxingLibrary"));
        list.add(new LibInfo("EventBus","https://github.com/greenrobot/EventBus"));
        list.add(new LibInfo("MarkdownView","https://github.com/tiagohm/MarkdownView"));
        list.add(new LibInfo("腾讯 X5 内核","https://x5.tencent.com/docs/index.html"));


    }

}
