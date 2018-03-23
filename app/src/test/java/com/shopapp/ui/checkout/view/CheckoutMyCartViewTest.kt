package com.shopapp.ui.checkout.view

import android.app.Application
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.product.ProductDetailsActivity
import kotlinx.android.synthetic.main.view_checkout_my_cart.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CheckoutMyCartViewTest {

    private lateinit var application: Application
    private lateinit var view: CheckoutMyCartView

    @Before
    fun setUpTest() {
        application = RuntimeEnvironment.application
        view = CheckoutMyCartView(application)
    }

    @Test
    fun shouldSetData() {
        val data = listOf<CartProduct>(mock(), mock())
        view.setData(data)
        assertEquals(data.size, view.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldStartProductDetailsActivityWhenOnItemClicked() {
        val productVariantMock = MockInstantiator.newProductVariant()
        val cartProduct: CartProduct = mock {
            on { productVariant } doReturn productVariantMock
        }
        val data = listOf(cartProduct)
        view.setData(data)
        view.onItemClicked(0)

        val startedIntent = Shadows.shadowOf(application).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(productVariantMock.productId,
            startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertEquals(productVariantMock,
            startedIntent.extras.getParcelable(ProductDetailsActivity.EXTRA_PRODUCT_VARIANT))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldNotStartProductDetailsActivityWhenOnItemClickedWithInvalidPosition() {
        val productVariantMock = MockInstantiator.newProductVariant()
        val cartProduct: CartProduct = mock {
            on { productVariant } doReturn productVariantMock
        }
        val data = listOf(cartProduct)
        view.setData(data)
        view.onItemClicked(data.size + 1)
        assertNull(Shadows.shadowOf(application).nextStartedActivity)
    }
}