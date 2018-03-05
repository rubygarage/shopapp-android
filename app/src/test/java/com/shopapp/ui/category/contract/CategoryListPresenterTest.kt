package com.shopapp.ui.category.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.category.CategoryListUseCase
import com.shopapp.gateway.entity.Category
import com.shopapp.test.MockInstantiator
import com.shopapp.test.MockInstantiator.DEFAULT_LIST_SIZE
import com.shopapp.test.MockInstantiator.DEFAULT_PAGINATION_VALUE
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CategoryListPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: CategoryListView

    @Mock
    private lateinit var categoryListUseCase: CategoryListUseCase

    private lateinit var presenter: CategoryListPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CategoryListPresenter(categoryListUseCase)
        presenter.attachView(view)
        MockInstantiator
        categoryListUseCase.mock()
    }

    @Test
    fun shouldExecuteUseCase() {
        val categories: List<Category> = mock()
        given(categoryListUseCase.buildUseCaseSingle(any())).willReturn(Single.just(categories))
        presenter.getCategoryList(DEFAULT_LIST_SIZE, DEFAULT_PAGINATION_VALUE)
        val params = CategoryListUseCase.Params(DEFAULT_LIST_SIZE, DEFAULT_PAGINATION_VALUE)
        verify(categoryListUseCase).execute(any(), any(), eq(params))
    }

    @Test
    fun shouldShowContent() {
        val categories: List<Category> = mock()
        given(categoryListUseCase.buildUseCaseSingle(any())).willReturn(Single.just(categories))
        presenter.getCategoryList(DEFAULT_LIST_SIZE, DEFAULT_PAGINATION_VALUE)
        val params = CategoryListUseCase.Params(DEFAULT_LIST_SIZE, DEFAULT_PAGINATION_VALUE)
        val inOrder = inOrder(categoryListUseCase, view)
        inOrder.verify(categoryListUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view).showContent(categories)
    }

    @Test
    fun shouldPrintStackTrace() {
        val error: Throwable = mock()
        given(categoryListUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.getCategoryList(DEFAULT_LIST_SIZE, DEFAULT_PAGINATION_VALUE)
        val params = CategoryListUseCase.Params(DEFAULT_LIST_SIZE, DEFAULT_PAGINATION_VALUE)
        val inOrder = inOrder(categoryListUseCase, error)
        inOrder.verify(categoryListUseCase).execute(any(), any(), eq(params))
        inOrder.verify(error).printStackTrace()
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }
}
