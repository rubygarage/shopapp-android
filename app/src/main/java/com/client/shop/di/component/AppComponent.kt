package com.client.shop.di.component

import com.client.shop.di.module.RepositoryModule
import com.client.shop.di.module.ValidatorModule
import com.client.shop.ui.account.di.AuthComponent
import com.client.shop.ui.account.di.AuthModule
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
@Component(modules = [RepositoryModule::class, ValidatorModule::class ])
interface AppComponent {

    fun attachBlogComponent(module: BlogModule): BlogComponent

    fun attachDetailsComponent(module: ProductDetailsModule): ProductDetailsComponent

    fun attachSearchComponent(module: SearchModule): SearchComponent

    fun attachProductComponent(module: ProductListModule): ProductListComponent

    fun attachRecentComponent(module: ProductHorizontalModule): ProductHorizontalComponent

    fun attachPopularComponent(module: PopularModule): PopularComponent

    fun attachCategoryComponent(module: CategoryModule): CategoryComponent

    fun attachCartComponent(module: CartModule): CartComponent

    fun attachAuthComponent(module: AuthModule): AuthComponent

    fun attachOrderComponent(module: OrderModule): OrderComponent

    fun attachAddressComponent(module: AddressModule): AddressComponent

    fun attachCheckoutComponent(module: CheckoutModule): CheckoutComponent

    fun attachCardPaymentComponent(module: CardPaymentModule): CardPaymentComponent

    fun attachCheckoutAddressComponent(module: CheckoutAddressModule): CheckoutAddressComponent

}