package com.shopapp.ui.splash.router

import android.app.Activity
import com.shopapp.TestShopApplication
import com.shopapp.ui.home.HomeActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class SplashRouterTest {

    private lateinit var activity: Activity
    private lateinit var router: SplashRouter

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(Activity::class.java)
        router = SplashRouter()
    }

    @Test
    fun shouldStartHomeActivity() {
        router.showHome(activity, true)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(HomeActivity::class.java, shadowIntent.intentClass)
    }
}