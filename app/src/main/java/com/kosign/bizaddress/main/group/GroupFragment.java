package com.kosign.bizaddress.main.group;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.kosign.bizaddress.main.retrofit.EmplThread;
import com.kosign.bizaddress.model.UserInfo;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 9..
 */
public class GroupFragment extends Fragment {
    private RecyclerView group_recycler;
    private GroupAdapter adapter;
    private ProgressDialog dlgProgress;
    private ArrayList<UserInfo> userdata;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_recycler, container, false);

        init(view);
        return view;
    }

    private void init(View view){
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.group_refresh);
        refreshLayout.setOnRefreshListener(new RefreshListener());
        group_recycler = (RecyclerView) view.findViewById(R.id.group_recycler);
        adapter = new GroupAdapter(getActivity(),new GroupClickListener());
        group_recycler.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        group_recycler.setLayoutManager(manager);
        group_recycler.setHasFixedSize(true);
    }

    public void setData(Bundle bundle){

    }


    public void stopRefresh(){
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }

    private class GroupClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            dlgProgress = ProgressDialog.show(getActivity(), null, "잠시만 기다려 주세요.");
            Handler groupHandler = new GroupReceiveHandler();
            Thread groupEmplThread = new EmplThread("그룹코드", groupHandler, getActivity());
            groupEmplThread.start();
        }
    }
    private class GroupReceiveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            userdata = (ArrayList<UserInfo>) msg.getData().getSerializable("EmplThread");

            dlgProgress.dismiss();
        }
    }

    /**
     * 새로고침 시 맨 처음 데이터를 다시 불러온다
     */
    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            ((MainActivity)getActivity()).getGroupData();
        }
    }

}
