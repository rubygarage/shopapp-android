package com.shopapp.ui.gallery

import android.content.Context
import android.view.View
import com.facebook.drawee.view.SimpleDraweeView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Image
import com.shopapp.ui.custom.zoomable.ZoomableDraweeView
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class ImageFragmentTest {

    private lateinit var fragment: ImageFragment
    private lateinit var context: Context

    private var image: Image = mock()

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldSetZoomableDraweeViewAsRoot() {
        fragment = SupportFragmentController
            .of(ImageFragment.newInstance(image, false))
            .create()
            .start()
            .get()

        assertTrue(fragment.view is ZoomableDraweeView)
    }

    @Test
    fun shouldSetDraweeViewAsRoot() {
        fragment = SupportFragmentController
            .of(ImageFragment.newInstance(image, true))
            .create()
            .start()
            .get()

        assertFalse(fragment.view is ZoomableDraweeView)
        assertTrue(fragment.view is SimpleDraweeView)
    }

    @Test
    fun shouldProceedOnClick() {
        val listener: View.OnClickListener = mock()
        val fragmentForSetup = ImageFragment.newInstance(image, true)
        fragmentForSetup.imageClickListener = listener

        fragment = SupportFragmentController
            .of(fragmentForSetup)
            .create()
            .start()
            .get()

        fragment.view?.performClick()
        verify(listener).onClick(any())
    }
}