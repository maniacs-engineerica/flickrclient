package com.matiascohen.flickrclient.extensions

import com.matiascohen.flickrclient.BuildConfig
import com.matiascohen.flickrclient.model.FlickrPhoto

fun FlickrPhoto.urlForSize(s: String): String? {
    return "${BuildConfig.API_URL_STATIC}${server}/${id}_${secret}_${s}.jpg"
}