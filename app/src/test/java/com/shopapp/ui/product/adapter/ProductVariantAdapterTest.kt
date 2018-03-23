package com.shopapp.ui.product.adapter

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Image
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.item.ProductVariantItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProductVariantAdapterTest {

    companion object {
        private const val SIZE = 5
    }

    @Mock
    private lateinit var dataList: List<ProductVariant>

    @Mock
    private lateinit var clickListener: OnItemClickListener

    private lateinit var adapter: ProductVariantAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = ProductVariantAdapter(dataList, clickListener)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldCallSetVariantImage() {
        val itemView: ProductVariantItem = mock()
        val variantSrc = "variant_src"
        val variantImage: Image = mock {
            on { src } doReturn variantSrc
        }
        val productVariant: ProductVariant = mock {
            on { image } doReturn variantImage
        }
        adapter.bindData(itemView, productVariant, 0)
        verify(itemView).setImageURI(variantSrc)
    }

    @Test
    fun shouldCallSetProductImage() {
        val itemView: ProductVariantItem = mock()
        val productSrc = "product_src"
        val image: Image = mock {
            on { src } doReturn productSrc
        }
        val productVariant: ProductVariant = mock {
            on { productImage } doReturn image
        }
        adapter.bindData(itemView, productVariant, 0)
        verify(itemView).setImageURI(productSrc)
    }
}