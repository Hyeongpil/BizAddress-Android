package com.kosign.bizaddress.api;

import android.util.Log;

import com.kosign.bizaddress.util.ComResource;
import com.kosign.bizaddress.util.HttpUtil;
import com.kosign.bizaddress.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by jung on 2016-02-23.
 */
public class BizplayApi {


    public static final String mBizplayUrl;
    public static final String mBizplayApiKey;
    public static final String mPtlId;

    public static final String mBizplayLoginApiId;

    static{

        //개발
        if(ComResource.mDevMode==1){
            mBizplayUrl = "http://sportal.dev.weplatform.co.kr:19990";
            mBizplayApiKey = "c40a650e-ce68-48ea-a96e-56c554d91165";
            mPtlId = "PTL_51";
        }else{
            mBizplayUrl = "https://www.bizplay.co.kr";
            mBizplayApiKey = "a8448e80-ae79-46b2-a2a2-5c7a2f985803";
            mPtlId = "PTL_3";
        }

        mBizplayLoginApiId = "user_jnng_cnfr_r001";
    }


    public String login(HashMap<String,String> inputMap){

        String apiUrl = BizplayApi.mBizplayUrl + "/BizplayGate";
        JSONObject mApiTrnHead = new JSONObject();
        JSONObject mApiTrnReqData = new JSONObject();

        String strApiTrnOutput = "";

        String strUserId = StringUtil.nvl(inputMap.get("USER_ID"));
        String strPasswd = StringUtil.nvl(inputMap.get("PASS_WD"));

        try{
            //BizplayGate Input
            mApiTrnHead.put("API_ID" , BizplayApi.mBizplayLoginApiId );
            mApiTrnHead.put("API_KEY" , BizplayApi.mBizplayApiKey );   //개발

            mApiTrnReqData.put("USER_ID" , strUserId );
            mApiTrnReqData.put("PWD",  strPasswd);
            mApiTrnReqData.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);

            //System.out.println("input data = " + mApiTrnHead);

            HttpUtil hutil = new HttpUtil();

            Log.e("bizplay","bizplayapi :"+URLEncoder.encode(mApiTrnHead.toString(),"UTF-8"));
            String strEnodeParam = "JSONData="+java.net.URLEncoder.encode(mApiTrnHead.toString()  ,"UTF-8");

            strApiTrnOutput = URLDecoder.decode(hutil.send(apiUrl, strEnodeParam, "POST", "utf-8", "utf-8", "5000", null), "UTF-8");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return strApiTrnOutput;
    }

}
