package com.sensorsdata.toolapp.dao;

public class LogMsg {

    private String time;
    private String msg;

    public LogMsg(String time,String msg){
        this.time=time;
        this.msg=msg;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
