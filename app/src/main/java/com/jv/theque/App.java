package com.jv.theque;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static App mContext;
    private static boolean firstLoad = true;

    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static boolean isFirstLoad(){
        return firstLoad;
    }

    public static void setFirstLoad(boolean value){
        firstLoad = value;
    }
}
