package com.shopapp.di.component

import com.shopapp.di.module.RepositoryModule
import com.shopapp.di.module.ValidatorModule
import com.shopapp.ui.account.di.AuthComponent
import com.shopapp.ui.address.account.di.AddressComponent
import com.shopapp.ui.address.checkout.di.CheckoutAddressComponent
import com.shopapp.ui.blog.di.BlogComponent
import com.shopapp.ui.cart.di.CartComponent
import com.shopapp.ui.category.di.CategoryComponent
import com.shopapp.ui.checkout.di.CheckoutComponent
import com.shopapp.ui.checkout.payment.card.di.CardPaymentComponent
import com.shopapp.ui.gallery.di.GalleryComponent
import com.shopapp.ui.order.di.OrderComponent
import com.shopapp.ui.product.di.ProductComponent
import com.shopapp.ui.search.di.SearchComponent
import com.shopapp.ui.splash.di.SplashComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ValidatorModule::class])
interface AppComponent {

    fun attachBlogComponent(): BlogComponent

    fun attachSearchComponent(): SearchComponent

    fun attachProductComponent(): ProductComponent

    fun attachCategoryComponent(): CategoryComponent

    fun attachCartComponent(): CartComponent

    fun attachAuthComponent(): AuthComponent

    fun attachOrderComponent(): OrderComponent

    fun attachAddressComponent(): AddressComponent

    fun attachCheckoutComponent(): CheckoutComponent

    fun attachCardPaymentComponent(): CardPaymentComponent

    fun attachCheckoutAddressComponent(): CheckoutAddressComponent

    fun attachSplashComponent(): SplashComponent

    fun attachGalleryComponent(): GalleryComponent

}