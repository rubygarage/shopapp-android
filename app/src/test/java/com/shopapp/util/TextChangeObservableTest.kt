package com.shopapp.util

import android.content.Context
import android.widget.TextView
import com.shopapp.TestShopApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class TextChangeObservableTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldSubscribeToViewTextChangeEvent() {
        val view = TextView(context)
        val observable = TextChangeObservable(view)
        val observer = observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .test()

        view.text = "t"
        view.text = "te"
        view.text = "tes"
        view.text = "test"

        assertFalse(observer.isCancelled)
        assertFalse(observer.isDisposed)

        observer
            .assertValueCount(4)
            .assertValueSequence(mutableListOf("t", "te", "tes", "test"))

        observer.dispose()
        assertTrue(observer.isDisposed)

    }

    @Test
    fun shouldRemoveWatcherOnDispose() {
        val view = TextView(context)
        val observable = TextChangeObservable(view)
        val observer = observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .test()

        val shadow = shadowOf(view)
        assertEquals(1, shadow.watchers.size)
        observer.dispose()
        assertEquals(0, shadow.watchers.size)

    }
}