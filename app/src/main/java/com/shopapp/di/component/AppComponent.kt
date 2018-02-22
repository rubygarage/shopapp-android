package com.shopapp.di.component

import com.shopapp.ui.blog.di.BlogComponent
import com.shopapp.ui.cart.di.CartComponent
import com.shopapp.ui.category.di.CategoryComponent
import com.shopapp.ui.checkout.di.CheckoutComponent
import com.shopapp.ui.checkout.payment.card.di.CardPaymentComponent
import com.shopapp.ui.order.di.OrderComponent
import com.shopapp.ui.popular.di.PopularComponent
import com.shopapp.ui.product.di.ProductDetailsComponent
import com.shopapp.ui.product.di.ProductHorizontalComponent
import com.shopapp.ui.product.di.ProductListComponent
import com.shopapp.ui.search.di.SearchComponent
import com.shopapp.di.module.RepositoryModule
import com.shopapp.di.module.ValidatorModule
import com.shopapp.ui.account.di.AuthComponent
import com.shopapp.ui.address.account.di.AddressComponent
import com.shopapp.ui.address.checkout.di.CheckoutAddressComponent
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