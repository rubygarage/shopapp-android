package com.shopapp.util.ext

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.interactor.base.SingleUseCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import io.reactivex.disposables.CompositeDisposable

inline fun <reified T : Any> CompletableUseCase<T>.mockUseCase() {
    given(isAttachedToLifecycle()).willReturn(true)
    given(execute(any(), any(), any())).willCallRealMethod()
    given(disposables).willReturn(CompositeDisposable())
}

inline fun <reified T : Any, reified P : Any> SingleUseCase<T, P>.mockUseCase() {
    given(isAttachedToLifecycle()).willReturn(true)
    given(execute(any(), any(), any())).willCallRealMethod()
    given(disposables).willReturn(CompositeDisposable())
}