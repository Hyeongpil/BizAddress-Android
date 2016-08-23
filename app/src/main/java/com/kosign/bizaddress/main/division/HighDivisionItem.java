package com.kosign.bizaddress.main.division;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.retrofit.DivisionEmplThread;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.util.GlobalApplication;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import java.util.HashMap;

/**
 * Created by Hyeongpil on 2016. 8. 22..
 */
public class HighDivisionItem extends AbstractExpandableAdapterItem {
    final static String TAG = "HighDivisionItem";
    private Context mContext;
    private Handler mHandler;
    private TextView tv_highDivision_name;
    private ImageView iv_arrow;
    private ImageView iv_search;
    private String division_code;
    private HashMap<String,String> division_map;

    public HighDivisionItem(Context mContext ,Handler DivisionEmplReceiveHandler) {
        this.mContext = mContext;
        this.mHandler = DivisionEmplReceiveHandler;
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
                //부서별 직원 목록 출력
                GlobalApplication.getInstance().showDlgProgress();
                Thread divisionEmplThread = new DivisionEmplThread(mHandler, mContext, division_code);
                divisionEmplThread.start();
            }
        });
    }

    @Override
    public void onSetViews() {
        iv_arrow.setImageResource(R.drawable.division_arrow);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        onSetViews();
        onExpansionToggled(getExpandableListItem().isExpanded());
        final HighDivision mHighDivision = (HighDivision) model;
        tv_highDivision_name.setText(mHighDivision.getHighDivision_name());
//        if (mHighDivision.getChildItemList().isEmpty() && mHighDivision.getChildItemList().size() == 0) {
//            iv_arrow.setVisibility(View.INVISIBLE);
//        }
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

}
