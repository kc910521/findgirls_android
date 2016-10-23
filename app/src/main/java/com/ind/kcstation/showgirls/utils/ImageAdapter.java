package com.ind.kcstation.showgirls.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ind.kcstation.showgirls.R;
import com.ind.kcstation.showgirls.http.HttpFuncion;
import com.ind.kcstation.showgirls.http.HttpUtils;
import com.ind.kcstation.showgirls.loader.ImageLoader;
import com.ind.kcstation.showgirls.view.NxtPage;
import com.ind.kcstation.showgirls.vo.ImagerVO;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KCSTATION on 2016/9/18.
 *
 * q
 * 图片需要雅压缩
 */
public class ImageAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    /**
     * 上下文对象的引用
     */
    private Context context;

    /**
     * Image Url的数组
     */
    //private String [] imageThumbUrls;
    private Handler refeshGridview;
    /**
     * GridView对象的应用
     */
    private GridView mGridView;

    /**
     * Image 下载器
     */
    private ImageLoader mImageDownLoader;

    /**
     * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
     * 参考http://blog.csdn.net/guolin_blog/article/details/9526203#comments
     */
    private boolean isFirstEnter = true;

    /**
     * 一屏中第一个item的位置
     */
    private int mFirstVisibleItem;

    /**
     * 一屏中所有item的个数
     */
    private int mVisibleItemCount;

    public static String [] imageThumbUrls = {
            "http://pic1.win4000.com/wallpaper/3/512ec5a1c9d1e.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/2cf5e0fe9925bc3121ee29195edf8db1ca1370fb.jpg",
            "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000" +
                    "&sec=1474793859&di=d6741cae026e2dff3123e158dd8287af&src=http://i0.sinaimg.cn/gm/2014/1216/U4341P115DT20141216145353.jpg",
            "http://www.k73.com/up/allimg/120510/22-1205101003133P.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1703415741,703164552&fm=21&gp=0.jpg",
            "http://www.sywnm.com/imgall/obuwgmzopjug63thonxxkltdn5wq/image/380530a5639c114b086.jpg",
            "http://www.sywnm.com/imgall/obuwgmzopjug63thonxxkltdn5wq/image/380530a5639c114b086.jpg",
            "http://image.tianjimedia.com/uploadImages/2012/229/72LDDRUQ87SC.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1703415741,703164552&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1703415741,703164552&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1703415741,703164552&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1703415741,703164552&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1703415741,703164552&fm=21&gp=0.jpg",
            "http://image.tianjimedia.com/uploadImages/2012/229/72LDDRUQ87SC.jpg",
            "http://image.tianjimedia.com/uploadImages/2012/229/72LDDRUQ87SC.jpg"
    };

    public static int NOW_PAGE = 1;

    public ImageAdapter(Context context, GridView mGridView,Handler refeshGridview){
        this.context = context;
        this.mGridView = mGridView;
        //this.imageThumbUrls = imageThumbUrls;
        this.refeshGridview = refeshGridview;
        mImageDownLoader = new ImageLoader(context);
        mGridView.setOnScrollListener(this);
        //ll
        this.inflater = LayoutInflater.from(context);
    }
    HttpUtils hu = null;
    public synchronized void appendResource(){
//        String [] newUrls = new String[moreImageUrls.length+imageThumbUrls.length];
//        int aIdx = 0;
//        for (String url : this.imageThumbUrls){
//            newUrls[aIdx++] = url;
//        }
//        for (String url : moreImageUrls){
//            newUrls[aIdx++] = url;
//        }
//        this.imageThumbUrls = newUrls;
        Log.i("check4","in appendResource()");
        hu = HttpUtils.getInstance(new HttpFuncion() {
            @Override
            public Object doWork(Response response, Context _context) {
                Log.i("check4","in HttpUtils doWork");
                Looper.prepare();
                Log.i("check4","in HttpUtils Looper prepare");
                Message msg = new Message();
                Bundle bd = new Bundle();
               // Log.i("key3",response.body().toString());
                ResponseBody responseBody = response.body();
                Log.i("check4","in HttpUtils responseBody.contentLength():"+responseBody.contentLength());
                if (responseBody.contentLength() == 0){
                    Looper.loop();
                    return null;
                }
                InputStream isis = responseBody.byteStream();
                String imgSource = convertStreamToString(isis);

                imgSource = replaceBlank(imgSource);
                imgSource = imgSource.replaceAll("/n","");
                System.out.println("11:"+imgSource+"!!");
                List<ImagerVO> imgvos = JSONArray.parseArray(imgSource,ImagerVO.class);
                if (imgvos != null && !imgvos.isEmpty()){
                    imageThumbUrls = new String[imgvos.size()];
                    for (int idx = 0; idx < imgvos.size(); idx ++){
                        imageThumbUrls[idx] = imgvos.get(idx).getImg();
                    }
                }

                bd.putCharSequence("key",imgSource);
                try {
                    isis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bd.putCharSequence("key",imgSource);
                msg.setData(bd);
                context = _context;
                Toast.makeText(context,imgSource+"67777777",Toast.LENGTH_SHORT).show();
                refeshGridview.sendMessage(msg);

                Looper.loop();
                Log.i("check4","doWork over");
                return null;
            }
        },context);
        hu.getHttp("http://ck.lchbl.com:3000/item/list/p/"+(NOW_PAGE++));
        //ImageAdapter.imageThumbUrls = moreImageUrls;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            showImage(mFirstVisibleItem, mVisibleItemCount);
        }else{
            cancelTask();
        }

    }


    /**
     * GridView滚动的时候调用的方法，刚开始显示GridView也会调用此方法
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        // 因此在这里为首次进入程序开启下载任务。
        if(isFirstEnter && visibleItemCount > 0){
            showImage(mFirstVisibleItem, mVisibleItemCount);
            isFirstEnter = false;
        }
    }
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    @Override
    public int getCount() {
        // 后面多加一个尾巴，所以适配器长度得加一
        return imageThumbUrls.length+1;
    }

    @Override
    public Object getItem(int position) {
        Log.i("pic","pos:"+position);
        return imageThumbUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private int mCount = 0;

    LayoutInflater inflater;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //RecyclerView.ViewHolder holder;
        EndViewHolder endHolder;
        //ViewHolder holder;
        //优化反复出现的position==0
        if (position == 0) {
            mCount++;
        }
        else {
            mCount = 0;
        }
        if (mCount > 1)
        {
            Log.v("ERR", "<getView> drop !!!");
            return convertView;
        }
        //==========over
        Log.i("ERR","A:"+position+",and:"+(this.getCount()-1)+",convertView:"+convertView);
        if (position == this.getCount()-1 ){
            //如果为最后一项
//            if (convertView == null) {
                endHolder = new EndViewHolder();
                convertView = inflater.inflate(R.layout.bottom_nxt_pg, parent, false);
                convertView.setTag(endHolder);
                endHolder.iv = (NxtPage) convertView.findViewById(R.id.nxt_pg);
                endHolder.iv.setClickable(true);
                endHolder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"in working2",Toast.LENGTH_SHORT).show();
                        appendResource();
                    }
                });
            //需要修改此部分
//            } else {
//                Log.i("ERR","TAG::"+(convertView.getTag()));
//                endHolder = (EndViewHolder) convertView.getTag();
//            }
            return convertView;
            //endHolder.iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
            //endHolder.tv.setText("footer");
        }else{
            ImageView mImageView;
            final String mImageUrl = imageThumbUrls[position];
            //if(convertView == null){
                mImageView = new ImageView(context);
//            }else{
//                mImageView = (ImageView) convertView;
//            }
            DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
            int width = (int) (dm.widthPixels);

            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            //给ImageView设置Tag,这里已经是司空见惯了
            mImageView.setTag(mImageUrl);
            /*******************************去掉下面这几行试试是什么效果****************************/
            Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
            if(bitmap != null){
                int bHeight =(int) (bitmap.getHeight()*((float)width/(float)bitmap.getWidth()));
                Log.i("img","1height:"+bHeight+",bitmap.getHeight():"+bitmap.getHeight()+",bitmap.getWidth():"+bitmap.getWidth()+",width:"+width);
                mImageView.setLayoutParams(new GridView.LayoutParams(width, bHeight<1?200:bHeight ));
                mImageView.setImageBitmap(bitmap);

            }else{

                //mImageView.setLayoutParams(new GridView.LayoutParams(width, 200));
                mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_img));
            }
            /**********************************************************************************/
            return mImageView;
        }


    }

    class EndViewHolder {
        NxtPage iv;
    }

    /**
     * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
     * @param firstVisibleItem
     * @param visibleItemCount
     */
    private void showImage(int firstVisibleItem, int visibleItemCount){
        Bitmap bitmap = null;
        for(int i=firstVisibleItem; i<firstVisibleItem + visibleItemCount && i < imageThumbUrls.length; i++){
            String mImageUrl = imageThumbUrls[i];
            final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);
            if (mImageView == null){
                return;
            }
            bitmap = mImageDownLoader.downloadImage(mImageUrl, new ImageLoader.onImageLoaderListener() {
                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if(mImageView != null && bitmap != null){
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });
            //ck new release
            if(bitmap != null){
                DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
                int width = (int) (dm.widthPixels);
                int bHeight =(int) (bitmap.getHeight()*((float)width/(float)bitmap.getWidth()));
                Log.i("img","2height:"+bHeight+",bitmap.getHeight():"+bitmap.getHeight()+",bitmap.getWidth():"+bitmap.getWidth()+",width:"+width);
                mImageView.setLayoutParams(new GridView.LayoutParams(width, bHeight<1?200:bHeight ));
                mImageView.setImageBitmap(bitmap);
                //mImageView.setImageBitmap(bitmap);
            }else{
                mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_img));
            }
        }
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();



        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line + "/n");

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return sb.toString();

    }

    /**
     * 取消下载任务
     */
    public void cancelTask(){
        mImageDownLoader.cancelTask();
    }
}
