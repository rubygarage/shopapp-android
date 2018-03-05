package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.*
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.CategoryRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.SortType
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

class CategoryRepositoryTest {

    companion object {
        private const val perPage = 5
        private const val paginationValue = "pagination"
        private const val categoryId = "categoryId"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    private lateinit var repository: CategoryRepository

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = CategoryRepositoryImpl(api)
    }

    @Test
    fun getCategoryShouldDelegateCallToApi() {
        repository.getCategory(categoryId, perPage, paginationValue, SortType.NAME).subscribe()
        verify(api).getCategoryDetails(eq(categoryId), eq(perPage), eq(paginationValue), eq(SortType.NAME), any())
    }

    @Test
    fun getCategoryShouldReturnValueWhenOnResult() {
        val category: Category = mock()
        given(api.getCategoryDetails(any(), any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Category>>(4)
            callback.onResult(category)
        })
        repository.getCategory(categoryId, perPage, paginationValue, SortType.NAME)
            .test()
            .assertValue(category)
    }

    @Test
    fun getCategoryShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getCategoryDetails(any(), any(), any(), any(), any())).will {
            val callback = it.getArgument<ApiCallback<Category>>(4)
            callback.onFailure(error)
        }

        repository.getCategory(categoryId, perPage, paginationValue, SortType.NAME)
            .test()
            .assertError(error)
    }

    @Test
    fun getCategoryListShouldDelegateCallToApi() {
        repository.getCategoryList(perPage, paginationValue).test()
        verify(api).getCategoryList(eq(perPage), eq(paginationValue), any())
    }

    @Test
    fun getCategoryListShouldReturnValueWhenOnResult() {
        val categoryList: List<Category> = mock()
        given(api.getCategoryList(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Category>>>(2)
            callback.onResult(categoryList)
        })

        repository.getCategoryList(perPage, paginationValue)
            .test()
            .assertValue(categoryList)
    }

    @Test
    fun getCategoryListShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getCategoryList(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Category>>>(2)
            callback.onFailure(error)
        })

        repository.getCategoryList(perPage, paginationValue)
            .test()
            .assertError(error)
    }
}