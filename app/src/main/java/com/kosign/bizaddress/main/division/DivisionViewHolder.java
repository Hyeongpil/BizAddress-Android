package com.kosign.bizaddress.main.division;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.MainActivity;
import com.kosign.bizaddress.main.retrofit.DivisionEmplThread;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.GlobalApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hyeongpil on 2016. 8. 5..
 */
public class DivisionViewHolder extends ChildViewHolder {
    final static String TAG = "DivisionViewHolder";
    private Context mContext;
    private TextView division_name;
    private HashMap<String,String> division_map;
    private String division_code;
    private ArrayList<UserInfo> userdata;
    private ProgressDialog dlgProgress;

    public DivisionViewHolder(View itemView, final Context mContext) {
        super(itemView);
        this.mContext = mContext;
        division_name = (TextView) itemView.findViewById(R.id.division_name);
        division_map = GlobalApplication.getInstance().getDivision_map();
        division_name.setOnClickListener(new View.OnClickListener() {
            //부서 클릭 시 부서 코드를 얻어옴
            @Override
            public void onClick(View view) {
                // 하위부서인 경우 앞의 "  ㄴ "을 제거
                if(division_map.get(division_name.getText()) == null){
                    String name = (String)division_name.getText().subSequence(5,division_name.length());
                    division_code = division_map.get(name);
                }else{ // 보통의 경우
                    division_code = division_map.get(division_name.getText());
                }
                Log.d(TAG,"name :"+division_name.getText());
                Log.d(TAG,"code :"+division_code);

                dlgProgress = ProgressDialog.show(mContext, null, "잠시만 기다려 주세요.");
                Handler divisionEmplHandler = new DivisionEmplReceiveHandler();
                Thread divisionEmplThread = new DivisionEmplThread(divisionEmplHandler, mContext, division_code);
                divisionEmplThread.start();
            }
        });
    }

    public TextView getDivision_name() {
        return division_name;
    }

    /**
     * DivisionEmplThread 에서 데이터를 받아
     * MainActivity로 넘겨줌
     */
    public class DivisionEmplReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            userdata = (ArrayList<UserInfo>) msg.getData().getSerializable("DivisionEmplThread");
            ((MainActivity)mContext).getEmplData(userdata);
            dlgProgress.dismiss();
        }
    }
}