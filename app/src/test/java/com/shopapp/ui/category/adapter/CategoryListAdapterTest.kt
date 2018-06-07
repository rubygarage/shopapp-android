package com.shopapp.ui.category.adapter

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.item.category.CategoryGridItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CategoryListAdapterTest {

    companion object {
        private const val SIZE = 4
    }

    @Mock
    private lateinit var dataList: List<Category>

    @Mock
    private lateinit var clickListener: OnItemClickListener

    private lateinit var adapter: CategoryListAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = CategoryListAdapter(dataList, clickListener)
    }

    @Test
    fun shouldReturnCategoryGridItem() {
        adapter = CategoryListAdapter(dataList, clickListener)
        val itemView = adapter.getItemView(RuntimeEnvironment.application.applicationContext, 0)
        assertNotNull(itemView)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldCallSetCategory() {
        val itemView: CategoryGridItem = mock()
        val category: Category = mock()
        adapter.bindData(itemView, category, 0)
        verify(itemView).setCategory(category)
    }
}