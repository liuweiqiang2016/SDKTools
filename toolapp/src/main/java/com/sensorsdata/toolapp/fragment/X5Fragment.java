package com.sensorsdata.toolapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.dao.LogMsg;
import com.sensorsdata.toolapp.tool.Utils;
import com.sensorsdata.toolapp.view.X5WebView;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;


/*
 *  X5WebView 加载 H5 页面
 * */
public class X5Fragment extends Fragment {

    private X5WebView webView;
    private boolean enableVerify,isOldVersion;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStateApp) {
        View view = inflater.inflate(R.layout.fragment_x5, container, false);
        webView=view.findViewById(R.id.x5_web);
        //System.out.println("版本号"+webView.getX5WebViewExtension().getQQBrowserVersion());
        mWebViewSetting();
        initData();
        //如果使用新版打通,这里无需额外处理
        webView.loadUrl(url);
        //如果是旧版本打通,需手动调用
        if (isOldVersion){
            SensorsDataAPI.sharedInstance().showUpX5WebView(webView,enableVerify);
        }
        return view;
    }

    //webview 设置
    private void mWebViewSetting(){
        webView.getSettings().setJavaScriptEnabled(true);
        //打印Console日志
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("ConsoleMessage",consoleMessage.message());
                EventBus.getDefault().post(new LogMsg(Utils.getTime(),consoleMessage.message()));
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                System.out.println("url:"+url);
                webView.loadUrl(url);
                return super.shouldOverrideUrlLoading(webView, url);
            }
        });
    }

    //参数设置
    private void initData() {
        isOldVersion=getActivity().getIntent().getBooleanExtra("isOldVersion",false);
        enableVerify=getActivity().getIntent().getBooleanExtra("enableVerify",false);
        url=getActivity().getIntent().getStringExtra("url");
    }

}
