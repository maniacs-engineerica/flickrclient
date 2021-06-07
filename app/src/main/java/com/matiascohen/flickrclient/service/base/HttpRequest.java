package com.matiascohen.flickrclient.service.base;

import com.matiascohen.flickrclient.BuildConfig;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequest<T extends Response> {

    protected Class<T> clazz;
    protected T result;

    public HttpRequest(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected abstract String getMethod();

    public abstract T execute() throws Exception;

    public T getResult() {
        return result;
    }

    protected String getServiceUrl() {
        StringBuilder builder = new StringBuilder(String.format("%s?method=%s", getServerUrl(), getMethod()));
        HashMap<String, String> parameters = getParameters();
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            builder.append(String.format("&%s=%s", parameter.getKey(), parameter.getValue()));
        }
        return builder.toString();
    }

    protected String getServerUrl() {
        return BuildConfig.API_URL;
    }

    protected HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<>();
        params.put("api_key", BuildConfig.API_KEY);
        params.put("extras", "url_s");
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        return params;
    }
}
