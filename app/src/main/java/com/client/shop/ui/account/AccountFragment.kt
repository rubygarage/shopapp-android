package com.client.shop.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.const.RequestCode
import com.client.shop.ui.account.contract.AccountPresenter
import com.client.shop.ui.account.contract.AccountView
import com.client.shop.ui.account.di.AuthModule
import com.client.shop.ui.policy.PolicyActivity
import com.domain.entity.Customer
import com.domain.entity.Policy
import com.domain.entity.Shop
import com.ui.base.lce.BaseFragment
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account_lce.*
import javax.inject.Inject

class AccountFragment : BaseFragment<Boolean, AccountView, AccountPresenter>(), AccountView {

    @Inject lateinit var accountPresenter: AccountPresenter
    private var shop: Shop? = null
    private var customer: Customer? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        if (shop == null) {
            presenter.getShopInfo()
        } else {
            setupShop()
        }

        customer?.let { customerReceived(it) }
        loadData(customer != null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val compatActivity = activity
        if (compatActivity is AppCompatActivity) {
            toolbar.setTitle(getString(R.string.my_account))
            compatActivity.setSupportActionBar(toolbar)
            compatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_account, menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == RequestCode.SIGN_IN || requestCode == RequestCode.SIGN_UP) && resultCode == Activity.RESULT_OK) {
            loadData()
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
    }

    override fun getRootView() = R.layout.fragment_account_lce

    override fun getContentView() = R.layout.fragment_account

    override fun createPresenter() = accountPresenter

    //SETUP

    private fun setupButtons() {
        signInButton.setOnClickListener {
            startActivityForResult(SignInActivity.getStartIntent(context), RequestCode.SIGN_IN)
        }
        createAccount.setOnClickListener {
            startActivityForResult(SignUpActivity.getStartIntent(context, shop?.privacyPolicy, shop?.termsOfService), RequestCode.SIGN_UP)
        }
        logout.setOnClickListener {
            presenter.signOut()
        }
    }

    private fun setupPolicy(view: View, policy: Policy?) {
        if (policy != null) {
            view.setOnClickListener { startActivity(PolicyActivity.getStartIntent(context, policy)) }
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    private fun setupShop() {
        shop?.let {
            setupPolicy(privacyPolicy, it.privacyPolicy)
            setupPolicy(refundPolicy, it.refundPolicy)
            setupPolicy(termsOfService, it.termsOfService)
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.isAuthorized()
    }

    override fun showContent(data: Boolean) {
        super.showContent(data)
        if (data) {
            presenter.getCustomer()
        } else {
            unauthGroup.visibility = View.VISIBLE
            authGroup.visibility = View.GONE
        }
    }

    override fun shopReceived(shop: Shop) {
        this.shop = shop
        setupShop()
    }

    override fun customerReceived(customer: Customer) {
        this.customer = customer
        authGroup.visibility = View.VISIBLE
        unauthGroup.visibility = View.GONE

        if (customer.firstName.isNotBlank() || customer.lastName.isNotBlank()) {
            val fullName = getString(R.string.full_name_pattern, customer.firstName, customer.lastName).trim()
            name.text = fullName
            avatarView.setName(fullName)
        } else {
            name.text = customer.email
            avatarView.setName(customer.email)
        }
    }

    override fun signedOut() {
        showMessage(R.string.logout_success_message)
        loadData()
    }
}