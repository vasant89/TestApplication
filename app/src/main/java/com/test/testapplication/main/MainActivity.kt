package com.test.testapplication.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.test.testapplication.R
import com.test.testapplication.ViewModelFactory
import com.test.testapplication.databinding.ActivityMainBinding
import com.test.testapplication.main.searchplace.SearchPlaceView
import com.test.testapplication.main.searchplace.SearchPlaceViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.support.design.widget.BottomSheetBehavior
import com.test.testapplication.extentions.*

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var mBinding: ActivityMainBinding

    @Inject
    lateinit var mapView: SearchPlaceView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupActionBar(R.id.toolbar) {
            title = getString(R.string.place_search_hint)
        }

        replaceFragmentInActivity(mapView, R.id.contentFrame)

        mBinding.viewModel = obtainMainViewModel().apply {
            start()
        }
    }

    override fun onResume() {
        super.onResume()
        showGPSDisabledAlertToUser()
    }


    override fun onBackPressed() {
        if (mapView.isAdded && mapView.sheetBehavior != null && mapView.sheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            mapView.sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            showActivityClosingDialog()
        }
    }

    fun obtainMainViewModel() = obtainViewModel(viewModelFactory, MainViewModel::class.java)
    fun obtainSearchPlaceViewModel() = obtainViewModel(viewModelFactory, SearchPlaceViewModel::class.java)
}
