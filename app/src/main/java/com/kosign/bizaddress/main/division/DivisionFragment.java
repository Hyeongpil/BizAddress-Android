package com.kosign.bizaddress.main.division;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kosign.bizaddress.R;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 */
public class DivisionFragment extends Fragment{
    private RecyclerView divi_recycler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.division_recycler, container, false);
        divi_recycler = (RecyclerView) view.findViewById(R.id.division_recycler);

        return view;
    }

    private void init(){
//        adapter = new Division_Adapter(getActivity(),new Find_Dis_ClickListener());
//        divi_recycler.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        divi_recycler.setLayoutManager(manager);
        divi_recycler.setHasFixedSize(true);
    }
}
