package com.kosign.bizaddress.main.division;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.MainActivity;
import com.kosign.bizaddress.main.retrofit.DivisionEmplThread;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.GlobalApplication;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hyeongpil on 2016. 8. 22..
 */
public class HighDivisionItem extends AbstractExpandableAdapterItem {
    final static String TAG = "HighDivisionItem";
    private Context mContext;
    private TextView tv_highDivision_name;
    private ImageView iv_arrow;
    private ImageView iv_search;
    private HighDivision mHighDivision;
    private String division_code;
    private HashMap<String,String> division_map;
    private ArrayList<UserInfo> userdata;

    public HighDivisionItem(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.division_high_item;
    }

    @Override
    public void onBindViews(View root) {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
            }
        });
        division_map = GlobalApplication.getInstance().getDivision_map();
        tv_highDivision_name = (TextView) root.findViewById(R.id.division_high_name);
        iv_arrow = (ImageView) root.findViewById(R.id.division_high_arrow);
        iv_search = (ImageView) root.findViewById(R.id.division_high_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                division_code = division_map.get(tv_highDivision_name.getText());

                Log.d(TAG,"name :"+tv_highDivision_name.getText());
                Log.d(TAG,"code :"+division_code);

                GlobalApplication.getInstance().showDlgProgress();
                Handler divisionEmplHandler = new HighDivisionEmplReceiveHandler();
                Thread divisionEmplThread = new DivisionEmplThread(divisionEmplHandler, mContext, division_code);
                divisionEmplThread.start();
            }
        });
    }

    @Override
    public void onSetViews() {
        iv_arrow.setImageResource(0);
        iv_arrow.setImageResource(R.drawable.division_arrow);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        onSetViews();
        onExpansionToggled(getExpandableListItem().isExpanded());
        mHighDivision = (HighDivision) model;
        tv_highDivision_name.setText(mHighDivision.getHighDivision_name());

        List<?> childItemList = mHighDivision.getChildItemList();
        if (childItemList == null && childItemList.isEmpty())
            iv_arrow.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        float start, target;
        if (expanded) {
            start = 0f;
            target = 180f;
            tv_highDivision_name.setTextColor(GlobalApplication.getInstance().getResources().getColor(R.color.common_titlebar));
        } else {
            start = 180f;
            target = 0f;
            tv_highDivision_name.setTextColor(GlobalApplication.getInstance().getResources().getColor(R.color.defaultText));
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv_arrow, View.ROTATION, start, target);
        objectAnimator.setDuration(300);
        objectAnimator.start();
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
            ((MainActivity)mContext).getDivisionEmplData(userdata);
            GlobalApplication.getInstance().dismissDlgProgress();
        }
    }

}
