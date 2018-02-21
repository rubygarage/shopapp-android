package com.client.shop.ui.account.di

import com.shopapp.ui.account.*
import com.shopapp.ui.account.di.AuthComponent
import dagger.Subcomponent

@Subcomponent(modules = [TestAuthModule::class])
interface TestAuthComponent : AuthComponent {

    override fun inject(fragment: AccountFragment)

    override fun inject(activity: SignUpActivity)

    override fun inject(activity: SignInActivity)

    override fun inject(activity: ForgotPasswordActivity)

    override fun inject(activity: PersonalInfoActivity)

    override fun inject(activity: ChangePasswordActivity)

    override fun inject(activity: AccountSettingsActivity)
}