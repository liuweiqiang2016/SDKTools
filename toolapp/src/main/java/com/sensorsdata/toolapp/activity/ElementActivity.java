package com.sensorsdata.toolapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.tool.Utils;
import com.sensorsdata.toolapp.view.MyExpandableListView;
import com.sensorsdata.toolapp.view.MyGridView;
import com.sensorsdata.toolapp.view.MyListView;

import java.util.ArrayList;
import java.util.List;

public class ElementActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, RatingBar.OnRatingBarChangeListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    CheckedTextView checkedTextView;
    public String[] groupStrings = {"西游记", "水浒传", "三国演义", "红楼梦"};
    public String[][] childStrings = {
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
            {"宋江", "林冲", "李逵", "鲁智深"},
            {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
            {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"}
    };
    private List<String> recyclerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);
        //判断是否打开可视化全埋点/App 点击分析配对码
        Utils.checkOpenPairingCode(this,getIntent());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("常用控件页面");
        }
        intView();
        initListView();
        initExpandListView();
        initRecycleView();
        initGridView();
    }

    void intView(){

        findViewById(R.id.ele_btn).setOnClickListener(this);
        findViewById(R.id.ele_cb).setOnClickListener(this);
        checkedTextView= findViewById(R.id.ele_ctv);
        checkedTextView.setOnClickListener(this);
        findViewById(R.id.ele_ib).setOnClickListener(this);
        findViewById(R.id.ele_iv).setOnClickListener(this);
        findViewById(R.id.ele_rb).setOnClickListener(this);
        findViewById(R.id.ele_tv).setOnClickListener(this);
        findViewById(R.id.ele_et).setOnClickListener(this);
        RadioGroup radioGroup= findViewById(R.id.ele_rg);
        radioGroup.setOnCheckedChangeListener(this);
        RatingBar ratingBar=findViewById(R.id.ele_rbar);
        ratingBar.setOnRatingBarChangeListener(this);
        SeekBar seekBar=findViewById(R.id.ele_sb);
        seekBar.setOnSeekBarChangeListener(this);
        Spinner spinner=findViewById(R.id.ele_sp);
        spinner.setOnItemSelectedListener(this);
        Switch sw=findViewById(R.id.ele_sw);
        sw.setOnCheckedChangeListener(this);
        ToggleButton toggleButton=findViewById(R.id.ele_tb);
        toggleButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.e("onClick", "visual debug info:==========AppClick=====================");
        if (view.getId()==R.id.ele_ctv){
            checkedTextView.toggle();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void initListView() {
        MyListView listView = findViewById(R.id.ele_lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    private void initExpandListView() {
        MyExpandableListView expandableListView = findViewById(R.id.ele_exlv);
        expandableListView.setAdapter(new MyExpandableAdapter());
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    private void initRecycleView() {
        int count = 10;
        while(count-- > 0) {
            recyclerList.add("测试Item：" + count);
        }

        RecyclerView recyclerView = findViewById(R.id.ele_rv);
        recyclerView.setAdapter(new MyRecyclerAdapter());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initGridView(){
        ArrayList<String> data=new ArrayList<>();
        for (int i=0;i<10;i++){
            data.add(i+"");
        }
        MyGridView mGridView =findViewById(R.id.ele_gv);
        mGridView.setHaveScrollbar(false);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(i+"项");
            }
        });
    }

    class MyExpandableAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return groupStrings.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childStrings[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupStrings[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childStrings[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView = (TextView) getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            textView.setText(groupStrings[groupPosition]);
            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView = (TextView) getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            textView.setText(childStrings[groupPosition][childPosition]);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(new TextView(ElementActivity.this));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.textView.setText(recyclerList.get(i));
            viewHolder.textView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Log.e("SA.S", "visual debug info:==========AppClick=====================");


                }
            });
        }

        @Override
        public int getItemCount() {
            return recyclerList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
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

}