package com.client.shop.ui.recent.di

import com.client.shop.ui.recent.contract.RecentPresenter
import com.client.shop.ui.recent.contract.RecentUseCase
import dagger.Module
import dagger.Provides

@Module
class RecentModule {

    @Provides
    fun provideBlogPresenter(recentUseCase: RecentUseCase): RecentPresenter {
        return RecentPresenter(recentUseCase)
    }
}