package com.matiascohen.flickrclient.service

import com.matiascohen.flickrclient.extensions.iterator
import com.matiascohen.flickrclient.model.FlickrPhoto
import com.matiascohen.flickrclient.service.base.GetRequest
import com.matiascohen.flickrclient.service.base.Response
import org.json.JSONObject
import java.util.HashMap

class FlickrPhotosSearch(private val text: String?, private val page: Int = 1) : GetRequest<FlickrPhotosSearch.FlickrPhotosSearchResponse>(FlickrPhotosSearchResponse::class.java){

    override fun getMethod(): String {
        return "flickr.photos.search"
    }

    override fun getParameters(): HashMap<String, String> {
        val params = super.getParameters()
        params["text"] = text.orEmpty()
        params["media"] = "photos"
        params["page"] = page.toString()
        return params
    }

    class FlickrPhotosSearchResponse(json: JSONObject, request: FlickrPhotosSearch) : Response<FlickrPhotosSearch>(json, request) {

        val photos: List<FlickrPhoto>
        val pages: Int

        init {
            val photos = json.getJSONObject("photos")
            this.pages = photos.getInt("pages")
            val photo = photos.getJSONArray("photo")
            this.photos = photo.iterator<JSONObject>().map { FlickrPhoto(it) }
        }
    }

}