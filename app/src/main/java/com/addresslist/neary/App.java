package com.addresslist.neary;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.uuzuche.lib_zxing.ZApplication;

/**
 * app 类
 */
public class App extends ZApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //用于调试数据库
        //在chrome中打开 chrome://inspect/#devices 即可调试
        Stetho.initializeWithDefaults(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
