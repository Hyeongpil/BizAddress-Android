package com.kosign.bizaddress.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jung on 2016-02-23.
 */
public class EmplPreference {

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
