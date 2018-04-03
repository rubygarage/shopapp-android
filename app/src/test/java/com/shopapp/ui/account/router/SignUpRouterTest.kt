package com.shopapp.ui.account.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.account.SignUpActivity
import com.shopapp.ui.policy.PolicyActivity
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
class SignUpRouterTest {

    private lateinit var activity: SignUpActivity
    private lateinit var router: SignUpRouter

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(SignUpActivity::class.java)
        router = SignUpRouter()
    }

    @Test
    fun shouldShowPolicyActivity() {
        val policy = MockInstantiator.newPolicy()
        router.showPolicy(activity, policy)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(PolicyActivity::class.java, shadowIntent.intentClass)
        assertEquals(policy, startedIntent.extras.getParcelable(PolicyActivity.EXTRA_POLICY))
    }

}