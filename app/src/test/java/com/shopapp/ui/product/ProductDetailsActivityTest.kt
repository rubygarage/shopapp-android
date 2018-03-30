package com.shopapp.ui.product

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_lce_error.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class ProductDetailsActivityTest {

    private lateinit var context: Context
    private lateinit var productVariant: ProductVariant
    private lateinit var activity: ProductDetailsActivity

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        productVariant = MockInstantiator.newProductVariant()
        val intent = ProductDetailsActivity.getStartIntent(context, productVariant)
        activity = Robolectric.buildActivity(ProductDetailsActivity::class.java, intent).create().resume().get()
    }

    @Test
    fun shouldLoadDataWhenOnCreate() {
        verify(activity.presenter).loadProductDetails(productVariant.productId)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowContentView() {
        val data = MockInstantiator.newProduct()
        activity.showContent(data)
        assertEquals(data.title, activity.productTitle.text)
        assertEquals(data.productDescription, activity.description.text)
    }

    @Test
    fun shouldDescriptionBeCollapsed() {
        val data = MockInstantiator.newProduct()
        activity.showContent(data)
        assertEquals(0, activity.description.layoutParams.height)
        assertEquals(R.drawable.ic_plus,
            shadowOf(activity.descriptionLabel.compoundDrawablesRelative[2]).createdFromResId)
    }

    @Test
    fun shouldDescriptionBeExpanded() {
        val data = MockInstantiator.newProduct()
        activity.showContent(data)
        activity.descriptionLabel.performClick()
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, activity.description.layoutParams.height)
        assertEquals(R.drawable.ic_minus,
            shadowOf(activity.descriptionLabel.compoundDrawables[2]).createdFromResId)
    }

    @Test
    fun shouldCallAddProductToCart() {
        val quantity = 5
        val data = MockInstantiator.newProduct()
        activity.showContent(data)
        activity.onVariantSelected(productVariant)
        activity.quantityEditText.setText(quantity.toString())
        activity.cartButton.performClick()
        verify(activity.presenter).addProductToCart(productVariant, data.title, data.currency, quantity.toString())
    }

    @Test
    fun shouldShowSuccessToast() {
        activity.productAddedToCart()
        val looper = shadowOf(activity.mainLooper)
        looper.idle()
        assertEquals(ShadowToast.getTextOfLatestToast(), context.getString(R.string.product_added))
    }

    @Test
    fun shouldClearQuantityViewFocus() {
        val intent = ProductDetailsActivity.getStartIntent(context, productVariant)
        val activity = Robolectric.buildActivity(ProductDetailsActivity::class.java, intent)
            .create()
            .resume()
            .get()

        assertFalse(activity.quantityEditText.isFocused)
    }

    @Test
    fun shouldHideRecentContainerWhenCallChangeVisibilityWithVisibleFalse() {
        activity.showContent(MockInstantiator.newProduct())
        val fragment = activity.supportFragmentManager.findFragmentById(R.id.relatedContainer) as ProductHorizontalFragment
        fragment.visibilityListener?.changeVisibility(false)
        assertEquals(View.GONE, activity.relatedContainer.visibility)
    }

    @Test
    fun shouldShowNotFoundErrorWithTargetWhenCriticalErrorReceived() {
        val target = context.getString(R.string.product)
        activity.showError(Error.Critical())
        assertEquals(View.VISIBLE, activity.lceLayout.errorView.visibility)
        assertEquals(context.getString(R.string.сould_not_find_with_placeholder, target),
            activity.lceLayout.errorView.errorMessage.text)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}