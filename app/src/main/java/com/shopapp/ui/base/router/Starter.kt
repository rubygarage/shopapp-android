package com.shopapp.ui.base.router

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment

class Starter(private val intent: Intent, private val requestCode: Int) {

    fun startWith(activity: Activity) {
        activity.startActivityForResult(intent, requestCode)
    }

    fun startWith(fragment: Fragment) {
        fragment.startActivityForResult(intent, requestCode)
    }
}
