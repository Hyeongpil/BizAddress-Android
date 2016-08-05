package com.kosign.bizaddress.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeongpil on 2016. 8. 5..
 */
public class HighDivision implements Serializable, ParentListItem {

    String highDivision_name;
    ArrayList<Division> division;

    public HighDivision(String highDivision_name) {
        this.highDivision_name = highDivision_name;
        division = new ArrayList<>();
    }

    @Override
    public List<?> getChildItemList() {
        return division;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getHighDivision_name() {return highDivision_name;}



    public ArrayList<Division> getDivision() {return division;}

    public void setDivision(ArrayList<Division> division) {this.division = division;}
}
