package com.shopapp.util

import android.content.Context
import android.support.v7.widget.SwitchCompat
import com.shopapp.TestShopApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CheckedChangeObservableTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldSubscribeToViewChangeEvent() {
        val view = SwitchCompat(context)
        val observable = CheckedChangeObservable(view)
        val observer = observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .test()

        view.isChecked = true
        view.isChecked = false
        view.isChecked = true

        assertFalse(observer.isCancelled)
        assertFalse(observer.isDisposed)

        observer
            .assertValueCount(3)
            .assertValueSequence(mutableListOf(true, false, true))

        observer.dispose()
        assertTrue(observer.isDisposed)

    }
}