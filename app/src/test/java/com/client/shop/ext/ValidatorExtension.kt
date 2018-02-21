package com.client.shop.ext

import com.domain.interactor.base.CompletableUseCase
import com.domain.interactor.base.SingleUseCase
import com.domain.validator.FieldValidator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import io.reactivex.disposables.CompositeDisposable

fun FieldValidator.mock() {
    given(isEmailValid(any())).willReturn(true)
    given(isPasswordValid(any())).willReturn(true)
    given(isAddressValid(any())).willReturn(true)
}