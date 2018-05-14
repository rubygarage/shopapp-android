package com.shopapp.magento.api

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.SortType
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MagentoApiCategoryTest : BaseMagentoApiTest() {

    @Test
    fun getCategoryDetailsWithNameSortTypeShouldReturnCategory() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductListResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CategoryDetailsResponse.json")))

        val testId = "testId"
        val perPage = 10
        val paginationValue = "2"
        val sortType = SortType.NAME
        val callback: ApiCallback<Category> = mock()
        api.getCategoryDetails(testId, perPage, paginationValue, sortType, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        assertTrue(productRequest.path.contains(perPage.toString()))
        assertTrue(productRequest.path.contains(paginationValue))
        assertTrue(productRequest.path.contains(testId))
        assertTrue(productRequest.path.contains(Constant.ASC_DIRECTION))

        val categoryRequest = server.takeRequest()
        assertEquals("/categories/$testId", categoryRequest.path)

        argumentCaptor<Category>().apply {
            verify(callback).onResult(capture())

            assertEquals("Jackets", firstValue.title)
            assertEquals("24-MG04", firstValue.productList[0].id)
        }
    }

    @Test
    fun getCategoryDetailsWithRecentSortTypeShouldReturnCategory() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductListResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CategoryDetailsResponse.json")))

        val testId = "testId"
        val perPage = 10
        val paginationValue = "2"
        val sortType = SortType.RECENT
        val callback: ApiCallback<Category> = mock()
        api.getCategoryDetails(testId, perPage, paginationValue, sortType, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        assertTrue(productRequest.path.contains(perPage.toString()))
        assertTrue(productRequest.path.contains(paginationValue))
        assertTrue(productRequest.path.contains(testId))
        assertTrue(productRequest.path.contains(Constant.CREATED_AT_FIELD))
        assertTrue(productRequest.path.contains(Constant.DESC_DIRECTION))

        val categoryRequest = server.takeRequest()
        assertEquals("/categories/$testId", categoryRequest.path)

        argumentCaptor<Category>().apply {
            verify(callback).onResult(capture())

            assertEquals("Jackets", firstValue.title)
            assertEquals("24-MG04", firstValue.productList[0].id)
        }
    }

    @Test
    fun getCategoryDetailsWithPriceHighToLowSortTypeShouldReturnCategory() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductListResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CategoryDetailsResponse.json")))

        val testId = "testId"
        val perPage = 10
        val paginationValue = "2"
        val sortType = SortType.PRICE_HIGH_TO_LOW
        val callback: ApiCallback<Category> = mock()
        api.getCategoryDetails(testId, perPage, paginationValue, sortType, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        assertTrue(productRequest.path.contains(perPage.toString()))
        assertTrue(productRequest.path.contains(paginationValue))
        assertTrue(productRequest.path.contains(testId))
        assertTrue(productRequest.path.contains(Constant.PRICE_FIELD))
        assertTrue(productRequest.path.contains(Constant.DESC_DIRECTION))

        val categoryRequest = server.takeRequest()
        assertEquals("/categories/$testId", categoryRequest.path)

        argumentCaptor<Category>().apply {
            verify(callback).onResult(capture())

            assertEquals("Jackets", firstValue.title)
            assertEquals("24-MG04", firstValue.productList[0].id)
        }
    }

    @Test
    fun getCategoryDetailsWithPriceLowToHighSortTypeShouldReturnCategory() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductListResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CategoryDetailsResponse.json")))

        val testId = "testId"
        val perPage = 10
        val paginationValue = "2"
        val sortType = SortType.PRICE_LOW_TO_HIGH
        val callback: ApiCallback<Category> = mock()
        api.getCategoryDetails(testId, perPage, paginationValue, sortType, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        assertTrue(productRequest.path.contains(perPage.toString()))
        assertTrue(productRequest.path.contains(paginationValue))
        assertTrue(productRequest.path.contains(testId))
        assertTrue(productRequest.path.contains(Constant.PRICE_FIELD))
        assertTrue(productRequest.path.contains(Constant.ASC_DIRECTION))

        val categoryRequest = server.takeRequest()
        assertEquals("/categories/$testId", categoryRequest.path)

        argumentCaptor<Category>().apply {
            verify(callback).onResult(capture())

            assertEquals("Jackets", firstValue.title)
            assertEquals("24-MG04", firstValue.productList[0].id)
        }
    }

    @Test
    fun getCategoryListShouldReturnCategoryList() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CategoryListResponse.json")))

        val perPage = 10
        val parentCategoryId = "parentCategoryId"
        val callback: ApiCallback<List<Category>> = mock()
        api.getCategoryList(perPage, null, parentCategoryId, callback)

        argumentCaptor<List<Category>>().apply {
            verify(callback).onResult(capture())

            assertEquals(21.toString(), firstValue[0].id)
            assertEquals("Tops", firstValue[0].title)
        }
    }

    @Test
    fun getCategoryListShouldReturnEmptyList() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CategoryListResponse.json")))

        val perPage = 10
        val paginationValue = "2"
        val parentCategoryId = "parentCategoryId"
        val callback: ApiCallback<List<Category>> = mock()
        api.getCategoryList(perPage, paginationValue, parentCategoryId, callback)

        argumentCaptor<List<Category>>().apply {
            verify(callback).onResult(capture())

            assertTrue(firstValue.isEmpty())
        }
    }
}