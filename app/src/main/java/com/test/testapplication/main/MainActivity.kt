package com.test.testapplication.main

import android.content.res.Configuration
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
import com.test.testapplication.main.photos.PhotoViewModel
import android.widget.Toast
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import com.test.testapplication.main.photos.PhotoView


class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var mBinding: ActivityMainBinding

    @Inject
    lateinit var searchPlaceView: SearchPlaceView

    @Inject
    lateinit var photoView: PhotoView

    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupActionBar(R.id.toolbar) {
            title = getString(R.string.place_search_hint)
            setDisplayHomeAsUpEnabled(true)
        }

        init()

        mBinding.viewModel = obtainMainViewModel().apply {
            start()
        }
    }

    private fun init() {
        drawerToggle = ActionBarDrawerToggle(this, mBinding.drawerLayout, R.string.open, R.string.close)

        drawerToggle?.let {
            mBinding.drawerLayout.addDrawerListener(it)
            it.syncState()
        }

        showSearchPlaceView()

        mBinding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.search_place -> {
                    showSearchPlaceView()
                }
                R.id.photos -> {
                    showPhotoView()
                }
            }
            mBinding.drawerLayout.closeDrawer(Gravity.START)
            true
        }
    }

    fun showSearchPlaceView() {
        replaceFragmentInActivity(searchPlaceView, R.id.contentFrame)
    }

    fun showPhotoView() {
        replaceFragmentInActivity(photoView, R.id.contentFrame)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle?.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        drawerToggle?.let {
            if (it.onOptionsItemSelected(item))
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        showGPSDisabledAlertToUser()
    }


    override fun onBackPressed() {
        if (searchPlaceView.isAdded && searchPlaceView.sheetBehavior != null && searchPlaceView.sheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            searchPlaceView.sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            showActivityClosingDialog()
        }
    }

    fun obtainMainViewModel() = obtainViewModel(viewModelFactory, MainViewModel::class.java)
    fun obtainSearchPlaceViewModel() = obtainViewModel(viewModelFactory, SearchPlaceViewModel::class.java)
    fun obtainPhotoViewModel() = obtainViewModel(viewModelFactory, PhotoViewModel::class.java)
}
