package com.sensorsdata.toolapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.adapter.LogAdapter;
import com.sensorsdata.toolapp.dao.LogMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogFragment extends Fragment {

    private ListView listView;
    private List<LogMsg> list=new ArrayList<LogMsg>();
    private LogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        listView=view.findViewById(R.id.lv);
        System.out.println("onCreateView");
        EventBus.getDefault().register(this);
        refreshUI();
        return view;
    }
    public void refreshUI(){
        //对list进行倒序
        Collections.reverse(list);
        adapter=new LogAdapter(getContext(),list);
        listView.setAdapter(adapter);

        System.out.println("refreshUI");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        System.out.println("onDestroyView");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(LogMsg logMsg){
        System.out.println("time:"+logMsg.getTime());
        System.out.println("msg:"+logMsg.getMsg());
        list.add(logMsg);
        System.out.println("size:"+list.size());
    }
}
