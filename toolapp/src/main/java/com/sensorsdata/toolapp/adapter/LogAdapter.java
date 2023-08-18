package com.sensorsdata.toolapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.dao.LogMsg;

import java.util.List;

public class LogAdapter extends BaseAdapter {

    private List<LogMsg> list;
    private Context mContext;

    public LogAdapter(Context context, List<LogMsg> list) {
        this.mContext =context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            holder = new ViewHolder();
            holder.time = convertView.findViewById(R.id.item_time);
            holder.msg=convertView.findViewById(R.id.item_msg);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.time.setText(list.get(position).getTime());
        holder.msg.setText(list.get(position).getMsg());
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    static class ViewHolder {
        TextView time;
        TextView msg;
    }
}
