package com.shopapp.di.component

import com.client.shop.ui.account.di.AuthComponent
import com.client.shop.ui.address.account.di.AddressComponent
import com.client.shop.ui.address.checkout.di.CheckoutAddressComponent
import com.client.shop.ui.blog.di.BlogComponent
import com.client.shop.ui.cart.di.CartComponent
import com.client.shop.ui.category.di.CategoryComponent
import com.client.shop.ui.checkout.di.CheckoutComponent
import com.client.shop.ui.checkout.payment.card.di.CardPaymentComponent
import com.client.shop.ui.order.di.OrderComponent
import com.client.shop.ui.popular.di.PopularComponent
import com.client.shop.ui.product.di.ProductDetailsComponent
import com.client.shop.ui.product.di.ProductHorizontalComponent
import com.client.shop.ui.product.di.ProductListComponent
import com.client.shop.ui.search.di.SearchComponent
import com.shopapp.di.module.RepositoryModule
import com.shopapp.di.module.ValidatorModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ValidatorModule::class])
interface AppComponent {

    fun attachBlogComponent(): BlogComponent

    fun attachDetailsComponent(): ProductDetailsComponent

    fun attachSearchComponent(): SearchComponent

    fun attachProductComponent(): ProductListComponent

    fun attachRecentComponent(): ProductHorizontalComponent

    fun attachPopularComponent(): PopularComponent

    fun attachCategoryComponent(): CategoryComponent

    fun attachCartComponent(): CartComponent

    fun attachAuthComponent(): AuthComponent

    fun attachOrderComponent(): OrderComponent

    fun attachAddressComponent(): AddressComponent

    fun attachCheckoutComponent(): CheckoutComponent

    fun attachCardPaymentComponent(): CardPaymentComponent

    fun attachCheckoutAddressComponent(): CheckoutAddressComponent

}