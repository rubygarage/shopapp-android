package com.shopapp.ui.custom

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AvatarViewTest {

    private lateinit var context: Context
    private lateinit var view: AvatarView

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        view = AvatarView(context)
    }

    @Test
    fun shouldSetTextWithFirstLetters() {
        val name = "test name"
        view.setName(name)
        assertEquals("TN", view.text.toString())
    }

    @Test
    fun shouldNotSetEmptyName() {
        val name = "test name"
        view.setName(name)
        assertEquals("TN", view.text.toString())

        val emptyName = ""
        view.setName(emptyName)
        assertNotEquals("", view.text.toString())
        assertEquals("TN", view.text.toString())
    }
}