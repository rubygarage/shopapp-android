package com.shopapp.ui.gallery

import android.content.Context
import com.shopapp.TestShopApplication
import com.shopapp.ui.account.SignInActivity
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
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
        MockitoAnnotations.initMocks(this)
        activity = Robolectric.setupActivity(GalleryActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }
}