package com.cathy.ninepatchcontroldemo.base;

import android.app.Application;

import com.cathy.ninepatchcontroldemo.utils.AppExecutors;

/**
 * +--------------------------------------+
 * + @author Catherine Liu
 * +--------------------------------------+
 * + 2020/7/7 16:53
 * +--------------------------------------+
 * + Des:
 * +--------------------------------------+
 */

public class BaseApplication extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mAppExecutors = new AppExecutors();

    }
    private static BaseApplication mInstance;


    public static BaseApplication getInstance() {
        return mInstance;
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }

}
