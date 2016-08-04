package com.kosign.bizaddress.main;

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
import com.kosign.bizaddress.api.retrofit.EmplThread;
import com.kosign.bizaddress.model.UserInfo;
import com.kosign.bizaddress.util.ViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeongpil on 2016. 8. 4..
 */
public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";
    private Context mContext;
    private ImageView menu;
    private ViewPageAdapter adapter;
    private ViewPager viewPager;
    private AddressFragment addressFragment;
    private DivisionFragment divisionFragment;
    private ArrayList<UserInfo> userdata;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        init();
        getEmplData();
        setViewpager();

    }

    private void init(){
        mContext = MainActivity.this;
        menu = (ImageView)findViewById(R.id.iv_title4_left);
        menu.setOnClickListener(menuClickListener);
    }

    private void getEmplData(){
        Handler EmplHandler = new EmplDataReceiveHandler();
        Thread emplThread = new EmplThread(EmplHandler, MainActivity.this);
        emplThread.start();
    }

    private void setViewpager(){
        addressFragment = new AddressFragment();
        divisionFragment = new DivisionFragment();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(addressFragment);
        fragments.add(divisionFragment);
        List<String> titles = new ArrayList<String>();
        titles.add("연락처");
        titles.add("부서");

        adapter = new ViewPageAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    ImageView.OnClickListener menuClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Log.e(TAG,"메뉴 클릭");
        }
    };

    //EmplThread 에서 받아옴
    private class EmplDataReceiveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {userdata = (ArrayList<UserInfo>) msg.getData().getSerializable("EmplThread");
            }catch (Exception e){Log.e(TAG,"데이터 가져오기 실패 :"+e.getMessage());}


        }
    }
}
