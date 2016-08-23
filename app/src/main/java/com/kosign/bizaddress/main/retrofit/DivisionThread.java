package com.kosign.bizaddress.main.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.kosign.bizaddress.api.BizplayApi;
import com.kosign.bizaddress.api.EmplApi;
import com.kosign.bizaddress.model.Division;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.model.LowDivision;
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
 * 부서 목록을 가져와 상위, 하위 부서로 분류하는 스레드
 */

public class DivisionThread extends Thread {
    final static String TAG = "Division";
    private Context mContext;
    private DivisionRepo divisionRepo;
    private Handler handler;

    private EmplPreference pref = GlobalApplication.getInstance().getPref();
    private JSONObject mApiTrnHead = new JSONObject();
    private JSONObject mApiTrnReqData = new JSONObject();

    private ArrayList<HighDivision> divisionList;
    private HashMap<String, String> division_map;

    //기본
    public DivisionThread(Handler handler, Context mContext) {
        this.mContext = mContext;
        this.handler = handler;
        division_map = new HashMap<>(); // 부서 맵 초기화
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
                    if(divisionRepo.getRSLT_CD().equals("0000")){
                        divisionList = new ArrayList<>();
                        int highDivision_count = -1; // 데이터 값이 순서대로 들어오므로 상위 부서의 카운트를 추가한다
                        int division_count = -1;
                        int lowDivision_count = -1;
                        String nowHighDivision = ""; // 현재 상위 부서
                        try {
                            for (int i = 0; i < divisionRepo.getRESP_DATA().get(0).getREC().size(); i++) {
                                ArrayList<DivisionRepo.RESP_DATA.REC> rec = divisionRepo.getRESP_DATA().get(0).getREC();
                                String highDivision = rec.get(i).getHigh_division();
                                String nowDivision = rec.get(i).getDivision();
                                String divisionCode = rec.get(i).getDivision_cd();
                                //부서 데이터 보기
//                                Log.e(TAG,"high :"+rec.get(i).getHigh_division());
//                                Log.e(TAG,"divi :"+rec.get(i).getDivision());
//                                Log.e(TAG,"---------------");
                                //상위 부서
                                if (highDivision == null) {
                                    nowHighDivision = nowDivision;
                                    divisionList.add(new HighDivision(nowDivision));
                                    division_map.put(nowDivision, divisionCode); // 부서 해시맵에 부서코드 추가
                                    highDivision_count++; // 카운트는 0부터 시작해서 상위 부서가 생길때마다 1씩 증가
                                    division_count = -1; // 상위 부서가 새로 만들어졌으므로 중간 부서 카운트 초기화
                                }
                                //중간 부서
                                else if (highDivision.equals(nowHighDivision)) {
                                    divisionList.get(highDivision_count).getDivision().add(new Division(nowDivision));
                                    division_map.put(nowDivision, divisionCode); // 부서 해시맵에 부서코드 추가
                                    division_count++; // 카운트는 0부터 시작해서 중간 부서가 생길때마다 1씩 증가
                                    lowDivision_count = -1; // 중간 부서가 새로 만들어졌으므로 하위 부서 카운트 초기화
                                }//하위 부서
                                else if (divisionList.get(highDivision_count).getDivision().get(division_count).getDivision_name().equals(highDivision)) {
                                    divisionList.get(highDivision_count).getDivision().get(division_count).getLowDivision().add(new LowDivision(nowDivision));
                                    division_map.put(nowDivision, divisionCode); // 부서 해시맵에 부서코드 추가
                                    lowDivision_count++;
                                } else {//최하위 부서도 하위부서에 포함
                                    for (int j = 0; j < divisionList.get(highDivision_count).getDivision().size(); j++) {
                                        try {
                                            if (divisionList.get(highDivision_count).getDivision().get(j).getLowDivision().get(lowDivision_count).getLowDivision_name().equals(highDivision)) {
                                                String temp = "   ㄴ " + nowDivision; // 하위 부서는 앞에 공백을 추가
                                                divisionList.get(highDivision_count).getDivision().get(division_count).getLowDivision().add(new LowDivision(temp));
                                                division_map.put(nowDivision, divisionCode);
                                                lowDivision_count++;
                                                break;
                                            }
                                        }catch (IndexOutOfBoundsException e){ // 최하위의 하위부서일 경우 캐치로 잡아 넣는다.
                                            String temp = "   ㄴ " + nowDivision; // 하위 부서는 앞에 공백을 추가
                                            divisionList.get(highDivision_count).getDivision().get(division_count).getLowDivision().add(new LowDivision(temp));
                                            division_map.put(nowDivision, divisionCode);
                                            lowDivision_count++;
                                            break;
                                        }
                                    }
                                }
                            }
                            //정렬 데이터 보기
//                            for(int i = 0; i < divisionList.size(); i++){
//                                Log.e(TAG,""+divisionList.get(i).getHighDivision_name());
//                                for(int j = 0; j < divisionList.get(i).getDivision().size(); j++){
//                                    Log.e(TAG,"   "+divisionList.get(i).getDivision().get(j).getDivision_name());
//                                    for(int k = 0; k < divisionList.get(i).getDivision().get(j).getLowDivision().size(); k++){
//                                        Log.e(TAG,"    "+divisionList.get(i).getDivision().get(j).getLowDivision().get(k).getLowDivision_name());
//                                    }
//                                }
//                            }

                        }catch (Exception e){
                            Log.e(TAG,"부서 정렬 오류 :"+e.getMessage());
                            Toast.makeText(mContext, "부서 정렬을 실패했습니다.\n새로고침 해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        //메인의 DivisionDataReceiveHandler 로 보내줌
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DivisionThread", divisionList);
                        pref.putDivisionHashMap(division_map);
                        GlobalApplication.getInstance().setDivision_map(division_map);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }else{
                        Toast.makeText(mContext, "부서 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,""+call.request());
                        Log.e(TAG,""+divisionRepo.getRSLT_CD());
                        Log.e(TAG,""+divisionRepo.getRSLT_MSG());
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
