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
public class DivisionRepo implements Serializable{
    @SerializedName("RESP_DATA") ArrayList<RESP_DATA> RESP_DATA;

    public class RESP_DATA {
        @SerializedName("REC") ArrayList<REC> REC;
        @SerializedName("INQ_TOTL_NCNT") String INQ_TOTL_NCNT; // 총 조회 건수

        public class REC {
            @SerializedName("DVSN_NM") String division; // 부서 명
            @SerializedName("DVSN_CD") String division_cd; // 부서 코드
            @SerializedName("HGRN_DVSN_CD") String high_division_cd;
            @SerializedName("HGRN_DVSN_NM") String high_division;

            public String getDivision() {return division;}
            public String getDivision_cd() {return division_cd;}
            public String getHigh_division_cd() {return high_division_cd;}
            public String getHigh_division() {return high_division;}
        }
        public ArrayList<DivisionRepo.RESP_DATA.REC> getREC() {return REC;}

        public String getINQ_TOTL_NCNT() {return INQ_TOTL_NCNT;}
    }

    public ArrayList<RESP_DATA> getResp_data() {return RESP_DATA;}

    public interface DivisionApiInterface {
        @GET("gw/ApiGate")
        Call<DivisionRepo> get_division_retrofit(@Query("JSONData") JSONObject data);
    }
}
