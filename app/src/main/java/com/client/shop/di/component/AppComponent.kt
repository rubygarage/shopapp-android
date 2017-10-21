package com.client.shop.di.component

import com.client.shop.di.module.RepositoryModule
import com.client.shop.ui.blog.di.BlogComponent
import com.client.shop.ui.blog.di.BlogModule
import com.client.shop.ui.category.di.CategoryComponent
import com.client.shop.ui.category.di.CategoryModule
import com.client.shop.ui.details.di.DetailsComponent
import com.client.shop.ui.details.di.DetailsModule
import com.client.shop.ui.recent.di.RecentComponent
import com.client.shop.ui.recent.di.RecentModule
import com.client.shop.ui.search.di.SearchComponent
import com.client.shop.ui.search.di.SearchModule
import com.client.shop.ui.splash.di.SplashComponent
import com.client.shop.ui.splash.di.SplashModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(RepositoryModule::class))
interface AppComponent {

    fun attachBlogComponent(module: BlogModule): BlogComponent

    fun attachSplashComponent(module: SplashModule): SplashComponent

    fun attachDetailsComponent(module: DetailsModule): DetailsComponent

    fun attachSearchComponent(module: SearchModule): SearchComponent

    fun attachRecentComponent(module: RecentModule): RecentComponent

    fun attachCategoryComponent(module: CategoryModule): CategoryComponent
}