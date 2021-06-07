package com.matiascohen.flickrclient.service.base;

import com.matiascohen.flickrclient.exceptions.BusinessException;
import com.matiascohen.flickrclient.exceptions.ConnectionException;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class GetRequest<T extends Response> extends HttpRequest<T> {

    public GetRequest(Class<T> clazz) {
        super(clazz);
    }

    public T execute() throws Exception {
        String urlPath = getServiceUrl();

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urlPath);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);

            InputStream in;

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                in = new BufferedInputStream(urlConnection.getInputStream());
            else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_PRECON_FAILED)
                in = new BufferedInputStream(urlConnection.getErrorStream());
            else
                throw new ConnectionException();

            if (!urlConnection.getHeaderField("Content-Type").equalsIgnoreCase("application/json")) {
                throw new ConnectionException();
            }

            HttpResponse<T> response = new HttpResponse<T>(in, this);
            urlConnection.disconnect();
            this.result = response.getResult();

        } catch (BusinessException e) {
            throw e;
        } catch (Throwable e) {
            throw new ConnectionException();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }
}
