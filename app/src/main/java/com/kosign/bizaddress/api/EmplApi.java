package com.kosign.bizaddress.api;

import android.content.res.Resources;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.util.ComResource;
import com.kosign.bizaddress.util.GlobalApplication;

/**
 * Created by jung on 2016-02-27.
 */
public class EmplApi {
    private static EmplApi ourInstance = new EmplApi();
    private static Resources resources = GlobalApplication.getInstance().getResources();

    public static final String mEmplInfoUrl;
    public static final String mEmplInfoApiKey;
    public static final String mPtlId;

    public static final String mEmplInfoSchUserApiId;
    public static final String mEmplInfoMyGrpApiId;
    public static final String mEmplInfoDvsnListApiId;
    public static final String mDvsnEmplListApiId;

    static{
        //개발
        if(ComResource.mDevMode==1){
//            mEmplInfoUrl = "http://emplinfo.webcash.co.kr"; // 한국 개발 서버
            mEmplInfoUrl = resources.getString(R.string.development_server_url);// 캄보디아 개발 서버
            mEmplInfoApiKey = resources.getString(R.string.development_api_key);
            mPtlId = resources.getString(R.string.development_ptl_id);
        }else{
            mEmplInfoUrl = resources.getString(R.string.server_url);
            mEmplInfoApiKey = resources.getString(R.string.api_key);
            mPtlId = resources.getString(R.string.ptl_id);
        }
        mEmplInfoSchUserApiId = resources.getString(R.string.mEmplInfoSchUserApiId);
        mEmplInfoMyGrpApiId = resources.getString(R.string.mEmplInfoMyGrpApiId);
        mEmplInfoDvsnListApiId = resources.getString(R.string.mEmplInfoDvsnListApiId);
        mDvsnEmplListApiId = resources.getString(R.string.mDvsnEmplListApiId);
    }

    public static EmplApi getInstance() {
        return ourInstance;
    }
    private EmplApi() {}
}
