package com.shopapp.di.component

import com.shopapp.di.module.TestRepositoryModule
import com.shopapp.di.module.ValidatorModule
import com.shopapp.ui.account.di.TestAuthComponent
import com.shopapp.ui.address.account.di.AddressComponent
import com.shopapp.ui.address.checkout.di.CheckoutAddressComponent
import com.shopapp.ui.blog.di.BlogComponent
import com.shopapp.ui.cart.di.CartComponent
import com.shopapp.ui.category.di.CategoryComponent
import com.shopapp.ui.checkout.di.CheckoutComponent
import com.shopapp.ui.checkout.payment.card.di.CardPaymentComponent
import com.shopapp.ui.order.di.TestOrderComponent
import com.shopapp.ui.popular.di.PopularComponent
import com.shopapp.ui.product.di.ProductDetailsComponent
import com.shopapp.ui.product.di.ProductHorizontalComponent
import com.shopapp.ui.product.di.ProductListComponent
import com.shopapp.ui.search.di.SearchComponent
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