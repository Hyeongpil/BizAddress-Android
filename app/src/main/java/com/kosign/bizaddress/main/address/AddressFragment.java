package com.kosign.bizaddress.main.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.DetailActivity;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.GlobalApplication;
import com.kosign.bizaddress.util.Searcher;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 */
public class AddressFragment extends Fragment {
    final static String TAG = "AddressFragment";
    private RecyclerView rv_address;
    private CoordinatorLayout mCoordinatorLayout;
    private AddressAdapter adapter;
    private ArrayList<UserInfo> initialData; // 검색 결과 총 데이터
    private ArrayList<UserInfo> mListData; // 20개씩 끊어서 보여주는 데이터

    private EditText et_search;
    private ImageView iv_clear;
    private TextWatcher textWatcher;
    private boolean searching = false; // 검색 중 일땐 BottomRefreshListener 비 활성화
    private int mListDataCount; // 바닥에 닿을 때 마다 +20씩 증가

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_recycler, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        rv_address = (RecyclerView) view.findViewById(R.id.address_recycler);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.address_coordinatorLayout);
        et_search = (EditText) view.findViewById(R.id.address_search);
        iv_clear = (ImageView) view.findViewById(R.id.address_search_clear);
        adapter = new AddressAdapter(getActivity(),new AddressClickListener());
        rv_address.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_address.setLayoutManager(manager);
        rv_address.setHasFixedSize(true);
        rv_address.setOnScrollListener(new BottomRefreshListener());
        et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() { // 검색 버튼 클릭 시 키보드 내리기
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                InputMethodManager mInputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                return true;
            }
        });
        iv_clear.setOnClickListener(new View.OnClickListener() { //검색 지우기
            @Override
            public void onClick(View view) {
                et_search.setText("");
                et_search.clearFocus();
                iv_clear.setVisibility(View.INVISIBLE);
            }
        });
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
                if(et_search.isFocusable() && et_search.length() > 0){
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
                            String searchPhoneNum = searchInitialData.get(temp).getStrPhone().trim();
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
                if(et_search.isFocusable() && String.valueOf(editable).equals("")){
                    iv_clear.setVisibility(View.INVISIBLE);
                    mListData = new ArrayList<>(); // mListData 초기화
                    mListDataCount = 20; // 카운트 초기화
                    searching = false;
                    //부서 검색을 했을때도 전체 직원 리스트를 가져오기 위해 글로벌에서 초기화
                    initialData = GlobalApplication.getInstance().getInitialData();
                    copyListData();
                }
                //검색어가 존재하면 x 표를 보여준다.
                if(et_search.isFocusable() && String.valueOf(editable).length() > 0){
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }
        };
        et_search.addTextChangedListener(textWatcher);
    }

    /**
     * EmplThread 에서 받아온 전체 직원 목록 데이터를
     * initialData에 넣는다
     */
    public void setData(ArrayList<UserInfo> initialData){
        mListData = new ArrayList<>(); // mListData 초기화
        mListDataCount = 20; // 카운트 초기화
        this.initialData = initialData;
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
        }catch (NullPointerException e){
            Log.e(TAG,""+e.getMessage());
        }
        finally {
            setAdapterData(mListData);
        }
    }

    /**
     * 데이터를 어댑터에 붙인다
     */
    public void setAdapterData(ArrayList<UserInfo> mListData){
        try {
            adapter.setData(mListData);
        }catch (NullPointerException e){
            Log.e(TAG,"setAdapterData null");
            Toast.makeText(getActivity(), "오류가 발생했습니다. 새로고침 해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private class BottomRefreshListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(!searching){ // 검색중이 아닐 때
                int LastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                if ((LastVisibleItem) == adapter.getData().size() - 1 && adapter.getData().size() > 18) {
                    mListDataCount += 20; // 바닥에 닿으면 카운트 20씩 증가
                    Log.d(TAG,"BottomRefreshListener mListDataCount :"+mListDataCount);
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
            try {
                final int position = rv_address.getChildLayoutPosition(view);
                UserInfo temp = mListData.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("data", temp);
                startActivity(intent);
            }catch (IndexOutOfBoundsException e){
                Snackbar.make(mCoordinatorLayout,"오류가 발생했습니다. 새로고침 해 주세요",Snackbar.LENGTH_SHORT).show();
                Log.e(TAG,"addressClickError :"+e.getMessage());
            }
        }
    }

    public EditText getEt_search() {return et_search;}
}
