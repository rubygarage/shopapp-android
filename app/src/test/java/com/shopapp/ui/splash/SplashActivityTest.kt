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
    fun shouldCallValidateItemsWhenOnCreate() {
        verify(activity.presenter).validateItems()
    }

    @Test
    fun shouldStartHomeActivityWhenShowContent() {
        activity.showContent(Unit)
        verify(activity.router).showHome(activity)
    }

    @Test
    fun shouldStartHomeActivityWhenValidationError() {
        activity.validationError()
        verify(activity.router).showHome(activity)
    }
}