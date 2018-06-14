package com.shopapp.ui.view

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.test.robolectric.ShadowTextInputLayoutWithoutInheritance
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, shadows = [ShadowTextInputLayoutWithoutInheritance::class])
class QuantityViewTest {

    private lateinit var context: Context
    private lateinit var view: QuantityView
    private val downEvent = MotionEvent.obtain(
        10L, 10L, MotionEvent.ACTION_DOWN,
        0f, 0f, 0
    )
    private val listener: QuantityView.OnQuantityChangeListener = mock()

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val contextWrapper = ContextThemeWrapper(context, R.style.AppTheme)
        view = QuantityView(contextWrapper)
    }

    @Test
    fun shouldDoNothingWhenViewReceiveDifferentMotionEvent() {
        val scrollEvent = MotionEvent.obtain(
            10L, 10L, MotionEvent.ACTION_SCROLL,
            0f, 0f, 0
        )
        view.onTouchEvent(scrollEvent)

        val popup = shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNull(popup)

        val dialog = shadowOf(RuntimeEnvironment.application).latestDialog
        assertNull(dialog)
    }

    @Test
    fun shouldShowNumberPopupWindowWhenTextIsEmpty() {
        view.onTouchEvent(downEvent)
        val popup = shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)
    }

    @Test
    fun shouldShowNumberPopupWindowWhenQuantityLessThenMaxQuantityNumber() {
        view.text = (QuantityView.MAX_QUANTITY_NUMBER - 1).toString()
        view.onTouchEvent(downEvent)
        val popup = shadowOf(RuntimeEnvironment.application).latestPopupWindow

        assertNotNull(popup)
    }

    @Test
    fun shouldShowNumberPopupWithCorrectChildCount() {
        view.onTouchEvent(downEvent)
        val popup = shadowOf(RuntimeEnvironment.application).latestPopupWindow
        val rootView = popup.contentView
        val viewContainer = rootView.findViewById<ViewGroup>(R.id.quantityNumberContainer)

        assertEquals(QuantityView.MAX_QUANTITY_NUMBER, viewContainer.childCount)
    }

    @Test
    fun shouldShowNumberPopupWithUnselectedFirstItem() {
        view.onTouchEvent(downEvent)
        val (_, item) = getChildFromNumberContainer(0)
        val viewBackground = item.background

        assertNotEquals(R.color.selectedColor, shadowOf(viewBackground).createdFromResId)
    }

    @Test
    fun shouldShowNumberPopupWithSelectedFirstItem() {
        view.text = 1.toString()
        view.onTouchEvent(downEvent)
        val (_, item) = getChildFromNumberContainer(0)
        val viewBackground = item.background

        assertEquals(R.color.selectedColor, shadowOf(viewBackground).createdFromResId)
    }

    @Test
    fun shouldDoNothingWhenSelectSameNumberAsCurrentText() {
        view.quantityChangeListener = listener
        view.text = 1.toString()
        view.onTouchEvent(downEvent)
        val (_, item) = getChildFromNumberContainer(0)
        item.performClick()

        verify(listener, never()).onQuantityChanged(any())
    }

    @Test
    fun shouldCallOnQuantityChangedAndSetNewQuantityWhenSelectDifferentNumber() {
        val selectedQuantity = QuantityView.MAX_QUANTITY_NUMBER
        view.quantityChangeListener = listener
        view.text = 1.toString()
        view.onTouchEvent(downEvent)
        val (popup, item) = getChildFromNumberContainer(selectedQuantity - 1)
        assertTrue(popup.isShowing)

        item.performClick()
        assertFalse(popup.isShowing)

        assertEquals(selectedQuantity.toString(), view.text)
        verify(listener).onQuantityChanged(selectedQuantity.toString())
    }

    @Test
    fun shouldNotCallOnQuantityChangedAndSetNewQuantityWhenListenerIsNull() {
        val selectedQuantity = QuantityView.MAX_QUANTITY_NUMBER
        view.text = 1.toString()
        view.onTouchEvent(downEvent)
        val (popup, item) = getChildFromNumberContainer(selectedQuantity - 1)
        assertTrue(popup.isShowing)

        item.performClick()
        assertFalse(popup.isShowing)

        assertEquals(selectedQuantity.toString(), view.text)
    }

    @Test
    fun shouldShowQuantityInputDialogWhenQuantityMoreThenMaxQuantityNumber() {
        view.text = (QuantityView.MAX_QUANTITY_NUMBER + 1).toString()
        view.onTouchEvent(downEvent)
        val dialog = shadowOf(RuntimeEnvironment.application).latestDialog
        assertNotNull(dialog)
    }

    @Test
    fun shouldShowQuantityInputDialogWhenMoreButtonPressed() {
        view.onTouchEvent(downEvent)
        val popup = shadowOf(RuntimeEnvironment.application).latestPopupWindow
        assertNotNull(popup)

        val moreView: View = popup.contentView.findViewById(R.id.more)
        assertNotNull(moreView)
        assertTrue(popup.isShowing)

        moreView.performClick()
        assertFalse(popup.isShowing)

        val dialog = shadowOf(RuntimeEnvironment.application).latestDialog
        assertNotNull(dialog)
    }

    @Test
    fun shouldDisablePositiveButtonWhenDialogInputTextIsEmpty() {
        view.quantityChangeListener = listener
        view.text = (QuantityView.MAX_QUANTITY_NUMBER + 1).toString()
        view.onTouchEvent(downEvent)

        val dialog = (ShadowDialog.getLatestDialog() as AlertDialog)
        assertNotNull(dialog)

        val dialogInput = dialog.findViewById<TextInputEditText>(R.id.dialogInput)
        assertNotNull(dialogInput)
        dialogInput!!.setText("")

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        assertNotNull(positiveButton)
        assertFalse(positiveButton.isEnabled)
    }

    @Test
    fun shouldChangeQuantityWithInputDialog() {
        val newQuantity = (99).toString()
        view.quantityChangeListener = listener
        view.text = (QuantityView.MAX_QUANTITY_NUMBER + 1).toString()
        view.onTouchEvent(downEvent)

        val dialog = (ShadowDialog.getLatestDialog() as AlertDialog)
        assertNotNull(dialog)

        val dialogInput = dialog.findViewById<TextInputEditText>(R.id.dialogInput)
        assertNotNull(dialogInput)
        dialogInput!!.setText(newQuantity)

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        assertNotNull(positiveButton)
        assertTrue(positiveButton.isEnabled)
        positiveButton.performClick()

        assertEquals(newQuantity, view.text)
        assertFalse(dialog.isShowing)
        verify(listener).onQuantityChanged(newQuantity)
    }

    @Test
    fun shouldNotChangeQuantityWithInputDialogWhenNewQuantityIsNegative() {
        val oldQuantity = (QuantityView.MAX_QUANTITY_NUMBER + 1).toString()
        val newQuantity = (-99).toString()
        view.quantityChangeListener = listener
        view.text = oldQuantity
        view.onTouchEvent(downEvent)

        val dialog = (ShadowDialog.getLatestDialog() as AlertDialog)
        assertNotNull(dialog)

        val dialogInput = dialog.findViewById<TextInputEditText>(R.id.dialogInput)
        assertNotNull(dialogInput)
        dialogInput!!.setText(newQuantity)

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        assertNotNull(positiveButton)
        assertTrue(positiveButton.isEnabled)
        positiveButton.performClick()

        assertEquals(oldQuantity, view.text)
        assertFalse(dialog.isShowing)
        verify(listener, never()).onQuantityChanged(any())
    }

    @Test
    fun shouldDismissDialogWhenDoneButtonClicked() {
        val oldQuantity = (QuantityView.MAX_QUANTITY_NUMBER + 1).toString()
        val newQuantity = (99).toString()
        view.quantityChangeListener = listener
        view.text = oldQuantity
        view.onTouchEvent(downEvent)

        val dialog = (ShadowDialog.getLatestDialog() as AlertDialog)
        assertNotNull(dialog)

        val dialogInput = dialog.findViewById<TextInputEditText>(R.id.dialogInput)
        assertNotNull(dialogInput)
        dialogInput!!.setText(newQuantity)

        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        assertNotNull(negativeButton)
        assertTrue(negativeButton.isEnabled)
        negativeButton.performClick()

        assertEquals(oldQuantity, view.text)
        assertFalse(dialog.isShowing)
        verify(listener, never()).onQuantityChanged(any())
    }

    @Test
    fun shouldRemoveTextListenerWhenDismissDialog() {
        view.quantityChangeListener = listener
        view.text = (QuantityView.MAX_QUANTITY_NUMBER + 1).toString()
        view.onTouchEvent(downEvent)

        val dialog = (ShadowDialog.getLatestDialog() as AlertDialog)
        assertNotNull(dialog)

        val dialogInput = dialog.findViewById<TextInputEditText>(R.id.dialogInput)
        assertNotNull(dialogInput)
        assertEquals(2, shadowOf(dialogInput).watchers.size)
        dialog.dismiss()
        assertEquals(1, shadowOf(dialogInput).watchers.size)
    }

    private fun getChildFromNumberContainer(childIndex: Int): Pair<PopupWindow, View> {
        val popup = shadowOf(RuntimeEnvironment.application).latestPopupWindow
        val rootView = popup.contentView
        val viewContainer = rootView.findViewById<ViewGroup>(R.id.quantityNumberContainer)
        return popup to viewContainer.getChildAt(childIndex)
    }
}