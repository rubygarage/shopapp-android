package com.shopapp.ui.product.view

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.VariantOption
import com.shopapp.test.MockInstantiator
import org.junit.Assert.assertEquals
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
class OptionsGroupContainerTest {

    @Mock
    private lateinit var product: Product

    @Mock
    private lateinit var onVariantSelectListener: OptionsGroupContainer.OnVariantSelectListener

    private lateinit var context: Context
    private lateinit var itemView: OptionsGroupContainer

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        context = RuntimeEnvironment.application.baseContext
        itemView = OptionsGroupContainer(context)
    }

    @Test
    fun shouldViewBeGoneWhenOptionListIsEmpty() {
        given(product.options).willReturn(emptyList())
        assertEquals(View.VISIBLE, itemView.visibility)
        itemView.setProduct(product, null)
        assertEquals(View.GONE, itemView.visibility)
    }

    @Test
    fun shouldViewBeGoneWhenOptionListWithOneItem() {
        val productOptions = MockInstantiator.newList(MockInstantiator.newProductOption(), 1)
        given(product.options).willReturn(productOptions)
        assertEquals(View.VISIBLE, itemView.visibility)
        itemView.setProduct(product, null)
        assertEquals(View.GONE, itemView.visibility)
    }

    @Test
    fun shouldViewBeVisibleWhenOptionListIsNotEmpty() {
        val productOptions = MockInstantiator.newList(MockInstantiator.newProductOption(), 2)
        given(product.options).willReturn(productOptions)
        assertEquals(View.VISIBLE, itemView.visibility)
        itemView.setProduct(product, null)
        assertEquals(View.VISIBLE, itemView.visibility)
    }

    @Test
    fun shouldAddCorrectOptionsContainersCount() {
        val size = 2
        val productOptions = MockInstantiator.newList(MockInstantiator.newProductOption(), size)
        given(product.options).willReturn(productOptions)
        assertEquals(1, itemView.childCount)
        itemView.setProduct(product, null)
        assertEquals(size + 1, itemView.childCount)
    }

    @Test
    fun shouldSelectFirstVariantByDefault() {
        itemView.variantSelectListener = onVariantSelectListener

        val firstVariantValue = "firstVariant"
        val firstProductVariant = MockInstantiator.newProductVariant()
        val firstVariantOption = VariantOption(MockInstantiator.DEFAULT_NAME, firstVariantValue)

        val secondVariantValue = "secondVariant"
        val secondProductVariant = MockInstantiator.newProductVariant()
        val secondVariantOption = VariantOption(MockInstantiator.DEFAULT_NAME, secondVariantValue)

        val productOption = MockInstantiator.newProductOption()

        given(firstProductVariant.selectedOptions).willReturn(listOf(firstVariantOption, secondVariantOption))
        given(productOption.values).willReturn(listOf(firstVariantValue, secondVariantValue))

        given(product.options).willReturn(listOf(productOption))
        given(product.variants).willReturn(listOf(firstProductVariant, secondProductVariant))

        itemView.setProduct(product, null)
        verify(onVariantSelectListener).onVariantSelected(product.variants.first())
    }

    @Test
    fun shouldSelectPreselectedVariant() {
        itemView.variantSelectListener = onVariantSelectListener

        val simpleVariant = MockInstantiator.newProductVariant()
        val simpleVariantOption = MockInstantiator.newVariantOption()
        given(simpleVariant.selectedOptions).willReturn(listOf(simpleVariantOption))
        val simpleProductOption = MockInstantiator.newProductOption()

        val preselectedVariant = MockInstantiator.newProductVariant()
        val preselectedVariantOption = MockInstantiator.newVariantOption()
        given(preselectedVariant.selectedOptions).willReturn(listOf(preselectedVariantOption))
        val preselectedProductOption = MockInstantiator.newProductOption()

        given(product.options).willReturn((listOf(simpleProductOption, preselectedProductOption)))
        given(product.variants).willReturn(listOf(simpleVariant, preselectedVariant))

        itemView.setProduct(product, preselectedVariant)
        verify(onVariantSelectListener).onVariantSelected(product.variants[1])
    }
}