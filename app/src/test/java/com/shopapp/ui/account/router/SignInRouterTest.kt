package com.shopapp.ui.account.router

import com.shopapp.TestShopApplication
import com.shopapp.ui.account.ForgotPasswordActivity
import com.shopapp.ui.account.SignInActivity
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
class SignInRouterTest {

    private lateinit var activity: SignInActivity
    private lateinit var router: SignInRouter

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(SignInActivity::class.java)
        router = SignInRouter()
    }

    @Test
    fun shouldShowForgotPasswordActivity() {
        router.showForgotPassword(activity)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(ForgotPasswordActivity::class.java, shadowIntent.intentClass)
    }

}