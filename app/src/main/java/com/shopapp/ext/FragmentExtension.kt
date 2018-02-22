package com.shopapp.ext

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

inline fun FragmentManager.replaceOnce(@IdRes containerViewId: Int, fragmentTag: String,
                                       body: () -> Fragment, withBackStack: Boolean = true): FragmentTransaction {
    val transaction = this.beginTransaction()
    val fragment = this.findFragmentByTag(fragmentTag)
    if (fragment == null) {
        transaction.replace(containerViewId, body(), fragmentTag)
        if (withBackStack) {
            transaction.addToBackStack(fragmentTag)
        }
    }
    return transaction
}

inline fun FragmentManager.replaceByTag(@IdRes containerViewId: Int, fragmentTag: String,
                                        body: () -> Fragment, withBackStack: Boolean = true): FragmentTransaction {
    val transaction = this.beginTransaction()
    val fragment = this.findFragmentByTag(fragmentTag)
    if (fragment != null) {
        transaction.replace(containerViewId, fragment, fragmentTag)
    } else {
        transaction.replace(containerViewId, body(), fragmentTag)
    }
    if (withBackStack) {
        transaction.addToBackStack(fragmentTag)
    }
    return transaction
}