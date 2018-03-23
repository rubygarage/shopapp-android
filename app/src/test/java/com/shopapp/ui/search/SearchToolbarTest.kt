package com.shopapp.ui.search

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.only
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_search_with_categories.*
import kotlinx.android.synthetic.main.toolbar_search.view.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class SearchToolbarTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var listener: SearchToolbar.SearchToolbarListener
    private lateinit var context: Context
    private lateinit var toolbar: SearchToolbar

    @Before
    fun setUp() {
        listener = mock()
        context = RuntimeEnvironment.application.baseContext
        val fragment = SearchWithCategoriesFragment()
        SupportFragmentTestUtil.startFragment(fragment, HomeActivity::class.java)
        toolbar = fragment.searchToolbar
        toolbar.searchToolbarListener = listener
    }

    @Test
    fun shouldBeCollapsed() {
        assertFalse(toolbar.isToolbarExpanded())
    }

    @Test
    fun shouldBeExpanded() {
        toolbar.changeToolbarState()
        assertTrue(toolbar.isToolbarExpanded())
        assertEquals(0, toolbar.searchInput.layoutParams.width)
        assertTrue(toolbar.searchInput.isFocusableInTouchMode)
    }

    @Test
    fun shouldHideCartWidgetOnExpand() {
        toolbar.changeToolbarState()
        assertTrue(toolbar.isToolbarExpanded())
        assertEquals(View.GONE, toolbar.cartWidget.visibility)
    }

    @Test
    fun shouldChangeIconToArrowOnExpand() {
        toolbar.changeToolbarState()
        assertTrue(toolbar.isToolbarExpanded())
        val drawableId = shadowOf(toolbar.searchIcon.drawable).createdFromResId
        assertEquals(R.drawable.ic_arrow_left, drawableId)
    }

    @Test
    fun shouldChangeBottomLineOnExpand() {
        toolbar.changeToolbarState()
        assertTrue(toolbar.isToolbarExpanded())
        val drawableId = shadowOf(toolbar.bottomLine.background).createdFromResId
        assertEquals(R.color.colorPrimary, drawableId)
        val params = toolbar.bottomLine.layoutParams as ViewGroup.MarginLayoutParams
        assertEquals(context.resources.getDimensionPixelSize(R.dimen.search_toolbar_expanded_line_margin_start), params.leftMargin)
        assertEquals(context.resources.getDimensionPixelSize(R.dimen.search_toolbar_expanded_line_margin_end), params.rightMargin)
    }

    @Test
    fun shouldChangeBottomLineOnCollapse() {
        toolbar.changeToolbarState()
        toolbar.changeToolbarState()
        assertFalse(toolbar.isToolbarExpanded())
        val drawableId = shadowOf(toolbar.bottomLine.background).createdFromResId
        assertEquals(R.color.colorBackgroundDark, drawableId)
        val params = toolbar.bottomLine.layoutParams as ViewGroup.MarginLayoutParams
        assertEquals(context.resources.getDimensionPixelSize(R.dimen.search_toolbar_collapsed_line_margin), params.leftMargin)
        assertEquals(context.resources.getDimensionPixelSize(R.dimen.search_toolbar_collapsed_line_margin), params.rightMargin)
    }

    @Test
    fun shouldChangeIconToArrowOnCollapse() {
        toolbar.changeToolbarState()
        toolbar.changeToolbarState()
        assertFalse(toolbar.isToolbarExpanded())
        val drawableId = shadowOf(toolbar.searchIcon.drawable).createdFromResId
        assertEquals(R.drawable.ic_searchbar, drawableId)
    }

    @Test
    fun shouldShowCartWidgetOnCollapse() {
        toolbar.changeToolbarState()
        toolbar.changeToolbarState()
        assertFalse(toolbar.isToolbarExpanded())
        assertEquals(View.VISIBLE, toolbar.cartWidget.visibility)
    }

    @Test
    fun shouldClearTextOnCollapse() {
        toolbar.changeToolbarState()
        toolbar.changeToolbarState()
        assertFalse(toolbar.isToolbarExpanded())
        assertEquals("", toolbar.searchInput.text.toString())
    }

    @Test
    fun shouldChangeToolbarState() {
        toolbar.searchIcon.performClick()
        assertTrue(toolbar.isToolbarExpanded())
        toolbar.searchIcon.performClick()
        assertFalse(toolbar.isToolbarExpanded())
    }

    @Test
    fun shouldClearText() {
        toolbar.searchInput.setText("dummy text")
        toolbar.clear.performClick()
        assertEquals("", toolbar.searchInput.text.toString())
    }

    @Test
    fun shouldDoNothingIfCollapsed() {
        assertFalse(toolbar.isToolbarExpanded())
        toolbar.clickableArea.performClick()
        assertFalse(toolbar.isToolbarExpanded())
    }

    @Test
    fun shouldCollapseIfExpanded() {
        assertFalse(toolbar.isToolbarExpanded())
        toolbar.changeToolbarState()
        assertTrue(toolbar.isToolbarExpanded())
        toolbar.clickableArea.performClick()
        assertFalse(toolbar.isToolbarExpanded())
    }

    @Test
    fun shouldDoNothingIfExpanded() {
        toolbar.changeToolbarState()
        assertTrue(toolbar.isToolbarExpanded())
        toolbar.searchInput.performClick()
        assertTrue(toolbar.isToolbarExpanded())
    }

    @Test
    fun shouldExpandIfCollapsed() {
        assertFalse(toolbar.isToolbarExpanded())
        toolbar.searchInput.performClick()
        assertTrue(toolbar.isToolbarExpanded())
    }

    @Test
    fun shouldHideClearButton() {
        shadowOf(toolbar).callOnAttachedToWindow()
        toolbar.searchInput.setText("sometext")
        assertEquals(View.GONE, toolbar.clear.visibility)
    }

    @Test
    fun shouldCallListener() {
        shadowOf(toolbar).callOnAttachedToWindow()
        toolbar.changeToolbarState()
        toolbar.searchInput.setText("sometext")
        verify(listener).onQueryChanged("sometext")
    }

    @Test
    fun shouldNotCallListener() {
        shadowOf(toolbar).callOnAttachedToWindow()
        toolbar.changeToolbarState()
        toolbar.searchInput.setText("sometext")
        verify(listener).onQueryChanged("sometext")

        shadowOf(toolbar).callOnDetachedFromWindow()
        toolbar.changeToolbarState()
        toolbar.searchInput.setText("someAnotherText")
        verify(listener, never()).onQueryChanged("someAnotherText")
    }

    @Test
    fun shouldCallQueryChangedWhenSearchEditorButtonClicked() {
        toolbar.searchInput.setText("sometext")
        toolbar.searchInput.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        verify(listener).onQueryChanged("sometext")
    }

    @Test
    fun shouldCallQueryChangedWhenNotSearchEditorButtonClicked() {
        toolbar.searchInput.setText("sometext")
        toolbar.searchInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
        verify(listener, never()).onQueryChanged("sometext")
    }
}