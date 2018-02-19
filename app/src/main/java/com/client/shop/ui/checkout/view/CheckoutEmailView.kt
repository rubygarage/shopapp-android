package com.client.shop.ui.checkout.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.client.shop.R
import com.client.shop.ext.hideKeyboard
import com.client.shop.ui.const.Constant
import com.client.shop.ui.custom.SimpleTextWatcher
import com.client.shop.gateway.entity.Customer
import com.domain.validator.FieldValidator
import kotlinx.android.synthetic.main.view_checkout_email.view.*

class CheckoutEmailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr),
    SimpleTextWatcher,
    TextView.OnEditorActionListener {

    private val fieldValidator = FieldValidator()
    var emailChangeListener: EmailChangeListener? = null

    init {
        View.inflate(context, R.layout.view_checkout_email, this)
        orientation = VERTICAL
        setBackgroundResource(R.color.white)
        emailInput.addTextChangedListener(this)
        emailInput.setOnEditorActionListener(this)
    }

    fun setData(customer: Customer?) {
        visibility = if (customer != null) View.GONE else View.VISIBLE
        emailInput.setText(customer?.email ?: Constant.DEFAULT_STRING)
    }

    fun getEmail(): String? {
        val email = emailInput.text.toString()
        return if (fieldValidator.isEmailValid(email)) email else null
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        emailChangeListener?.onEmailChanged()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE && v != null) {
            v.hideKeyboard()
            v.clearFocus()
            title.requestFocus()
            true
        } else {
            false
        }
    }

    interface EmailChangeListener {

        fun onEmailChanged()
    }
}