package com.shopapp.ui.blog.di

import com.shopapp.ui.blog.ArticleActivity
import com.shopapp.ui.blog.BlogActivity
import com.shopapp.ui.blog.BlogFragment
import dagger.Subcomponent

@Subcomponent(modules = [BlogModule::class])
interface BlogComponent {

    fun inject(activity: BlogActivity)

    fun inject(fragment: BlogFragment)

    fun inject(articleActivity: ArticleActivity)
}