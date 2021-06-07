package com.matiascohen.flickrclient.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Menu
import android.view.View.GONE
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.matiascohen.flickrclient.R
import com.matiascohen.flickrclient.extensions.urlForSize
import com.matiascohen.flickrclient.model.FlickrPhoto
import com.matiascohen.flickrclient.views.PhotosView

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    @BindView(R.id.photos)
    lateinit var photosView: PhotosView

    @BindView(R.id.start)
    lateinit var startView: Button

    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setupPhotosView()
    }

    private fun setupPhotosView() {
        photosView.onPhotoClickCallback = {
            openFullscreen(it)
        }
    }

    private fun openFullscreen(photo: FlickrPhoto) {
        val intent = Intent(this, FullscreenActivity::class.java)
        intent.putExtra(FullscreenActivity.PHOTO_URL, photo.urlForSize("b"))
        startActivity(intent)
    }

    @OnClick(R.id.start)
    fun onStartClicked(){
        startView.visibility = GONE
        searchView.isIconified = false
        searchView.requestFocusFromTouch()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        val search = menu!!.findItem(R.id.action_search)
        searchView = search.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        scheduleSearch()
        return true
    }

    private fun scheduleSearch() {
        searchHandler.removeCallbacks(search)
        searchHandler.postDelayed(search, 500)
    }

    private val searchHandler = Handler()
    private val search: Runnable = Runnable {
        startView.visibility = GONE
        photosView.text = searchView.query.toString()
    }
}
