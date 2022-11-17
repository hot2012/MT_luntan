package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override //监听加载进度
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override//接受网页标题
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //把当前的Title设置到Activity的title上显示
                setTitle(title);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                Log.i("test", "openFileChooser 1");
                uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
                Log.i("test", "openFileChooser 2");
                uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.i("test", "openFileChooser 3");
                uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android  >= 5.0
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
                uploadFiles = filePathCallback;
                openFileChooseProcess();
                return true;
            }

            class MyWebViewClient extends WebViewClient {
               @Override  //WebView代表是当前的WebView
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    //表示在当前的WebView继续打开网页
                   view.loadUrl(request.getUrl().toString());
                  return true;
               }
           }
        });
        //加载本地网页
      //webView.loadUrl("file:///android_asset/index.html");
        //加载外部网页
      webView.loadUrl("https://bbs.binmt.cc/index.php?fromuid=42093");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        WebSettings settings = webView.getSettings();
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        //设定支持网页viewport
       // webView.getSettings().setUseWideViewPort(true);
        //支持缩放
       // webView.getSettings().setSupportZoom(true);
        // 设置显示缩放按钮
       // webView.getSettings().setBuiltInZoomControls(true);
        //优先使用缓存
       // webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

    }
    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "选择文件"), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (uploadFile != null) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    if (uploadFiles != null) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFiles.onReceiveValue(new Uri[]{result});
                        uploadFiles = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (uploadFile != null) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }
            if (uploadFiles != null) {
                uploadFiles.onReceiveValue(null);
                uploadFiles = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    /**
     * 确保注销配置能够被释放
     */
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果按返回键，此时WebView网页可以后退
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

        //支持HTTPS访问

    }

}


