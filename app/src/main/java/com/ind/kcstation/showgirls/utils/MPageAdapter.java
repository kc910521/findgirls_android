package com.ind.kcstation.showgirls.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ind.kcstation.showgirls.view.GridFragment;
import com.ind.kcstation.showgirls.view.RGridFragment;

/**
 * Created by KCSTATION on 2016/11/6.
 */
public class MPageAdapter extends FragmentPagerAdapter {

    public MPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new RGridFragment();
            default:
                return new GridFragment();
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
