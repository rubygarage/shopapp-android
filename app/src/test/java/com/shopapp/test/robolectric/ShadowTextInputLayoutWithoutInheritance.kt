package com.shopapp.test.robolectric

import android.support.design.widget.TextInputLayout
import org.robolectric.annotation.Implements
import org.robolectric.annotation.RealObject
import org.robolectric.shadows.ShadowLinearLayout

@Implements(value = TextInputLayout::class)
class ShadowTextInputLayoutWithoutInheritance : ShadowLinearLayout() {

    @RealObject
    private lateinit var realTextInputLayout: TextInputLayout
}