package com.kosign.bizaddress.main.address;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */
public class Address_Recycler_Adapter extends RecyclerView.Adapter<Address_Recycler_Adapter.AddressViewHolder>{
    final static String TAG = "Address_Adapter";
    private Context mContext;
    public List<UserInfo> mListData = null;
    private View.OnClickListener listener;

    public Address_Recycler_Adapter(Context mContext, View.OnClickListener listener) {
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
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        UserInfo data = mListData.get(position);

        //부서가 있을 땐 회사 / 부서 로 출력
        String company = data.getStrCompany();
        if(data.getStrDivision() != null){
            company = company+" /";
        }

        holder.getName().setText(data.getStrName());
        holder.getCompany().setText(company);
        holder.getDivision().setText(data.getStrDivision());
        //네비게이션 프로필
        Glide.with(mContext)
                .load(data.getStrProfileImg())
                .error(R.drawable.default_profile)
                .bitmapTransform(new CropCircleTransformation(Glide.get(mContext).getBitmapPool())).into(holder.getProfileImg());

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

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView company;
        public TextView division;
        public ImageView profileImg;
        public AddressViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.address_name);
            company = (TextView) itemView.findViewById(R.id.address_company);
            division = (TextView) itemView.findViewById(R.id.address_division);
            profileImg = (ImageView) itemView.findViewById(R.id.address_img);
        }

        public TextView getName() {return name;}
        public TextView getCompany() {return company;}
        public TextView getDivision() {return division;}
        public ImageView getProfileImg() {return profileImg;}
    }
}
