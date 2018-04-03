package com.shopapp.ui.cart

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.cart.adapter.CartAdapter
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import kotlinx.android.synthetic.main.view_lce_empty.view.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
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
    fun shouldStartCheckout() {
        activity.checkoutButton.performClick()
        verify(activity.router).showCheckout(activity)
    }

    @Test
    fun shouldStartHome() {
        activity.onEmptyButtonClicked()
        verify(activity.router).showHome(activity, true)
    }

    @Test
    fun shouldShowProductDetails() {
        val products = MockInstantiator.newList(MockInstantiator.newCartProduct(), 3)
        activity.showContent(products)
        activity.onItemClicked(0)
        verify(activity.router).showProduct(activity, products[0].productVariant)
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
    fun shouldShowEmptyState() {
        activity.showEmptyState()
        val id = shadowOf(activity.emptyView.emptyImage.drawable).createdFromResId
        assertEquals(R.drawable.ic_cart_empty, id)
        assertEquals(context.getString(R.string.empty_cart_message), activity.emptyView.emptyMessage.text)
        assertEquals(context.getString(R.string.start_shopping), activity.emptyView.emptyButton.text)
        assertEquals(View.VISIBLE, activity.emptyView.emptyButton.visibility)
    }

    @Test
    fun shouldRemoveProductWhenOnItemSwiped() {
        val viewHolder: RecyclerView.ViewHolder = mock()
        given(viewHolder.adapterPosition).willReturn(0)
        val products = MockInstantiator.newList(MockInstantiator.newCartProduct())
        activity.showContent(products)
        activity.onItemSwiped(viewHolder, 0)

        verify(activity.presenter).removeProduct(products.first().productVariant.productId)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}