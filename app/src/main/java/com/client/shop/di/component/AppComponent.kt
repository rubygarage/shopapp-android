package com.client.shop.di.component

import com.client.shop.di.module.RepositoryModule
import com.client.shop.di.module.ValidatorModule
import com.client.shop.ui.account.di.AuthComponent
import com.client.shop.ui.address.account.di.AddressComponent
import com.client.shop.ui.address.account.di.AddressModule
import com.client.shop.ui.address.checkout.di.CheckoutAddressComponent
import com.client.shop.ui.address.checkout.di.CheckoutAddressModule
import com.client.shop.ui.blog.di.BlogComponent
import com.client.shop.ui.blog.di.BlogModule
import com.client.shop.ui.cart.di.CartComponent
import com.client.shop.ui.cart.di.CartModule
import com.client.shop.ui.category.di.CategoryComponent
import com.client.shop.ui.category.di.CategoryModule
import com.client.shop.ui.checkout.di.CheckoutComponent
import com.client.shop.ui.checkout.di.CheckoutModule
import com.client.shop.ui.checkout.payment.card.di.CardPaymentComponent
import com.client.shop.ui.checkout.payment.card.di.CardPaymentModule
import com.client.shop.ui.order.di.OrderComponent
import com.client.shop.ui.order.di.OrderModule
import com.client.shop.ui.popular.di.PopularComponent
import com.client.shop.ui.popular.di.PopularModule
import com.client.shop.ui.product.di.*
import com.client.shop.ui.search.di.SearchComponent
import com.client.shop.ui.search.di.SearchModule
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