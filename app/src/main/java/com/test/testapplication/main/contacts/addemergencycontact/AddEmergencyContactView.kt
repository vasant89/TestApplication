package com.test.testapplication.main.contacts.addemergencycontact

import android.Manifest
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import com.test.testapplication.R
import com.test.testapplication.adapters.AddEmergencyContactRvAdapter
import com.test.testapplication.adapters.AllContactRvAdapter
import com.test.testapplication.adapters.EmergencyContactAdapter
import com.test.testapplication.adapters.FavouritesAdapter
import com.test.testapplication.data.Contact
import com.test.testapplication.databinding.AddContactViewBinding
import com.test.testapplication.di.scope.ActivityScoped
import com.test.testapplication.extentions.call
import com.test.testapplication.location.PermissionHelper
import com.test.testapplication.utils.ContactDividerItemDecoration
import dagger.android.support.DaggerFragment
import javax.inject.Inject

@ActivityScoped
class AddEmergencyContactView
@Inject
constructor() : DaggerFragment() {

    lateinit var mBinding: AddContactViewBinding

    @Inject
    lateinit var kProgressHUD: KProgressHUD

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = AddContactViewBinding.inflate(inflater, container, false).apply {
            this.viewModel = (activity as AddEmergencyContactActivity).obtainViewModel()
            this.rvFavouritesContacts.apply {
                val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                layoutManager = manager
                itemAnimator = DefaultItemAnimator()
                addItemDecoration(ContactDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 36))
                adapter = FavouritesAdapter(viewModel!!.picasso, { contact, index ->
                    viewModel?.addRemoveEmergencyContact(contact, index)
                }, { isEmpty ->
                    viewModel?.let {
                        it.isEmpty.set(isEmpty)
                    }
                })
            }
            this.rvAllContacts.apply {
                val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                layoutManager = manager
                itemAnimator = DefaultItemAnimator()
                addItemDecoration(ContactDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 36))
                adapter = AddEmergencyContactRvAdapter(viewModel!!.picasso, { contact, index ->
                    viewModel?.addRemoveEmergencyContact(contact, index)
                }, { isEmpty ->
                    viewModel?.let {
                        it.isEmpty.set(isEmpty)
                    }
                })
            }
        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.viewModel?.apply {

            start()

            repository.getAllContact().observe(this@AddEmergencyContactView, Observer { contacts ->
                if (!contacts.isNullOrEmpty()) {
                    contactList.clear()
                    contactList.addAll(contacts)
                }
            })

            isDataLoading.observe(this@AddEmergencyContactView, Observer {
                when (it) {
                    true -> {
                        kProgressHUD.show()
                    }
                    false -> {
                        kProgressHUD.dismiss()
                    }
                }
            })
        }
    }
}