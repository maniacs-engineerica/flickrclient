package com.matiascohen.flickrclient.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.snackbar.Snackbar
import com.matiascohen.flickrclient.R
import com.matiascohen.flickrclient.model.FlickrPhoto
import com.matiascohen.flickrclient.service.FlickrPhotosSearch
import com.matiascohen.flickrclient.service.base.Requester
import com.paginate.Paginate
import kotlin.properties.Delegates

class PhotosView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShimmerRecyclerView(context, attrs, defStyleAttr), Requester.RequestListener<FlickrPhotosSearch.FlickrPhotosSearchResponse> {

    private var photos = mutableListOf<FlickrPhoto>()

    private var photoAdapter = PhotoAdapter()

    private var page: Int = 1
    private var pages: Int = -1
    private lateinit var paginate: Paginate

    private var requesting = false

    var onPhotoClickCallback: ((FlickrPhoto) -> (Unit))? = null

    var text: String? by Delegates.observable<String?>(null) { _, _, _ ->
        restartViewData()
    }

    init {
        setupView()
        restartViewData()
    }

    private fun setupView() {
        setDemoShimmerAngle(30)
        setDemoShimmerDuration(1000)
        setDemoChildCount(20)
        setDemoShimmerColor(Color.parseColor("#73FFFFFF"))

        layoutManager = LinearLayoutManager(context)
        adapter = photoAdapter

        paginate = Paginate.with(this, PaginateCallback())
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(true)
                .build()
    }

    private fun restartViewData() {
        page = 1
        pages = -1
        photos.clear()
        adapter?.notifyDataSetChanged()
        paginate.setHasMoreDataToLoad(false)

        if (!text.isNullOrEmpty()){
            requestPhotos()
        }
    }

    private fun requestMorePhotos(){
        page++
        requestPhotos()
    }

    private fun requestPhotos() {
        Requester(FlickrPhotosSearch(text, page), this).execute()
    }

    override fun onRequestStart() {
        requesting = true
        if (page == 1){
            showShimmerAdapter()
        }
    }

    override fun onRequestFinish(result: FlickrPhotosSearch.FlickrPhotosSearchResponse) {
        requesting = false

        val prevSize = photos.size

        photos.addAll(result.photos)
        pages = result.pages

        if (page == 1) {
            hideShimmerAdapter()
            adapter?.notifyDataSetChanged()
        } else {
            adapter?.notifyItemRangeInserted(prevSize, result.photos.size)
        }

        paginate.setHasMoreDataToLoad(page < pages)
    }

    override fun onRequestFailed(message: String) {
        requesting = false
        if (page == 1){
            hideShimmerAdapter()
        }
        Snackbar.make(this, message, 3000).show()
    }

    inner class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            @BindView(R.id.photo)
            lateinit var photoView: PhotoView

            init {
                ButterKnife.bind(this, view)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vh_photo, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val photo = photos[position]
            holder.photoView.photo = photo

            onPhotoClickCallback?.let {
                holder.photoView.setOnClickListener { it(photo) }
            }
        }

        override fun getItemCount(): Int {
            return photos.size
        }

    }

    inner class PaginateCallback: Paginate.Callbacks {
        override fun onLoadMore() {
            requestMorePhotos()
        }

        override fun isLoading(): Boolean {
            return requesting
        }

        override fun hasLoadedAllItems(): Boolean {
            return page >= pages
        }

    }

    override fun onDetachedFromWindow() {
        paginate.unbind()
        super.onDetachedFromWindow()
    }

}