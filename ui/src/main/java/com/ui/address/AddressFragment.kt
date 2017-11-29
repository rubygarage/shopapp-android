package com.ui.address

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.domain.entity.Address
import com.domain.validator.FieldValidator
import com.ui.R
import kotlinx.android.synthetic.main.fragment_address.*

class AddressFragment : Fragment() {

    companion object {

        private const val WITHOUT_EMAIL = "without_email"
        private const val ADDRESS = "address"

        fun newInstance(withoutEmail: Boolean = false, address: Address? = null): AddressFragment {
            val fragment = AddressFragment()
            val args = Bundle()
            args.putBoolean(WITHOUT_EMAIL, withoutEmail)
            args.putParcelable(ADDRESS, address)
            fragment.arguments = args
            return fragment
        }
    }

    private val fieldValidator = FieldValidator()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_address, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val withoutEmail = arguments.getBoolean(WITHOUT_EMAIL, false)
        val address = arguments.getParcelable<Address?>(ADDRESS)

        if (withoutEmail) {
            emailInputContainer.visibility = View.GONE
        }
        address?.let { fillInAddress(it) }
    }

    private fun fillInAddress(addressObject: Address) {
        with(addressObject) {
            firstNameInput.setText(firstName)
            lastNameInput.setText(lastName)
            addressInput.setText(address)
            cityInput.setText(city)
            countryInput.setText(country)
            postalCodeInput.setText(zip)
            phoneInput.setText(phone ?: "")
        }
    }

    fun getValidAddress(): Address? {
        val address = Address(
                addressInput.text.toString(),
                cityInput.text.toString(),
                countryInput.text.toString(),
                firstNameInput.text.toString(),
                lastNameInput.text.toString(),
                postalCodeInput.text.toString(),
                phoneInput.text.toString()
        )
        return if (fieldValidator.isAddressValid(address)) address else null
    }

    fun getValidEmail(): String? {
        val email = emailInput.text.toString()
        return if (fieldValidator.isEmailValid(email)) email else null
    }

}