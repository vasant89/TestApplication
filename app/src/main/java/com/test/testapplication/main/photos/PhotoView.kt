package com.test.testapplication.main.photos

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import com.test.testapplication.databinding.PhotoViewBinding
import com.test.testapplication.di.scope.ActivityScoped
import com.test.testapplication.main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import android.widget.GridView
import android.util.TypedValue
import com.squareup.picasso.Picasso
import com.test.testapplication.adapters.PhotoGrAdapter
import java.util.*
import com.test.testapplication.extentions.getScreenWidth
import com.test.testapplication.main.photos.photofullscreen.FullscreenActivity


@ActivityScoped
class PhotoView
@Inject
constructor() : DaggerFragment() {

    lateinit var mBinding: PhotoViewBinding

    @Inject
    lateinit var kProgressHUD: KProgressHUD

    @Inject
    lateinit var picasso: Picasso

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = PhotoViewBinding.inflate(inflater, container, false).apply {
            this.viewModel = (activity as MainActivity).obtainPhotoViewModel()
        }

        initGridLayout()

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.viewModel?.apply {

            start()

            isDataLoading.observe(this@PhotoView, Observer {
                when (it) {
                    true -> {
                        kProgressHUD.show()
                    }
                    false -> {
                        kProgressHUD.dismiss()
                    }
                }
            })

            showToastMessage.observe(this@PhotoView, Observer {
                Toast.makeText(activity, "$it", Toast.LENGTH_LONG).show()
            })

            showPhotosEvent.observe(this@PhotoView, Observer { photos ->
                photos?.let {
                    mBinding.grPhotos.adapter = PhotoGrAdapter(
                        picasso,
                        it as ArrayList<String>,
                        object : PhotoGrAdapter.PhotoClickListener {
                            override fun onPhotoClick(index: Int, photos: ArrayList<String>) {
                                Intent(activity, FullscreenActivity::class.java).apply {
                                    putStringArrayListExtra("photos", photos)
                                    putExtra("index", index)
                                    startActivity(this)
                                }
                            }
                        }
                    )
                }
            })
        }
    }

    private var columnWidth: Int = 0

    private fun initGridLayout() {
        val r = resources
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            GRID_PADDING.toFloat(),
            r.displayMetrics
        )

        columnWidth = ((context!!.getScreenWidth() - (NUM_OF_COLUMNS + 1) * padding) / NUM_OF_COLUMNS).toInt()

        mBinding.grPhotos.let {
            it.numColumns = NUM_OF_COLUMNS
            it.columnWidth = columnWidth
            it.stretchMode = GridView.NO_STRETCH
            it.setPadding(
                padding.toInt(),
                padding.toInt(),
                padding.toInt(),
                padding.toInt()
            )
            it.horizontalSpacing = padding.toInt()
            it.verticalSpacing = padding.toInt()
        }

    }

    companion object {
        val NUM_OF_COLUMNS = 3
        val GRID_PADDING = 0
    }
}