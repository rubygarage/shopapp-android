package com.shopapp.ui.category.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.category.CategoryUseCase
import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.util.MockInstantiator
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
    fun shouldPrintStackTrace() {
        val error: Throwable = mock()
        given(categoryUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.loadProductList(PER_PAGE, PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val params = CategoryUseCase.Params(PER_PAGE, PAGINATION_VALUE, MockInstantiator.DEFAULT_ID, SORT_TYPE)
        val inOrder = inOrder(categoryUseCase, error)
        inOrder.verify(categoryUseCase).execute(any(), any(), eq(params))
        inOrder.verify(error).printStackTrace()
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }
}
