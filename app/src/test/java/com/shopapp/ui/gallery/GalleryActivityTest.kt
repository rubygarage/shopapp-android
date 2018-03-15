package com.shopapp.ui.gallery

import android.content.Context
import android.view.View
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Product
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.gallery.GalleryActivity.Companion.EXTRA_PRODUCT
import kotlinx.android.synthetic.main.activity_gallery.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.fakes.RoboMenuItem

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class GalleryActivityTest {

    private lateinit var activity: GalleryActivity
    private lateinit var context: Context
    private lateinit var product: Product
    private val position = 5

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        product = MockInstantiator.newProduct()
        activity = Robolectric.buildActivity(GalleryActivity::class.java,
            GalleryActivity.getStartIntent(context, product, position))
            .create()
            .resume()
            .get()
    }

    @Test
    fun shouldReceiveCorrectData() {
        assertEquals(product, activity.intent.getParcelableExtra<Product>(EXTRA_PRODUCT))
        assertEquals(position, activity.intent.getIntExtra(GalleryActivity.EXTRA_SELECTED_POSITION, -1))
    }

    @Test
    fun shouldFinishActivityWhenBackButtonClicked() {
        val menuItem = RoboMenuItem(android.R.id.home)
        activity.onOptionsItemSelected(menuItem)

        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldDoNothingWhenAnotherMenuItemClicked() {
        val menuItem = RoboMenuItem(android.R.id.edit)
        activity.onOptionsItemSelected(menuItem)

        assertFalse(activity.isFinishing)
    }

    @Test
    fun shouldShowGalleryFragment() {
        val fragment = activity.supportFragmentManager.findFragmentById(R.id.galleryContainer)
        assertNotNull(fragment)
        assertTrue(fragment is GalleryFragment)
    }

    @Test
    fun shouldHideToolbar() {
        val fragment = activity.supportFragmentManager.findFragmentById(R.id.galleryContainer)

        assertNotNull(fragment)
        assertTrue(fragment is GalleryFragment)

        val galleryFragment = fragment as GalleryFragment
        activity.toolbar.visibility = View.VISIBLE
        galleryFragment.imageClickListener?.onClick(View(context))

        assertEquals(View.GONE, activity.toolbar.visibility)
    }

    @Test
    fun shouldShowToolbar() {
        val fragment = activity.supportFragmentManager.findFragmentById(R.id.galleryContainer)

        assertNotNull(fragment)
        assertTrue(fragment is GalleryFragment)

        val galleryFragment = fragment as GalleryFragment
        activity.toolbar.visibility = View.GONE
        galleryFragment.imageClickListener?.onClick(View(context))

        assertEquals(View.VISIBLE, activity.toolbar.visibility)
    }

    @After
    fun tearDown() {
        activity.finish()
    }

}