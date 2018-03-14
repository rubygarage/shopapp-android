package com.shopapp.ui.product.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.gateway.entity.VariantOption
import com.shopapp.ui.base.recycler.OnItemClickListener
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
class ProductOptionsAdapterTest {

    companion object {
        private const val SIZE = 5
        private const val TEST_DATA = "test data"
    }

    @Mock
    private lateinit var defaultVariantOptions: VariantOption

    @Mock
    private lateinit var dataList: List<String>

    @Mock
    private lateinit var clickListener: OnItemClickListener

    @Mock
    private lateinit var itemView: TextView

    private lateinit var adapter: ProductOptionsAdapter
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = ProductOptionsAdapter(defaultVariantOptions, dataList, clickListener)
        context = RuntimeEnvironment.application.baseContext
        given(itemView.context).willReturn(context)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldCallSetText() {
        adapter.bindData(itemView, TEST_DATA, 0)
        verify(itemView).text = TEST_DATA
    }

    @Test
    fun shouldSetSelectedStyles() {
        given(defaultVariantOptions.value).willReturn(TEST_DATA)
        adapter.bindData(itemView, TEST_DATA, 0)
        verify(itemView).setBackgroundResource(R.drawable.background_selected_variant)
        verify(itemView).setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    @Test
    fun shouldSetUnSelectedStyles() {
        given(defaultVariantOptions.value).willReturn("")
        adapter.bindData(itemView, TEST_DATA, 0)
        verify(itemView).setBackgroundResource(R.drawable.background_unselected_variant)
        verify(itemView).setTextColor(ContextCompat.getColor(context, R.color.black))
    }

    @Test
    fun shouldReturnTextView(){
        val view = adapter.getItemView(context, 0)
        assertTrue(view is TextView)
    }

    @Test
    fun shouldSetUnSelectedStylesOnNullSelectedVariant(){
        adapter.selectedVariantOptions = null
        adapter.bindData(itemView, TEST_DATA, 0)
        verify(itemView).setBackgroundResource(R.drawable.background_unselected_variant)
        verify(itemView).setTextColor(ContextCompat.getColor(context, R.color.black))
    }
}