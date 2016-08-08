package com.kosign.bizaddress.main.address;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.UserInfo;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 */
public class AddressFragment extends Fragment {
    final static String TAG = "AddressFragment";
    private RecyclerView add_recycler;
    private Address_Recycler_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_recycler, container, false);
        add_recycler = (RecyclerView) view.findViewById(R.id.address_recycler);

        init();

        return view;
    }

    private void init(){
        adapter = new Address_Recycler_Adapter(getActivity(),new AddressClickListener());
        add_recycler.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        add_recycler.setLayoutManager(manager);
        add_recycler.setHasFixedSize(true);
    }

    public void setData(ArrayList<UserInfo> mListData){
        adapter.setData(mListData);
    }

    private class AddressClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position = add_recycler.getChildLayoutPosition(view);

            Toast.makeText(getActivity(), "클릭됐어요", Toast.LENGTH_SHORT).show();
        }
    }
}
