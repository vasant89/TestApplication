package com.test.testapplication.main.contacts

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kaopiz.kprogresshud.KProgressHUD
import com.test.testapplication.adapters.EmergencyContactAdapter
import com.test.testapplication.databinding.AllContactViewBinding
import com.test.testapplication.di.scope.ActivityScoped
import com.test.testapplication.main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import com.test.testapplication.adapters.AllContactRvAdapter
import com.test.testapplication.data.Contact
import com.test.testapplication.extentions.call
import com.test.testapplication.location.PermissionHelper
import com.test.testapplication.main.contacts.addemergencycontact.AddEmergencyContactActivity
import android.support.v7.widget.DividerItemDecoration
import com.test.testapplication.utils.ContactDividerItemDecoration


@ActivityScoped
class AllContactsView
@Inject
constructor() : DaggerFragment() {

    lateinit var mBinding: AllContactViewBinding

    @Inject
    lateinit var kProgressHUD: KProgressHUD

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = AllContactViewBinding.inflate(inflater, container, false).apply {
            this.viewModel = (activity as MainActivity).obtainAllContactsViewModel()
            this.rvEmergencyContacts.apply {
                val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                layoutManager = manager
                itemAnimator = DefaultItemAnimator()
                adapter = EmergencyContactAdapter(viewModel!!.picasso) { isAddContact, position, item ->
                    if (!isAddContact) {
                        item.phone?.let {
                            PermissionHelper().checkPermission(activity!!,
                                Manifest.permission.CALL_PHONE, "Grant permission for call.",
                                object : PermissionHelper.PermissionInterface {
                                    override fun granted(granted: Boolean) {
                                        if (granted) {
                                            context.call(it)
                                        }
                                    }
                                })

                        }
                    } else {
                        Intent(activity, AddEmergencyContactActivity::class.java).apply {
                            startActivity(this)
                        }
                    }
                }
            }
            this.rvAllContacts.apply {
                val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                layoutManager = manager
                itemAnimator = DefaultItemAnimator()
                addItemDecoration(ContactDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 36))
                adapter = AllContactRvAdapter(viewModel!!.picasso, { mobile ->
                    PermissionHelper().checkPermission(activity!!,
                        Manifest.permission.CALL_PHONE, "Grant permission for call.",
                        object : PermissionHelper.PermissionInterface {
                            override fun granted(granted: Boolean) {
                                if (granted) {
                                    context.call(mobile)
                                }
                            }
                        })

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

            repository.getAllContact().observe(this@AllContactsView, Observer {contacts->
                if (!contacts.isNullOrEmpty()) {
                    contactList.clear()
                    contactList.addAll(contacts)
                } else {
                    getContacts()
                }
            })

            isDataLoading.observe(this@AllContactsView, Observer {
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