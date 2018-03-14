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
import kotlinx.android.synthetic.main.view_lce_error.view.*
import org.junit.Assert.assertEquals
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
        SupportFragmentTestUtil.startFragment(fragment)
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
        fragment.showError(false)
        assertEquals(View.GONE, fragment.lceLayout.loadingView.visibility)
        fragment.lceLayout.tryAgainButton.performClick()
        assertEquals(View.VISIBLE, fragment.lceLayout.loadingView.visibility)
    }

    class TestBaseLceFragment : BaseLceFragment<Any, BaseLceView<Any>, BaseLcePresenter<Any, BaseLceView<Any>>>() {

        override fun inject() {
        }

        override fun getContentView(): Int = R.layout.activity_splash

        override fun createPresenter(): BaseLcePresenter<Any, BaseLceView<Any>> = mock()
    }
}