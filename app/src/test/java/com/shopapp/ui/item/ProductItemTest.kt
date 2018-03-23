package com.shopapp.ui.item

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.Product
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_product.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProductItemTest {

    companion object {
        private const val WIDTH = 100
        private const val HEIGHT = 200
        private const val FORMATTED_PRICE = "price 50"
    }

    @Mock
    private lateinit var numberFormatter: NumberFormatter

    private lateinit var context: Context
    private lateinit var itemView: ProductItem
    private lateinit var product: Product

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        context = RuntimeEnvironment.application.baseContext
        itemView = ProductItem(context, WIDTH, HEIGHT, numberFormatter)

        product = MockInstantiator.newProduct()
        given(numberFormatter.formatPrice(any(), any())).willReturn(FORMATTED_PRICE)
    }

    @Test
    fun shouldSetCorrectSize() {
        assertEquals(WIDTH, itemView.layoutParams.width)
        assertEquals(HEIGHT, itemView.layoutParams.height)
    }

    @Test
    fun shouldSetProductDataWithAlternativePrice() {
        given(product.hasAlternativePrice).willReturn(true)
        itemView.setProduct(product)

        assertEquals(product.title, itemView.titleTextView.text)
        assertEquals(context.getString(R.string.range_price,
            numberFormatter.formatPrice(product.price, product.currency)), itemView.price.text)
    }

    @Test
    fun shouldSetProductDataWithoutAlternativePrice() {
        given(product.hasAlternativePrice).willReturn(false)
        itemView.setProduct(product)

        assertEquals(product.title, itemView.titleTextView.text)
        assertEquals(numberFormatter.formatPrice(product.price, product.currency), itemView.price.text)
    }
}