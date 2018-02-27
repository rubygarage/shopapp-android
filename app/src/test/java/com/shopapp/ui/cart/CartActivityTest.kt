package com.shopapp.ui.cart

import android.content.Context
import com.shopapp.R
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment

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
        Assert.assertEquals(context.getString(R.string.my_cart), activity.toolbar.toolbarTitle.text)
    }



    @After
    fun tearDown() {
        activity.finish()
    }
}