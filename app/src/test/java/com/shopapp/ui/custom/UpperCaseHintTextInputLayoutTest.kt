package com.shopapp.ui.custom

import android.view.ContextThemeWrapper
import com.shopapp.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class UpperCaseHintTextInputLayoutTest {

    private lateinit var view: UpperCaseHintTextInputLayout

    @Before
    fun setUpTest() {
        val contextWrapper = ContextThemeWrapper(RuntimeEnvironment.application.baseContext, R.style.AppTheme)
        view = UpperCaseHintTextInputLayout(contextWrapper)
    }

    @Test
    fun shouldSetUppercaseHint() {
        val lowercaseText = "test text"
        view.hint = lowercaseText
        assertEquals(lowercaseText.toUpperCase(), view.hint)
    }

    @Test
    fun shouldNotSetHint() {
        val lowercaseText: String? = null
        view.hint = lowercaseText
        assertEquals(null, view.hint)
    }
}