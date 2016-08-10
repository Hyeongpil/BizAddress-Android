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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.UserInfo;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Hyeongpil on 2016. 8. 10..
 */
public class DetailActivity extends Activity{
    final static String TAG = "DetailActivity";
    private Context mContext;
    private TextView name;
    private TextView phonenum;
    private TextView division;
    private TextView email;
    private ImageView profileImg;
    private ImageView address_add;
    private ImageView sms;
    private ImageView call;
    private UserInfo data;
    private ClipboardManager clipboardManager;



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
        name = (TextView) findViewById(R.id.detail_name);
        phonenum = (TextView) findViewById(R.id.detail_phonenum);
        division = (TextView) findViewById(R.id.detail_division);
        email = (TextView) findViewById(R.id.detail_email);
        profileImg = (ImageView) findViewById(R.id.detail_img);
        address_add = (ImageView) findViewById(R.id.detail_add);
        sms = (ImageView) findViewById(R.id.detail_sms);
        call = (ImageView) findViewById(R.id.detail_call);
        data = (UserInfo) getIntent().getSerializableExtra("data");
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private void setData(){
        name.setText(data.getStrName());
        phonenum.setText(data.getStrPhoneNum());
        division.setText(data.getStrDivision());
        email.setText(data.getStrEmail());
        //프로필 사진
        Glide.with(mContext)
                .load(data.getStrProfileImg())
                .error(R.drawable.default_profile)
                .bitmapTransform(new CropCircleTransformation(Glide.get(mContext).getBitmapPool())).into(profileImg);
        sms.setOnClickListener(new OnClickListener());
        call.setOnClickListener(new OnClickListener());
        address_add.setOnClickListener(new OnClickListener());
        email.setOnClickListener(new OnClickListener());
        phonenum.setOnClickListener(new OnClickListener());
    }

    private class OnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.detail_email:
                    clipboardManager.setText(email.getText());
                    Toast.makeText(DetailActivity.this, "e-mail이 클립 보드에 복사 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.detail_phonenum:
                    clipboardManager.setText(data.getStrPhoneNum());
                    Toast.makeText(DetailActivity.this, "번호가 클립 보드에 복사 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.detail_add:
                    Intent add_intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactsContract.Intents.Insert.PHONE, data.getStrPhoneNum());
                    add_intent.putExtras(bundle);
                    startActivity(add_intent);
                    break;

                case R.id.detail_sms:
                    Uri uri= Uri.parse("smsto:"+data.getStrPhoneNum()); //sms 문자와 관련된 Data는 'smsto:'로 시작. 이후는 문자를 받는 사람의 전화번호
                    Intent sms_intent = new Intent(Intent.ACTION_SENDTO,uri);
                    startActivity(sms_intent);
                    break;

                case R.id.detail_call:
                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+data.getStrPhoneNum())));
                    break;
            }
        }
    }


}
