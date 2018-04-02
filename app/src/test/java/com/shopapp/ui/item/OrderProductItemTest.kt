package com.shopapp.ui.item

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.VariantOption
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_order_product.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class OrderProductItemTest {

    private val numberFormatter = NumberFormatter()
    private lateinit var context: Context
    private lateinit var itemView: OrderProductItem

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        itemView = OrderProductItem(context, numberFormatter)
    }

    @Test
    fun shouldDisplayOrderProductData() {
        val orderProduct = MockInstantiator.newOrderProduct()
        val productVariant = orderProduct.productVariant
        itemView.setOrderProduct(orderProduct, MockInstantiator.DEFAULT_CURRENCY)

        assertEquals(productVariant!!.title, itemView.titleText.text)
        assertEquals(context.getString(R.string.quantity_label_pattern, orderProduct.quantity), itemView.quantity.text)
        val totalPrice = numberFormatter.formatPrice(
            productVariant.price * orderProduct.quantity.toBigDecimal(),
            MockInstantiator.DEFAULT_CURRENCY
        )
        assertEquals(totalPrice, itemView.totalPrice.text)
    }

    @Test
    fun shouldOptionsTextViewBeVisible() {
        val orderProduct = MockInstantiator.newOrderProduct()
        val productVariant = orderProduct.productVariant
        val selectedOptions = MockInstantiator.newList(MockInstantiator.newVariantOption(), 3)
        given(productVariant!!.selectedOptions).willReturn(selectedOptions)
        itemView.setOrderProduct(orderProduct, MockInstantiator.DEFAULT_CURRENCY)
        assertEquals(View.VISIBLE, itemView.optionsTextView.visibility)
    }

    @Test
    fun shouldOptionsTextViewBeGone() {
        val orderProduct = MockInstantiator.newOrderProduct()
        val productVariant = orderProduct.productVariant
        val selectedOptions = emptyList<VariantOption>()
        given(productVariant!!.selectedOptions).willReturn(selectedOptions)
        itemView.setOrderProduct(orderProduct, MockInstantiator.DEFAULT_CURRENCY)
        assertEquals(View.GONE, itemView.optionsTextView.visibility)
    }

    @Test
    fun shouldEachPriceBeVisible() {
        val orderProduct = MockInstantiator.newOrderProduct()
        given(orderProduct.quantity).willReturn(3)
        itemView.setOrderProduct(orderProduct, MockInstantiator.DEFAULT_CURRENCY)
        assertEquals(View.VISIBLE, itemView.eachPrice.visibility)
    }

    @Test
    fun shouldEachPriceBeGone() {
        val orderProduct = MockInstantiator.newOrderProduct()
        given(orderProduct.quantity).willReturn(1)
        itemView.setOrderProduct(orderProduct, MockInstantiator.DEFAULT_CURRENCY)
        assertEquals(View.GONE, itemView.eachPrice.visibility)
    }

    @Test
    fun shouldOptionsTextViewBeGoneAndShowStubTotalPriceWhenProductVariantIsNull() {
        val orderProduct = MockInstantiator.newOrderProduct()
        given(orderProduct.productVariant).willReturn(null)
        itemView.setOrderProduct(orderProduct, MockInstantiator.DEFAULT_CURRENCY)
        assertEquals(context.getString(R.string.n_a_placeholder), itemView.totalPrice.text)
        assertEquals(View.GONE, itemView.optionsTextView.visibility)
        assertEquals(View.GONE, itemView.eachPrice.visibility)
    }
}