package com.ind.kcstation.showgirls;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.ind.kcstation.showgirls.utils.ImageAdapter;
import com.ind.kcstation.showgirls.utils.MPageAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private WebView webViewMain;

//    private GridView mGridView;
//
//    private ImageAdapter mImageAdapter;

    private TabLayout mTabLayout;

    private MPageAdapter mPageAdapter;

    private RelativeLayout container;
//    private FileUtils fileUtils;

/*    private Handler refeshGridview = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //重新刷新gridview区域
            mImageAdapter = new ImageAdapter(mGridView.getContext(), mGridView, this);
            mGridView.setAdapter(mImageAdapter);
            mImageAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };*/

    //private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
       /* webViewMain = (WebView)findViewById(R.id.webv_main);
        if (Build.VERSION.SDK_INT >= 19) {
            webViewMain.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webViewMain.getSettings().setJavaScriptEnabled(true);
        webViewMain.setWebViewClient(new DIYWebViewClient());
        webViewMain.loadUrl("http://ck.lchbl.com:3000/show");*/
//        fileUtils = new FileUtils(this);
        mTabLayout = (TabLayout) findViewById(R.id.tabs_main);
        container = (RelativeLayout) findViewById(R.id.main_client);
        mPageAdapter = new MPageAdapter(this.getSupportFragmentManager());

        this.initTabLayout(mTabLayout);
        initFragmentView(0);
//        mGridView = (GridView) findViewById(R.id.gv_img_main);
//        mImageAdapter = new ImageAdapter(this, mGridView, refeshGridview);
//        mGridView.setAdapter(mImageAdapter);
        //===---==
//        mGridView.setOnScrollListener(
//                new AbsListView.OnScrollListener() {
//                      @Override
//                      public void onScrollStateChanged(AbsListView view, int scrollState) {
//                          if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                              if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                                  //loadMoreData();
//                                  view.addView(new NxtPage(view.getContext()),view.getCount() - 1);
//                              }
//                          }
//                      }
//
//                      @Override
//                      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                      }
//                  }
//        );

//        mImageAdapter.notifyDataSetChanged();
        //====other func
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "AAA action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

//        mImageAdapter.appendResource();
    }

    private void initTabLayout(final TabLayout mTabLayout){
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText("inspect gurls"),0,true);//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText("find gurls"),1,false);
//        mPageAdapter = new MPageAdapter()
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                effectFrag(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                effectFrag(tab);
            }

            private void effectFrag(TabLayout.Tab tab){
                int position = tab.getPosition();
                initFragmentView(position);
            }
        });
    }

    /**
     * 加载指定fragment
     * @param position
     */
    private void initFragmentView(int position){
        Log.i("frag","position in:"+position);
        Fragment fragment = (Fragment) mPageAdapter.instantiateItem(container,position);
        mPageAdapter.setPrimaryItem(container,position,fragment);
        mPageAdapter.finishUpdate(container);
    }

    private class DIYWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onDestroy() {
//        mImageAdapter.cancelTask();
        super.onDestroy();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
