package com.shopapp.ui.category.adapter

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.item.CategoryItem
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
class CategoryGridAdapterTest {

    companion object {
        private const val SIZE = 4
    }

    @Mock
    private lateinit var dataList: List<Category>

    @Mock
    private lateinit var clickListener: OnItemClickListener

    private lateinit var adapter: CategoryGridAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = CategoryGridAdapter(dataList, clickListener)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldReturnCorrectType() {
        val itemView = adapter.getItemView(RuntimeEnvironment.application.applicationContext, 0)
        assertTrue(itemView is CategoryItem)
    }

    @Test
    fun shouldCallSetCategory() {
        val itemView: CategoryItem = mock()
        val category: Category = mock()
        adapter.bindData(itemView, category, 0)
        verify(itemView).setCategory(category)
    }
}