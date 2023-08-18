package com.sensorsdata.toolapp.tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.sensorsdata.analytics.android.minisdk.MiniSensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.dialog.SensorsDataDialogUtils;
import com.sensorsdata.analytics.android.sdk.visual.HeatMapService;
import com.sensorsdata.analytics.android.sdk.visual.VisualizedAutoTrackService;
import com.sensorsdata.analytics.android.sdk.visual.view.PairingCodeEditDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    //跳转页面类型
    public static final int SDK_NATIVE=100;
    public static final int NATIVE_VIEW=101;
    public static final int SDK_BRIDGE=200;
    public static final int BRIDGE_WEBVIEW=201;
    public static final int BRIDGE_X5WEBVIEW=202;
    public static final int SDK_VISUALIZED=300;
    public static final int SETTING_HOME=400;
    public static final int SETTING_USER=401;
    public static final int SETTING_ABOUT=402;
    public static final int SETTING_OPEN=403;
    public static String PAGE_TYPE="type";
    //SDK 参数保存的各类信息
    public static final String SP_NAME="appConfig";
    public static final String SP_NATIVE_SA_URL="native_sa_url";
    public static final String SP_NATIVE_AUTO="native_auto";
    public static final String SP_NATIVE_ENCRYPT="native_encrypt";
    public static final String SP_BRIDGE_SA_URL="bridge_sa_url";
    public static final String SP_BRIDGE_H5_URL="bridge_h5_url";
    public static final String SP_BRIDGE_H5_SW="bridge_h5_sw";
    public static final String SP_BRIDGE_VERSION_SW="bridge_version_sw";
    public static final String SP_BRIDGE_JB_SW="bridge_jb_sw";
    public static final String SP_BRIDGE_CHECK_SW="bridge_check_sw";
    public static final String SP_VISUALIZED_SA_URL="visualized_sa_url";
    public static final String SP_VISUALIZED_H5_URL="visualized_h5_url";
    public static final String SP_VISUALIZED_PROPERTIES_SW="visualized_properties_sw";
    public static final String SP_VISUALIZED_BRIDGE_SW="visualized_bridge_sw";
    public static final String SP_VISUALIZED_TYPE_SW="visualized_type_sw";
    //设置页面存储信息
    public static final String SP_SETTING="appSetting";
    public static final String SP_SETTING_SDK="setting_sdk";
    public static final String SP_SETTING_CONFIG="setting_config";

    //获取当前时间
    public static String getTime(){

        long currentTime=System.currentTimeMillis();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date=new Date(currentTime);
        String time=formatter.format(date);
        return time;
    }

    /**
     * 判断 SDK 是否初始化
     * @return SDK 初始化状态
     */
    public static boolean SDKIsInit(){
        String distinct_id= SensorsDataAPI.sharedInstance().getDistinctId();
        //如果 distinct_id 不为空，说明 SDK 已正常初始化
        if (distinct_id!=null){
            return true;
        }
        return false;
    }

    /**
     * 检查 SP 状态
     * @param context context 对象
     */
    public static void checkSPState(Context context){
        SharedPreferences sp=context.getSharedPreferences(SP_SETTING, Context.MODE_PRIVATE);
        //如果不存储 SDK 历史信息，主动清除之前存在的 SP
        boolean sdkIsSave=sp.getBoolean(SP_SETTING_SDK,true);
        if (!sdkIsSave){
            String[] sps=new String[]{"com.sensorsdata.analytics.android.sdk.SensorsDataAPI","sensorsdata"};
            for (String name:sps){
                SharedPreferences sdkSP=context.getSharedPreferences(name, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sdkSP.edit();
                editor.clear();
                editor.apply();
            }
        }
        //如果不存储参数配置信息
        boolean configIsSave=sp.getBoolean(SP_SETTING_CONFIG,true);
        if (!configIsSave){
            SharedPreferences sdkSP=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sdkSP.edit();
            editor.clear();
            editor.apply();
        }

    }

    /**
     * 检查是否弹出配对码
     * @param context context 对象
     */
    public static void checkOpenPairingCode(Context context, Intent intent){
        //只有可视化全埋点服务味在运行且 App 点击分析服务未运行且标记开启可视化/App 点击分析，才需要弹窗
        if (!VisualizedAutoTrackService.getInstance().isServiceRunning()&&!HeatMapService.getInstance().isServiceRunning()&&intent.getBooleanExtra("visSwitch",false)){
//            SensorsDataDialogUtils.showPairingCodeInputDialog(context);
            PairingCodeEditDialog dialog = new PairingCodeEditDialog(context);
            dialog.show();
        }

    }


    /**
     * @param context context 对象
     * @return 当前应用的版本信息
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化 mini SDK，防止与主 SDK 相互干扰，用来采集、分析小工具使用情况
     * @param context context 对象
     */
    public static void initMini(Context context){
        MiniSensorsDataAPI.sharedInstance(context,"http://10.129.20.62:8106/sa?project=liuweiqiang", MiniSensorsDataAPI.DebugMode.DEBUG_OFF);
        MiniSensorsDataAPI.sharedInstance().trackAppCrash();
        MiniSensorsDataAPI.sharedInstance().track("toolLaunch");
        MiniSensorsDataAPI.sharedInstance().enableLog(true);
    }

}
