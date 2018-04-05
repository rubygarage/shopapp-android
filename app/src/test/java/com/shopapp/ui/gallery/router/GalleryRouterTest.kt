package com.shopapp.ui.gallery.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.cart.CartActivity
import com.shopapp.ui.gallery.GalleryActivity
import com.shopapp.ui.gallery.GalleryFragment
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class GalleryRouterTest {

    private lateinit var activity: CartActivity
    private lateinit var router: GalleryRouter

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(CartActivity::class.java)
        router = GalleryRouter()
    }

    @Test
    fun shouldStartGalleryActivityWhenThumbnailImageClicked() {
        val product = MockInstantiator.newProduct()
        val index = 3
        val fragment = SupportFragmentController
            .of(GalleryFragment.newInstance(product, true, index))
            .create()
            .start()
            .get()

        router.showFullGallery(fragment.context, product, fragment.pager.currentItem)

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(product, startedIntent.extras.getParcelable(GalleryActivity.EXTRA_PRODUCT))
        assertEquals(fragment.pager.currentItem, startedIntent.extras.getInt(GalleryActivity.EXTRA_SELECTED_POSITION))
        assertEquals(GalleryActivity::class.java, shadowIntent.intentClass)
    }
}