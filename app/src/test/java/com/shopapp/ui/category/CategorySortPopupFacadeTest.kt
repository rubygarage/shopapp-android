package com.shopapp.ui.category

import android.content.Context
import android.view.View
import android.widget.TextView
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.SortType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CategorySortPopupFacadeTest {

    private lateinit var facade: CategorySortPopupFacade
    private lateinit var context: Context
    private var listener: CategorySortPopupFacade.OnSortTypeChangeListener = mock()

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        facade = CategorySortPopupFacade(context, listener)
    }

    @Test
    fun shouldShowPopup() {
        val anchor = View(context)
        facade.showSortPopup(anchor, SortType.TYPE)
        val popup = Shadows.shadowOf(RuntimeEnvironment.application).latestPopupWindow

        assertNotNull(popup)
    }

    @Test
    fun shouldNewestBeCheckedWithRecentSortType() {
        val anchor = View(context)
        facade.showSortPopup(anchor, SortType.RECENT)
        val popup = Shadows.shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)

        val view = popup.contentView.findViewById<TextView>(R.id.newestSort)
        val drawable = view.compoundDrawables[2]

        assertNotNull(drawable)
        assertEquals(R.drawable.ic_check_black, shadowOf(drawable).createdFromResId)
    }

    @Test
    fun shouldHighToLowBeCheckedWithHighToLowSortType() {
        val anchor = View(context)
        facade.showSortPopup(anchor, SortType.PRICE_HIGH_TO_LOW)
        val popup = Shadows.shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)

        val view = popup.contentView.findViewById<TextView>(R.id.highToLowSort)
        val drawable = view.compoundDrawables[2]

        assertNotNull(drawable)
        assertEquals(R.drawable.ic_check_black, shadowOf(drawable).createdFromResId)
    }

    @Test
    fun shouldLowToHighBeCheckedWithLowToHighSortType() {
        val anchor = View(context)
        facade.showSortPopup(anchor, SortType.PRICE_LOW_TO_HIGH)
        val popup = Shadows.shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)

        val view = popup.contentView.findViewById<TextView>(R.id.lowToHighSort)
        val drawable = view.compoundDrawables[2]

        assertNotNull(drawable)
        assertEquals(R.drawable.ic_check_black, shadowOf(drawable).createdFromResId)
    }

    @Test
    fun shouldReturnRecentSortType() {
        val anchor = View(context)
        facade.showSortPopup(anchor, SortType.TYPE)
        val popup = Shadows.shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)

        val newestSort: TextView = popup.contentView.findViewById(R.id.newestSort)
        assertNotNull(newestSort)

        newestSort.performClick()
        verify(listener).onSortTypeChanged(SortType.RECENT)
        assertFalse(popup.isShowing)
    }

    @Test
    fun shouldReturnHighToLowSortType() {
        val anchor = View(context)
        facade.showSortPopup(anchor, SortType.TYPE)
        val popup = Shadows.shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)

        val highToLowSort: TextView = popup.contentView.findViewById(R.id.highToLowSort)
        assertNotNull(highToLowSort)

        highToLowSort.performClick()
        verify(listener).onSortTypeChanged(SortType.PRICE_HIGH_TO_LOW)
        assertFalse(popup.isShowing)
    }

    @Test
    fun shouldReturnLowToHighSortType() {
        val anchor = View(context)
        facade.showSortPopup(anchor, SortType.TYPE)
        val popup = Shadows.shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)

        val lowToHighSort: TextView = popup.contentView.findViewById(R.id.lowToHighSort)
        assertNotNull(lowToHighSort)

        lowToHighSort.performClick()
        verify(listener).onSortTypeChanged(SortType.PRICE_LOW_TO_HIGH)
        assertFalse(popup.isShowing)
    }

    @Test
    fun shouldSetDefaultSortType() {
        val anchor = View(context)
        facade.showSortPopup(anchor, SortType.RECENT)
        val popup = Shadows.shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)

        val newestSort: TextView = popup.contentView.findViewById(R.id.newestSort)
        assertNotNull(newestSort)

        newestSort.performClick()
        verify(listener).onSortTypeChanged(SortType.NAME)
        assertFalse(popup.isShowing)
    }
}