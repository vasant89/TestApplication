package com.test.testapplication.main.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.testapplication.R
import com.test.testapplication.databinding.PhotoViewBinding
import com.test.testapplication.di.scope.ActivityScoped
import com.test.testapplication.main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

@ActivityScoped
class PhotoView
@Inject
constructor():DaggerFragment(){

    lateinit var mBinding: PhotoViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= PhotoViewBinding.inflate(inflater,container,false).apply {
            this.viewModel = (activity as MainActivity).obtainPhotoViewModel()
        }
        return view
    }
}