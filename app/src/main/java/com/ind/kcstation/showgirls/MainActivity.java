package com.ind.kcstation.showgirls;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.ind.kcstation.showgirls.http.HttpFuncion;
import com.ind.kcstation.showgirls.http.HttpUtils;
import com.ind.kcstation.showgirls.utils.FileUtils;
import com.ind.kcstation.showgirls.utils.ImageAdapter;
import com.ind.kcstation.showgirls.view.NxtPage;
import com.squareup.okhttp.Response;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private WebView webViewMain;

    private GridView mGridView;
    //http://ck.lchbl.com:3000/item/list/p/1

    private ImageAdapter mImageAdapter;
//    private FileUtils fileUtils;

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

    //private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
       /* webViewMain = (WebView)findViewById(R.id.webv_main);
        if (Build.VERSION.SDK_INT >= 19) {
            webViewMain.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webViewMain.getSettings().setJavaScriptEnabled(true);
        webViewMain.setWebViewClient(new DIYWebViewClient());
        webViewMain.loadUrl("http://ck.lchbl.com:3000/show");*/
//        fileUtils = new FileUtils(this);
        mGridView = (GridView) findViewById(R.id.gv_img_main);
        mImageAdapter = new ImageAdapter(this, mGridView, refeshGridview);
        mGridView.setAdapter(mImageAdapter);
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


/*
        HttpUtils hu = HttpUtils.getInstance(new HttpFuncion() {
            @Override
            public Object doWork(Response response, Context _context) {
                Looper.prepare();
                Message msg = new Message();
                Bundle bd = new Bundle();
                bd.putCharSequence("key",response.body().toString());
                msg.setData(bd);
                context = _context;
                Toast.makeText(context,response.body().toString()+"7777777",Toast.LENGTH_SHORT);
                imgHandler.sendMessage(msg);

                Looper.loop();
                return null;
            }
        },this);
        hu.getHttp("http://ck.lchbl.com:3000/item/list/p/1");*/
        mImageAdapter.notifyDataSetChanged();
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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        mImageAdapter.cancelTask();
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
