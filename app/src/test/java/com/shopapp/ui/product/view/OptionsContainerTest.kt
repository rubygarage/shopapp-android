package com.shopapp.ui.product.view

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.gateway.entity.ProductOption
import com.shopapp.gateway.entity.VariantOption
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.container_options.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class OptionsContainerTest {

    private lateinit var context: Context
    private lateinit var itemView: OptionsContainer
    private lateinit var productOption: ProductOption

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        itemView = OptionsContainer(context)
        productOption = MockInstantiator.newProductOption()
    }

    @Test
    fun shouldShowCorrectData() {
        itemView.setProductOption(productOption, null)
        assertEquals(context.getString(R.string.product_option_title_pattern, productOption.name), itemView.titleText.text.toString())
        assertEquals(productOption.values.size, itemView.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldReturnVariantOption() {
        itemView.setProductOption(productOption, null)
        assertEquals(VariantOption(productOption.name, productOption.values.first()),
            itemView.getSelectedVariantOption())
    }

    @Test
    fun shouldReturnPreselectedVariantOption() {
        val preselectedVariantOption = MockInstantiator.newVariantOption()
        itemView.setProductOption(productOption, preselectedVariantOption)
        assertEquals(preselectedVariantOption, itemView.getSelectedVariantOption())
    }

    @Test
    fun shouldReturnSelectedVariantByClick() {
        val productOption = MockInstantiator.newProductOption()
        itemView.setProductOption(productOption, null)
        val onSelectOptionListener: OptionsContainer.OnSelectOptionListener = mock()
        itemView.onSelectOptionListener = onSelectOptionListener
        itemView.onItemClicked(0)
        verify(onSelectOptionListener).onOptionsSelected()
    }

    @Test
    fun shouldReturnNullBeforeAdapterSet() {
        assertNull(itemView.getSelectedVariantOption())
    }

    @Test
    fun shouldDoNothingIfDataNotSet() {
        val onSelectOptionListener: OptionsContainer.OnSelectOptionListener = mock()
        itemView.onSelectOptionListener = onSelectOptionListener
        itemView.onItemClicked(0)
        verify(onSelectOptionListener, never()).onOptionsSelected()
    }
}