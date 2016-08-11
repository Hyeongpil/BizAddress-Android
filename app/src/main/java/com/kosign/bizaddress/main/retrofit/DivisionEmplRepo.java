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
public class DivisionEmplRepo implements Serializable{
    @SerializedName("RESP_DATA") ArrayList<RESP_DATA> RESP_DATA;
    @SerializedName("RSLT_CD") String RSLT_CD;
    @SerializedName("RSLT_MSG") String RSLT_MSG;

    public class RESP_DATA {
        @SerializedName("REC") ArrayList<REC> REC;

        public class REC {
            @SerializedName("USER_NM") String name; // 이름
            @SerializedName("CLPH_NO") String phoneNum; // 핸드폰 번호
            @SerializedName("DVSN_NM") String division; // 부서명
            @SerializedName("RSPT_NM") String position; // 직책
            @SerializedName("EML") String email; // 이메일
            @SerializedName("PRFL_PHTG") String profileImg; // 프로필 사진

            public String getName() {return name;}
            public String getPhoneNum() {return phoneNum;}
            public String getDivision() {return division;}
            public String getPosition() {return position;}
            public String getEmail() {return email;}
            public String getProfileImg() {return profileImg;}
        }
        public ArrayList<DivisionEmplRepo.RESP_DATA.REC> getREC() {return REC;}

    }

    public ArrayList<DivisionEmplRepo.RESP_DATA> getRESP_DATA() {return RESP_DATA;}
    public String getRSLT_CD() {return RSLT_CD;}
    public String getRSLT_MSG() {return RSLT_MSG;}

    public interface DivisionEmplApiInterface {
        @GET("gw/ApiGate")
        Call<DivisionEmplRepo> get_divisionEmpl_retrofit(@Query("JSONData") JSONObject data);
    }
}
