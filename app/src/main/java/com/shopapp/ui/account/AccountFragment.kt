package com.shopapp.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.Policy
import com.shopapp.gateway.entity.Shop
import com.shopapp.ui.account.contract.AccountPresenter
import com.shopapp.ui.account.contract.AccountView
import com.shopapp.ui.account.router.AccountRouter
import com.shopapp.ui.base.lce.BaseLceFragment
import com.shopapp.ui.const.RequestCode
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account_lce.*
import javax.inject.Inject

class AccountFragment :
    BaseLceFragment<Boolean, AccountView, AccountPresenter>(),
    AccountView {

    @Inject
    lateinit var accountPresenter: AccountPresenter

    @Inject
    lateinit var router: AccountRouter

    private var shop: Shop? = null
    private var customer: Customer? = null
    private var settingsMenuItem: MenuItem? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        settingsMenuItem = menu.findItem(R.id.settings)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        router.showSettings(context)
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val isResultOk = resultCode == Activity.RESULT_OK
        val isSignIn = requestCode == RequestCode.SIGN_IN
        val isSignUp = requestCode == RequestCode.SIGN_UP
        val isPersonalInfo = requestCode == RequestCode.PERSONAL_INFO

        if ((isSignIn || isSignUp || isPersonalInfo) && isResultOk) {
            loadData()
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent().inject(this)
    }

    override fun getRootView() = R.layout.fragment_account_lce

    override fun getContentView() = R.layout.fragment_account

    override fun createPresenter() = accountPresenter

    //SETUP

    private fun setupButtons() {
        signInButton.setOnClickListener {
            router.showSignInForResult(this, RequestCode.SIGN_IN)
        }
        createAccount.setOnClickListener {
            router.showSignUpForResult(this, shop?.privacyPolicy, shop?.termsOfService, RequestCode.SIGN_UP)
        }
        myOrders.setOnClickListener {
            router.showOrderList(context)
        }
        personalInfo.setOnClickListener {
            router.showPersonalInfoForResult(this, RequestCode.PERSONAL_INFO)
        }
        shippingAddress.setOnClickListener {
            router.showAddressList(context)
        }
        logout.setOnClickListener {
            presenter.signOut()
        }
    }

    private fun setupPolicy(view: View, policy: Policy?) {
        if (policy != null) {
            view.setOnClickListener { router.showPolicy(context, policy) }
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
            settingsMenuItem?.isVisible = true
            presenter.getCustomer()
        } else {
            settingsMenuItem?.isVisible = false
            unauthGroup.visibility = View.VISIBLE
            authGroup.visibility = View.GONE
        }
    }

    override fun shopReceived(shop: Shop) {
        this.shop = shop
        setupShop()
    }

    override fun customerReceived(customer: Customer?) {
        this.customer = customer
        if (customer != null) {
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
        } else {
            loadData()
        }
    }

    override fun signedOut() {
        customer = null
        showMessage(R.string.logout_success_message)
        loadData()
    }

}