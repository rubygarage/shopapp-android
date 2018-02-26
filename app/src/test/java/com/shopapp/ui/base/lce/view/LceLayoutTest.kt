package com.shopapp.ui.base.lce.view

import android.content.Context
import android.view.View
import com.shopapp.R
import kotlinx.android.synthetic.main.layout_lce.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
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
}