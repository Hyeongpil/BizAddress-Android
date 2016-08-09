package com.kosign.bizaddress.main.retrofit;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hyeongpil on 2016. 8. 9..
 */
public class GroupRepo implements Serializable{
    @SerializedName("RESP_DATA") ArrayList<RESP_DATA> RESP_DATA;
    @SerializedName("RSLT_CD") String RSLT_CD;
    @SerializedName("RSLT_MSG") String RSLT_MSG;

    public class RESP_DATA {
        @SerializedName("REC") ArrayList<REC> REC;

        public class REC {
            @SerializedName("GRP_CD") String group_code; // 그룹 코드
            @SerializedName("GRP_NM") String group_name; // 그룹 이름

            public String getGroup_code() {return group_code;}
            public String getGroup_name() {return group_name;}
        }
        public ArrayList<GroupRepo.RESP_DATA.REC> getREC() {return REC;}

    }

    public ArrayList<GroupRepo.RESP_DATA> getRESP_DATA() {return RESP_DATA;}
    public String getRSLT_CD() {return RSLT_CD;}
    public String getRSLT_MSG() {return RSLT_MSG;}

    public interface GroupApiInterface {
        @GET("gw/ApiGate")
        Call<GroupRepo> get_group_retrofit(@Query("JSONData") JSONObject data);
    }
}
