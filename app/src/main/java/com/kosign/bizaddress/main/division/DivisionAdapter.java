package com.kosign.bizaddress.main.division;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.Division;
import com.kosign.bizaddress.model.HighDivision;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 5..
 */
public class DivisionAdapter extends ExpandableRecyclerAdapter<HighDivisionViewHolder, DivisionViewHolder> {

    private LayoutInflater mInflator;

    public DivisionAdapter(Context context, @NonNull ArrayList<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public HighDivisionViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View highDivisionView = mInflator.inflate(R.layout.division_high_item, parentViewGroup, false);
        return new HighDivisionViewHolder(highDivisionView);
    }

    @Override
    public DivisionViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.division_item, childViewGroup, false);
        return new DivisionViewHolder(ingredientView);
    }

    @Override
    public void onBindParentViewHolder(HighDivisionViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        HighDivision highDivision =  (HighDivision) parentListItem;
        parentViewHolder.getHighDivision_name().setText(highDivision.getHighDivision_name());
    }

    @Override
    public void onBindChildViewHolder(DivisionViewHolder childViewHolder, int position, Object childListItem) {
        Division division = (Division) childListItem;
        childViewHolder.getDivision_name().setText(division.getDivision_name());
    }

}
