package com.shopapp.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.checkedChanges
import com.shopapp.ui.account.contract.AccountSettingsPresenter
import com.shopapp.ui.account.contract.AccountSettingsView
import com.shopapp.ui.base.lce.BaseLceActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_account_settings.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AccountSettingsActivity :
    BaseLceActivity<Boolean, AccountSettingsView, AccountSettingsPresenter>(),
    AccountSettingsView {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, AccountSettingsActivity::class.java)

        private const val CHANGE_DEBOUNCE = 500L

    }

    @Inject
    lateinit var accountSettingsPresenter: AccountSettingsPresenter

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.settings))
        loadData()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent().inject(this)
    }

    override fun getContentView() = R.layout.activity_account_settings

    override fun createPresenter() = accountSettingsPresenter

    //SETUP

    private fun setupChangeListener() {
        acceptPromotionSwitch
            .checkedChanges()
            .debounce(CHANGE_DEBOUNCE, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { presenter.updateSettings(it) }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getCustomer()
    }

    override fun showContent(data: Boolean) {
        super.showContent(data)
        acceptPromotionSwitch.isChecked = data
        setupChangeListener()
    }
}