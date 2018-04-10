package com.shopapp.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.shopapp.R
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.popup_quantity.view.*
import kotlinx.android.synthetic.main.view_dialog_quantity_input.view.*

@SuppressLint("InflateParams")
class QuantityView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        const val MAX_QUANTITY_NUMBER = 5
    }

    private val numberTextSize = context.resources.getDimension(R.dimen.popup_text_size)
    private val horizontalNumberPadding = context.resources.getDimensionPixelSize(R.dimen.horizontal_space)
    private val verticalNumberPadding = context.resources.getDimensionPixelSize(R.dimen.element_space)
    private val numberTextColor = ContextCompat.getColor(context, R.color.textColorPrimary)
    private val selectedNumberTextBackgroundRes = R.color.selectedColor
    private val numberTextBackgroundRes: Int
    var quantityChangeListener: OnQuantityChangeListener? = null

    init {
        val outValue = TypedValue()
        getContext().theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        numberTextBackgroundRes = outValue.resourceId
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            playSoundEffect(SoundEffectConstants.CLICK)
            val selectedQuantityNumber = text.toString().toIntOrNull() ?: 0
            if (selectedQuantityNumber > MAX_QUANTITY_NUMBER) {
                showQuantityInputDialog(selectedQuantityNumber.toString())
            } else {
                showQuantityNumberPopup(selectedQuantityNumber)
            }
        }
        return true
    }

    private fun showQuantityNumberPopup(selectedQuantityNumber: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_quantity, null)

        val width = context.resources.getDimensionPixelSize(R.dimen.popup_quantity_width)
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, WRAP_CONTENT, focusable)

        fillQuantityNumberContainer(popupWindow, popupView.quantityNumberContainer, selectedQuantityNumber)
        popupView.more.setOnClickListener {
            showQuantityInputDialog()
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(this, 0, -this.height)
    }

    private fun fillQuantityNumberContainer(
        popupWindow: PopupWindow,
        quantityNumberContainer: ViewGroup,
        selectedQuantityNumber: Int
    ) {
        val clickListener = View.OnClickListener {
            if (it is TextView) {
                selectQuantity(it.text.toString())
                popupWindow.dismiss()
            }
        }
        quantityNumberContainer.removeAllViews()
        for (i in 1..MAX_QUANTITY_NUMBER) {
            val numberQuantityTextView = TextView(context)
            numberQuantityTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, numberTextSize)
            numberQuantityTextView.setTextColor(numberTextColor)
            numberQuantityTextView.setPadding(horizontalNumberPadding, verticalNumberPadding,
                horizontalNumberPadding, verticalNumberPadding)
            numberQuantityTextView.setOnClickListener(clickListener)
            numberQuantityTextView.text = i.toString()
            val backgroundColor = if (i == selectedQuantityNumber)
                selectedNumberTextBackgroundRes
            else
                numberTextBackgroundRes
            numberQuantityTextView.setBackgroundResource(backgroundColor)

            quantityNumberContainer.addView(numberQuantityTextView)
        }
    }

    private fun showQuantityInputDialog(selectedQuantity: String = "") {
        val dialogInputView = LayoutInflater.from(context).inflate(R.layout.view_dialog_quantity_input, null)
        val dialogInput = dialogInputView.dialogInput
        val dialog = AlertDialog.Builder(context)
            .setTitle(R.string.quantity)
            .setView(dialogInputView)
            .setNegativeButton(R.string.cancel_button, { _, _ -> /*do nothing*/ })
            .setPositiveButton(R.string.done, { _, _ ->
                selectQuantity(dialogInput.text.toString())
            })
            .show()

        val positiveButton: Button? = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton?.isEnabled = false

        val textChangeListener = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                positiveButton?.isEnabled = s.isNotBlank()
            }
        }
        dialogInput.addTextChangedListener(textChangeListener)
        dialogInput.setText(selectedQuantity)
        dialogInput.setSelection(dialogInput.text.length)
        dialog.setOnDismissListener {
            dialogInput.removeTextChangedListener(textChangeListener)
        }
    }

    private fun selectQuantity(quantity: String) {
        val quantityNumber = quantity.toIntOrNull() ?: 0
        if (quantityNumber > 0 && text.toString() != quantity) {
            text = quantity
            quantityChangeListener?.onQuantityChanged(quantity)
        }
    }

    interface OnQuantityChangeListener {

        fun onQuantityChanged(quantity: String)
    }
}