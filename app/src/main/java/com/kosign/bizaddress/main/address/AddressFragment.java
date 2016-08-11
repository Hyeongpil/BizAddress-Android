package com.kosign.bizaddress.main.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.DetailActivity;
import com.kosign.bizaddress.main.MainActivity;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.GlobalApplication;
import com.kosign.bizaddress.util.Searcher;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 */
public class AddressFragment extends Fragment {
    final static String TAG = "AddressFragment";
    private RecyclerView add_recycler;
    private CoordinatorLayout mCoordinatorLayout;
    private AddressAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<UserInfo> initialData; // 검색 결과 총 데이터
    private ArrayList<UserInfo> mListData; // 20개씩 끊어서 보여주는 데이터

    private EditText search_et;
    private TextWatcher textWatcher;
    private boolean searching = false; // 검색 중 일땐 BottomRefreshListener 비 활성화
    private int mListDataCount = 20; // 초기값 20   바닥에 닿을 때 마다 +20씩 증가

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_recycler, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        add_recycler = (RecyclerView) view.findViewById(R.id.address_recycler);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.address_coordinatorLayout);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.address_refresh);
        search_et = (EditText) view.findViewById(R.id.address_search);
        refreshLayout.setOnRefreshListener(new RefreshListener());
        adapter = new AddressAdapter(getActivity(),new AddressClickListener());
        add_recycler.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        add_recycler.setLayoutManager(manager);
        add_recycler.setHasFixedSize(true);
        add_recycler.setOnScrollListener(new BottomRefreshListener());
        setSearch();
    }

    /**
     * 검색 필터
     */
    private void setSearch(){
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<UserInfo> searchInitialData = GlobalApplication.getInstance().getInitialData(); // 검색용 전 직원 데이터
                if(search_et.isFocusable() && search_et.length() > 0){
                    searching = true;
                    mListData.clear(); // 초기화
                    for(int temp = 0; temp < searchInitialData.size(); temp++){
                        String keyWord = charSequence.toString();

                        try { // 이름 검색
                            String searchName = searchInitialData.get(temp).getStrName().toLowerCase().trim();
                            // 한글 초성 이름 검색
                            if (Searcher.matchString(searchName, keyWord)) {
                                mListData.add(searchInitialData.get(temp));
                                continue;
                            }
                            // 영문자 이름 검색
                            if (searchName.startsWith(keyWord)) {
                                mListData.add(searchInitialData.get(temp));
                                continue;
                            }
                        }catch (NullPointerException e){}

                        try { // 부서 검색
                            String searchDivision = searchInitialData.get(temp).getStrDivision().toLowerCase().trim();
                            // 한글 부서 검색
                            if(Searcher.matchString(searchDivision,keyWord)){
                                mListData.add(searchInitialData.get(temp));
                                continue;
                            }
                            // 영문 부서 검색
                            if(searchDivision.startsWith(keyWord)){
                                mListData.add(searchInitialData.get(temp));
                                continue;
                            }
                        }catch (NullPointerException e){}

                        try { // 번호 검색
                            String searchPhoneNum = searchInitialData.get(temp).getStrPhoneNum().trim();
                            // 번호 검색
                            if(searchPhoneNum.startsWith(keyWord)){
                                mListData.add(searchInitialData.get(temp));
                                continue;
                            }
                        }catch (NullPointerException e){}
                    }
                    setAdapterData(mListData);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //검색어가 없으면 초기 데이터를 보여준다
                if(search_et.getText().toString().equals("")){
                    searching = false;
                    setAdapterData(GlobalApplication.getInstance().getInitialData());
                }
            }
        };
        search_et.addTextChangedListener(textWatcher);
    }


    /**
     * EmplThread 에서 받아온 전체 직원 목록 데이터를
     * initialData에 넣는다
     */
    public void setData(ArrayList<UserInfo> initialData){
        mListData = new ArrayList<>(); // mListData 초기화
        mListDataCount = 20; // 카운트 초기화
        this.initialData = initialData;
        refreshLayout.setRefreshing(false);
        copyListData();
    }

    /**
     * 20개씩 표시해 주기 위해 mListData에 20개씩 복사한다.
     */
    private void copyListData(){
        try {
            for (int i = mListDataCount - 20; i < mListDataCount; i++) {
                if(i == initialData.size()){ // 마지막 페이지 필터링
                    Snackbar.make(mCoordinatorLayout,"마지막 페이지입니다.",Snackbar.LENGTH_SHORT).show();
                    break;
                }else{
                    mListData.add(initialData.get(i));
                }
            }
        }catch (IndexOutOfBoundsException e){
            Log.d(TAG,"copyListData error :"+e.getMessage());
        }
        finally {
            setAdapterData(mListData);
        }
    }

    /**
     * 데이터를 어댑터에 붙인다
     */
    public void setAdapterData(ArrayList<UserInfo> mListData){
        adapter.setData(mListData);
    }

    public void stopRefresh(){
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }

    private class BottomRefreshListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(!searching){ // 검색중이 아닐 때
                int LastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                if ((LastVisibleItem) == adapter.getData().size() - 1 && !refreshLayout.isRefreshing() && adapter.getData().size() > 19) {
                    mListDataCount += 20; // 바닥에 닿으면 카운트 20씩 증가
                    copyListData();
                }
            }
        }
    }

    /**
     * 연락처 클릭 시 연락처 상세 페이지로
     */
    private class AddressClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final int position = add_recycler.getChildLayoutPosition(view);
            UserInfo temp = mListData.get(position);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("data",temp);
            startActivity(intent);
        }
    }

    /**
     * 새로고침 시 getEmplData를 호출하여 initialData를 갱신한다
     */
    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refreshing();
        }
    }

    public void refreshing(){
        searching = false;
        search_et.setText("");
        ((MainActivity)getActivity()).getEmplData();
    }

    public EditText getSearch_et() {return search_et;}
}
