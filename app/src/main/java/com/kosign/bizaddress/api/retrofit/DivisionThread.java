package com.kosign.bizaddress.api.retrofit;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.kosign.bizaddress.api.BizplayApi;
import com.kosign.bizaddress.api.EmplApi;
import com.kosign.bizaddress.util.EmplPreference;
import com.kosign.bizaddress.util.GlobalApplication;
import com.kosign.bizaddress.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */

public class DivisionThread extends Thread {
    final static String TAG = "Division";
    private Context mContext;
    private DivisionRepo divisionRepo;
    private Handler handler;

    private EmplPreference pref = GlobalApplication.getInstance().getPref();
    private JSONObject mApiTrnHead = new JSONObject();
    private JSONObject mApiTrnReqData = new JSONObject();

    private int nPageNo = 1; // 페이지 번호
    private int nPagePerCnt = 20; // 페이지당 갯수
    private int nTOTL_PAGE_NCNT = 0; // 총 조회 페이지


    //기본
    public DivisionThread(Handler handler, Context mContext) {
        this.mContext = mContext;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        try {
            String strChnlId = StringUtil.nvl((String) "CHNL_1"); // 채널 id
            String strUseInttId = StringUtil.nvl((String) pref.getString("USE_INTT_ID")); // 이용기관 id
            String strPagePerCnt = StringUtil.nvl((String) String.valueOf(nPagePerCnt)); // 한 페이지에 출력되는 결과 수
            String strPageNo = StringUtil.nvl((String) String.valueOf(nPageNo)); // 페이지 번호 기본 1
            String strDvsnNm = StringUtil.nvl((String) pref.getString(""));

            // 필수
            mApiTrnHead.put("SVC_CD", EmplApi.mEmplInfoDvsnListApiId);
            mApiTrnHead.put("SECR_KEY", EmplApi.mEmplInfoApiKey);
            mApiTrnHead.put("PTL_STS", "C");
            mApiTrnHead.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnHead.put("CHNL_ID", strChnlId);
            mApiTrnHead.put("USE_INTT_ID", strUseInttId);
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);

            mApiTrnReqData.put("ACVT_YN", "Y");
            mApiTrnReqData.put("DVSN_NM",strDvsnNm);
            mApiTrnReqData.put("PAGE_PER_CNT", strPagePerCnt);
            mApiTrnReqData.put("PAGE_NO", strPageNo);

        } catch (JSONException e) {
            Log.e(TAG, "JsonError :" + e.getMessage());
        }

        Retrofit client = new Retrofit.Builder().baseUrl(EmplApi.mEmplInfoUrl).addConverterFactory(GsonConverterFactory.create()).build();
        DivisionRepo.DivisionApiInterface service = client.create(DivisionRepo.DivisionApiInterface.class);
        Call<DivisionRepo> call = service.get_division_retrofit(mApiTrnHead);
        call.enqueue(new Callback<DivisionRepo>() {
            @Override
            public void onResponse(Call<DivisionRepo> call, Response<DivisionRepo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"response raw :"+response.raw());
                    divisionRepo = response.body();
                    int count = Integer.parseInt(divisionRepo.getResp_data().get(0).getINQ_TOTL_NCNT());
                    for(int i = 0; i < count; i++){
                        Log.d(TAG,"division :"+divisionRepo.getResp_data().get(0).getREC().get(i).getDivision());

                    }

                }
            }

            @Override
            public void onFailure(Call<DivisionRepo> call, Throwable t) {
                Log.e(TAG,"실패 :"+t.getMessage());
            }
        });
    }
}
