package com.kosign.bizaddress.api;

import android.os.AsyncTask;

import com.kosign.bizaddress.util.ComResource;
import com.kosign.bizaddress.util.HttpUtil;
import com.kosign.bizaddress.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
            mEmplInfoUrl = "http://emplinfo.webcash.co.kr";
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

    private EmplApi() {
    }

    //직원 조회
    public String emplinfo05(JSONObject inputJson){

        String apiUrl = EmplApi.mEmplInfoUrl + "/gw/ApiGate?";

        JSONObject mApiTrnHead = new JSONObject();
        JSONObject mApiTrnReqData = new JSONObject();

        String strApiTrnOutput = "";

        try{

            String strChnlId = StringUtil.nvl((String)inputJson.get("CHNL_ID"));
            String strUseInttId = StringUtil.nvl((String)inputJson.get("USE_INTT_ID"));
            String strPagePerCnt  = StringUtil.nvl((String) inputJson.get("PAGE_PER_CNT"));
            String strPageNo  = StringUtil.nvl((String) inputJson.get("PAGE_NO"));

            //검색어
            String strSrchWord  = StringUtil.nvl((String) inputJson.get("SRCH_WORD"));

            //그룹코드
            String strGrpCd = StringUtil.nvl((String)inputJson.get("GRP_CD"));

            String strUserId = StringUtil.nvl((String) inputJson.get("USER_ID"));

            String strDvsnCd = StringUtil.nvl((String) inputJson.get("DVSN_CD"));


            mApiTrnHead.put("SVC_CD" , EmplApi.mEmplInfoSchUserApiId );
            mApiTrnHead.put("SECR_KEY" , EmplApi.mEmplInfoApiKey );
            mApiTrnHead.put("PTL_STS" , "C" );
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);


            mApiTrnReqData.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnReqData.put("CHNL_ID", strChnlId );
            mApiTrnReqData.put("USE_INTT_ID", strUseInttId);

            mApiTrnReqData.put("ACVT_YN", "Y");
            mApiTrnReqData.put("PAGE_NO", strPageNo);
            mApiTrnReqData.put("PAGE_PER_CNT", strPagePerCnt);

            mApiTrnReqData.put("SRCH_WORD", strSrchWord);
            mApiTrnReqData.put("GRP_CD", strGrpCd);
            mApiTrnReqData.put("DVSN_CD", strDvsnCd);
            mApiTrnReqData.put("USER_ID", strUserId);



            //System.out.println("input data = " + mApiTrnHead);

            HttpUtil hutil = new HttpUtil();

            String strEnodeParam = "JSONData="+java.net.URLEncoder.encode(mApiTrnHead.toString()  ,"UTF-8");

            strApiTrnOutput = URLDecoder.decode(hutil.send(apiUrl, strEnodeParam, "POST", "utf-8", "utf-8", "5000", null), "UTF-8");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return strApiTrnOutput;
    }



    //나의 그룹 조회
    public String emplinfo09(JSONObject inputJson){

        String apiUrl = EmplApi.mEmplInfoUrl + "/gw/ApiGate?";

        JSONObject mApiTrnHead = new JSONObject();
        JSONObject mApiTrnReqData = new JSONObject();

        String strApiTrnOutput = "";

        try{

            String strChnlId = StringUtil.nvl((String)inputJson.get("CHNL_ID"));
            String strUseInttId = StringUtil.nvl((String)inputJson.get("USE_INTT_ID"));
            String strUserId = StringUtil.nvl((String) inputJson.get("USER_ID"));

            mApiTrnHead.put("SVC_CD" , EmplApi.mEmplInfoMyGrpApiId );
            mApiTrnHead.put("SECR_KEY" , EmplApi.mEmplInfoApiKey );
            mApiTrnHead.put("PTL_STS" , "C" );
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);


            mApiTrnReqData.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnReqData.put("CHNL_ID", strChnlId );
            mApiTrnReqData.put("USE_INTT_ID", strUseInttId);

            mApiTrnReqData.put("USER_ID", strUserId);
            mApiTrnReqData.put("INQY_DSNC", "J");
            //System.out.println("input data = " + mApiTrnHead);

            HttpUtil hutil = new HttpUtil();

            String strEnodeParam = "JSONData="+java.net.URLEncoder.encode(mApiTrnHead.toString()  ,"UTF-8");

            strApiTrnOutput = URLDecoder.decode(hutil.send(apiUrl, strEnodeParam, "POST", "utf-8", "utf-8", "5000", null), "UTF-8");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return strApiTrnOutput;
    }

    //부서 목록 조회
    public String emplinfo19(JSONObject inputJson){
        String apiUrl = EmplApi.mEmplInfoUrl + "/gw/ApiGate?";

        JSONObject mApiTrnHead = new JSONObject();
        JSONObject mApiTrnReqData = new JSONObject();


        String strApiTrnOutput = "";

        try{
            String strChnlId = StringUtil.nvl((String)inputJson.get("CHNL_ID"));
            String strUseInttId = StringUtil.nvl((String)inputJson.get("USE_INTT_ID"));
            String strPagePerCnt  = StringUtil.nvl((String) inputJson.get("PAGE_PER_REQ_CNT"));
            String strPageNo  = StringUtil.nvl((String) inputJson.get("PAGE_NO"));

            mApiTrnHead.put("SVC_CD" , EmplApi.mEmplInfoDvsnListApiId );
            mApiTrnHead.put("SECR_KEY" , EmplApi.mEmplInfoApiKey );
            mApiTrnHead.put("PTL_STS" , "C" );
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);

            mApiTrnReqData.put("ACVT_YN", "Y");
            mApiTrnReqData.put("GRP_CD", "");
            mApiTrnReqData.put("DVSN_CD", "");
            mApiTrnReqData.put("PAGE_PER_REQ_CNT" , strPagePerCnt );
            mApiTrnReqData.put("PAGE_NO" , strPageNo );


            mApiTrnHead.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnHead.put("CHNL_ID", strChnlId );
            mApiTrnHead.put("USE_INTT_ID", strUseInttId);


            //System.out.println("input data = " + mApiTrnHead);

            HttpUtil hutil = new HttpUtil();

            String strEnodeParam = "JSONData="+java.net.URLEncoder.encode(mApiTrnHead.toString()  ,"UTF-8");

            strApiTrnOutput = URLDecoder.decode(hutil.send(apiUrl, strEnodeParam, "POST", "utf-8", "utf-8", "5000", null), "UTF-8");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return strApiTrnOutput;
    }


    private class ProcessEmplInfoTask extends AsyncTask<JSONObject,JSONObject,JSONObject> {

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            EmplApi emplApi = new EmplApi();

            JSONObject objResult = null;
            try {
                objResult = new JSONObject(emplApi.emplinfo05(params[0]));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //System.out.println("strEmplResult = " + objResult );

            return objResult;
        }

        protected void onPostExecute(JSONObject result) {
        }
    }


}
