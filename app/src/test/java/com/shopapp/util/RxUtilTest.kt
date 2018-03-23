package com.shopapp.util

import com.shopapp.TestShopApplication
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class RxUtilTest {

    @Test
    fun shouldFinishWithError() {
        val observer: TestObserver<String> = TestObserver()

        val thread = Thread({ RxUtil.checkMainThread(observer) })
        thread.start()
        thread.join()

        observer.assertError { it is IllegalStateException }
        assertEquals(1, observer.errors().size)
        assertTrue(observer.errors()[0] is IllegalStateException)
    }

    @Test
    fun shouldPassTheCheck() {
        val observer: TestObserver<String> = TestObserver()

        assertTrue(RxUtil.checkMainThread(observer))
        assertEquals(0, observer.errors().size)
    }
}