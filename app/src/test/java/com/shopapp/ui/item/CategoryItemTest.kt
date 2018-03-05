package com.shopapp.ui.item

import android.content.Context
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_category.view.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CategoryItemTest {

    private lateinit var context: Context
    private lateinit var itemView: CategoryItem

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        itemView = CategoryItem(context)
    }

    @Test
    fun shouldDisplayCategoryData() {
        val category = MockInstantiator.newCategory()
        itemView.setCategory(category)
        Assert.assertEquals(MockInstantiator.DEFAULT_TITLE, itemView.titleText.text)
    }
}