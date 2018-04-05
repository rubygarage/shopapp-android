package com.shopapp.ui.category.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.category.CategoryUseCase
import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.test.MockInstantiator
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Single
import org.junit.*
import org.junit.Assert.assertTrue
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CategoryPresenterTest {

    companion object {
        private const val PER_PAGE = 4
        private const val PAGINATION_VALUE = "paginationValue"
        private val SORT_TYPE = SortType.RECENT
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: CategoryView

    @Mock
    private lateinit var categoryUseCase: CategoryUseCase

    private lateinit var presenter: CategoryPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CategoryPresenter(categoryUseCase)
        presenter.attachView(view)
        categoryUseCase.mock()
    }

    @Test
    fun shouldExecuteUseCase() {
        val products: List<Product> = mock()
        val category: Category = mock {
            on { productList } doReturn products
        }
        given(categoryUseCase.buildUseCaseSingle(any())).willReturn(Single.just(category))
        presenter.loadProductList(PER_PAGE, PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val params = CategoryUseCase.Params(PER_PAGE, PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        verify(categoryUseCase).execute(any(), any(), eq(params))
    }

    @Test
    fun shouldShowContent() {
        val products: List<Product> = mock()
        val category: Category = mock {
            on { productList } doReturn products
        }
        given(categoryUseCase.buildUseCaseSingle(any())).willReturn(Single.just(category))
        presenter.loadProductList(PER_PAGE, PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val params = CategoryUseCase.Params(PER_PAGE, PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val inOrder = inOrder(categoryUseCase, view)
        inOrder.verify(categoryUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view).showContent(products)
    }

    @Test
    fun shouldShowEmptyStateWhenReceiveEmptyList() {
        val products: List<Product> = emptyList()
        val category: Category = mock {
            on { productList } doReturn products
        }
        given(categoryUseCase.buildUseCaseSingle(any())).willReturn(Single.just(category))
        presenter.loadProductList(PER_PAGE, null, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val params = CategoryUseCase.Params(PER_PAGE, null, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val inOrder = inOrder(categoryUseCase, view)
        inOrder.verify(categoryUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view).showEmptyState()
    }

    @Test
    fun shouldNotShowEmptyStateWhenReceiveEmptyList() {

        val products: List<Product> = emptyList()
        val category: Category = mock {
            on { productList } doReturn products
        }
        given(categoryUseCase.buildUseCaseSingle(any())).willReturn(Single.just(category))
        presenter.loadProductList(PER_PAGE, MockInstantiator.DEFAULT_PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val params = CategoryUseCase.Params(PER_PAGE, MockInstantiator.DEFAULT_PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val inOrder = inOrder(categoryUseCase, view)
        inOrder.verify(categoryUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view, never()).showEmptyState()
    }


    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(categoryUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.loadProductList(PER_PAGE, MockInstantiator.DEFAULT_PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val params = CategoryUseCase.Params(PER_PAGE, MockInstantiator.DEFAULT_PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)

        val inOrder = inOrder(view, categoryUseCase)
        inOrder.verify(categoryUseCase).execute(any(), any(), eq(params))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(categoryUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.loadProductList(PER_PAGE, MockInstantiator.DEFAULT_PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val params = CategoryUseCase.Params(PER_PAGE, MockInstantiator.DEFAULT_PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)

        val inOrder = inOrder(view, categoryUseCase)
        inOrder.verify(categoryUseCase).execute(any(), any(), eq(params))
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
