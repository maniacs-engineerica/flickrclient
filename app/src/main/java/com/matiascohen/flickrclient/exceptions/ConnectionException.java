package com.matiascohen.flickrclient.exceptions;

import com.matiascohen.flickrclient.FlickrClientApplication;
import com.matiascohen.flickrclient.R;

public class ConnectionException extends Exception {

    public ConnectionException() {
        super(FlickrClientApplication.getInstance().getString(R.string.connection_exception));
    }

}
