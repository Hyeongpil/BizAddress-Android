package com.kosign.bizaddress.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.main.address.AddressFragment;
import com.kosign.bizaddress.main.division.DivisionFragment;
import com.kosign.bizaddress.main.group.GroupFragment;
import com.kosign.bizaddress.main.retrofit.DivisionThread;
import com.kosign.bizaddress.main.retrofit.EmplThread;
import com.kosign.bizaddress.main.retrofit.GroupThread;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.GlobalApplication;
import com.kosign.bizaddress.util.ViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */
public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";
    private Context mContext;
    private ImageView refresh;
    private ViewPageAdapter adapter;
    private ViewPager viewPager;
    private AddressFragment addressFragment;
    private DivisionFragment divisionFragment;
    private GroupFragment groupFragment;
    private ArrayList<UserInfo> userdata;
    private ProgressDialog dlgProgress;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        init();
        getEmplData();
        getDivisionData();
        getGroupData();
        setViewpager();
    }

    private void init(){
        dlgProgress = ProgressDialog.show(MainActivity.this, null, "잠시만 기다려 주세요.");
        mContext = MainActivity.this;
        refresh = (ImageView)findViewById(R.id.iv_title4_left);
        refresh.setOnClickListener(refreshClickListener);
    }

    /**
     * emplThread 에서 직원 데이터를 받아
     * EmplDataReceiveHandler 로 리턴
     */
    public void getEmplData(){
        Handler EmplHandler = new EmplDataReceiveHandler();
        Thread emplThread = new EmplThread(EmplHandler, MainActivity.this);
        emplThread.start();
    }

    /**
     * emplThread 에서 직원 데이터를 받아
     * EmplDataReceiveHandler 로 리턴
     */
    public void getDivisionData(){
        Handler divisionHandler = new DivisionDataReceiveHandler();
        Thread divisionThread = new DivisionThread(divisionHandler,MainActivity.this);
        divisionThread.start();
    }

    /**
     * GroupThread 에서 그룹 데이터를 받아
     * GroupDataReceiveHandler 로 리턴
     */
    public void getGroupData(){
        Handler groupHandler = new GroupDataReceiveHandler();
        Thread groupThread = new GroupThread(groupHandler,MainActivity.this);
        groupThread.start();
    }

    /**
     * DivisionViewHolder 에서 부서 클릭 시 또는 GroupFragment 에서 그룹 클릭 시
     * 부서별 직원 데이터를 받아와 addressFragment에 데이터 입력
     * 부서별 직원 데이터 Api에서는 사진을 주지 않는다.
     */
    public void getDivisionEmplData(ArrayList<UserInfo> userdata){
        addressFragment.setData(userdata);
        viewPager.setCurrentItem(0);
    }



    private void setViewpager(){
        addressFragment = new AddressFragment();
        divisionFragment = new DivisionFragment();
        groupFragment = new GroupFragment();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(addressFragment);
        fragments.add(divisionFragment);
        fragments.add(groupFragment);
        List<String> titles = new ArrayList<String>();
        titles.add("연락처");
        titles.add("부서");
        titles.add("그룹");

        adapter = new ViewPageAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2); // 뷰 페이저 캐싱 페이지 개수
        tabLayout.setupWithViewPager(viewPager);
    }

    //새로고침 버튼을 누르면 직원 데이터 불러옴
    ImageView.OnClickListener refreshClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            getEmplData();
            GlobalApplication.getInstance().setPage(1);
        }
    };

    /**
     * EmplThread 에서 데이터를 받아
     * addressFragment 에 전달
     */
    private class EmplDataReceiveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {userdata = (ArrayList<UserInfo>) msg.getData().getSerializable("EmplThread");
            }catch (Exception e){Log.e(TAG,"데이터 가져오기 실패 :"+e.getMessage());}
            dlgProgress.dismiss();
            addressFragment.setData(userdata);
            stopRefresh();
        }
    }

    /**
     * DivisionThread 에서 데이터를 받아
     * divisionFragment 에 전달
     */
    private class DivisionDataReceiveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            stopRefresh();
            divisionFragment.setData(msg.getData());
        }
    }

    /**
     * GroupThread 에서 그룹 데이터를 받아
     * groupFragment 에 전달
     */
    private class GroupDataReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            stopRefresh();
            groupFragment.setData(msg.getData());
        }
    }

    /**
     * 주소록 연동 실패
     */
    public void dataException(){
        dlgProgress.dismiss();

    }

    /**
     * 오류 발생 시 새로고침 멈춤
     */
    public void stopRefresh(){
        addressFragment.stopRefresh();
        groupFragment.stopRefresh();
        divisionFragment.stopRefresh();
    }
}
