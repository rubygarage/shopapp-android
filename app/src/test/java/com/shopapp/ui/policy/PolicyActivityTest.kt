package com.shopapp.ui.policy

import android.content.Context
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.test.RxImmediateSchedulerRule
import kotlinx.android.synthetic.main.activity_policy.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.fakes.RoboMenuItem

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class PolicyActivityTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var activity: PolicyActivity

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
        val intent = PolicyActivity.getStartIntent(context, MockInstantiator.newPolicy())
        activity = Robolectric.buildActivity(PolicyActivity::class.java, intent).create().get()
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(MockInstantiator.DEFAULT_TITLE, activity.toolbar.toolbarTitle.text.toString())
    }

    @Test
    fun shouldSetPolicyBodyOnCreate() {
        assertEquals(MockInstantiator.DEFAULT_DESCRIPTION, activity.body.text.toString())
    }

    @Test
    fun shouldFinishActivityWhenBackButtonClicked() {
        val menuItem = RoboMenuItem(android.R.id.home)
        activity.onOptionsItemSelected(menuItem)

        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldDoNothingWhenAnotherMenuItemClicked() {
        val menuItem = RoboMenuItem(android.R.id.edit)
        activity.onOptionsItemSelected(menuItem)

        assertFalse(activity.isFinishing)
    }
}