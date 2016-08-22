package com.kosign.bizaddress.model;

import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeongpil on 2016. 8. 5..
 */
public class Division implements ExpandableListItem {
    private boolean mExpand = false;
    String division_name;
    ArrayList<LowDivision> lowDivision;

    public Division(String division_name) {
        this.division_name = division_name;
        lowDivision = new ArrayList<>();
    }

    @Override
    public List<?> getChildItemList() {
        return lowDivision;
    }

    @Override
    public boolean isExpanded() {
        return mExpand;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        mExpand = isExpanded;
    }

    @Override
    public String toString() {
        return "Division{" +
                "mExpand=" + mExpand +
                ", name='" + division_name + '\'' +
                ", lowDivisions=" + lowDivision +
                '}';
    }

    public String getDivision_name() {return division_name;}

    public ArrayList<LowDivision> getLowDivision() {return lowDivision;}
}