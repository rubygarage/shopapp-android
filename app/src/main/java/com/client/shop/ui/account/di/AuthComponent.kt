package com.client.shop.ui.account.di

import com.client.shop.ui.account.AccountFragment
import com.client.shop.ui.account.SignInActivity
import com.client.shop.ui.account.SignUpFragment
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(activity: AccountFragment)

    fun inject(fragment: SignUpFragment)

    fun inject(fragment: SignInActivity)
}