package com.kosign.bizaddress.main.division;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
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
public class HighDivisionViewHolder extends ParentViewHolder {
    final static String TAG = "HighDivisionViewHolder";
    private Context mContext;
    private final float INITIAL_POSITION = 0.0f;
    private final float ROTATED_POSITION = 180f;
    private ImageView mArrowExpandImageView;
    private TextView highDivision_name;
    private ImageView highDivision_search;
    private String division_code;
    private HashMap<String,String> division_map;
    private ProgressDialog dlgProgress;
    private ArrayList<UserInfo> userdata;

    public HighDivisionViewHolder(View itemView, final Context mContext) {
        super(itemView);
        this.mContext = mContext;
        division_map = GlobalApplication.getInstance().getDivision_map();
        highDivision_name = (TextView) itemView.findViewById(R.id.division_high_name);
        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.division_high_arrow);
        highDivision_search = (ImageView) itemView.findViewById(R.id.division_high_search);
        highDivision_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                division_code = division_map.get(highDivision_name.getText());

                Log.d(TAG,"name :"+highDivision_name.getText());
                Log.d(TAG,"code :"+division_code);

                dlgProgress = ProgressDialog.show(mContext, null, "잠시만 기다려 주세요.");
                Handler divisionEmplHandler = new HighDivisionEmplReceiveHandler();
                Thread divisionEmplThread = new DivisionEmplThread(divisionEmplHandler, mContext, division_code);
                divisionEmplThread.start();
            }
        });
    }

    //화살표 회전 애니메이션
    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                highDivision_name.setTextColor(GlobalApplication.getInstance().getResources().getColor(R.color.common_titlebar));
                mArrowExpandImageView.setRotation(ROTATED_POSITION);
            } else {
                highDivision_name.setTextColor(GlobalApplication.getInstance().getResources().getColor(R.color.grey));
                mArrowExpandImageView.setRotation(INITIAL_POSITION);
            }
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise

                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            mArrowExpandImageView.startAnimation(rotateAnimation);
        }
    }

    public TextView getHighDivision_name() {
        return highDivision_name;
    }

    /**
     * DivisionEmplThread 에서 데이터를 받아
     * MainActivity로 넘겨줌
     */
    public class HighDivisionEmplReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            userdata = (ArrayList<UserInfo>) msg.getData().getSerializable("DivisionEmplThread");
            ((MainActivity)mContext).getEmplData(userdata);
            dlgProgress.dismiss();
        }
    }
}
