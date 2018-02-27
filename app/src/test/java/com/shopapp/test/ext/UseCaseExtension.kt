package com.shopapp.test.ext

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.interactor.base.ObservableUseCase
import com.shopapp.domain.interactor.base.SingleUseCase
import io.reactivex.disposables.CompositeDisposable

inline fun <reified T : Any> CompletableUseCase<T>.mock() {
    given(isAttachedToLifecycle()).willReturn(true)
    given(execute(any(), any(), any())).willCallRealMethod()
    given(disposables).willReturn(CompositeDisposable())
}

inline fun <reified T : Any?, reified P : Any> SingleUseCase<T, P>.mock() {
    given(isAttachedToLifecycle()).willReturn(true)
    given(execute(any(), any(), any())).willCallRealMethod()
    given(disposables).willReturn(CompositeDisposable())
}

inline fun <reified T : Any?, reified P : Any> ObservableUseCase<T, P>.mock() {
    given(isAttachedToLifecycle()).willReturn(true)
    given(execute(any(), any(), any(), any())).willCallRealMethod()
    given(disposables).willReturn(CompositeDisposable())
}