package com.kosign.bizaddress.login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.api.BizplayApi;
import com.kosign.bizaddress.main.MainActivity;
import com.kosign.bizaddress.util.EmplPreference;
import com.kosign.bizaddress.util.GlobalApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jung
 */

public class LoginActivity extends AppCompatActivity{
    private EditText et_id;
    private EditText et_pwd;
    private Button btn_login;
    private CheckBox chkBox_auto;
    private ProgressDialog dlgProgress = null;
    private EmplPreference pref;
    private String loginYN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if ("Y".equals(loginYN)) { // 자동 로그인 체크되어 있을 경우
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void init(){
        et_id = (EditText)findViewById(R.id.login_id);
        et_pwd = (EditText)findViewById(R.id.login_pwd);
        chkBox_auto = (CheckBox)findViewById(R.id.login_auto);
        btn_login = (Button)findViewById(R.id.login_loginbtn);
        btn_login.setOnClickListener(mClickListener);
        pref= GlobalApplication.getInstance().getPref();
        loginYN = pref.getString("LOGIN_YN", "N");
        et_id.setText(pref.getString("USER_ID"));
        et_pwd.setText("");
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            String strUserId = et_id.getText().toString().trim();

            if (strUserId.length() == 0) {
                Toast.makeText(getApplicationContext(), "사용자 ID 를 입력하여 주세요.", Toast.LENGTH_LONG).show();
                return;
            }

            String strPassword = et_pwd.getText().toString().trim();

            if (strPassword.length() == 0) {
                Toast.makeText(getApplicationContext(), "비밀번호를 입력하여 주세요.", Toast.LENGTH_LONG).show();
                return;
            }

            if(dlgProgress==null)
                dlgProgress = ProgressDialog.show(LoginActivity.this, null, "잠시만 기다려 주세요.");

            new ProcessBizplayLoginTask().execute(strUserId, strPassword, null);
        }
    };

    private class ProcessBizplayLoginTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... contents) {

            String strUserId = contents[0].toString();
            String strPassword = contents[1].toString();

            HashMap<String,String> inputMap = new HashMap<String,String>();

            inputMap.put("USER_ID",strUserId);
            inputMap.put("PASS_WD",strPassword);

            String strApiTrnOutput = "";

            try {
                BizplayApi bizplay = new BizplayApi();
                strApiTrnOutput = bizplay.login(inputMap);
                //System.out.println("결과값 = " + strApiTrnOutput);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return strApiTrnOutput;
        }

        protected void onPostExecute(String result) {

            JSONObject resultObj = null;

            JSONObject resultRespData = null;

            String RSLT_CD = "";
            String RSLT_MSG = "";
            String strChnlId = "";
            String strUseInttId = "";
            String strUserId = "";
            String strUserNm = "";
            String strImgPath = "";
            String strBsnnNm = "";  //회사명
            String strEml = "";

            try {
                resultObj = new JSONObject(result);

                RSLT_CD = (String)resultObj.get("RSLT_CD");
                RSLT_MSG = (String)resultObj.get("RSLT_MSG");

                resultRespData = (JSONObject)((JSONArray) resultObj.get("RESP_DATA")).get(0);

                //오류인 경우
                if(!"0000".equals(RSLT_CD) && RSLT_CD.length()>0){
                    Toast.makeText(getApplicationContext(),"["+RSLT_CD+"]"+ RSLT_MSG, Toast.LENGTH_LONG).show();
                }
                //정상인 경우
                else{

                    if(chkBox_auto.isChecked()){
                        pref.putString("LOGIN_YN", "Y");
                    }
                    else{
                        pref.putString("LOGIN_YN", "N");
                    }

                    strUserId = (String)resultRespData.get("USER_ID");
                    strUseInttId = (String)resultRespData.get("USE_INTT_ID");
                    strChnlId = "CHNL_1";
                    strUserNm = (String)resultRespData.get("USER_NM");
                    try {
                        strImgPath = (String) resultRespData.get("IMG_PATH");
                    }catch (Exception e){}
                    strBsnnNm =  (String)resultRespData.get("BSNN_NM");
                    strEml = (String)resultRespData.get("EML");

                    pref.putString("USER_ID", strUserId);
                    pref.putString("USE_INTT_ID", strUseInttId);
                    pref.putString("CHNL_ID", strChnlId);
                    pref.putString("USER_NM", strUserNm);
                    pref.putString("IMG_PATH", strImgPath);
                    pref.putString("BSNN_NM", strBsnnNm);
                    pref.putString("EML", strEml);

                    //Toast.makeText(getApplicationContext(), RSLT_MSG, Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(myIntent, 200);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
            }
            finally{
                if(dlgProgress!=null) {
                    dlgProgress.dismiss();
                    dlgProgress = null;
                }
            }
        }
    }

}
