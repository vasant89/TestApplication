package com.test.testapplication.main.photos.photofullscreen

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.squareup.picasso.Picasso
import com.test.testapplication.R
import com.test.testapplication.adapters.PhotoFullScreenAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_fullscreen.*
import javax.inject.Inject
import com.test.testapplication.extentions.copyText



class FullscreenActivity : DaggerAppCompatActivity() {
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {

    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    @Inject
    lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        val bundle = intent.extras

        bundle?.let {
            val index = bundle.getInt("index", 0)
            val photos = bundle.getStringArrayList("photos") as ArrayList<String>

            vp_photo.adapter = PhotoFullScreenAdapter(picasso, photos) { photoUrl ->
                this.copyText(photoUrl, "Url copied successfully")
            }
            vp_photo.currentItem = index
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    private fun hide() {
        supportActionBar?.hide()
        mVisible = false

        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        mVisible = true

        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }


    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {

        private val AUTO_HIDE = true

        private val AUTO_HIDE_DELAY_MILLIS = 3000

        private val UI_ANIMATION_DELAY = 300
    }
}
