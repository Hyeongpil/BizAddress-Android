package com.kosign.bizaddress.util;

import android.app.Activity;
import android.app.Application;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */
public class GlobalApplication extends Application {
    private static volatile GlobalApplication instance = null;
    private static volatile Activity currentActivity = null;
    private EmplPreference pref;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static GlobalApplication getInstance() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        pref = new EmplPreference(this);
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    public EmplPreference getPref() {
        return pref;
    }
}