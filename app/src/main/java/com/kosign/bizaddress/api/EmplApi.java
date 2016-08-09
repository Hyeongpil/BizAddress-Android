package com.kosign.bizaddress.api;

import com.kosign.bizaddress.util.ComResource;

/**
 * Created by jung on 2016-02-27.
 */
public class EmplApi {
    private static EmplApi ourInstance = new EmplApi();

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
            mEmplInfoUrl = "http://emplinfo.wecambodia.com";// 캄보디아 개발 서버
            mEmplInfoApiKey = "de51933d-e5b7-f464-95f0-a414e162c1e1";
            mPtlId = "PTL_51";
        }else{
            mEmplInfoUrl = "https://emplinfo.appplay.co.kr";
            mEmplInfoApiKey = "0d931cf9-0d22-6ff6-a9df-55b623537f59";
            mPtlId = "PTL_3";
        }
        mEmplInfoSchUserApiId = "emplinfo05";
        mEmplInfoMyGrpApiId = "emplinfo09";
        mEmplInfoDvsnListApiId = "emplinfo21";
        mDvsnEmplListApiId = "emplinfo25";
    }

    public static EmplApi getInstance() {
        return ourInstance;
    }
    private EmplApi() {}
}
