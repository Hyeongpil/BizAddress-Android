package com.kosign.bizaddress.main;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.StringUtil;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Hyeongpil on 2016. 8. 10..
 */
public class DetailActivity extends Activity{
    final static String TAG = "DetailActivity";
    private Context mContext;
    private TextView tv_company;
    private TextView tv_name;
    private TextView tv_phonenum;
    private TextView tv_division;
    private TextView tv_email;
    private TextView tv_position;
    private TextView tv_address;
    private ImageView iv_profileImg;
    private ImageView iv_address_add;
    private ImageView iv_sms;
    private ImageView iv_call;
    private ImageView iv_email;
    private LinearLayout ll_address;
    private UserInfo data;
    private ClipboardManager clipboardManager;
    private String phone;
    private String phone_contryCode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        init();
        setData();
    }

    private void init(){
        mContext = this;
        tv_company = (TextView) findViewById(R.id.detail_company);
        tv_name = (TextView) findViewById(R.id.detail_name);
        tv_phonenum = (TextView) findViewById(R.id.detail_phonenum);
        tv_division = (TextView) findViewById(R.id.detail_division);
        tv_email = (TextView) findViewById(R.id.detail_email);
        tv_position = (TextView) findViewById(R.id.detail_position);
        tv_address = (TextView) findViewById(R.id.detail_address);
        iv_profileImg = (ImageView) findViewById(R.id.detail_img);
        iv_address_add = (ImageView) findViewById(R.id.detail_address_icon);
        iv_sms = (ImageView) findViewById(R.id.detail_sms_icon);
        iv_call = (ImageView) findViewById(R.id.detail_call_icon);
        iv_email = (ImageView) findViewById(R.id.detail_email_icon);
        ll_address = (LinearLayout) findViewById(R.id.detail_address_ll);
        data = (UserInfo) getIntent().getSerializableExtra("data");
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        phone = StringUtil.getCallNum(data.getStrPhone(),data.getStrPhone_contryCode());
    }

    private void setData(){
        tv_company.setText(data.getStrCompany());
        tv_name.setText(data.getStrName());
        tv_position.setText(data.getStrPosition());
        tv_address.setText(data.getStrAddress());
        if(data.getStrAddress().equals("")){
            ll_address.setVisibility(View.GONE);
        }
        tv_phonenum.setText(StringUtil.getViewNum(data.getStrPhone(),data.getStrPhone_contryCode()));
        tv_division.setText(data.getStrDivision());
        tv_email.setText(data.getStrEmail());
        //프로필 사진
        Glide.with(mContext)
                .load(data.getStrProfileImg())
                .error(R.drawable.default_profile)
                .thumbnail(0.1f)
                .bitmapTransform(new CropCircleTransformation(Glide.get(mContext).getBitmapPool())).into(iv_profileImg);
        iv_sms.setOnClickListener(new OnClickListener());
        iv_call.setOnClickListener(new OnClickListener());
        iv_address_add.setOnClickListener(new OnClickListener());
        iv_email.setOnClickListener(new OnClickListener());
        tv_email.setOnClickListener(new OnClickListener());
        tv_phonenum.setOnClickListener(new OnClickListener());
    }

    private class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.detail_email:
                    clipboardManager.setText(tv_email.getText());
                    Toast.makeText(DetailActivity.this, "e-mail이 클립 보드에 복사 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.detail_phonenum:
                    clipboardManager.setText(phone);
                    Toast.makeText(DetailActivity.this, "번호가 클립 보드에 복사 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.detail_address_icon:
                    Intent add_intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactsContract.Intents.Insert.PHONE, phone);
                    add_intent.putExtras(bundle);
                    startActivity(add_intent);
                    break;

                case R.id.detail_sms_icon:
                    Uri uri= Uri.parse("smsto:"+phone); //iv_sms 문자와 관련된 Data는 'smsto:'로 시작. 이후는 문자를 받는 사람의 전화번호
                    Intent sms_intent = new Intent(Intent.ACTION_SENDTO,uri);
                    startActivity(sms_intent);
                    break;

                case R.id.detail_call_icon:
                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone)));
                    break;

                case R.id.detail_email_icon:
                    Intent email_intent = new Intent(Intent.ACTION_SEND);
                    email_intent.setType("plain/text");
                    String[] address = {data.getStrEmail()};
                    email_intent.putExtra(Intent.EXTRA_EMAIL,address);
                    startActivity(email_intent);
            }
        }
    }


}
