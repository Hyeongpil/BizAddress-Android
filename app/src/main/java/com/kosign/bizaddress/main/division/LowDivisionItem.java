package com.kosign.bizaddress.main.division;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.MainActivity;
import com.kosign.bizaddress.main.retrofit.DivisionEmplThread;
import com.kosign.bizaddress.model.LowDivision;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.GlobalApplication;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hyeongpil on 2016. 8. 22..
 */
public class LowDivisionItem extends AbstractAdapterItem {
    final static String TAG = "LowDivisionItem";
    private Context mContext;
    private TextView tv_lowDivision_name;
    private HashMap<String,String> division_map;
    private String division_code;
    private ArrayList<UserInfo> userdata;

    public LowDivisionItem(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.division_low_item;
    }

    @Override
    public void onBindViews(View root) {
        division_map = GlobalApplication.getInstance().getDivision_map();
        tv_lowDivision_name = (TextView) root.findViewById(R.id.division_low_name);
        tv_lowDivision_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 하위부서인 경우 앞의 "  ㄴ "을 제거
                if(division_map.get(tv_lowDivision_name.getText()) == null){
                    String name = (String)tv_lowDivision_name.getText().subSequence(5,tv_lowDivision_name.length());
                    division_code = division_map.get(name);
                }else{ // 보통의 경우
                    division_code = division_map.get(tv_lowDivision_name.getText());
                }

                Log.d(TAG,"name :"+tv_lowDivision_name.getText());
                Log.d(TAG,"code :"+division_code);

                GlobalApplication.getInstance().showDlgProgress();
                Handler divisionEmplHandler = new DivisionEmplReceiveHandler();
                Thread divisionEmplThread = new DivisionEmplThread(divisionEmplHandler, mContext, division_code);
                divisionEmplThread.start();
            }
        });
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        if (model instanceof LowDivision) {
            LowDivision lowDivision = (LowDivision) model;
            tv_lowDivision_name.setText(lowDivision.getLowDivision_name());
        }
    }
    /**
     * DivisionEmplThread 에서 데이터를 받아
     * MainActivity로 넘겨줌
     */
    private class DivisionEmplReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            userdata = (ArrayList<UserInfo>) msg.getData().getSerializable("DivisionEmplThread");
            ((MainActivity)mContext).getDivisionEmplData(userdata);
            GlobalApplication.getInstance().dismissDlgProgress();
        }
    }
}
