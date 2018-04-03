package com.shopapp.di.component

import com.shopapp.di.module.TestRepositoryModule
import com.shopapp.di.module.TestValidatorModule
import com.shopapp.ui.account.di.TestAuthComponent
import com.shopapp.ui.address.account.di.TestAddressComponent
import com.shopapp.ui.address.checkout.di.TestCheckoutAddressComponent
import com.shopapp.ui.blog.di.TestBlogComponent
import com.shopapp.ui.cart.di.TestCartComponent
import com.shopapp.ui.category.di.TestCategoryComponent
import com.shopapp.ui.checkout.di.TestCheckoutComponent
import com.shopapp.ui.checkout.payment.card.di.TestCardPaymentComponent
import com.shopapp.ui.gallery.di.TestGalleryComponent
import com.shopapp.ui.order.di.TestOrderComponent
import com.shopapp.ui.product.di.TestProductComponent
import com.shopapp.ui.search.di.TestSearchComponent
import com.shopapp.ui.splash.di.TestSplashComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestRepositoryModule::class, TestValidatorModule::class])
interface TestAppComponent : AppComponent {

    override fun attachBlogComponent(): TestBlogComponent

    override fun attachSearchComponent(): TestSearchComponent

    override fun attachProductComponent(): TestProductComponent

    override fun attachCategoryComponent(): TestCategoryComponent

    override fun attachCartComponent(): TestCartComponent

    override fun attachAuthComponent(): TestAuthComponent

    override fun attachOrderComponent(): TestOrderComponent

    override fun attachAddressComponent(): TestAddressComponent

    override fun attachCheckoutComponent(): TestCheckoutComponent

    override fun attachCardPaymentComponent(): TestCardPaymentComponent

    override fun attachCheckoutAddressComponent(): TestCheckoutAddressComponent

    override fun attachSplashComponent(): TestSplashComponent

    override fun attachGalleryComponent(): TestGalleryComponent
}