package com.client.shop.ui.auth.di

import com.client.shop.ui.auth.AccountFragment
import com.client.shop.ui.auth.SignInFragment
import com.client.shop.ui.auth.SignUpFragment
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(activity: AccountFragment)

    fun inject(fragment: SignUpFragment)

    fun inject(fragment: SignInFragment)
}