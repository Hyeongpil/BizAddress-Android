package com.kosign.bizaddress.main.division;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.MainActivity;
import com.kosign.bizaddress.model.HighDivision;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 */
public class DivisionFragment extends Fragment{
    final static String TAG = "DivisionFragment";
    private SwipeRefreshLayout refreshLayout;
    public DivisionAdapter adapter;
    private RecyclerView divi_recycler;
    private ArrayList<HighDivision> divisionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.division_recycler, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        divi_recycler = (RecyclerView) view.findViewById(R.id.division_recycler);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.division_refresh);
        refreshLayout.setOnRefreshListener(new RefreshListener());
    }

    public void setData (ArrayList<HighDivision> divisionList){
        this.divisionList = divisionList;
        setAdapter();
//        데이터 보는 로그
//        for(int i= 0 ; i<divisionList.size(); i++){
//            Log.e(TAG,"high :"+divisionList.get(i).getHighDivision_name());
//            for(int j = 0; j< divisionList.get(i).getDivision().size(); j++){
//                Log.e(TAG,"division :"+divisionList.get(i).getDivision().get(j).getDivision_name());
//            }
//        }
    }

    private void setAdapter(){
        adapter = new DivisionAdapter(getActivity(), divisionList);

        adapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {} // 상위부서 Expanded 리스너
            @Override
            public void onListItemCollapsed(int position) {}// 상위부서 Collapsed 리스너
        });
        divi_recycler.setAdapter(adapter);
        divi_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        divi_recycler.setHasFixedSize(true);
    }

    public void stopRefresh(){
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     * 새로고침 시 맨 처음 데이터를 다시 불러온다
     */
    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            ((MainActivity)getActivity()).getDivisionData();
        }
    }

}
