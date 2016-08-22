package com.kosign.bizaddress.main.division;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.MainActivity;
import com.kosign.bizaddress.model.Division;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.model.LowDivision;
import com.kosign.bizaddress.util.GlobalApplication;
import com.zaihuishou.expandablerecycleradapter.adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 */
public class DivisionFragment extends Fragment{
    final static String TAG = "DivisionFragment";
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rv_division;
    private ArrayList<HighDivision> divisionList;
    private BaseExpandableAdapter adapter;

    private final int ITEM_TYPE_HIGHDIVISION = 1;
    private final int ITEM_TYPE_DIVISION = 2;
    private final int ITEM_TYPE_LOWDIVISION = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.division_recycler, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        rv_division = (RecyclerView) view.findViewById(R.id.division_recycler);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.division_refresh);
        refreshLayout.setOnRefreshListener(new RefreshListener());
    }

    public void setData (ArrayList<HighDivision> divisionList){
        this.divisionList = divisionList;
        setAdapter();
    }

    private void setAdapter(){
        adapter = new BaseExpandableAdapter(divisionList) {
            @NonNull
            @Override
            public AbstractAdapterItem<Object> getItemView(Object type) {
                int itemType = (int) type;
                switch (itemType) {
                    case ITEM_TYPE_HIGHDIVISION:
                        return new HighDivisionItem(getActivity());
                    case ITEM_TYPE_DIVISION:
                        return new DivisionItem(getActivity());
                    case ITEM_TYPE_LOWDIVISION:
                        return new LowDivisionItem(getActivity());
                }
                return null;
            }

            @Override
            public Object getItemViewType(Object t) {
                if (t instanceof HighDivision) {
                    return ITEM_TYPE_HIGHDIVISION;
                } else if (t instanceof Division)
                    return ITEM_TYPE_DIVISION;
                else if (t instanceof LowDivision)
                    return ITEM_TYPE_LOWDIVISION;
                return -1;
            }
        };
        adapter.setExpandCollapseListener(new BaseExpandableAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {

            }

            @Override
            public void onListItemCollapsed(int position) {

            }
        });
        rv_division.setAdapter(adapter);
        rv_division.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_division.setHasFixedSize(true);
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
            GlobalApplication.getInstance().showDlgProgress();
            ((MainActivity)getActivity()).getDivisionData();
        }
    }

}
