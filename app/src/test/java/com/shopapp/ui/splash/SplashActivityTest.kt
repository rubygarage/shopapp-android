package com.shopapp.ui.splash

import android.content.Context
import com.shopapp.TestShopApplication
import com.shopapp.ui.home.HomeActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
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
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(HomeActivity::class.java, shadowIntent.intentClass)
    }
}