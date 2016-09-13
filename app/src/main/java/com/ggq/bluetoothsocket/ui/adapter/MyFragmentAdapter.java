package com.ggq.bluetoothsocket.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;


import java.util.List;

/**
 * Created by DYK on 2015/11/14.
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter{

    public List<Fragment> mFragmentList;

    public MyFragmentAdapter(FragmentManager fm,List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList=mFragmentList;
    }


    @Override
    public int getCount() {
        return mFragmentList==null?0:mFragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((Fragment) object).getView();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


}
