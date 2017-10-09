package com.client.shop.di.component

import com.client.shop.di.module.NetworkModule
import com.client.shop.ui.blog.BlogActivity
import com.client.shop.ui.blog.BlogFragment
import com.client.shop.ui.category.CategoryActivity
import com.client.shop.ui.category.CategoryFragment
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.recent.RecentActivity
import com.client.shop.ui.recent.RecentFragment
import com.client.shop.ui.search.SearchActivity
import com.client.shop.ui.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface AppComponent {

    fun inject(activity: SplashActivity)

    fun inject(activity: CategoryActivity)

    fun inject(activity: DetailsActivity)

    fun inject(activity: SearchActivity)

    fun inject(activity: RecentActivity)

    fun inject(activity: BlogActivity)


    fun inject(fragment: RecentFragment)

    fun inject(fragment: BlogFragment)

    fun inject(fragment: CategoryFragment)
}