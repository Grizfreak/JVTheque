package com.jv.theque;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static App mContext;

    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }
}
