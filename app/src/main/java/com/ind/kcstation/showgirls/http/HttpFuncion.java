package com.ind.kcstation.showgirls.http;

import android.content.Context;

import com.squareup.okhttp.Response;

/**
 * Created by KCSTATION on 2016/9/20.
 */
public interface HttpFuncion {
    /**
     * internet operations
     * @param response
     * @param _context
     * @return
     */
    Object doWork(Response response, Context _context);

}
