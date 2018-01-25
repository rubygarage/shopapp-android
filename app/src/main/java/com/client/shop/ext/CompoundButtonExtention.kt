package com.client.shop.ext

import android.widget.CompoundButton
import com.client.shop.util.CompoundButtonCheckedChangeObservable
import io.reactivex.Observable

fun CompoundButton.checkedChanges(): Observable<Boolean> =
    CompoundButtonCheckedChangeObservable(this)
