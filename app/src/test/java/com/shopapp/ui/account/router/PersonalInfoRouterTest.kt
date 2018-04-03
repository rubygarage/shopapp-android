package com.shopapp.ui.account.router

import com.shopapp.TestShopApplication
import com.shopapp.ui.account.ChangePasswordActivity
import com.shopapp.ui.account.PersonalInfoActivity
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
class PersonalInfoRouterTest {

    private lateinit var activity: PersonalInfoActivity
    private lateinit var router: PersonalInfoRouter

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(PersonalInfoActivity::class.java)
        router = PersonalInfoRouter()
    }

    @Test
    fun shouldShowPolicyActivity() {
        router.showChangePassword(activity)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(ChangePasswordActivity::class.java, shadowIntent.intentClass)
    }

}