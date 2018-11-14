package com.test.testapplication.main.photos

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import com.test.testapplication.R
import com.test.testapplication.adapters.PhotoRvAdapter
import com.test.testapplication.databinding.PhotoViewBinding
import com.test.testapplication.di.scope.ActivityScoped
import com.test.testapplication.main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

@ActivityScoped
class PhotoView
@Inject
constructor() : DaggerFragment() {

    lateinit var mBinding: PhotoViewBinding

    @Inject
    lateinit var kProgressHUD: KProgressHUD

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = PhotoViewBinding.inflate(inflater, container, false).apply {
            this.viewModel = (activity as MainActivity).obtainPhotoViewModel()
            this.rvPhotos.let {
                it.itemAnimator = DefaultItemAnimator()
                it.setHasFixedSize(true)
                it.adapter = PhotoRvAdapter(viewModel!!.picasso)
            }
        }
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

            showPhotosEvent.observe(this@PhotoView, Observer { photos->
                photos?.let {
                   (mBinding.rvPhotos.adapter as PhotoRvAdapter).replaceList(it)
                }
            })
        }
    }
}