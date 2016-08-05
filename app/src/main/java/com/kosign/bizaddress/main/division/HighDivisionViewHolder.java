package com.kosign.bizaddress.main.division;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.util.GlobalApplication;

/**
 * Created by Hyeongpil on 2016. 8. 5..
 */
public class HighDivisionViewHolder extends ParentViewHolder {

    private final float INITIAL_POSITION = 0.0f;
    private final float ROTATED_POSITION = 180f;
    private final ImageView mArrowExpandImageView;
    private TextView highDivision_name;

    public HighDivisionViewHolder(View itemView) {
        super(itemView);
        highDivision_name = (TextView) itemView.findViewById(R.id.division_high_name);
        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.division_high_arrow);
    }

    public void bind(HighDivision highDivision) {
        highDivision_name.setText(highDivision.getHighDivision_name());
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
}
