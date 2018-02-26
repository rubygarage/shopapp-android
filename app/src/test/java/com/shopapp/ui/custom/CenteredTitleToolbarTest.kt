package com.shopapp.ui.custom

import android.content.Context
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CenteredTitleToolbarTest {

    private lateinit var context: Context
    private lateinit var toolbar: CenteredTitleToolbar

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        toolbar = CenteredTitleToolbar(context)
    }

    @Test
    fun shouldDisplayCorrectTitle() {
        val testTitle = "testTitle"
        toolbar.setTitle(testTitle)
        assertEquals(testTitle, toolbar.toolbarTitle.text)
    }
}