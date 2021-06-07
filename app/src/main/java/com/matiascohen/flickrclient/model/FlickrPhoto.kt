package com.matiascohen.flickrclient.model

import org.json.JSONObject

class FlickrPhoto(json: JSONObject) {

    val id: String
    val title: String
    val url: String
    val width: Int
    val height: Int
    val server: String
    val secret: String

    init {
        id = json.getString("id")
        title = json.getString("title")
        url = json.getString("url_s")
        width = json.getInt("width_s")
        height = json.getInt("height_s")
        server = json.getString("server")
        secret = json.getString("secret")
    }

}