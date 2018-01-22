package com.client.shop.ui.profile.di

import com.client.shop.ui.profile.PersonalInfoActivity
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {

    fun inject(personalInfoActivity: PersonalInfoActivity)

}