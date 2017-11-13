package com.client.shop.ui.auth.di

import com.client.shop.ui.auth.AuthActivity
import com.client.shop.ui.auth.SignInFragment
import com.client.shop.ui.auth.SignUpFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(AuthModule::class))
interface AuthComponent {

    fun inject(activity: AuthActivity)

    fun inject(fragment: SignUpFragment)

    fun inject(fragment: SignInFragment)
}