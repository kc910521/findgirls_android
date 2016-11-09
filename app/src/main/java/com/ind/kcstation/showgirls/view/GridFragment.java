package com.ind.kcstation.showgirls.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ind.kcstation.showgirls.R;
import com.ind.kcstation.showgirls.utils.ImageAdapter;


/**
 * Created by KCSTATION on 2016/11/6.
 */
public class GridFragment extends Fragment {

    private GridView mGridView;

    private ImageAdapter mImageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.content_main,container,false);
        this.initView(vw);
        return vw;
    }

    private void initView(View view){
        Log.i("init","init view");
        mGridView = (GridView) view.findViewById(R.id.gv_img_main);
        mImageAdapter = new ImageAdapter(view.getContext(), mGridView, refeshGridview);
        mGridView.setAdapter(mImageAdapter);
        mImageAdapter.notifyDataSetChanged();
        mImageAdapter.appendResource();
    }

    @Override
    public void onDestroy() {
        mImageAdapter.cancelTask();
        super.onDestroy();
    }

    private Handler refeshGridview = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //重新刷新gridview区域
            mImageAdapter = new ImageAdapter(mGridView.getContext(), mGridView, this);
            mGridView.setAdapter(mImageAdapter);
            mImageAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null){
            this.getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
        }
    }
}
