package com.simple.ebook.base;

import android.app.Application;
import android.content.Context;

import com.allen.library.RxHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author hych
 * @date 2018/7/9 14:26
 */
public class BookApplication extends Application {
    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
//        initRxHttpUtils();
    }

    public static Context getContent() {
        return app;
    }

    private void initRxHttpUtils() {
        /**
         * 初始化配置
         */
        RxHttpUtils.init(this);
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS);
        RxHttpUtils
                .getInstance()
                //开启全局配置
                .config()
                //全局的BaseUrl
                .setBaseUrl(Constant.BASE_URL)
                //全局的请求头信息
//                .setHeaders(map)
                //全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(false)
                //全局ssl证书认证
                //信任所有证书,不安全有风险
//                .setSslSocketFactory()
                //使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.cer"))
                //使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.bks"), "123456", getAssets().open("your.cer"))
                //全局超时配置
                //全局是否打开请求log日志
                .setOkClient(client.build())
                .setLog(true);


    }
}
