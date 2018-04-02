package com.shopapp.ui.base.lce

import android.content.Context
import android.view.View
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Error
import com.shopapp.ui.base.lce.view.LceLayout
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import kotlinx.android.synthetic.main.view_lce_error.view.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BaseLceActivityTest {

    private lateinit var activity: TestBaseLceActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        activity = Robolectric.buildActivity(TestBaseLceActivity::class.java)
            .create()
            .resume()
            .visible()
            .get()
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldInflateContentLayout() {
        assertEquals(1, activity.lceLayout.contentView.childCount)
    }

    @Test
    fun shouldSetCorrectTitle() {
        val title = "Test title"
        activity.setTitle(title)
        assertEquals(title, activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldShowToastMessageString() {
        val testMessage = "test message"
        activity.showMessage(testMessage)
        val looper = Shadows.shadowOf(activity.mainLooper)
        looper.idle()
        assertEquals(ShadowToast.getTextOfLatestToast(), testMessage)
    }

    @Test
    fun shouldShowToastMessageRes() {
        val testMessageRes = R.string.app_name
        activity.showMessage(testMessageRes)
        val looper = Shadows.shadowOf(activity.mainLooper)
        looper.idle()
        assertEquals(ShadowToast.getTextOfLatestToast(), context.getString(testMessageRes))
    }

    @Test
    fun shouldShowLoading() {
        activity.showLoading(false)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowContent() {
        activity.lceLayout.loadingView.visibility = View.VISIBLE
        activity.showContent(Unit)
        assertEquals(View.GONE, activity.lceLayout.loadingView.visibility)
        assertEquals(View.VISIBLE, activity.lceLayout.contentView.visibility)
    }

    @Test
    fun shouldShowEmptyView() {
        activity.showEmptyState()
        assertEquals(View.VISIBLE, activity.lceLayout.emptyView.visibility)
    }

    @Test
    fun shouldDelegateStateToChild() {
        assertEquals(View.GONE, activity.lceLayout.emptyView.visibility)
        activity.changeState(LceLayout.LceState.EmptyState)
        assertEquals(View.VISIBLE, activity.lceLayout.emptyView.visibility)
    }

    @Test
    fun shouldShowLoadingWhenLoadData() {
        activity.loadData()
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldFinishActivityOnCloseMenuItemClick() {
        val item = RoboMenuItem(R.id.close)
        activity.onOptionsItemSelected(item)
        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldFinishActivityOnHomeMenuItemClick() {
        val item = RoboMenuItem(android.R.id.home)
        activity.onOptionsItemSelected(item)
        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldShowProgressOnReload() {
        activity.changeState(LceLayout.LceState.ErrorState(Error.Content()))
        assertEquals(View.GONE, activity.lceLayout.loadingView.visibility)
        activity.lceLayout.tryAgainButton.performClick()
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldActivityFinishWhenBackButtonClick() {
        assertFalse(activity.isFinishing)
        activity.lceLayout.backButton.performClick()
        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldShowNotFoundErrorWithDefaultMessageWhenCriticalErrorReceived() {
        activity.showError(Error.Critical())
        assertEquals(View.VISIBLE, activity.lceLayout.errorView.visibility)
        assertEquals(context.getString(R.string.сould_not_find),
            activity.lceLayout.errorView.errorMessage.text)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}