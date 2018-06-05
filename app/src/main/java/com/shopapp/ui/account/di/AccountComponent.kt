package com.shopapp.ui.account.di

import com.shopapp.ui.account.*
import dagger.Subcomponent

@Subcomponent(modules = [AccountModule::class])
interface AccountComponent {

    fun inject(fragment: AccountFragment)

    fun inject(activity: SignUpActivity)

    fun inject(activity: SignInActivity)

    fun inject(activity: ForgotPasswordActivity)

    fun inject(activity: PersonalInfoActivity)

    fun inject(activity: ChangePasswordActivity)

    fun inject(activity: AccountSettingsActivity)
}