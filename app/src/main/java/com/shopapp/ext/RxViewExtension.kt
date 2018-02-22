package com.shopapp.ext

import android.widget.CompoundButton
import android.widget.TextView
import com.shopapp.util.CheckedChangeObservable
import com.shopapp.util.TextChangeObservable
import io.reactivex.Observable

fun CompoundButton.checkedChanges(): Observable<Boolean> = CheckedChangeObservable(this)

fun TextView.textChanges(): Observable<String> = TextChangeObservable(this)