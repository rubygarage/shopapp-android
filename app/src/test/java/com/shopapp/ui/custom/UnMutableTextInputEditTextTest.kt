package com.shopapp.ui.custom

import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class UnMutableTextInputEditTextTest {

    private lateinit var view: UnMutableTextInputEditText

    @Before
    fun setUpTest() {
        val contextWrapper = ContextThemeWrapper(RuntimeEnvironment.application.baseContext, R.style.AppTheme)
        view = UnMutableTextInputEditText(contextWrapper, null)
    }

    @Test
    fun shouldSetTextProgrammatically() {
        val testText = "testText"
        view.setText(testText)
        assertEquals(testText, view.text.toString())
    }

    @Test
    fun shouldNotSetFocus() {
        view.performClick()
        assertEquals(false, view.isFocused)
    }

    @Test
    fun shouldProceedClickOnDownAction() {
        val event: MotionEvent = mock {
            on { action } doReturn MotionEvent.ACTION_DOWN
        }
        val listener: View.OnClickListener = mock()
        view.setOnClickListener(listener)
        view.onTouchEvent(event)
        verify(listener).onClick(view)
    }
}