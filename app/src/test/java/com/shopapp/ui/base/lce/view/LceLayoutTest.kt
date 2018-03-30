package com.shopapp.ui.base.lce.view

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.R
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_lce_empty.view.*
import kotlinx.android.synthetic.main.view_lce_error.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LceLayoutTest {

    private lateinit var context: Context
    private lateinit var view: LceLayout

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        view = LceLayout(context)
    }

    @Test
    fun shouldInflateContentLayout() {
        assertEquals(0, view.contentView.childCount)
        view.setupContentLayout(R.layout.activity_splash)
        assertEquals(1, view.contentView.childCount)
    }

    @Test
    fun shouldDelegateStateToChild() {
        assertEquals(View.GONE, view.emptyView.visibility)
        view.changeState(LceLayout.LceState.EmptyState)
        assertEquals(View.VISIBLE, view.emptyView.visibility)
    }

    @Test
    fun shouldSetListenerToEmptyButton() {
        val listener: View.OnClickListener = mock()
        val emptyButton = shadowOf(view.emptyButton)
        assertNull(emptyButton.onClickListener)
        view.emptyButtonClickListener = listener
        assertEquals(listener, view.emptyButtonClickListener)
        assertEquals(listener, emptyButton.onClickListener)
    }

    @Test
    fun shouldSetListenerToTryAgainButton() {
        val listener: View.OnClickListener = mock()
        val tryAgainButton = shadowOf(view.tryAgainButton)
        assertNull(tryAgainButton.onClickListener)
        view.tryAgainButtonClickListener = listener
        assertEquals(listener, view.tryAgainButtonClickListener)
        assertEquals(listener, tryAgainButton.onClickListener)
    }

    @Test
    fun shouldSetListenerToBackButton() {
        val listener: View.OnClickListener = mock()
        val backButton = shadowOf(view.backButton)
        assertNull(backButton.onClickListener)
        view.backButtonClickListener = listener
        assertEquals(listener, view.backButtonClickListener)
        assertEquals(listener, backButton.onClickListener)
    }
}