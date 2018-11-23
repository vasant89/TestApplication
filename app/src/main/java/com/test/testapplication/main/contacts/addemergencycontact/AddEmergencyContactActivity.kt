package com.test.testapplication.main.contacts.addemergencycontact

import android.os.Bundle
import android.view.MenuItem
import com.test.testapplication.R
import com.test.testapplication.ViewModelFactory
import com.test.testapplication.extentions.obtainViewModel
import com.test.testapplication.extentions.replaceFragmentInActivity
import com.test.testapplication.extentions.setupActionBar
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class AddEmergencyContactActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var addContactView: AddEmergencyContactView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        setupActionBar(R.id.toolbar) {
            title = getString(R.string.add_emergency_contact)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        replaceFragmentInActivity(addContactView, R.id.contentFrame)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                android.R.id.home -> {
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun obtainViewModel() = obtainViewModel(viewModelFactory, AddEmergencyContactViewModel::class.java)
}
