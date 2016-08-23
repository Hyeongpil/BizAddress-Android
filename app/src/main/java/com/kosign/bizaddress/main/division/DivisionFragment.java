package com.kosign.bizaddress.main.division;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.model.Division;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.model.LowDivision;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.GlobalApplication;
import com.zaihuishou.expandablerecycleradapter.adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 * https://github.com/zaihuishou/ExpandableRecyclerview 참조
 */
public class DivisionFragment extends Fragment{
    final static String TAG = "DivisionFragment";
    private RecyclerView rv_division;
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
    }


    public void setAdapter(final ArrayList<HighDivision> divisionList){
        adapter = new BaseExpandableAdapter(divisionList) {
            @NonNull
            @Override
            public AbstractAdapterItem<Object> getItemView(Object type) {
                int itemType = (int) type;
                switch (itemType) {
                    case ITEM_TYPE_HIGHDIVISION:
                        return new HighDivisionItem(getActivity(),new DivisionEmplReceiveHandler());
                    case ITEM_TYPE_DIVISION:
                        return new DivisionItem(getActivity(),new DivisionEmplReceiveHandler());
                    case ITEM_TYPE_LOWDIVISION:
                        return new LowDivisionItem(getActivity(),new DivisionEmplReceiveHandler());
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

    public class DivisionEmplReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArrayList<UserInfo> userdata = (ArrayList<UserInfo>) msg.getData().getSerializable("DivisionEmplThread");
            getDivisionEmplData(userdata);
            GlobalApplication.getInstance().dismissDlgProgress();
        }
    }

    /**
     * DivisionItem 에서 부서 클릭 시
     * 부서별 직원 데이터를 받아와 DivisionDetailActivity 에 데이터 입력
     * 부서별 직원 데이터 Api에서는 사진과 사업장명(또는 상위 부서명)을 주지 않는다.
     */
    public void getDivisionEmplData(ArrayList<UserInfo> userdata){
        if(userdata.size() == 0){
            Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(getActivity(), DivisionDetailActivity.class);
            intent.putExtra("userdata",userdata);
            startActivity(intent);
        }
    }

}
