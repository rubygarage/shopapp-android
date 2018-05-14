package com.shopapp.ui.item.category

import android.content.Context
import android.support.v4.content.ContextCompat
import com.shopapp.R
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_category_grid.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CategoryItemGridTest {

    private lateinit var context: Context
    private lateinit var itemView: CategoryGridItem

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldDisplayCategoryData() {
        itemView = CategoryGridItem(context)
        assertEquals(ContextCompat.getDrawable(context, R.color.white), itemView.background)

        val category = MockInstantiator.newCategory()
        itemView.setCategory(category)
        assertEquals(MockInstantiator.DEFAULT_TITLE, itemView.titleText.text)
        assertNotNull(itemView.image)
    }
}