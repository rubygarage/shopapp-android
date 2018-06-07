package com.shopapp.ui.account

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Shop
import com.shopapp.ui.account.router.AccountRouter
import com.shopapp.ui.const.RequestCode
import kotlinx.android.synthetic.main.fragment_account_unauth.*
import javax.inject.Inject

class AccountUnAuthFragment : Fragment() {

    companion object {

        private const val SHOP = "shop"

        fun newInstance(shop: Shop?): AccountUnAuthFragment {
            val fragment = AccountUnAuthFragment()
            val args = Bundle()
            args.putParcelable(SHOP, shop)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var router: AccountRouter

    private var shop: Shop? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ShopApplication.appComponent.attachAccountComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_unauth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shop = arguments?.getParcelable(SHOP)

        signInButton.setOnClickListener {
            router.showSignInForResult(this, RequestCode.SIGN_IN)
        }
        createAccount.setOnClickListener {
            router.showSignUpForResult(
                this,
                shop?.privacyPolicy,
                shop?.termsOfService,
                RequestCode.SIGN_UP
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        parentFragment?.onActivityResult(requestCode, resultCode, data)
    }
}