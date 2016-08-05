package com.kosign.bizaddress.main.division;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.Division;

/**
 * Created by Hyeongpil on 2016. 8. 5..
 */
public class DivisionViewHolder extends ChildViewHolder {

    private TextView division_name;

    public DivisionViewHolder(View itemView) {
        super(itemView);
        division_name = (TextView) itemView.findViewById(R.id.division_middle_name);
    }

    public void bind(Division division) {
        division_name.setText(division.getDivision_name());
    }

    public TextView getDivision_name() {
        return division_name;
    }
}