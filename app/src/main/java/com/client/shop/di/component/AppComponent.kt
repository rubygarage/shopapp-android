package com.client.shop.di.component

import com.client.shop.di.module.RepositoryModule
import com.client.shop.di.module.RouterModule
import com.client.shop.ui.account.di.AuthComponent
import com.client.shop.ui.account.di.AuthModule
import com.client.shop.ui.blog.di.BlogComponent
import com.client.shop.ui.blog.di.BlogModule
import com.client.shop.ui.cart.di.CartComponent
import com.client.shop.ui.cart.di.CartModule
import com.client.shop.ui.category.di.CategoryComponent
import com.client.shop.ui.category.di.CategoryModule
import com.client.shop.ui.details.di.DetailsComponent
import com.client.shop.ui.details.di.DetailsModule
import com.client.shop.ui.home.di.HomeComponent
import com.client.shop.ui.home.di.HomeModule
import com.client.shop.ui.recent.di.RecentComponent
import com.client.shop.ui.recent.di.RecentModule
import com.client.shop.ui.search.di.SearchComponent
import com.client.shop.ui.search.di.SearchModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RouterModule::class, RepositoryModule::class])
interface AppComponent {

    fun attachBlogComponent(module: BlogModule): BlogComponent

    fun attachHomeComponent(module: HomeModule): HomeComponent

    fun attachDetailsComponent(module: DetailsModule): DetailsComponent

    fun attachSearchComponent(module: SearchModule): SearchComponent

    fun attachRecentComponent(module: RecentModule): RecentComponent

    fun attachCategoryComponent(module: CategoryModule): CategoryComponent

    fun attachCartComponent(module: CartModule): CartComponent

    fun attachAuthComponent(module: AuthModule): AuthComponent
}