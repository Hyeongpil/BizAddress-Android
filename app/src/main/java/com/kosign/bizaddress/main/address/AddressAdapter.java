package com.kosign.bizaddress.main.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    final static String TAG = "Address_Adapter";
    private Context mContext;
    public List<UserInfo> mListData = null;
    private View.OnClickListener listener;

    public AddressAdapter(Context mContext, View.OnClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        itemView.setOnClickListener(listener);
        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, final int position) {
        final UserInfo data = mListData.get(position);

        //부서가 없으면 부서 미지정
        String division = (data.getStrDivision() == null) ? "부서 미지정" : data.getStrDivision();
        String phone = StringUtil.getViewNum(data.getStrPhone(),data.getStrPhone_contryCode());
        //내선 번호가 있을 땐 부서 / 내선번호로 출력
        try{if(!data.getStrInnerPhone().equals("")){
            phone = phone+" /";
        }}catch (NullPointerException e){}
        holder.getName().setText(data.getStrName());
        holder.getInner_phone().setText(StringUtil.getViewNum(data.getStrInnerPhone(),data.getStrInnerPhone_contryCode()));
        holder.getDivision().setText(division);
        holder.getPhone().setText(phone);

        //네비게이션 프로필
        Glide.with(mContext)
                .load(data.getStrProfileImg())
                .error(R.drawable.default_profile)
                .thumbnail(0.1f)
                .bitmapTransform(new CropCircleTransformation(Glide.get(mContext).getBitmapPool())).into(holder.getProfileImg());

        //전화 걸기 버튼
        holder.getCall().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //국가 코드를 붙여줌
                String phone = StringUtil.getCallNum(data.getStrPhone(),data.getStrPhone_contryCode());
                String innerPhone = StringUtil.getCallNum(data.getStrInnerPhone(),data.getStrInnerPhone_contryCode());

                //ACTION_DIAL 전화 걸기 화면    ACTION_CALL 전화 걸기
                mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone)));
                Toast.makeText(mContext, data.getStrName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListData == null){
            return 0;
        }else{
            return mListData.size();
        }
    }

    public void setData(ArrayList<UserInfo> mListData){
        this.mListData = mListData;
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public List<UserInfo> getData() {
        return mListData;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView division;
        public TextView phone;
        public TextView inner_phone;
        public ImageView profileImg;
        public FrameLayout call;
        public AddressViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.address_name);
            phone = (TextView) itemView.findViewById(R.id.address_phone);
            inner_phone = (TextView) itemView.findViewById(R.id.address_inner_phone);
            division = (TextView) itemView.findViewById(R.id.address_division);
            profileImg = (ImageView) itemView.findViewById(R.id.address_img);
            call = (FrameLayout) itemView.findViewById(R.id.address_call);
        }

        public TextView getName() {return name;}
        public TextView getPhone() {return phone;}
        public TextView getInner_phone() {return inner_phone;}
        public TextView getDivision() {return division;}
        public ImageView getProfileImg() {return profileImg;}
        public FrameLayout getCall() {return call;}
    }
}
