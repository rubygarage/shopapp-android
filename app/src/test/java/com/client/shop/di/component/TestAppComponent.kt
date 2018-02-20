package com.client.shop.di.component

import com.client.shop.di.module.TestRepositoryModule
import com.client.shop.di.module.ValidatorModule
import com.client.shop.ui.account.di.TestAuthComponent
import com.client.shop.ui.address.account.di.AddressComponent
import com.client.shop.ui.address.checkout.di.CheckoutAddressComponent
import com.client.shop.ui.blog.di.BlogComponent
import com.client.shop.ui.cart.di.CartComponent
import com.client.shop.ui.category.di.CategoryComponent
import com.client.shop.ui.checkout.di.CheckoutComponent
import com.client.shop.ui.checkout.payment.card.di.CardPaymentComponent
import com.client.shop.ui.order.di.TestOrderComponent
import com.client.shop.ui.popular.di.PopularComponent
import com.client.shop.ui.product.di.ProductDetailsComponent
import com.client.shop.ui.product.di.ProductHorizontalComponent
import com.client.shop.ui.product.di.ProductListComponent
import com.client.shop.ui.search.di.SearchComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestRepositoryModule::class, ValidatorModule::class])
interface TestAppComponent : AppComponent {

    override fun attachBlogComponent(): BlogComponent

    override fun attachDetailsComponent(): ProductDetailsComponent

    override fun attachSearchComponent(): SearchComponent

    override fun attachProductComponent(): ProductListComponent

    override fun attachRecentComponent(): ProductHorizontalComponent

    override fun attachPopularComponent(): PopularComponent

    override fun attachCategoryComponent(): CategoryComponent

    override fun attachCartComponent(): CartComponent

    override fun attachAuthComponent(): TestAuthComponent

    override fun attachOrderComponent(): TestOrderComponent

    override fun attachAddressComponent(): AddressComponent

    override fun attachCheckoutComponent(): CheckoutComponent

    override fun attachCardPaymentComponent(): CardPaymentComponent

    override fun attachCheckoutAddressComponent(): CheckoutAddressComponent

}