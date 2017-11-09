package com.client.shop.ui.auth.di

import com.client.shop.ui.auth.SignUpFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(AuthModule::class))
interface AuthComponent {

    fun inject(fragment: SignUpFragment)
}