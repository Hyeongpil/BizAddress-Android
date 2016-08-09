package com.kosign.bizaddress.main.address;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.kosign.bizaddress.main.MainActivity;
import com.kosign.bizaddress.main.retrofit.EmplThread;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.GlobalApplication;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 3..
 */
public class AddressFragment extends Fragment {
    final static String TAG = "AddressFragment";
    private RecyclerView add_recycler;
    private AddressAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<UserInfo> mListData;
    private EditText search_et;
    private int page;
    private TextWatcher textWatcher;
    private boolean searching = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_recycler, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        add_recycler = (RecyclerView) view.findViewById(R.id.address_recycler);
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

    private void setSearch(){
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(search_et.isFocusable()){
                    searching = true;
                    Handler EmplHandler = new SearchEmplReceiveHandler();
                    Thread emplThread = new EmplThread(EmplHandler, getActivity(), search_et.getText().toString());
                    emplThread.start();
                    Log.e(TAG,"search :"+search_et.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(search_et.getText().toString().equals("")){
                    searching = false;
                }
            }
        };
        search_et.addTextChangedListener(textWatcher);
    }

    public void setData(ArrayList<UserInfo> mListData){
        this.mListData = mListData;
        adapter.setData(mListData);
        refreshLayout.setRefreshing(false);
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

            int LastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

            if ((LastVisibleItem) == adapter.getData().size() - 1 && !refreshLayout.isRefreshing() && adapter.getData().size() > 18) {
                GlobalApplication.getInstance().pageCountup(); // 글로벌 어플리케이션의 페이지 카운트를 올려줌
                page = GlobalApplication.getInstance().getPage(); // 페이지 값을 가져옴
                refreshLayout.setRefreshing(true);
                if(!searching){ // 검색중이 아닐 때
                    Handler handler = new BottomRefreshHandler();
                    Thread thread = new EmplThread(handler, getActivity(), page);
                    thread.start();
                }
            }
        }
    }

    private class AddressClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final int position = add_recycler.getChildLayoutPosition(view);
//            address_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(ContactsContract.Intents.Insert.PHONE, mListData.get(position).getStrPhoneNum());
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
            Log.e(TAG,"클릭");
        }
    }

    /**
     * 새로고침 시 맨 처음 데이터를 다시 불러온다
     */
    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            searching = false;
            search_et.setText("");
            ((MainActivity)getActivity()).getEmplData();
            GlobalApplication.getInstance().setPage(1);
        }
    }

    private class BottomRefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArrayList<UserInfo> temp = (ArrayList<UserInfo>) msg.getData().getSerializable("EmplThread");
            mListData.addAll(temp);
            adapter.setData(mListData);
            refreshLayout.setRefreshing(false);
        }
    }

    private class SearchEmplReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArrayList<UserInfo> temp = (ArrayList<UserInfo>) msg.getData().getSerializable("EmplThread");
            adapter.setData(temp);
        }
    }

}
