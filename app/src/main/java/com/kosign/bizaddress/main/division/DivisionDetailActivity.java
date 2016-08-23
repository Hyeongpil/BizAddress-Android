package com.kosign.bizaddress.main.division;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.DetailActivity;
import com.kosign.bizaddress.main.address.AddressAdapter;
import com.kosign.bizaddress.model.UserInfo;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 23..
 * 부서 직원목록 Activity
 * emplinfo25 api에서는 프로필 사진과 사업장명을 주지 않는다.
 */
public class DivisionDetailActivity extends AppCompatActivity {
    final static String TAG = "DivisionDetailActivity";
    private Context mContext;
    private RecyclerView rv_divisionDetail;
    private AddressAdapter adapter;
    private ArrayList<UserInfo> mListData; // 부서 직원 데이터
    private ImageView iv_back;
    private com.kosign.bizaddress.model.BizTitleBar title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.division_detail_recycler);
        init();
        setData();
    }

    private void init(){
        mContext = this;
        title = (com.kosign.bizaddress.model.BizTitleBar) findViewById(R.id.division_detail_title);
        iv_back = (ImageView) findViewById(R.id.iv_title1_left);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rv_divisionDetail = (RecyclerView) findViewById(R.id.division_detail_recycler);
        adapter = new AddressAdapter(mContext,new AddressClickListener());
        rv_divisionDetail.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        rv_divisionDetail.setLayoutManager(manager);
        rv_divisionDetail.setHasFixedSize(true);
    }

    public void setData(){
        mListData = (ArrayList<UserInfo>) getIntent().getSerializableExtra("userdata");
        adapter.setData(mListData);
        title.setTitle(mListData.get(0).getStrDivision());
    }

    /**
     * 연락처 클릭 시 연락처 상세 페이지로
     */
    private class AddressClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            try {
                final int position = rv_divisionDetail.getChildLayoutPosition(view);
                UserInfo temp = mListData.get(position);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("data", temp);
                startActivity(intent);
            }catch (IndexOutOfBoundsException e){

            }
        }
    }
}
