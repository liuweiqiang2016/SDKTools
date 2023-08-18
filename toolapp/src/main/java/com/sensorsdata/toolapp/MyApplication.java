package com.sensorsdata.toolapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.multidex.MultiDex;

import com.sensorsdata.toolapp.tool.Utils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);
        initX5();
        Utils.initMini(this);
    }

    //修复低版本手机方法数超过65535的异常
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean b) {
                System.out.println("状态:"+b);
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                if (!b) {
                    Toast.makeText(getApplicationContext(), "X5 内核初始化失败!", Toast.LENGTH_SHORT).show();
                    //设置自带webView属性
                    WebView webView = new WebView(getApplicationContext());
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setBlockNetworkImage(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    }
                }
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //流量下载内核
        QbSdk.setDownloadWithoutWifi(true);
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                System.out.println("Download:"+i);
            }

            @Override
            public void onInstallFinish(int i) {
                System.out.println("Install:"+i);
            }

            @Override
            public void onDownloadProgress(int i) {
                System.out.println("Progress:"+i);
            }
        });
    }
}
