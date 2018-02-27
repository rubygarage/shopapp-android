package com.shopapp.ui.cart

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.cart.adapter.CartAdapter
import com.shopapp.ui.checkout.CheckoutActivity
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.product.ProductDetailsActivity
import com.shopapp.util.MockInstantiator
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CartActivityTest {

    private lateinit var activity: CartActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(CartActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(context.getString(R.string.my_cart), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldAddLinearLayoutManagerOnCreate() {
        assertNotNull(activity.recyclerView.layoutManager)
        assertTrue(activity.recyclerView.layoutManager is LinearLayoutManager)
        val manager = activity.recyclerView.layoutManager as LinearLayoutManager
        assertEquals(OrientationHelper.VERTICAL, manager.orientation)
    }

    @Test
    fun shouldAddAdapterOnCreate() {
        assertNotNull(activity.recyclerView.adapter)
        assertTrue(activity.recyclerView.adapter is CartAdapter)
        val adapter = activity.recyclerView.adapter as CartAdapter
        assertEquals(0, adapter.itemCount)
        shadowOf(activity.recyclerView).onTouchListener
    }

    @Test
    fun shouldLoadProductsOnCreate() {
        verify(activity.presenter).loadCartItems()
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowProductsList() {
        val count = 5
        val products = MockInstantiator.newList(MockInstantiator.newCartProduct(), count)
        activity.showContent(products)
        assertEquals(View.GONE, activity.emptyView.visibility)
        assertEquals(count, activity.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldStartCheckoutActivity() {
        activity.checkoutButton.performClick()
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(CheckoutActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartHomeActivity() {
        activity.onEmptyButtonClicked()
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(HomeActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldRemoveItem() {
        val productVariantId = "productVariantId"
        activity.onRemoveButtonClicked(productVariantId)
        verify(activity.presenter).removeProduct(productVariantId)
    }

    @Test
    fun shouldChangeVariantQuantity() {
        val productVariantId = "productVariantId"
        val quantity = 2
        activity.onQuantityChanged(productVariantId, quantity)
        verify(activity.presenter).changeProductQuantity(productVariantId, quantity)
    }

    @Test
    fun shouldStartProductDetailsActivity() {
        val products = MockInstantiator.newList(MockInstantiator.newCartProduct(), 3)
        activity.showContent(products)
        activity.onItemClicked(0)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertNotNull(startedIntent.extras.getParcelable(ProductDetailsActivity.EXTRA_PRODUCT_VARIANT))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}