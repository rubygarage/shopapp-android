package com.shopapp.ui.account

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Config
import com.shopapp.gateway.entity.Customer
import com.shopapp.ui.account.router.AccountRouter
import com.shopapp.ui.const.RequestCode
import kotlinx.android.synthetic.main.fragment_account_auth.*
import javax.inject.Inject

class AccountAuthFragment : Fragment() {

    companion object {

        private const val CUSTOMER = "customer"

        fun newInstance(customer: Customer): AccountAuthFragment {
            val fragment = AccountAuthFragment()
            val args = Bundle()
            args.putParcelable(CUSTOMER, customer)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var config: Config

    @Inject
    lateinit var router: AccountRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        ShopApplication.appComponent.attachAccountComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customer: Customer? = arguments?.getParcelable(CUSTOMER)
        customer?.let {
            if (it.firstName.isNotBlank() || customer.lastName.isNotBlank()) {
                val fullName = getString(
                    R.string.full_name_pattern,
                    customer.firstName,
                    customer.lastName
                ).trim()
                name.text = fullName
                avatarView.setName(fullName)
            } else {
                name.text = customer.email
                avatarView.setName(customer.email)
            }
        }

        myOrders.visibility = if (config.isOrdersEnabled) View.VISIBLE else View.GONE

        myOrders.setOnClickListener {
            router.showOrderList(context)
        }
        personalInfo.setOnClickListener {
            router.showPersonalInfoForResult(this, RequestCode.PERSONAL_INFO)
        }
        shippingAddress.setOnClickListener {
            router.showAddressList(context)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        parentFragment?.onActivityResult(requestCode, resultCode, data)
    }
}