package com.kosign.bizaddress.main.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kosign.bizaddress.api.BizplayApi;
import com.kosign.bizaddress.api.EmplApi;
import com.kosign.bizaddress.model.Division;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.util.EmplPreference;
import com.kosign.bizaddress.util.GlobalApplication;
import com.kosign.bizaddress.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    private String company;
    private ArrayList<HighDivision> highDivision;
    private HashMap<String, String> division_map;

    //기본
    public DivisionThread(Handler handler, Context mContext) {
        this.mContext = mContext;
        this.handler = handler;
        division_map = GlobalApplication.getInstance().getDivision_map(); // 부서 맵 초기화
    }

    @Override
    public void run() {
        super.run();

        try {
            String strChnlId = StringUtil.nvl((String) "CHNL_1"); // 채널 id
            String strUseInttId = StringUtil.nvl((String) pref.getString("USE_INTT_ID")); // 이용기관 id

            // 필수
            mApiTrnHead.put("SVC_CD", EmplApi.mEmplInfoDvsnListApiId);
            mApiTrnHead.put("SECR_KEY", EmplApi.mEmplInfoApiKey);
            mApiTrnHead.put("PTL_STS", "C");
            mApiTrnHead.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnHead.put("CHNL_ID", strChnlId);
            mApiTrnHead.put("USE_INTT_ID", strUseInttId);
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);

            mApiTrnReqData.put("ACVT_YN", "Y");
            mApiTrnReqData.put("LRRN_DVSN_INLS_YN","Y");

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
                    highDivision = new ArrayList<>();
                    int highDivision_count = -1; // 데이터 값이 순서대로 들어오므로 상위 부서의 카운트를 추가한다
                    int division_count = -1;
                    for(int i = 0; i < divisionRepo.getRESP_DATA().get(0).getREC().size() ; i++){
                        ArrayList<DivisionRepo.RESP_DATA.REC> rec = divisionRepo.getRESP_DATA().get(0).getREC();
                        //회사 이름
                        if(rec.get(i).getHigh_division() == null){
                            company = rec.get(i).getDivision();
                            continue;
                        }
                        //상위 부서
                        else if(rec.get(i).getHigh_division().equals(company)){
                            highDivision.add(new HighDivision(rec.get(i).getDivision()));
                            highDivision_count++; // 카운트는 0부터 시작해서 상위 부서가 생길때마다 1씩 증가
                            division_count = -1; // 상위 부서가 새로 만들어졌으므로 중간 부서 카운트 초기화
                        }//중간 부서
                        else if(highDivision.get(highDivision_count).getHighDivision_name().equals(rec.get(i).getHigh_division())){
                            highDivision.get(highDivision_count).getDivision().add(new Division(rec.get(i).getDivision(),rec.get(i).getDivision_cd()));
                            division_map.put(rec.get(i).getDivision(),rec.get(i).getDivision_cd()); // 부서 해시맵에 코드 추가
                            division_count++;
                        }else{//하위 부서도 중간부서에 포함
                            for(int j = 0; j < highDivision.get(highDivision_count).getDivision().size(); j++){
                                if(highDivision.get(highDivision_count).getDivision().get(j).getDivision_name().equals(rec.get(i).getHigh_division())){
                                    String temp = "   ㄴ "+rec.get(i).getDivision(); // 하위 부서는 앞에 공백을 추가
                                    highDivision.get(highDivision_count).getDivision().add(new Division(temp,rec.get(i).getDivision_cd()));
                                    division_map.put(rec.get(i).getDivision(),rec.get(i).getDivision_cd());
                                    division_count++;
                                    break;
                                }
                            }
                        }
                    }
                    //메인의 DivisionDataReceiveHandler 로 보내줌
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("highDivision", highDivision);
                    bundle.putString("company", company);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
            @Override
            public void onFailure(Call<DivisionRepo> call, Throwable t) {
                Log.e(TAG,"실패 :"+t.getMessage());
            }
        });
    }
}
