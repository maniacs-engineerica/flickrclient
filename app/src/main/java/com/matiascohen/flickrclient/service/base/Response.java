package com.matiascohen.flickrclient.service.base;

import android.util.Log;

import com.matiascohen.flickrclient.exceptions.BusinessException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Response<T extends HttpRequest> {

    private T request;

    public Response(@NotNull JSONObject json, @NotNull T request) throws BusinessException {
        Log.i(this.getClass().getName(), json.toString());

        this.request = request;
        boolean success = json.optBoolean("success");
        if (success) return;

        String status = json.optString("stat");
        String message = json.optString("message");

        if (status.equals("ok")) return;

        throw new BusinessException(message);
    }

    public T getRequest() {
        return request;
    }
}
