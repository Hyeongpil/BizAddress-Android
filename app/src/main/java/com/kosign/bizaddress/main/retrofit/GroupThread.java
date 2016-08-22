package com.kosign.bizaddress.main.retrofit;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Hyeongpil on 2016. 8. 9..
 * 내 그룹 목록을 가져 오는 스레드
 *
 * 현재 implinfo09는 직원 2.0에서 쓰이지 않는 EMPL_BKMK를 포함하고 있어 Database Transaction 가 발생한다.
 */

public class GroupThread extends Thread {
    final static String TAG = "GroupThread";
    private Context mContext;
    private GroupRepo groupRepo;
    private Handler handler;

    private EmplPreference pref = GlobalApplication.getInstance().getPref();

    //기본
    public GroupThread(Handler handler, Context mContext) {
        this.mContext = mContext;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        JSONObject mApiTrnHead = new JSONObject();
        JSONObject mApiTrnReqData = new JSONObject();

        try {
            String strChnlId = StringUtil.nvl((String) "CHNL_1"); // 채널 id
            String strUseInttId = StringUtil.nvl((String) pref.getString("USE_INTT_ID")); // 이용기관 id
            String strUserId = StringUtil.nvl((String) pref.getString("USER_ID"));

            // 필수
            mApiTrnHead.put("SVC_CD", EmplApi.mEmplInfoMyGrpApiId); // emplinfo09
            mApiTrnHead.put("SECR_KEY", EmplApi.mEmplInfoApiKey);
            mApiTrnHead.put("PTL_STS", "C");
            mApiTrnHead.put("REQ_DATA", mApiTrnReqData);

            mApiTrnReqData.put("PTL_ID", BizplayApi.mPtlId);
            mApiTrnReqData.put("CHNL_ID", strChnlId);
            mApiTrnReqData.put("USE_INTT_ID", strUseInttId);
            mApiTrnReqData.put("USER_ID",strUserId);
            mApiTrnReqData.put("INQY_DSNC","J");

        } catch (JSONException e) {
            Log.e(TAG, "JsonError :" + e.getMessage());
        }

        Retrofit client = new Retrofit.Builder().baseUrl(EmplApi.mEmplInfoUrl).addConverterFactory(GsonConverterFactory.create()).build();
        GroupRepo.GroupApiInterface service = client.create(GroupRepo.GroupApiInterface.class);
        Call<GroupRepo> call = service.get_group_retrofit(mApiTrnHead);
        call.enqueue(new Callback<GroupRepo>() {
            @Override
            public void onResponse(Call<GroupRepo> call, Response<GroupRepo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"response raw :"+response.raw());
                    groupRepo = response.body();
                    if(groupRepo.getRSLT_CD().equals("0000")){ //정상 응답
                        Log.e(TAG,"repo :"+groupRepo.getRESP_DATA());
                    }else{ // 예외 처리
                        Toast.makeText(mContext, "그룹 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,"request :"+call.request());
                        Log.e(TAG,""+groupRepo.getRSLT_MSG());
                        Log.e(TAG,""+groupRepo.getRSLT_CD());
                    }
                }
            }
            @Override
            public void onFailure(Call<GroupRepo> call, Throwable t) {
                Toast.makeText(mContext, "그룹 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"request :"+call.request());
            }
        });

    }
}
