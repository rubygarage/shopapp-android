package com.shopapp.ui.category.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.category.GetCategoriesUseCase
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
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    private lateinit var presenter: CategoryListPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CategoryListPresenter(getCategoriesUseCase)
        presenter.attachView(view)
        getCategoriesUseCase.mock()
    }

    @Test
    fun shouldExecuteUseCase() {
        val categories: List<Category> = mock()
        given(getCategoriesUseCase.buildUseCaseSingle(any())).willReturn(Single.just(categories))
        presenter.getCategoryList(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        val params =
            GetCategoriesUseCase.Params(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        verify(getCategoriesUseCase).execute(any(), any(), eq(params))
    }

    @Test
    fun shouldShowContent() {
        val categories: List<Category> = mock()
        given(getCategoriesUseCase.buildUseCaseSingle(any())).willReturn(Single.just(categories))
        presenter.getCategoryList(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        val params =
            GetCategoriesUseCase.Params(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE)
        val inOrder = inOrder(getCategoriesUseCase, view)
        inOrder.verify(getCategoriesUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view).showContent(categories)
    }


    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(getCategoriesUseCase.buildUseCaseSingle(any())).willReturn(
            Single.error(
                Error.NonCritical(
                    "ErrorMessage"
                )
            )
        )
        presenter.getCategoryList(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE, DEFAULT_ID)
        val params = GetCategoriesUseCase.Params(
            DEFAULT_PER_PAGE_COUNT,
            DEFAULT_PAGINATION_VALUE
        )

        val inOrder = inOrder(view, getCategoriesUseCase)
        inOrder.verify(getCategoriesUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(getCategoriesUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getCategoryList(DEFAULT_PER_PAGE_COUNT, DEFAULT_PAGINATION_VALUE, DEFAULT_ID)
        val params = GetCategoriesUseCase.Params(
            DEFAULT_PER_PAGE_COUNT,
            DEFAULT_PAGINATION_VALUE
        )

        val inOrder = inOrder(view, getCategoriesUseCase)
        inOrder.verify(getCategoriesUseCase).execute(any(), any(), eq(params))
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
