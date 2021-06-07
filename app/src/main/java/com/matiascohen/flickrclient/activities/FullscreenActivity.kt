package com.matiascohen.flickrclient.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.matiascohen.flickrclient.R
import com.matiascohen.flickrclient.utils.GlideApp
import kotlin.math.max
import kotlin.math.min


class FullscreenActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_URL = "PHOTO_URL"
    }

    @BindView(R.id.image)
    lateinit var imageView: ImageView

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen)
        ButterKnife.bind(this)

        intent.getStringExtra(PHOTO_URL)?.let {
            loadPhoto(it)
        }

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }

    private fun loadPhoto(url: String) {
        GlideApp.with(this)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(imageView)
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        scaleGestureDetector?.onTouchEvent(motionEvent)
        return true
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(1f, min(scaleFactor, 10.0f))
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }

}