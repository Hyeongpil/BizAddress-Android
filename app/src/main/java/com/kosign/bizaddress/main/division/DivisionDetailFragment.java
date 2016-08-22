package com.kosign.bizaddress.main.division;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.DetailActivity;
import com.kosign.bizaddress.main.address.AddressAdapter;
import com.kosign.bizaddress.model.UserInfo;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 22..
 */
public class DivisionDetailFragment extends Fragment {
    final static String TAG = "DivisionDetailFragment";
    private RecyclerView rv_divisionDetail;
    private AddressAdapter adapter;
    private ArrayList<UserInfo> mListData; // 부서 직원 데이터

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.division_detail_recycler, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        rv_divisionDetail = (RecyclerView) view.findViewById(R.id.division_detail_recycler);
        adapter = new AddressAdapter(getActivity(),new AddressClickListener());
        rv_divisionDetail.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_divisionDetail.setLayoutManager(manager);
        rv_divisionDetail.setHasFixedSize(true);
    }

    public void setData(ArrayList<UserInfo> mListData){
        this.mListData = mListData;
        adapter.setData(mListData);
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
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("data", temp);
                startActivity(intent);
            }catch (IndexOutOfBoundsException e){

            }
        }
    }



}
