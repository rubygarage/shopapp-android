package com.shopapp.ui.checkout.view

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.verify
import kotlinx.android.synthetic.main.view_checkout_failure.view.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CheckoutFailureViewTest {

    @Mock
    private lateinit var tryAgainListener: View.OnClickListener

    @Mock
    private lateinit var backToShopListener: View.OnClickListener

    private lateinit var context: Context
    private lateinit var view: CheckoutFailureView

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        context = RuntimeEnvironment.application.baseContext
        view = CheckoutFailureView(context)
        view.setListeners(tryAgainListener, backToShopListener)
    }

    @Test
    fun shouldCallTryAgainWhenButtonClicked() {
        view.tryAgainCheckoutButton.performClick()
        verify(tryAgainListener).onClick(view.tryAgainCheckoutButton)
    }

    @Test
    fun shouldCallBackToShopWhenButtonClicked() {
        view.backToShop.performClick()
        verify(backToShopListener).onClick(view.backToShop)
    }
}