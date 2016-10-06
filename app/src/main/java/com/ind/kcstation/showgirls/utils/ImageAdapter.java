package com.ind.kcstation.showgirls.utils;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.ind.kcstation.showgirls.R;
import com.ind.kcstation.showgirls.loader.ImageLoader;
import com.ind.kcstation.showgirls.view.NxtPage;

/**
 * Created by KCSTATION on 2016/9/18.
 */
public class ImageAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    /**
     * 上下文对象的引用
     */
    private Context context;

    /**
     * Image Url的数组
     */
    private String [] imageThumbUrls;

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


    public ImageAdapter(Context context, GridView mGridView, String [] imageThumbUrls){
        this.context = context;
        this.mGridView = mGridView;
        this.imageThumbUrls = imageThumbUrls;
        mImageDownLoader = new ImageLoader(context);
        mGridView.setOnScrollListener(this);
        //ll
        this.inflater = LayoutInflater.from(context);
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


    LayoutInflater inflater;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //RecyclerView.ViewHolder holder;
        EndViewHolder endHolder;
        //ViewHolder holder;
        Log.i("ERR","A:"+position+",and:"+(this.getCount()-1));
        if (position == this.getCount()-1 ){
            //如果为最后一项
//            if (convertView == null) {
                endHolder = new EndViewHolder();
                convertView = inflater.inflate(R.layout.bottom_nxt_pg, parent, false);
                convertView.setTag(endHolder);
                endHolder.iv = (NxtPage) convertView.findViewById(R.id.nxt_pg);
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
            if(convertView == null){
                mImageView = new ImageView(context);
            }else{
                mImageView = (ImageView) convertView;
            }
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

    /**
     * 取消下载任务
     */
    public void cancelTask(){
        mImageDownLoader.cancelTask();
    }
}
