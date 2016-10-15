package com.ind.kcstation.showgirls.http;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by KCSTATION on 2016/9/19.
 */
public class HttpUtils {

    private OkHttpClient okHttpClient = null;

    private HttpFuncion httpFuncion = null;

    private Context context = null;

    private HttpUtils(HttpFuncion httpFuncion,Context context){
        this.okHttpClient = new OkHttpClient();
        this.httpFuncion = httpFuncion;
        this.context = context;
    }

    private static HttpUtils httpUtils = null;

    public static HttpUtils getInstance(HttpFuncion httpFuncion,Context context){
        if (HttpUtils.httpUtils == null){
            HttpUtils.httpUtils = new HttpUtils(httpFuncion,context);
        }
        return HttpUtils.httpUtils;
    }

    public void getHttp(String tarUrl){
        Request request = new Request.Builder()
                .url(tarUrl)
                .build();
        Call call  = okHttpClient.newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("OKHttp","fail in:"+e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                try {
//
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }
                if(response.isSuccessful()){
                    //The call was successful. print it to the log
                    //Log.v("OKHttp",response.body().string());
                    httpFuncion.doWork(response, context);
                }
            }
        });

    }


}
