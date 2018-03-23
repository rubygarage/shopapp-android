package com.shopapp.ui.item

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.shopapp.R
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_address_content.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AddressContentItemTest {

    private lateinit var context: Context
    private lateinit var view: AddressContentItem

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        view = AddressContentItem(context)
    }

    @Test
    fun shouldDisplayAddressData() {
        val address = MockInstantiator.newAddress()
        view.setAddress(address)

        assertEquals(context.getString(R.string.full_name_pattern, address.firstName, address.lastName),
            view.name.text)
        val addressText = "${address.address}, ${address.secondAddress}, ${address.city}," +
                " ${address.state}, ${address.zip}, ${address.country}"
        assertEquals(addressText, view.addressText.text.toString())
    }

    @Test
    fun shouldPhoneBeVisible() {
        val address = MockInstantiator.newAddress()
        given(address.phone).willReturn(MockInstantiator.DEFAULT_PHONE)
        view.setAddress(address)

        assertEquals(View.VISIBLE, view.phone.visibility)
        assertEquals(context.getString(R.string.tel_prefix, address.phone), view.phone.text.toString())
    }

    @Test
    fun shouldPhoneBeGone() {
        val address = MockInstantiator.newAddress()
        given(address.phone).willReturn("")
        view.setAddress(address)

        assertEquals(View.GONE, view.phone.visibility)
    }
}