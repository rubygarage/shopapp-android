package com.shopapp.ui.search.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.search.SearchUseCase
import com.shopapp.gateway.entity.Product
import com.shopapp.test.MockInstantiator.DEFAULT_PAGINATION_VALUE
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchPresenterTest {

    companion object {
        private const val SEARCH_QUERY = "search query"
        private const val EMPTY_SEARCH_QUERY = ""
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: SearchView

    @Mock
    private lateinit var searchUseCase: SearchUseCase

    private lateinit var presenter: SearchPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = SearchPresenter(searchUseCase)
        presenter.attachView(view)
        searchUseCase.mock()
    }

    @Test
    fun shouldCallUseCaseOnLoadItems() {
        val products: List<Product> = mock()
        given(searchUseCase.buildUseCaseSingle(any())).willReturn(Single.just(products))
        presenter.search(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE, SEARCH_QUERY)
        verify(searchUseCase).execute(any(), any(), any())
    }

    @Test
    fun shouldShowContent() {
        val products: List<Product> = mock()
        given(searchUseCase.buildUseCaseSingle(any())).willReturn(Single.just(products))
        presenter.search(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE, SEARCH_QUERY)
        verify(view).showContent(products)
    }

    @Test
    fun shouldHideProgressOnSearchError() {
        val error: Throwable = mock()
        given(searchUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.search(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE, SEARCH_QUERY)
        val inOrder = inOrder(searchUseCase, view)
        inOrder.verify(searchUseCase).execute(any(), any(), any())
        inOrder.verify(view).hideProgress()
    }

    @Test
    fun shouldShowContentWithEmptyList() {
        presenter.search(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE, EMPTY_SEARCH_QUERY)
        verify(view).showContent(listOf())
        verify(searchUseCase, never()).execute(any(), any(), any())
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

}