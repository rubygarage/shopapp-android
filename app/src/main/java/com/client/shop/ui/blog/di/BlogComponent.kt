package com.client.shop.ui.blog.di

import com.client.shop.ui.blog.BlogActivity
import com.client.shop.ui.blog.BlogFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(BlogModule::class))
interface BlogComponent {

    fun inject(activity: BlogActivity)

    fun inject(fragment: BlogFragment)
}