package com.kosign.bizaddress.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.model.UserInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jung on 2016-02-23.
 */
public class EmplPreference {
    final static String TAG = "EmplPreference";
    private final String mPreferenceId = "com.emplinfo.pref";

    public final static String PREF_INTRO_USER_AGREEMENT = "PREF_USER_AGREEMENT";
    public final static String PREF_MAIN_VALUE = "PREF_MAIN_VALUE";


    static Context mContext;

    public EmplPreference(Context c) {
        mContext = c;
    }

    public void putString(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 직원 데이터를 받아 Gson으로 변환 후 저장
     */
    public void putEmplList(ArrayList<UserInfo> value) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("EmplList", toJson((value)));
        editor.commit();
    }
    public void putDivisionList(ArrayList<HighDivision> value){
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("DivisionList", toJson((value)));
        editor.commit();
    }
    public static String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

    public ArrayList<UserInfo> getEmplList(){
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        ArrayList<UserInfo> emplList = new ArrayList<>();
        try{
            String json = pref.getString("EmplList","");
            emplList = (ArrayList<UserInfo>)fromJson(json, new TypeToken<ArrayList<UserInfo>>() {}.getType());
            return emplList;
        }catch (Exception e){
            Log.e(TAG,"gson 변환 오류"+e.getMessage());
        }
            return emplList;
    }

    public ArrayList<HighDivision> getDivisionList(){
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        ArrayList<HighDivision> divisionList = new ArrayList<>();
        try{
            String json = pref.getString("DivisionList","");
            divisionList = (ArrayList<HighDivision>)fromJson(json, new TypeToken<ArrayList<HighDivision>>() {}.getType());
            return divisionList;
        }catch (Exception e){
            Log.e(TAG,"gson 변환 오류"+e.getMessage());
        }
        return divisionList;
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }


    public String getString(String key) {
        try {
            return getString(key, "");
        } catch (Exception e) {}

        return "";
    }

    public String getString(String key, String val) {
        SharedPreferences pref = mContext.getSharedPreferences(mPreferenceId, Activity.MODE_PRIVATE);
        try {
            return pref.getString(key, val);
        } catch (Exception e) {
            return val;
        }
    }
}
