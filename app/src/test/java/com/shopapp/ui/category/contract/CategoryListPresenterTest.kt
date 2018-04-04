package com.shopapp.ui.category.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.category.CategoryListUseCase
import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.Error
import com.shopapp.test.MockInstantiator.DEFAULT_PAGINATION_VALUE
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import io.reactivex.Single
import org.junit.*
import org.junit.Assert.assertTrue
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
        categoryListUseCase.mock()
    }

    @Test
    fun shouldExecuteUseCase() {
        val categories: List<Category> = mock()
        given(categoryListUseCase.buildUseCaseSingle(any())).willReturn(Single.just(categories))
        presenter.getCategoryList(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        val params = CategoryListUseCase.Params(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        verify(categoryListUseCase).execute(any(), any(), eq(params))
    }

    @Test
    fun shouldShowContent() {
        val categories: List<Category> = mock()
        given(categoryListUseCase.buildUseCaseSingle(any())).willReturn(Single.just(categories))
        presenter.getCategoryList(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        val params = CategoryListUseCase.Params(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        val inOrder = inOrder(categoryListUseCase, view)
        inOrder.verify(categoryListUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view).showContent(categories)
    }


    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(categoryListUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getCategoryList(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        val params = CategoryListUseCase.Params(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)

        val inOrder = inOrder(view, categoryListUseCase)
        inOrder.verify(categoryListUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(categoryListUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getCategoryList(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        val params = CategoryListUseCase.Params(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)

        val inOrder = inOrder(view, categoryListUseCase)
        inOrder.verify(categoryListUseCase).execute(any(), any(), eq(params))
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }
}
