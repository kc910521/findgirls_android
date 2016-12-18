package com.ind.kcstation.showgirls.http;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KCSTATION on 2016/9/19.
 */
public class HttpUtils {

    private OkHttpClient okHttpClient = null;

    private HttpFuncion httpFuncion = null;

    private Context context = null;

    private HttpUtils(HttpFuncion httpFuncion,Context context){
        //this.okHttpClient = new OkHttpClient();
        //that may cause no response in function call.enqueue(new Callback())
        this.httpFuncion = httpFuncion;
        this.context = context;
    }

    //private static HttpUtils httpUtils = null;
    //need a better method to refine
    public static HttpUtils getInstance(HttpFuncion httpFuncion,Context context){
        //if (HttpUtils.httpUtils == null){
            //HttpUtils.httpUtils = new HttpUtils(httpFuncion,context);
        //}
        //this part should establish a hashtable to direct key of url with value
        return new HttpUtils(httpFuncion,context);
    }

    public void getHttp(String tarUrl){
        Request request = new Request.Builder()
                .url(tarUrl)
                .build();
        Log.i("check4","tarUrl:"+tarUrl);
        Call call  = new OkHttpClient().newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("check4","fail in:"+e.toString());
            }
            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("check4","response.isSuccessful():"+response.isSuccessful()+",");
                if(response.isSuccessful()){
                    //The call was successful. print it to the log
                    //Log.v("OKHttp",response.body().string());
                    httpFuncion.doWork(response, context);
                }
            }
        });

    }


    public static String convertStreamToString(InputStream is) {

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
        return replaceBlank(sb.toString()).replaceAll("/n","");

    }

    private static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}
