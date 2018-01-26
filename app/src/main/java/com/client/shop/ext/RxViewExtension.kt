package com.client.shop.ext

import android.widget.CompoundButton
import android.widget.TextView
import com.client.shop.util.CheckedChangeObservable
import com.client.shop.util.TextChangeObservable
import io.reactivex.Observable

fun CompoundButton.checkedChanges(): Observable<Boolean> = CheckedChangeObservable(this)

fun TextView.textChanges(): Observable<String> = TextChangeObservable(this)