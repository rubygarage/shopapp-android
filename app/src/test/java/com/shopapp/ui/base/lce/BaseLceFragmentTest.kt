package com.shopapp.ui.base.lce

import android.content.Context
import android.view.View
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Error
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_lce_error.view.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BaseLceFragmentTest {

    private lateinit var fragment: TestBaseLceFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = TestBaseLceFragment()
        SupportFragmentTestUtil.startFragment(fragment, TestBaseLceActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldInflateContentLayout() {
        assertEquals(1, fragment.lceLayout.contentView.childCount)
    }

    @Test
    fun shouldShowToastMessageString() {
        val testMessage = "test message"
        fragment.showMessage(testMessage)
        val looper = Shadows.shadowOf(fragment.activity?.mainLooper)
        looper.idle()
        assertEquals(ShadowToast.getTextOfLatestToast(), testMessage)
    }

    @Test
    fun shouldShowToastMessageRes() {
        val testMessageRes = R.string.app_name
        fragment.showMessage(testMessageRes)
        val looper = Shadows.shadowOf(fragment.activity?.mainLooper)
        looper.idle()
        assertEquals(ShadowToast.getTextOfLatestToast(), context.getString(testMessageRes))
    }

    @Test
    fun shouldShowLoading() {
        fragment.showLoading(false)
        assertEquals(View.VISIBLE, fragment.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowContent() {
        fragment.lceLayout.loadingView.visibility = View.VISIBLE
        fragment.showContent(Unit)
        assertEquals(View.GONE, fragment.lceLayout.loadingView.visibility)
        assertEquals(View.VISIBLE, fragment.lceLayout.contentView.visibility)
    }

    @Test
    fun shouldShowEmptyView() {
        fragment.showEmptyState()
        assertEquals(View.VISIBLE, fragment.lceLayout.emptyView.visibility)
    }

    @Test
    fun shouldShowLoadingWhenLoadData() {
        fragment.loadData()
        assertEquals(View.VISIBLE, fragment.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowProgressOnReload() {
        fragment.showError(Error.Content())
        assertEquals(View.GONE, fragment.lceLayout.loadingView.visibility)
        fragment.lceLayout.tryAgainButton.performClick()
        assertEquals(View.VISIBLE, fragment.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldActivityFinishWhenBackButtonClick() {
        val activity = fragment.activity
        assertNotNull(activity)
        assertFalse(activity!!.isFinishing)
        fragment.lceLayout.backButton.performClick()
        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldShowNotFoundErrorWithDefaultMessageWhenCriticalErrorReceived() {
        fragment.showError(Error.Critical())
        assertEquals(View.VISIBLE, fragment.lceLayout.errorView.visibility)
        assertEquals(context.getString(R.string.сould_not_find),
            fragment.lceLayout.errorView.errorMessage.text)
    }
}