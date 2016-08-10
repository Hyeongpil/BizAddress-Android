package com.kosign.bizaddress.main.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.kosign.bizaddress.api.BizplayApi;
import com.kosign.bizaddress.api.EmplApi;
import com.kosign.bizaddress.main.MainActivity;
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
 * Created by Hyeongpil on 2016. 8. 4..
 * 직원 목록을 가져오는 스레드
 */

public class EmplThread extends Thread {
    final static String TAG = "EmplThread";
    private Context mContext;
    private EmplRepo emplRepo;
    private Handler handler;
    private int callCount = GlobalApplication.getInstance().getEmplThreadCount(); // 호출 순서를 알기 위해 글로벌 어플리케이션에서 가져옴

    private EmplPreference pref = GlobalApplication.getInstance().getPref();
    private JSONObject mApiTrnHead = new JSONObject();
    private JSONObject mApiTrnReqData = new JSONObject();

    private int nPageNo = 1; // 페이지 번호
    private int nPagePerCnt = 20; // 페이지당 갯수
    private int nTOTL_PAGE_NCNT = 0; // 총 조회 페이지
    private String search = "";
    private String grp_cd = "";
    private String dvsn_cd = "";

    private ArrayList<UserInfo> userdata = new ArrayList<>();

    //기본
    public EmplThread(Handler handler, Context mContext) {
        this.mContext = mContext;
        this.handler = handler;
    }

    //페이지
    public EmplThread(Handler handler, Context mContext, int page) {
        this.mContext = mContext;
        this.handler = handler;
        this.nPageNo = page;
    }

    //검색
    public EmplThread(Handler handler, Context mContext, String search) {
        this.mContext = mContext;
        this.handler = handler;
        this.search = search;
    }

    //그룹 검색
    public EmplThread(String grp_cd, Handler handler, Context mContext) {
        this.mContext = mContext;
        this.handler = handler;
        this.grp_cd = grp_cd;
    }

    @Override
    public void run() {
        super.run();
        GlobalApplication.getInstance().setEmplThreadCount(callCount+1);// 호출 될 때 마다 호출 카운트를 늘려준다

        try {
            // 필수
            String strChnlId = StringUtil.nvl((String) "CHNL_1"); // 채널 id
            String strUseInttId = StringUtil.nvl((String) pref.getString("USE_INTT_ID")); // 이용기관 id
            String strPagePerCnt = StringUtil.nvl((String) String.valueOf(nPagePerCnt)); // 한 페이지에 출력되는 결과 수
            String strPageNo = StringUtil.nvl((String) String.valueOf(nPageNo)); // 페이지 번호 기본 1
            //검색어
            String strSrchWord = StringUtil.nvl((String) search); // 검색어
            //그룹코드
            String strGrpCd = StringUtil.nvl((String) grp_cd); // 그룹 코드
            String strUserId = StringUtil.nvl((String) pref.getString("USER_ID"));
            String strDvsnCd = StringUtil.nvl((String) dvsn_cd); // 부서 코드

            // 필수
            mApiTrnHead.put("SVC_CD", EmplApi.mEmplInfoSchUserApiId);
            mApiTrnHead.put("SECR_KEY", EmplApi.mEmplInfoApiKey);
            mApiTrnHead.put("PTL_STS", "C");
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);

            mApiTrnReqData.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnReqData.put("CHNL_ID", strChnlId);
            mApiTrnReqData.put("USE_INTT_ID", strUseInttId);

            mApiTrnReqData.put("ACVT_YN", "Y");
            mApiTrnReqData.put("PAGE_NO", strPageNo);
            mApiTrnReqData.put("PAGE_PER_CNT", strPagePerCnt);

            mApiTrnReqData.put("SRCH_WORD", strSrchWord);
            mApiTrnReqData.put("GRP_CD", strGrpCd);
            mApiTrnReqData.put("DVSN_CD", strDvsnCd);
            mApiTrnReqData.put("USER_ID", strUserId);
        } catch (JSONException e) {
            Log.e(TAG, "JsonError :" + e.getMessage());
        }


        Retrofit client = new Retrofit.Builder().baseUrl(EmplApi.mEmplInfoUrl).addConverterFactory(GsonConverterFactory.create()).build();
        EmplRepo.EmplApiInterface service = client.create(EmplRepo.EmplApiInterface.class);
        Call<EmplRepo> call = service.get_empl_retrofit(mApiTrnHead);
        call.enqueue(new Callback<EmplRepo>() {
            @Override
            public void onResponse(Call<EmplRepo> call, Response<EmplRepo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"response raw :"+response.raw());
                    emplRepo = response.body();
                    if(emplRepo.getRSLT_CD().equals("0000")) { // 응답 정상 처리
                        int count = emplRepo.getResp_data().get(0).getREC().size();
                        ArrayList<EmplRepo.RESP_DATA.REC> rec = emplRepo.getResp_data().get(0).getREC();
                        for (int i = 0; i < count; i++) {
                            UserInfo temp = new UserInfo();
                            String[] company = rec.get(i).getCompany().split(" ");
                            temp.setStrName(rec.get(i).getName());
                            temp.setStrCompany(company[0]);
                            temp.setStrDivision(rec.get(i).getDivision());
                            temp.setStrEmail(rec.get(i).getEmail());
                            temp.setStrPhoneNum(rec.get(i).getPhoneNum());
                            temp.setStrPosition(rec.get(i).getPosition());
                            temp.setStrProfileImg(rec.get(i).getProfileImg());
                            temp.setStrInnerPhoneNum(rec.get(i).getInnerPhoneNum());
                            userdata.add(temp);
                        }
                        //메인의 EmplDataReceiveHandler 로 보내줌
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("EmplThread", userdata);
                        bundle.putInt("callCount",callCount);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }else{
                        Toast.makeText(mContext, "주소록 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
                        ((MainActivity)mContext).stopRefresh();
                        Log.e(TAG,""+emplRepo.getRSLT_MSG());
                    }
                }
            }

            @Override
            public void onFailure(Call<EmplRepo> call, Throwable t) {
                Log.e(TAG,"실패, 요청 메시지:"+call.request());
                ((MainActivity)mContext).stopRefresh();
                Toast.makeText(mContext, "주소록 불러오기를 실패했습니다. 새로고침 해주세요", Toast.LENGTH_SHORT).show();
                ((MainActivity)mContext).stopDlgProgress();
            }
        });
    }
}
