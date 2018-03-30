package com.shopapp.ui.base.lce.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.shopapp.R
import com.shopapp.gateway.entity.Error
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LceLoadingViewTest {

    private lateinit var context: Context
    private lateinit var view: LceLoadingView

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        view = LceLoadingView(context)
    }

    @Test
    fun shouldViewBeVisible() {
        view.changeState(LceLayout.LceState.LoadingState())
        assertEquals(View.VISIBLE, view.visibility)
        assertEquals(ContextCompat.getDrawable(context, R.color.colorBackgroundLight), view.background)
    }

    @Test
    fun shouldViewBeTranslucent() {
        view.changeState(LceLayout.LceState.LoadingState(true))
        assertEquals(View.VISIBLE, view.visibility)
        assertEquals(ContextCompat.getDrawable(context, R.color.colorBackgroundLightTranslucent), view.background)
    }

    @Test
    fun shouldViewBeGone() {
        view.changeState(LceLayout.LceState.ContentState)
        assertEquals(View.GONE, view.visibility)

        view.changeState(LceLayout.LceState.ErrorState(Error.Content()))
        assertEquals(View.GONE, view.visibility)

        view.changeState(LceLayout.LceState.EmptyState)
        assertEquals(View.GONE, view.visibility)
    }
}