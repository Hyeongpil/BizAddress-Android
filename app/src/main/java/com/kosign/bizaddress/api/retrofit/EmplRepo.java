package com.kosign.bizaddress.api.retrofit;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */
public class EmplRepo implements Serializable{
    @SerializedName("RESP_DATA") ArrayList<RESP_DATA> RESP_DATA;

    public class RESP_DATA {
        @SerializedName("REC") ArrayList<REC> REC;
        @SerializedName("TOTL_RSLT_CNT") String TOTL_RSLT_CNT;
        @SerializedName("TOTL_PAGE_NCNT") String TOTL_PAGE_NCNT;

        public class REC {
            @SerializedName("FLNM") String name; // 이름
            @SerializedName("SLPH_NO") String phoneNum; // 핸드폰 번호
            @SerializedName("BSNN_NM") String company; // 사업장명
            @SerializedName("DVSN_NM") String division; // 부서명
            @SerializedName("RSPT_NM") String position; // 직책
            @SerializedName("EML") String email; // 이메일
            @SerializedName("EXNM_NO") String innerPhoneNum; // 내선번호
            @SerializedName("PRFL_PHTG") String profileImg; // 프로필 사진

            public String getName() {return name;}
            public String getPhoneNum() {return phoneNum;}
            public String getCompany() {return company;}
            public String getDivision() {return division;}
            public String getPosition() {return position;}
            public String getEmail() {return email;}
            public String getInnerPhoneNum() {return innerPhoneNum;}
            public String getProfileImg() {return profileImg;}
        }
        public ArrayList<EmplRepo.RESP_DATA.REC> getREC() {return REC;}
        public String getTOTL_RSLT_CNT() {return TOTL_RSLT_CNT;}
        public String getTOTL_PAGE_NCNT() {return TOTL_PAGE_NCNT;}
    }

    public ArrayList<RESP_DATA> getResp_data() {return RESP_DATA;}

    public interface EmplApiInterface {
        @GET("gw/ApiGate")
        Call<EmplRepo> get_empl_retrofit(@Query("JSONData") JSONObject data);
    }
}