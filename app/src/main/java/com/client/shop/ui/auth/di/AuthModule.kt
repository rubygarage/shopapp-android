package com.client.shop.ui.auth.di

import com.client.shop.ui.auth.contract.SignUpPresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @Provides
    fun provideSignUpPresenter(repository: Repository): SignUpPresenter {
        return SignUpPresenter(repository)
    }
}