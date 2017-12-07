package com.client.shop.ui.account.di

import com.client.shop.ui.account.AccountFragment
import com.client.shop.ui.account.SignInActivity
import com.client.shop.ui.account.SignUpActivity
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(activity: AccountFragment)

    fun inject(fragment: SignUpActivity)

    fun inject(fragment: SignInActivity)
}