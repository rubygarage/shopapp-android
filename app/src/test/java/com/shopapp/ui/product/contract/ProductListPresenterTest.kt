package com.shopapp.ui.product.contract

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.inOrder
import com.shopapp.domain.interactor.product.ProductListUseCase
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.const.Constant
import com.shopapp.util.RxImmediateSchedulerRule
import com.shopapp.util.ext.mock
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProductListPresenterTest {

    companion object {
        private const val PAGINATION = "pagination"
        private const val KEYWORD = "keyword"
        private const val EXCLUDE_KEYWORD = "exclude_keyword"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: ProductListView

    @Mock
    private lateinit var useCase: ProductListUseCase

    @Mock
    private lateinit var data: List<Product>

    private lateinit var presenter: ProductListPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = ProductListPresenter(useCase)
        presenter.attachView(view)
        useCase.mock()
    }

    @Test
    fun shouldShowContentOnSingleSuccess() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.just(data))
        presenter.loadProductList(SortType.NAME, Constant.DEFAULT_PER_PAGE_COUNT, PAGINATION, KEYWORD, EXCLUDE_KEYWORD)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(),
            eq(ProductListUseCase.Params(SortType.NAME, Constant.DEFAULT_PER_PAGE_COUNT, PAGINATION, KEYWORD, EXCLUDE_KEYWORD)))
        inOrder.verify(view).showContent(data)
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }
}