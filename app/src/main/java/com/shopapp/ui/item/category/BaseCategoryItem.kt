package com.shopapp.ui.item.category

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import com.shopapp.R
import com.shopapp.gateway.entity.Category

@Suppress("LeakingThis")
abstract class BaseCategoryItem(context: Context) : ConstraintLayout(context) {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        inflate(context, getLayout(), this)
        setBackgroundResource(R.color.white)
    }

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun setCategory(category: Category)
}