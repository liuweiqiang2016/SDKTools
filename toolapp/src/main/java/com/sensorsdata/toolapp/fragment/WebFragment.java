package com.sensorsdata.toolapp.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.dialog.SensorsDataDialogUtils;
import com.sensorsdata.toolapp.R;
import com.sensorsdata.toolapp.dao.LogMsg;
import com.sensorsdata.toolapp.tool.Utils;

import org.greenrobot.eventbus.EventBus;


/*
 * 原生 WebView 加载 H5 页面
 * */
public class WebFragment extends Fragment {

    private WebView webView;
    private boolean isSupportJellyBean,enableVerify,isOldVersion;
    private String url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStateApp) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        //判断是否打开可视化全埋点/App 点击分析配对码
       Utils.checkOpenPairingCode(getContext(),getActivity().getIntent());
        //允许WebView使用inspect调试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView=view.findViewById(R.id.h5_web);
        mWebViewSetting();
        initData();
        //如果使用新版打通,这里无需额外处理
        webView.loadUrl(url);
        //如果是旧版本打通,需手动调用
        if (isOldVersion){
            SensorsDataAPI.sharedInstance().showUpWebView(webView,isSupportJellyBean,enableVerify);
        }
        return view;
    }

    //webview 设置
    private void mWebViewSetting(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //打印Console日志
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("ConsoleMessage",consoleMessage.message());
                EventBus.getDefault().post(new LogMsg(Utils.getTime(),consoleMessage.message()));
                return true;
            }
        });
//        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setDatabaseEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("url:"+url);
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        //修复中文乱码
        webSettings.setDefaultTextEncodingName("UTF-8");

//        //设置true,才能让Webivew支持<meta>标签的viewport属性
//        webSettings.setUseWideViewPort(true);
//        //设置可以支持缩放
//        webSettings.setSupportZoom(true);
//        //设置出现缩放工具
//        webSettings.setBuiltInZoomControls(true);

    }

    //参数设置
    private void initData() {
        isOldVersion=getActivity().getIntent().getBooleanExtra("isOldVersion",false);
        isSupportJellyBean=getActivity().getIntent().getBooleanExtra("isSupportJellyBean",false);
        enableVerify=getActivity().getIntent().getBooleanExtra("enableVerify",false);
        url=getActivity().getIntent().getStringExtra("url");
    }

    public boolean isIndexPage(){
        String currentURL=webView.getUrl();
        if (webView!=null&&currentURL!=null){
            if (currentURL.equals("about:blank")||currentURL.equals(url)||currentURL.equals(url+"/")){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    public void goBack(){
        if (webView!=null){
            webView.goBack();
        }
    }

}
