package com.ggq.bluetoothsocket.ui;

import android.bluetooth.BluetoothAdapter;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;

import android.widget.Toast;
import com.ggq.bluetoothsocket.R;
import com.ggq.bluetoothsocket.util.UtilPool;
import com.ggq.bluetoothsocket.ui.adapter.MyFragmentAdapter;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

import java.util.List;



public class MainActivity extends FragmentActivity {


    private DrawerLayout mDrawerLayout;
    private BluetoothAdapter bluetoothAdapter;//主要玩的就是这个
    private Boolean isBluetoothOpen = false;
    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;
    private List<Fragment> mFragmentList;

    protected static final int SUCCESS_CONNECT = 0;
    protected static final int SUCCESS_ACCEPT = 1;
    private UtilPool utilPool;


    private static final int RequestCode = 1234;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {


                case SUCCESS_CONNECT:
                    Toast.makeText(getApplicationContext(), "链接成功", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS_ACCEPT:
                    Toast.makeText(getApplicationContext(), "收到请求", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initEvents();
        if (isBluetoothOpen && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.startDiscovery();
        } else {
            Toast.makeText(MainActivity.this, "请打开蓝牙", Toast.LENGTH_LONG).show();
        }

    }

    private void init() {
        mFragmentManager= getSupportFragmentManager();
        mFragmentList=new ArrayList<Fragment>();
        mFragmentList.add(new FragmentDeviceList());
        mFragmentList.add(new FragmentChat());
        mViewPager=(ViewPager)findViewById(R.id.viewpager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        utilPool = new UtilPool(BluetoothAdapter.getDefaultAdapter(), mHandler);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {
            isBluetoothOpen = true;
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, RequestCode);
        }
mViewPager.setAdapter(new MyFragmentAdapter(mFragmentManager,mFragmentList));
    }

    private void initEvents() {
        mDrawerLayout.setDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                float leftScale = 1 - 0.3f * scale;
                ViewHelper.setScaleX(mMenu, leftScale);
                ViewHelper.setScaleY(mMenu, leftScale);
                ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                ViewHelper.setPivotX(mContent, 0);
                ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                mContent.invalidate();
                ViewHelper.setScaleX(mContent, rightScale);
                ViewHelper.setScaleY(mContent, rightScale);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode) {
            isBluetoothOpen = true;
        }
    }
}
