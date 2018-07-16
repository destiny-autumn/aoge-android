package com.zoe.oa.aoge_android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zoe.custom.CustomProgress;
import com.zoe.custom.DialogManager;
import com.zoe.custom.DownLoadManager;
import com.zoe.custom.DownloadUtils;
import com.zoe.custom.MyWebViewDownLoadListener;
import com.zoe.custom.NetworkHelper;
import com.zoe.custom.VersionInfoParse;
import com.zoe.entity.VersionEntity;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Created by scorp on 2018/5/8.
 */
public class MainActivity extends AppCompatActivity {
    private VersionEntity versionEntity;
    private String localVersion;
    private final int UPDATA_NONEED = 0;
    private final int UPDATA_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int SDCARD_NOMOUNTED = 3;
    private final int DOWN_ERROR = 4;

    private String CookieStr = "";
    private WebView webView;
    private View mProgressView;
    private DownloadUtils downloadUtils;

    private String newVersionName = "";

    /*private final String LOGIN_URL = "https://120.86.191.104/sso";
    private final String URL = "http://19.104.9.199/";*/

    private final String LOGIN_URL = "http://www.workwow.cn:9009/Login.aspx";

//    private final String URL = "http://www.workwow.cn:9009/";

    private final String URL = "https://www.baidu.com/";



    /*private final String LOGIN_URL = "https://59.37.20.114/sso";
    private final String URL = "http://19.104.9.162/";*/

//    private final String LOGIN_URL = "https://120.86.191.116/sso";
//    private final String URL = "http://19.104.9.199/";

    private String openUrl = "";

    private CustomProgress loading;

    /*private TextView userid;
    private TextView usercode;
    private TextView username;
    private TextView loginticket;
    private TextView moreinfo;
    private EditText txtopenurl;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyWebViewDownLoadListener.mainActivity = this;

        // 检测网络状态
        if (!NetworkHelper.getInstance(this).isNetSuccess()) {
            DialogManager.getInstance().showNetMsgDialog(this);
            return;
        }

        //版本检测
        try {
            localVersion = getVersionName();
            CheckVersionTask cv = new CheckVersionTask();
            new Thread(cv).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);

        mProgressView = findViewById(R.id.loading_progress);
        webView = (WebView) findViewById(R.id.webview);


        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowFileAccess(true);// 设置可以访问文件
        webSettings.setAppCacheEnabled(true);//是否使用缓存
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);//DOM Storage

        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);
//        webView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY); //取消滚动条

        /*webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg)
            {
                //return true or false after performing the URL request
                WebView newWebView = new WebView(MainActivity.this);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    }
                });
                return true;
            }
        });*/

        // 先退出登录
        //logout();


        //Intent intent = this.getIntent();
        //String baseurl = intent.getStringExtra("baseurl");

        /*loginticket = (TextView) findViewById(R.id.loginticket);
        moreinfo = (TextView) findViewById(R.id.moreinfo);
        txtopenurl = (EditText) findViewById(R.id.txtopenurl);
        String[] userinfo = intent.getStringArrayExtra("userinfo");
        if (userinfo != null) {
            try {

                String s = "";
                for (int i = 0; i < userinfo.length; i++) {
                    s += "\n" + String.valueOf(i) + ": " + userinfo[i];
                }
                moreinfo.setText(s);
                loginticket.setText(userinfo[6]);
                SSOClientBuilder builder = new SSOClientBuilder();
                builder.setClientId("clientId");
                builder.setClientSecret("clientSecret");
                builder.setSsoBaseEndpoint(LOGIN_URL);
                SSOClient ssoClient = builder.build();
                openUrl = ssoClient.getLoginTicketLoginEndpoint(userinfo[6], URL + "app/Index.aspx");//接下来要调用浏览器打开openUrl
                txtopenurl.setText(openUrl);
            } catch (Exception ex) {
                loginticket.setText(ex.getMessage());
            }

            Button mEmailSignInButton = (Button) findViewById(R.id.load_url);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadUrl();
                }
            });

        }*/


        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，而不是新开Android的系统browser中响应该链接，必须覆盖 webView的WebViewClient对象
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return false; // false 显示frameset, true 不显示Frameset
//            }
//
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed(); // Ignore SSL certificate errors
//                //handler.cancel(); //默认的处理方式，WebView变成空白页
//                //handleMessage(Message msg); //其他处理
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                //有页面跳转时被回调
//                showProgress(true);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                //页面跳转结束后被回调
//                super.onPageFinished(view, url);
//                showProgress(false);
//                webView.clearHistory();
//            }
////            @Override
////            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
////                Toast.makeText(MainActivity.this, "出错了! " + description, Toast.LENGTH_SHORT).show();
////            }
//
//        });

        //调用下载文件功能


        /**
         * @author Destiny
         * @create 2018/7/16 0016 10:37
         * @Describe 调用保存Cookie与设置Cookie的重写类
         */
        webView.setWebViewClient(new MyWebViewClient());


        webView.setDownloadListener(new MyWebViewDownLoadListener(MainActivity.this));

//        Download("http://192.168.0.103:8080/destiny/destiny.apk","destiny");

//        downloadBySystem("http://192.168.0.103:8080/destiny/destiny.png","","destiny");


        loadUrl();
    }


    /**
     * @author Destiny
     * @create 2018/7/16 0016 10:32
     * @Describe 保存Cookie
     */
    void setCookie() {
        String StringCookie = "key=" + CookieStr + ";path=/";
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();
            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie("", StringCookie);
    }


    /**
     * @author Destiny
     * @create 2018/7/16 0016 10:36
     * @Describe 设置Cookie
     */
    public class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            setCookie();
            webview.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
            CookieStr = cookieManager.getCookie(url);
            super.onPageFinished(view, url);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        // 检测网络状态
        if (!NetworkHelper.getInstance(this).isNetSuccess()) {
            DialogManager.getInstance().showNetMsgDialog(this);
            return;
        }

        //版本检测
        try {
            localVersion = getVersionName();
            CheckVersionTask cv = new CheckVersionTask();
            new Thread(cv).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        String url = getIntent().getStringExtra(URL + "Logout.aspx");
        webView.loadUrl(url);
    }

    private void loadUrl() {
        //loading = CustomProgress.show(MainActivity.this, "正在载入...", false, null);

        //openUrl = getIntent().getStringExtra("openUrl");
        /*if (!openUrl.equals(""))
            webView.loadUrl(openUrl);*/
        //webView.loadUrl(URL + "app/Index.aspx");
        //webView.loadUrl("http://workwow.cn/test/test.html");
        webView.loadUrl(URL);
    }


    /*WebChromeClient webClient = new WebChromeClient(){
        public void onCloseWindow(WebView w){
            super.onCloseWindow(w);
        }
    };*/

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            webView.setVisibility(show ? View.GONE : View.VISIBLE);
            webView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    webView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            webView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private String getVersionName() throws Exception {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }


    public class CheckVersionTask implements Runnable {

        InputStream is;

        public void run() {
            try {
                String path = getResources().getString(R.string.url_server);
                java.net.URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    is = conn.getInputStream();
                    versionEntity = VersionInfoParse.getUpdataInfo(is);
                    if (versionEntity.version.equals(localVersion)) {
                        Message msg = new Message();
                        msg.what = UPDATA_NONEED;
                        handler.sendMessage(msg);
                    } else {
                        newVersionName = versionEntity.version;
                        Message msg = new Message();
                        msg.what = UPDATA_CLIENT;
                        handler.sendMessage(msg);
                    }
                }

            } catch (Exception e) {
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case UPDATA_NONEED:
                    break;
                case UPDATA_CLIENT:
                    // 清除webview缓存
                    //MainActivity.this.webView.loadUrl(url);
                    webView.reload();

                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    Toast.makeText(MainActivity.this, "获取系统版本信息失败", Toast.LENGTH_LONG).show();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(MainActivity.this, "下载新版本失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    /**
     * 弹出对话框通知用户更新程序
     * 弹出对话框的步骤：
     * 1.创建alertDialog的builder.
     * 2.要给builder设置属性, 对话框的内容,样式,按钮
     * 3.通过builder 创建一个对话框
     * 4.对话框show()出来
     */
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("发现新版本V" + newVersionName + "，请尽快升级。");
        //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                downloadApk();
            }
        });
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //login();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }


    /*
     * 从服务器中下载APK
     */
    protected void downloadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("DOWNLOAD:" + versionEntity.url);
                    File file = DownLoadManager.getFileFromServer(versionEntity.url, pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    // 如果不做任何处理，浏览网页，点击系统“Back”键，整个Browser会调用finish()而结束自身，
    // 如果希望浏览的网 页回退而不是推出浏览器，需要在当前Activity中处理并消费掉该Back事件。
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if((keyCode==KeyEvent.KEYCODE_BACK)&&webview.canGoBack()){
        // webview.goBack();
        // return true;
        // }
        return false;
    }


    //tomcat-test
//    public void Download(String url, String name) {
//        downloadUtils = new DownloadUtils(MainActivity.this);
//        downloadUtils.downloadAPK(url, name);
//    }


}

