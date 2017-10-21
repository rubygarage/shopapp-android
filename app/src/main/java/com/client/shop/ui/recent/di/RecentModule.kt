package com.client.shop.ui.recent.di

import com.client.shop.ui.recent.contract.RecentPresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class RecentModule {

    @Provides
    fun provideBlogPresenter(repository: Repository): RecentPresenter {
        return RecentPresenter(repository)
    }
}