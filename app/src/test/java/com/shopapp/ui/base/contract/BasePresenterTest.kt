package com.client.shop.ui.base.contract

import com.domain.interactor.base.UseCase
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.ui.base.contract.BasePresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class BasePresenterTest {

    @Mock
    private lateinit var useCase: UseCase

    private lateinit var presenter: BasePresenter<MvpView>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldAttachUseCaseToLifecycle() {
        presenter = BasePresenter(useCase)
        verify(useCase).attachToLifecycle()
    }

    @Test
    fun shouldDisposeUseCaseOnDetachView() {
        presenter = BasePresenter(useCase)
        verify(useCase).attachToLifecycle()

        presenter.detachView(false)
        verify(useCase).dispose()
    }
}
