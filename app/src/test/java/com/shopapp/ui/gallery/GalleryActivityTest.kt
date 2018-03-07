package com.shopapp.ui.gallery

import android.content.Context
import android.view.View
import com.shopapp.R
import com.shopapp.TestShopApplication
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

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class GalleryActivityTest {

    private lateinit var activity: GalleryActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        activity = Robolectric.setupActivity(GalleryActivity::class.java)
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