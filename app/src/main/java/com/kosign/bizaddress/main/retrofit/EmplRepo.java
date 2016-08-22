package com.kosign.bizaddress.main.retrofit;

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
    @SerializedName("RSLT_CD") String RSLT_CD;
    @SerializedName("RSLT_MSG") String RSLT_MSG;

    public class RESP_DATA {
        @SerializedName("REC") ArrayList<REC> REC;
        @SerializedName("TOTL_RSLT_CNT") String TOTL_RSLT_CNT;
        @SerializedName("TOTL_PAGE_NCNT") String TOTL_PAGE_NCNT;

        public class REC {
            @SerializedName("FLNM") String name; // 이름
            @SerializedName("CLPH_NO") String phoneNum; // 핸드폰 번호
            @SerializedName("BSNN_NM") String company; // 사업장명
            @SerializedName("DVSN_NM") String division; // 부서명
            @SerializedName("RSPT_NM") String position; // 직책
            @SerializedName("EML") String email; // 이메일
            @SerializedName("EXNM_NO") String innerPhoneNum; // 내선번호
            @SerializedName("PRFL_PHTG") String profileImg; // 프로필 사진
            @SerializedName("CLPH_NTNL_CD") String phone_countryCode; // 휴대폰 국가 코드
            @SerializedName("EXNM_NO_NTNL_CD") String innerPhone_contryCode; // 내선번호 국가 코드
            @SerializedName("DTPL_DTL_ADRS") String address; // 주소
            public String getName() {return name;}
            public String getPhoneNum() {return phoneNum;}
            public String getCompany() {return company;}
            public String getDivision() {return division;}
            public String getPosition() {return position;}
            public String getEmail() {return email;}
            public String getInnerPhoneNum() {return innerPhoneNum;}
            public String getProfileImg() {return profileImg;}
            public String getPhone_countryCode() {return phone_countryCode;}
            public String getInnerPhone_contryCode() {return innerPhone_contryCode;}
            public String getAddress() {return address;}
        }
        public ArrayList<EmplRepo.RESP_DATA.REC> getREC() {return REC;}
        public String getTOTL_RSLT_CNT() {return TOTL_RSLT_CNT;}
        public String getTOTL_PAGE_NCNT() {return TOTL_PAGE_NCNT;}
    }

    public ArrayList<RESP_DATA> getResp_data() {return RESP_DATA;}
    public String getRSLT_CD() {return RSLT_CD;}
    public String getRSLT_MSG() {return RSLT_MSG;}

    public interface EmplApiInterface {
        @GET("gw/ApiGate")
        Call<EmplRepo> get_empl_retrofit(@Query("JSONData") JSONObject data);
    }
}
