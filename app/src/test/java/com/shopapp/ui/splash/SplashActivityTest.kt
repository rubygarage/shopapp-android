package com.shopapp.ui.splash

import android.content.Context
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class SplashActivityTest {

    private lateinit var activity: SplashActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        activity = Robolectric.setupActivity(SplashActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldStartHomeActivity() {
        Robolectric.getForegroundThreadScheduler().advanceBy(1, TimeUnit.SECONDS)
        verify(activity.router).showHome(activity)
    }
}