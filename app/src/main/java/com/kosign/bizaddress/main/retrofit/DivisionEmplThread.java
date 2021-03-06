package com.kosign.bizaddress.main.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.kosign.bizaddress.api.BizplayApi;
import com.kosign.bizaddress.api.EmplApi;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.EmplPreference;
import com.kosign.bizaddress.util.GlobalApplication;
import com.kosign.bizaddress.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Hyeongpil on 2016. 8. 8..
 * 부서별 직원 조회를 하는 스레드
 */

public class DivisionEmplThread extends Thread {
    final static String TAG = "DivisionEmplThread";
    private Context mContext;
    DivisionEmplRepo divisionEmplRepo;
    private Handler handler;

    private EmplPreference pref = GlobalApplication.getInstance().getPref();
    private JSONObject mApiTrnHead = new JSONObject();
    private JSONObject mApiTrnReqData = new JSONObject();

    private int nPageNo = 1; // 페이지 번호
    private int nPagePerCnt = 100; // 페이지당 갯수
    private String dvsn_cd = "";

    private ArrayList<UserInfo> userdata;


    //기본
    public DivisionEmplThread(Handler handler, Context mContext,String dvsn_cd) {
        this.mContext = mContext;
        this.handler = handler;
        this.dvsn_cd = dvsn_cd;
    }

    @Override
    public void run() {
        super.run();
        userdata = new ArrayList<>();

        try {
            String strChnlId = StringUtil.nvl((String) "CHNL_1"); // 채널 id
            String strUseInttId = StringUtil.nvl((String) pref.getString("USE_INTT_ID")); // 이용기관 id
            String strPagePerCnt = StringUtil.nvl((String) String.valueOf(nPagePerCnt)); // 한 페이지에 출력되는 결과 수
            String strPageNo = StringUtil.nvl((String) String.valueOf(nPageNo)); // 페이지 번호 기본 1
            String strDvsnCd = StringUtil.nvl((String) dvsn_cd); // 부서 코드

            // 필수
            mApiTrnHead.put("SVC_CD", EmplApi.mDvsnEmplListApiId); // emplinfo25
            mApiTrnHead.put("SECR_KEY", EmplApi.mEmplInfoApiKey);
            mApiTrnHead.put("PTL_STS", "C");
            mApiTrnHead.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnHead.put("CHNL_ID", strChnlId);
            mApiTrnHead.put("USE_INTT_ID", strUseInttId);
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);

            mApiTrnReqData.put("DVSN_CD", strDvsnCd);
            mApiTrnReqData.put("LRRN_DVSN_INLS_YN", "Y"); // 하위 부서 포함 여부
            mApiTrnReqData.put("ACVT_YN", "Y");


        } catch (JSONException e) {
            Log.e(TAG, "JsonError :" + e.getMessage());
        }
        Retrofit client = new Retrofit.Builder().baseUrl(EmplApi.mEmplInfoUrl).addConverterFactory(GsonConverterFactory.create()).build();
        DivisionEmplRepo.DivisionEmplApiInterface service = client.create(DivisionEmplRepo.DivisionEmplApiInterface.class);
        Call<DivisionEmplRepo> call = service.get_divisionEmpl_retrofit(mApiTrnHead);
        call.enqueue(new Callback<DivisionEmplRepo>() {
            @Override
            public void onResponse(Call<DivisionEmplRepo> call, Response<DivisionEmplRepo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"response raw :"+response.raw());
                    divisionEmplRepo = response.body();
                    if(divisionEmplRepo.getRSLT_CD().equals("0000")){
                        ArrayList<DivisionEmplRepo.RESP_DATA.REC> rec = divisionEmplRepo.getRESP_DATA().get(0).getREC();
                        for(int i = 0; i < divisionEmplRepo.getRESP_DATA().get(0).getREC().size(); i++){
                            UserInfo temp= new UserInfo();
                            temp.setStrName(rec.get(i).getName());
                            temp.setStrDivision(rec.get(i).getDivision());
                            temp.setStrEmail(rec.get(i).getEmail());
                            temp.setStrPhone(rec.get(i).getPhoneNum());
                            temp.setStrPosition(rec.get(i).getPosition());
                            temp.setStrProfileImg(rec.get(i).getProfileImg());
                            userdata.add(temp);
                        }
                        //DivisionFragment 로 보내줌
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DivisionEmplThread", userdata);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }else{
                        GlobalApplication.getInstance().dismissDlgProgress();
                        Toast.makeText(mContext, "주소록 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,""+call.request());
                        Log.e(TAG,""+divisionEmplRepo.getRSLT_CD());
                        Log.e(TAG,""+divisionEmplRepo.getRSLT_MSG());
                    }
                }
            }

            @Override
            public void onFailure(Call<DivisionEmplRepo> call, Throwable t) {

                Log.e(TAG,"실패 :"+call.request());
                Toast.makeText(mContext, "주소록 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
