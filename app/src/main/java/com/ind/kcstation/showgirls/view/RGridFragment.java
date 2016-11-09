package com.ind.kcstation.showgirls.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ind.kcstation.showgirls.R;

/**
 * Created by KCSTATION on 2016/11/6.
 */
public class RGridFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.content_main_2,container,false);
        Log.i("frag","gf inir");
        return vw;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null){
            this.getView().setVisibility(menuVisible? View.VISIBLE:View.GONE);
        }
    }
}
