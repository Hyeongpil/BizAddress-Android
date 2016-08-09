package com.kosign.bizaddress.main.division;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.HighDivision;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 */
public class DivisionFragment extends Fragment{
    final static String TAG = "DivisionFragment";
    public DivisionAdapter adapter;
    private RecyclerView divi_recycler;
    private ArrayList<HighDivision> highDivision;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.division_recycler, container, false);
        divi_recycler = (RecyclerView) view.findViewById(R.id.division_recycler);
        return view;
    }

    public void setData (Bundle bundle){
        this.highDivision = (ArrayList<HighDivision>) bundle.getSerializable("highDivision");
//        데이터 보는 로그
//        for(int i= 0 ; i<highDivision.size(); i++){
//            Log.e(TAG,"high :"+highDivision.get(i).getHighDivision_name());
//            for(int j = 0; j< highDivision.get(i).getDivision().size(); j++){
//                Log.e(TAG,"division :"+highDivision.get(i).getDivision().get(j).getDivision_name());
//            }
//        }
        init();
    }

    private void init(){
        adapter = new DivisionAdapter(getActivity(),highDivision);

        adapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {

            }

            @Override
            public void onListItemCollapsed(int position) {

            }
        });
        divi_recycler.setAdapter(adapter);
        divi_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        divi_recycler.setHasFixedSize(true);

    }

}
