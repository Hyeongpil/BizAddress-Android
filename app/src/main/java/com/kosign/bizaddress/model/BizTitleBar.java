package com.kosign.bizaddress.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kosign.bizaddress.R;


/**
 * 
 * @title		: Common Title Bar class
 * @author		: bunna
 * @date		: 2016. 2. 06
 * @description	:
 */
public class BizTitleBar extends RelativeLayout {
	
	private Context mContext;
	
	private TextView mTvTitle;

	private String mType = "10";
	
	public BizTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		View titleBarLayout = View.inflate(mContext, R.layout.common_title_bar, null);
		addView(titleBarLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		//+ type
		String strType = attrs.getAttributeValue(null, "Type");
		setTitleBarType(strType);

		//+ title
		String strTitle = attrs.getAttributeValue(null, "title");
		setTitle(strTitle);
	}
	
	/**
	 * set type to titlebar
	 * @param strType
	 */
	public void setTitleBarType(String strType) {
		
		if(strType == null) {
			return;
		}

		mType = strType;

		/*
			1. img left            + title middle
			2.                       title middle
			3. img  +  title left  +                +   img right
			4. img  +  title left  +                +  text right
			5. img  +  title left
			6. title left          +                +  img(1) + img(2) right
			7. title left          +                +  img(2) right
			8. title left
		*/
		
		//+ type
		if(strType.equals("1")) {
			findViewById(R.id.rl_title_1).setVisibility(View.VISIBLE);
			findViewById(R.id.rl_title_2).setVisibility(View.GONE);
			findViewById(R.id.rl_title_3).setVisibility(View.GONE);

			findViewById(R.id.iv_title1_left).setVisibility(View.VISIBLE);
			mTvTitle =(TextView)findViewById(R.id.tv_title1_title);
		} else if(strType.equals("2")) {
			findViewById(R.id.rl_title_1).setVisibility(View.VISIBLE);
			findViewById(R.id.rl_title_2).setVisibility(View.GONE);
			findViewById(R.id.rl_title_3).setVisibility(View.GONE);

			findViewById(R.id.iv_title1_left).setVisibility(View.GONE);
			mTvTitle =(TextView)findViewById(R.id.tv_title1_title);
		} else if(strType.equals("3")) {
			findViewById(R.id.rl_title_1).setVisibility(View.GONE);
			findViewById(R.id.rl_title_2).setVisibility(View.VISIBLE);
			findViewById(R.id.rl_title_3).setVisibility(View.GONE);

			findViewById(R.id.tv_title2_right).setVisibility(View.GONE);
			findViewById(R.id.iv_title2_right).setVisibility(View.VISIBLE);
			mTvTitle =(TextView)findViewById(R.id.tv_title2_title);
		} else if(strType.equals("4")) {
			findViewById(R.id.rl_title_1).setVisibility(View.GONE);
			findViewById(R.id.rl_title_2).setVisibility(View.VISIBLE);
			findViewById(R.id.rl_title_3).setVisibility(View.GONE);

			findViewById(R.id.tv_title2_right).setVisibility(View.VISIBLE);
			findViewById(R.id.iv_title2_right).setVisibility(View.GONE);
			mTvTitle =(TextView)findViewById(R.id.tv_title2_title);
		} else if(strType.equals("5")) {
			findViewById(R.id.rl_title_1).setVisibility(View.GONE);
			findViewById(R.id.rl_title_2).setVisibility(View.VISIBLE);
			findViewById(R.id.rl_title_3).setVisibility(View.GONE);

			findViewById(R.id.tv_title2_right).setVisibility(View.GONE);
			findViewById(R.id.iv_title2_right).setVisibility(View.GONE);
			mTvTitle =(TextView)findViewById(R.id.tv_title2_title);
		}

	}

	/**
	 *  set title for title bar
	 * @param title
	 */
	public void setTitle(String title) {
		
		if(mTvTitle == null) {
			return;
		}
		
		if(null == title || "".equals(title)) {
			mTvTitle.setText("");
			return;
		}
		mTvTitle.setText(title);
	}
	
	/**
	 * set onclick to all titlebar view
	 */
	public void setOnClickListener(OnClickListener clickListener) {
		
		findViewById(R.id.iv_title1_left).setOnClickListener(clickListener);
		findViewById(R.id.iv_title2_left).setOnClickListener(clickListener);
		findViewById(R.id.iv_title2_right).setOnClickListener(clickListener);
		findViewById(R.id.tv_title2_right).setOnClickListener(clickListener);

	}

	public String getType() {
		return mType;
	}

}
