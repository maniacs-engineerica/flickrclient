package com.matiascohen.flickrclient.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.request.RequestOptions
import com.matiascohen.flickrclient.R
import com.matiascohen.flickrclient.model.FlickrPhoto
import com.matiascohen.flickrclient.utils.GlideApp
import kotlin.properties.Delegates

class PhotoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ViewTreeObserver.OnGlobalLayoutListener {

    @BindView(R.id.image)
    lateinit var imageView: ImageView

    @BindView(R.id.title)
    lateinit var titleView: TextView

    @BindView(R.id.highlight)
    lateinit var highlightView: View

    var photo: FlickrPhoto? by Delegates.observable<FlickrPhoto?>(null) { _, _, _ ->
        reload()
    }

    init {
        inflate(context, R.layout.photo_view, this)
        ButterKnife.bind(this)
    }

    private fun reload(){
        titleView.text = photo?.title

        photo?.let {
            checkSize(it)
        } ?: run {
            imageView.setImageBitmap(null)
        }
    }

    private fun checkSize(photo: FlickrPhoto){
        if (width > 0) {
            resizeView(photo.width, photo.height)
        } else {
            titleView.visibility = INVISIBLE
            viewTreeObserver.addOnGlobalLayoutListener(this)
        }
    }

    private fun resizeView(width: Int, height: Int){
        titleView.visibility = VISIBLE

        val lp = layoutParams
        lp.height =  this.width * height / width
        layoutParams = lp

        GlideApp.with(context).load(photo?.url).apply(RequestOptions().override(this.width, lp.height)).into(imageView)
    }

    override fun onGlobalLayout() {
        viewTreeObserver.removeOnGlobalLayoutListener(this)

        photo?.let {
            resizeView(it.width, it.height)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        highlightView.setOnClickListener(l)
    }
}