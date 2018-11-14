package com.test.testapplication.extentions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun View.hideKeyBoard(){
  (context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken,0)
}
