package com.shopapp.ui.item

import android.view.View
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import kotlinx.android.synthetic.main.item_address_header.view.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AddressHeaderItemTest {

    @Test
    fun shouldSetOnClickListener() {
        val view = AddressHeaderItem(RuntimeEnvironment.application.baseContext)
        val listener: View.OnClickListener = mock()
        view.headerClickListener = listener
        view.addNewAddressButton.performClick()

        verify(listener).onClick(view.addNewAddressButton)
    }
}