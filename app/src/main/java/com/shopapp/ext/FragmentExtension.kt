package com.shopapp.ext

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

inline fun FragmentManager.replaceOnce(
    @IdRes containerViewId: Int, fragmentTag: String,
    creator: () -> Fragment,
    withBackStack: Boolean = true
): Fragment {
    val transaction = this.beginTransaction()
    var fragment = this.findFragmentByTag(fragmentTag)
    if (fragment == null) {
        fragment = creator()
        transaction.replace(containerViewId, fragment, fragmentTag)
        if (withBackStack) {
            transaction.addToBackStack(fragmentTag)
        }
    }
    transaction.commit()
    return fragment
}

inline fun FragmentManager.replaceByTag(
    @IdRes containerViewId: Int, fragmentTag: String,
    body: () -> Fragment, withBackStack: Boolean = true
): Fragment {
    val transaction = this.beginTransaction()
    var fragment = this.findFragmentByTag(fragmentTag)
    if (fragment != null) {
        transaction.replace(containerViewId, fragment, fragmentTag)
    } else {
        fragment = body()
        transaction.replace(containerViewId, fragment, fragmentTag)
    }
    if (withBackStack) {
        transaction.addToBackStack(fragmentTag)
    }
    transaction.commit()
    return fragment
}