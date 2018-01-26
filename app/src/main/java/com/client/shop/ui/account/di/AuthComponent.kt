package com.client.shop.ui.account.di

import com.client.shop.ui.account.*
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(fragment: AccountFragment)

    fun inject(activity: SignUpActivity)

    fun inject(activity: SignInActivity)

    fun inject(activity: ForgotPasswordActivity)

    fun inject(activity: PersonalInfoActivity)

    fun inject(activity: ChangePasswordActivity)

    fun inject(activity: AccountSettingsActivity)
}