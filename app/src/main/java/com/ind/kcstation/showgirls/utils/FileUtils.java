package com.ind.kcstation.showgirls.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by KCSTATION on 2016/9/18.
 */
public class FileUtils {
    /**
     * sd卡的根目录
     */
    private static String mSdRootPath = null;
    /**
     * 手机的缓存根目录
     */
    private static String mDataRootPath = null;
    /**
     * 保存Image的目录名
     */
    private final static String FOLDER_NAME = "/AndroidImage";


    public FileUtils(Context context){
        mDataRootPath = context.getCacheDir().getPath();
    }


    /**
     * 获取储存Image的目录
     * @return
     */
    private String getStorageDirectory(){
        Log.v("OKHttp",Environment.getExternalStorageState()+" ! ");
        if (mDataRootPath != null){
            return mDataRootPath;
        }else if (mSdRootPath != null){
            return mSdRootPath;
        }else{
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                mSdRootPath = sdCardFile.getAbsolutePath()+""+FOLDER_NAME;
                return mSdRootPath;
            }else{
                File iStFile = new File(Environment.getDataDirectory().getAbsolutePath());
                mDataRootPath = iStFile.getAbsolutePath()+""+FOLDER_NAME;
                return mDataRootPath;
            }
        }

    }

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public void savaBitmap(String fileName, Bitmap bitmap) throws IOException {
        if(bitmap == null){
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }
        File file = null;
        //makeRootDirectory(path + File.separator);
        file = new File(path + File.separator + fileName);
        if (!file.exists()){
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
           //0 bitmap.recycle();
        }

    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从手机或者sd卡获取Bitmap
     * @param fileName
     * @return
     */
    public Bitmap getBitmap(String fileName){
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
    }

    /**
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    public boolean isFileExists(String fileName){
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    /**
     * 获取文件的大小
     * @param fileName
     * @return
     */
    public long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }


    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public void deleteFile() {
        File dirFile = new File(getStorageDirectory());
        if(! dirFile.exists()){
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }

        dirFile.delete();
    }


}
