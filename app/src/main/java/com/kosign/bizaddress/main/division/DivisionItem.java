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
import com.kosign.bizaddress.model.Division;
import com.kosign.bizaddress.util.GlobalApplication;
import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Hyeongpil on 2016. 8. 22..
 */
public class DivisionItem extends AbstractExpandableAdapterItem implements View.OnClickListener {
    final static String TAG = "DivisionItem";
    private Context mContext;
    private Handler mHandler;
    private TextView tv_division_name;
    private ImageView iv_arrow;
    private ImageView iv_search;
    private HashMap<String,String> division_map;

    public DivisionItem(Context mContext, Handler DivisionEmplReceiveHandler) {
        this.mContext = mContext;
        this.mHandler = DivisionEmplReceiveHandler;
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        float start, target;
        if (expanded) {
            start = 0f;
            target = 180f;
            tv_division_name.setTextColor(GlobalApplication.getInstance().getResources().getColor(R.color.common_titlebar));
        } else {
            start = 180f;
            target = 0f;
            tv_division_name.setTextColor(GlobalApplication.getInstance().getResources().getColor(R.color.defaultText));
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv_arrow, View.ROTATION, start, target);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.division_item;
    }

    @Override
    public void onBindViews(View root) {
        division_map = GlobalApplication.getInstance().getDivision_map();
        tv_division_name = (TextView) root.findViewById(R.id.division_name);
        iv_arrow = (ImageView) root.findViewById(R.id.division_arrow);
        iv_search = (ImageView) root.findViewById(R.id.division_search);
        tv_division_name.setOnClickListener(this);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(division_map.get(tv_division_name.getText()));
            }
        });
    }

    @Override
    public void onSetViews() {
        iv_arrow.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        if(getExpandableListItem().getChildItemList().size() == 0){
            search(division_map.get(tv_division_name.getText()));
        }
        else if (getExpandableListItem() != null && getExpandableListItem().getChildItemList() != null) {
            if (getExpandableListItem().isExpanded()) {
                collapseView();
            } else {
                expandView();
            }
        }
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        onSetViews();
        Division division = (Division) model;
        tv_division_name.setText(division.getDivision_name());
        ExpandableListItem parentListItem = (ExpandableListItem) model;
        List<?> childItemList = parentListItem.getChildItemList();
        if (childItemList != null && !childItemList.isEmpty())
            iv_arrow.setVisibility(View.VISIBLE);
    }

    private void search(String division_code){
        Log.d(TAG,"name :"+tv_division_name.getText());
        Log.d(TAG,"code :"+division_code);

        GlobalApplication.getInstance().showDlgProgress();
        Thread divisionEmplThread = new DivisionEmplThread(mHandler, mContext, division_code);
        divisionEmplThread.start();
    }

}
