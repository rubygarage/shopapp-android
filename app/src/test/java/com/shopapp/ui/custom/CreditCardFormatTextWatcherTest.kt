package com.shopapp.ui.custom

import android.widget.EditText
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CreditCardFormatTextWatcherTest {

    @Test
    fun shouldAddSpacesToText() {

        val padding = 5
        val formatter = CreditCardFormatTextWatcher(padding)
        val cardNumberText = "4242424242424242"

        val textView = EditText(RuntimeEnvironment.application.baseContext)
        textView.setText(cardNumberText)
        val spans = textView.text.getSpans(0, cardNumberText.length, CreditCardFormatTextWatcher.PaddingRightSpan::class.java)
        assertEquals(0, spans.size)

        formatter.formatCardNumber(textView)
        val formattedSpans = textView.text.getSpans(0, cardNumberText.length, CreditCardFormatTextWatcher.PaddingRightSpan::class.java)
        assertEquals(3, formattedSpans.size)
        assertEquals(padding, formattedSpans.first().mPadding)
    }
}