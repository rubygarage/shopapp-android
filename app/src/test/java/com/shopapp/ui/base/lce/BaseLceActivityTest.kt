package com.shopapp.ui.base.lce

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import com.shopapp.ui.base.lce.view.LceLayout
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BaseLceActivityTest {

    private lateinit var activity: TestBaseLceActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(TestBaseLceActivity::class.java)
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

    @After
    fun tearDown() {
        activity.finish()
    }

    private class TestBaseLceActivity : BaseLceActivity<Any, BaseLceView<Any>, BaseLcePresenter<Any, BaseLceView<Any>>>() {

        override fun inject() {
        }

        override fun getContentView(): Int = R.layout.activity_splash

        override fun createPresenter(): BaseLcePresenter<Any, BaseLceView<Any>> = mock()
    }
}