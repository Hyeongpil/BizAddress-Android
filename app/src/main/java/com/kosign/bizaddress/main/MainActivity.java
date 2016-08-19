package com.kosign.bizaddress.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosign.bizaddress.R;
import com.kosign.bizaddress.login.LoginActivity;
import com.kosign.bizaddress.main.address.AddressFragment;
import com.kosign.bizaddress.main.division.DivisionFragment;
import com.kosign.bizaddress.main.group.GroupFragment;
import com.kosign.bizaddress.main.retrofit.DivisionThread;
import com.kosign.bizaddress.main.retrofit.EmplThread;
import com.kosign.bizaddress.main.retrofit.GroupThread;
import com.kosign.bizaddress.model.HighDivision;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.EmplPreference;
import com.kosign.bizaddress.util.GlobalApplication;
import com.kosign.bizaddress.util.ViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */
public class MainActivity extends AppCompatActivity{
    final static String TAG = "MainActivity";
    private final long	FINSH_INTERVAL_TIME    = 2000;
    private long		backPressedTime        = 0;
    private ImageView refresh;
    private ImageView logout;
    private ViewPageAdapter adapter;
    private ViewPager viewPager;
    private AddressFragment addressFragment;
    private DivisionFragment divisionFragment;
    private GroupFragment groupFragment;
    private ArrayList<UserInfo> emplList = new ArrayList<>();
    private ArrayList<HighDivision> divisionList = new ArrayList<>();

    private EmplPreference pref;
    private com.kosign.bizaddress.model.BizTitleBar title;
    private ProgressDialog dlgProgress;

    // TODO: 2016. 8. 10. 그룹 추가 하기
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        init();
        checkEmplData();
        // TODO: 2016. 8. 10. 그룹 추가 시 주석 풀기
//        getGroupData();
        setViewpager();
    }

    private void init(){
        dlgProgress = new ProgressDialog(this);
        GlobalApplication.getInstance().setDlgProgress(dlgProgress);
        addressFragment = new AddressFragment();
        divisionFragment = new DivisionFragment();
        // TODO: 2016. 8. 10. 그룹 추가 시 주석 풀기
//        groupFragment = new GroupFragment();
        refresh = (ImageView)findViewById(R.id.iv_title4_left);
        logout = (ImageView)findViewById(R.id.iv_title4_right);
        refresh.setOnClickListener(refreshClickListener);
        logout.setOnClickListener(logoutClickListener);
        pref = GlobalApplication.getInstance().getPref();
        //타이틀 세팅
        title = (com.kosign.bizaddress.model.BizTitleBar)findViewById(R.id.BizTitleBar);
        title.setTitle(pref.getString("BSNN_NM"));
    }

    /**
     * pref에서 직원 데이터를 확인하고 바로 가져옴
     */
    private void checkEmplData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                emplList = pref.getEmplList();
                divisionList = pref.getDivisionList();
                //직원 데이터
                if (emplList == null) {
                    getEmplData();
                }else {
                    //타이틀 회사 이름으로
                    GlobalApplication.getInstance().setInitialData(emplList); // 글로벌 어플리케이션에 초기값 저장
                    GlobalApplication.getInstance().dismissDlgProgress();
                    addressFragment.setData(emplList);
                }
                //부서 데이터
                if(divisionList == null){
                    getDivisionData();
                }else{
                    divisionFragment.setData(divisionList);
                }
            }
        },100);
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
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(addressFragment);
        fragments.add(divisionFragment);
        // TODO: 2016. 8. 10. 그룹 추가 시 주석 풀기
//        fragments.add(groupFragment);
        List<String> titles = new ArrayList<String>();
        titles.add("직원");
        titles.add("부서");
        // TODO: 2016. 8. 10. 그룹 추가 시 주석 풀기
//        titles.add("그룹");

        adapter = new ViewPageAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPagerScrolledListener());
        // TODO: 2016. 8. 10. 그룹 추가 시 주석 풀기
//        viewPager.setOffscreenPageLimit(2); // 뷰 페이저 캐싱 페이지 개수  그룹 추가 시 주석 해제하기
        tabLayout.setupWithViewPager(viewPager);
    }

    //새로고침 버튼을 누르면 직원 데이터 불러옴
    ImageView.OnClickListener refreshClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            GlobalApplication.getInstance().showDlgProgress();
            getDivisionData();
            addressFragment.refreshing();
        }
    };

    ImageView.OnClickListener logoutClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            pref.putString("LOGIN_YN", "N");
            Intent Intent = new Intent(getApplicationContext(), LoginActivity.class);
            Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(Intent);
            finish();
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
            try {
                emplList = (ArrayList<UserInfo>) msg.getData().getSerializable("EmplThread");
                //직원 데이터 pref 저장
                pref.putEmplList(emplList); // pref에 저장
            }catch (Exception e){Log.e(TAG,"데이터 가져오기 실패 :"+e.getMessage());}
            GlobalApplication.getInstance().dismissDlgProgress();
            addressFragment.setData(emplList);
            GlobalApplication.getInstance().setInitialData(emplList); // 글로벌 어플리케이션에 초기값 저장
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
            divisionList = (ArrayList<HighDivision>) msg.getData().getSerializable("DivisionThread");
            pref.putDivisionList(divisionList);
            stopRefresh();
            GlobalApplication.getInstance().dismissDlgProgress();
            divisionFragment.setData(divisionList);
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
     * 오류 발생 시 새로고침 멈춤
     */
    public void stopRefresh(){
        addressFragment.stopRefresh();
        // TODO: 2016. 8. 10. 그룹 추가 시 주석 풀기
//        groupFragment.stopRefresh();
        divisionFragment.stopRefresh();
    }

    public class ViewPagerScrolledListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {
            if(position == 1){ // 부서로 스크롤 시 키보드 내리고 검색 초기화
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addressFragment.getSearch_et().getWindowToken(), 0);
                addressFragment.getSearch_et().clearFocus();
                addressFragment.getSearch_et().setText("");
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {}
    }

    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"'뒤로'버튼을 한번더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }
}
