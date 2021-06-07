package com.matiascohen.flickrclient.service.base;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

public class Requester<T extends Response> {

    private HttpRequest<T> request;
    private RequestListener<T> listener;
    private Exception e;

    public Requester(HttpRequest<T> request) {
        this(request, null);
    }

    public Requester(HttpRequest<T> request, RequestListener<T> listener) {
        this.request = request;
        this.listener = listener;
    }

    public void execute() {
        Task task = new Task();
        task.execute();
    }

    public interface RequestListener<T extends Response> {
        void onRequestStart();

        void onRequestFinish(@NonNull T result);

        void onRequestFailed(@NonNull String message);
    }

    private class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            if (listener != null)
                listener.onRequestStart();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                request.execute();
            } catch (Exception e) {
                Requester.this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (listener == null) return;

            if (e != null) {
                try {
                    throw e;
                } catch (Exception e) {
                    listener.onRequestFailed(e.getMessage());
                }
                return;
            }
            listener.onRequestFinish(request.getResult());
        }

    }
}
