package com.shopapp.ui.gallery

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Product
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class GalleryFragmentTest {

    companion object {
        private const val SELECTED_INDEX = 0
    }

    private lateinit var controller: SupportFragmentController<GalleryFragment>
    private lateinit var context: Context
    private lateinit var product: Product

    @Before
    fun setUp() {
        product = MockInstantiator.newProduct()
        context = RuntimeEnvironment.application.baseContext
        val fragment = GalleryFragment.newInstance(product, false, SELECTED_INDEX)
        controller = SupportFragmentController.of(fragment)
    }

    @Test
    fun shouldSetupPager() {
        val fragment = controller.create().start().get()
        assertEquals(SELECTED_INDEX, fragment.pager.currentItem)
    }

    @Test
    fun shouldShowPageIndicator() {
        val fragment = controller.create().start().get()
        assertEquals(View.VISIBLE, fragment.indicator.visibility)
    }

    @Test
    fun shouldHidePageIndicator() {
        given(product.images).willReturn(listOf())
        val fragment = controller.create().start().get()
        assertEquals(View.INVISIBLE, fragment.indicator.visibility)

    }

    @Test
    fun shouldHidePlaceHolder() {
        val fragment = controller.create().start().get()
        assertEquals(View.GONE, fragment.noImagePlaceholder.visibility)
    }

    @Test
    fun shouldShowPlaceHolder() {
        given(product.images).willReturn(listOf())
        val fragment = controller.create().start().get()
        assertEquals(View.VISIBLE, fragment.noImagePlaceholder.visibility)
    }

}