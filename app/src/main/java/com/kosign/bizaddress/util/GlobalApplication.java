package com.kosign.bizaddress.util;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.kosign.bizaddress.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */
public class GlobalApplication extends Application {
    private static volatile GlobalApplication instance = null;
    private static volatile Activity currentActivity = null;
    private EmplPreference pref;
    private HashMap<String,String> division_map = new HashMap<>(); // 부서 코드를 담고 있는 부서 해시맵 < 이름 , 부서코드 >
    private ArrayList<UserInfo> initialData;
    private ProgressDialog dlgProgress;

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

    public HashMap<String, String> getDivision_map() {return division_map;}
    public void setDivision_map(HashMap<String, String> division_map) {this.division_map = division_map;}

    public ArrayList<UserInfo> getInitialData() {return initialData;}
    public void setInitialData(ArrayList<UserInfo> initialData) {this.initialData = initialData;}

    public void setDlgProgress(ProgressDialog dialog){
        dlgProgress = dialog;
        dlgProgress.setMessage("데이터를 불러오는 중입니다.\n잠시만 기다려 주세요.");
        dlgProgress.setCancelable(true);
        dlgProgress.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                dlgProgress.cancel();
                dlgProgress.dismiss();
            }});
    }
    public void showDlgProgress(){
        dlgProgress.show();
    }
    public void dismissDlgProgress(){
        dlgProgress.dismiss();
    }

}
