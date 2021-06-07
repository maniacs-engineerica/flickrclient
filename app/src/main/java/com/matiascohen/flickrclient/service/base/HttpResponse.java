package com.matiascohen.flickrclient.service.base;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class HttpResponse<T extends Response> {

    private T result;

    public HttpResponse(InputStream in, HttpRequest<T> request) throws Throwable {
        try {
            String jsonString = parseResponse(in);
            if (!TextUtils.isEmpty(jsonString)) {
                JSONObject json = new JSONObject(jsonString);
                try {
                    result = request.clazz.getConstructor(JSONObject.class, request.getClass()).newInstance(json, request);
                } catch (NoSuchMethodException e) {
                    result = request.clazz.getConstructor(JSONObject.class).newInstance(json);
                }
            }
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public static String parseResponse(InputStream in) throws IOException {

        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder(in.available());
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        } finally {
            r.close();
            in.close();
        }
    }

    public T getResult() {
        return result;
    }

}
