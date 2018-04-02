package com.shopapp.ui.base.lce.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.shopapp.R
import com.shopapp.gateway.entity.Error
import kotlinx.android.synthetic.main.view_lce_empty.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LceEmptyViewTest {

    private lateinit var context: Context
    private lateinit var view: LceEmptyView

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        view = LceEmptyView(context)
    }

    @Test
    fun shouldViewBeVisible() {
        view.changeState(LceLayout.LceState.EmptyState)
        assertEquals(View.VISIBLE, view.visibility)
    }

    @Test
    fun shouldViewBeGone() {
        view.changeState(LceLayout.LceState.ContentState)
        assertEquals(View.GONE, view.visibility)

        view.changeState(LceLayout.LceState.ErrorState(Error.Content()))
        assertEquals(View.GONE, view.visibility)

        view.changeState(LceLayout.LceState.LoadingState())
        assertEquals(View.GONE, view.visibility)
    }

    @Test
    fun shouldCorrectCustomise() {
        view.customiseEmptyButtonVisibility(false)
        assertEquals(View.GONE, view.emptyButton.visibility)

        view.customiseEmptyButtonVisibility(true)
        assertEquals(View.VISIBLE, view.emptyButton.visibility)

        view.customiseEmptyButtonText(R.string.app_name)
        assertEquals(context.getString(R.string.app_name), view.emptyButton.text.toString())

        view.customiseEmptyMessage(R.string.shop)
        assertEquals(context.getString(R.string.shop), view.emptyMessage.text.toString())

        view.customiseEmptyImage(R.drawable.ic_launcher_background)
        assertEquals(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background),
            view.emptyImage.drawable)
    }
}