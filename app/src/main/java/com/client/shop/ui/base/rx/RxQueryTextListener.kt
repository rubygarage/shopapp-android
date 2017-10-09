package com.client.shop.ui.base.rx

import android.support.v7.widget.SearchView
import io.reactivex.ObservableEmitter

class RxQueryTextListener(private val emitter: ObservableEmitter<String>) : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String): Boolean {
        if (!emitter.isDisposed) {
            emitter.onNext(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (!emitter.isDisposed) {
            emitter.onNext(newText)
        }
        return true
    }

}