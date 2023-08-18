package com.sensorsdata.toolapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.activity.ElementActivity;
import com.sensorsdata.toolapp.dao.LogMsg;
import com.sensorsdata.toolapp.tool.Utils;
import com.sensorsdata.toolapp.view.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


public class NativeViewFragment extends Fragment implements View.OnClickListener {

    MyListView lv_event,lv_profile,lv_user,lv_item,lv_idm3;
    Spinner spinner;
    Button btn_track,btn_element;
    String[] str_event=new String[]{"track 无属性事件","有属性事件(news:战争爆发)","设置静态公共属性(utm:tiktok)","激活事件"};
    String[] str_profile=new String[]{"设置用户事件(age:18)","取消用户属性(age:18)","删除用户"};
    String[] str_user=new String[]{"设置匿名ID(uuid)","随机设置匿名ID","重置匿名ID","登录用户(Tom)","随机登录账号","退出登录"};
    String[] str_item=new String[]{"item设置(type_123,id_456)","取消设置(type_123,id_456)"};
    String[] str_idm3=new String[]{"绑定业务ID(wid:123456)","解绑业务ID(wid:123456)"};

    public NativeViewFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_native_view,container,false); //  此处的布局文件是普通的线性布局（此博客忽略）
        initViews(view);
        return view;
    }

    private void initViews(View view){
        lv_event=view.findViewById(R.id.lv_event);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,str_event);
        lv_event.setAdapter(adapter1);
        lv_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        SensorsDataAPI.sharedInstance().track("hello");
                        break;
                    case 1:
                        try {
                            SensorsDataAPI.sharedInstance().track("hello",new JSONObject().put("news","战争爆发"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            SensorsDataAPI.sharedInstance().registerSuperProperties(new JSONObject().put("utm","tiktok"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        SensorsDataAPI.sharedInstance().trackAppInstall();
                        break;
                }
                //记录操作信息
                EventBus.getDefault().post(new LogMsg(Utils.getTime(),str_event[i]));
            }
        });

        lv_profile=view.findViewById(R.id.lv_profile);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,str_profile);
        lv_profile.setAdapter(adapter2);
        lv_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        SensorsDataAPI.sharedInstance().profileSet("age",18);
                        break;
                    case 1:
                        SensorsDataAPI.sharedInstance().profileUnset("age");
                        break;
                    case 2:
                        SensorsDataAPI.sharedInstance().profileDelete();
                        break;
                }
                //记录操作信息
                EventBus.getDefault().post(new LogMsg(Utils.getTime(),str_profile[i]));
            }
        });


        lv_item=view.findViewById(R.id.lv_item);
        ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,str_item);
        lv_item.setAdapter(adapter3);
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        SensorsDataAPI.sharedInstance().itemSet("type_123","id_456",new JSONObject());
                        break;
                    case 1:
                        SensorsDataAPI.sharedInstance().itemDelete("type_123","id_456");
                        break;
                }
                //记录操作信息
                EventBus.getDefault().post(new LogMsg(Utils.getTime(),str_item[i]));
            }
        });


        lv_user=view.findViewById(R.id.lv_user);
        ArrayAdapter<String> adapter4=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,str_user);
        lv_user.setAdapter(adapter4);
        lv_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String distinc_id="";
                switch (i){
                    case 0:
                        SensorsDataAPI.sharedInstance().identify("uuid");
                        break;
                    case 1:
                        SensorsDataAPI.sharedInstance().identify("uuid_"+(int)((Math.random()*1000)%1000));
                        break;
                    case 2:
                        SensorsDataAPI.sharedInstance().resetAnonymousId();
                        break;
                    case 3:
                        SensorsDataAPI.sharedInstance().login("Tom");
                        break;
                    case 4:
                        SensorsDataAPI.sharedInstance().login("Tom_"+(int)((Math.random()*1000)%1000));
                        break;
                    case 5:
                        SensorsDataAPI.sharedInstance().logout();
                        break;
                }
                distinc_id=SensorsDataAPI.sharedInstance().getDistinctId();
                //记录操作信息
                EventBus.getDefault().post(new LogMsg(Utils.getTime(),str_user[i])+",distinct_id 为"+distinc_id);
            }
        });


        lv_idm3=view.findViewById(R.id.lv_idm3);
        ArrayAdapter<String> adapter5=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,str_idm3);
        lv_idm3.setAdapter(adapter5);
        lv_idm3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        SensorsDataAPI.sharedInstance().bind("wid","123456");
                        break;
                    case 1:
                        SensorsDataAPI.sharedInstance().unbind("wid","123456");
                        break;
                }
                //记录操作信息
                EventBus.getDefault().post(new LogMsg(Utils.getTime(),str_idm3[i]));
            }
        });
        spinner=view.findViewById(R.id.sp_counts);
        btn_track=view.findViewById(R.id.btn_track);
        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=spinner.getSelectedItemPosition();
                switch (count){
                    case 0:
                        //记录操作信息
                        EventBus.getDefault().post(new LogMsg(Utils.getTime(),"一次性触发100条数据"));
                        int i=0;
                        while (i<100){
                            SensorsDataAPI.sharedInstance().track("largeEvent"+i);
                            i++;
                        }
                        break;
                    case 1:
                        //记录操作信息
                        EventBus.getDefault().post(new LogMsg(Utils.getTime(),"一次性触发1000条数据"));
                        int j=0;
                        while (j<1000){
                            SensorsDataAPI.sharedInstance().track("largeEvent"+j);
                            j++;
                        }
                        break;
                    case 2:
                        //记录操作信息
                        EventBus.getDefault().post(new LogMsg(Utils.getTime(),"一次性触发1000条数据"));
                        int k=0;
                        while (k<10000){
                            SensorsDataAPI.sharedInstance().track("largeEvent"+k);
                            k++;
                        }
                        break;
                }

            }
        });

        btn_element=view.findViewById(R.id.btn_element);
        btn_element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //记录操作信息
                EventBus.getDefault().post(new LogMsg(Utils.getTime(),"切换至常用控件页面"));
                Intent intent=new Intent(getContext(), ElementActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onClick(View view) {

    }



}
