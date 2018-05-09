package com.shopapp.magento.api

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.gateway.entity.SortType
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class MagentoApiProductTest : BaseMagentoApiTest() {

    @Test
    fun getProductShouldReturnProduct() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductResponse.json")))

        val testId = "testId"
        val callback: ApiCallback<Product> = mock()
        api.getProduct(testId, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        assertEquals("/products/$testId", productRequest.path)

        argumentCaptor<Product>().apply {
            verify(callback).onResult(capture())

            assertEquals("USD", firstValue.currency)
            assertEquals("WSH10-28-Orange", firstValue.id)
        }
    }

    @Test
    fun getProductListWithRecentSortTypeShouldReturnProductList() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductListResponse.json")))

        val perPage = 10
        val paginationValue = "12"
        val sortBy = SortType.RECENT
        val callback: ApiCallback<List<Product>> = mock()
        api.getProductList(perPage, paginationValue, sortBy, null, null, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        val requestPath = "/products?searchCriteria%5BpageSize%5D=$perPage&searchCriteria%5BsortOrders" +
                "%5D%5B0%5D%5Bfield%5D=created_at&searchCriteria%5BsortOrders%5D%5B0%5D%5Bdirection" +
                "%5D=DESC&searchCriteria%5BcurrentPage%5D=12&searchCriteria%5BfilterGroups%5D%5B0%5D" +
                "%5Bfilters%5D%5B0%5D%5Bfield%5D=type_id&searchCriteria%5BfilterGroups%5D%5B0%5D%5B" +
                "filters%5D%5B0%5D%5Bcondition_type%5D=eq&searchCriteria%5BfilterGroups%5D%5B0%5D%5B" +
                "filters%5D%5B0%5D%5Bvalue%5D=simple"
        assertEquals(requestPath, productRequest.path)

        argumentCaptor<List<Product>>().apply {
            verify(callback).onResult(capture())

            assertEquals("USD", firstValue[0].currency)
            assertEquals("24-MG04", firstValue[0].id)
        }
    }

    @Test
    fun getProductListWithTypeSortTypeShouldReturnProductList() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductListResponse.json")))

        val perPage = 10
        val paginationValue = "12"
        val sortBy = SortType.TYPE
        val keyword = "testKeyword"
        val excludeKeyword = "testExcludeKeyword"
        val callback: ApiCallback<List<Product>> = mock()
        api.getProductList(perPage, paginationValue, sortBy, keyword, excludeKeyword, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        val requestPath = "/products?searchCriteria%5BpageSize%5D=$perPage&searchCriteria%5BcurrentPage" +
                "%5D=12&searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D%5Bfield%5D=" +
                "attribute_set_id&searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D%5B" +
                "condition_type%5D=eq&searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D" +
                "%5Bvalue%5D=$keyword&searchCriteria%5BfilterGroups%5D%5B1%5D%5Bfilters%5D%5B0%5D" +
                "%5Bvalue%5D=$excludeKeyword&searchCriteria%5BfilterGroups%5D%5B1%5D%5Bfilters%5D" +
                "%5B0%5D%5Bcondition_type%5D=neq&searchCriteria%5BfilterGroups%5D%5B1%5D%5Bfilters%5D" +
                "%5B0%5D%5Bfield%5D=name&searchCriteria%5BfilterGroups%5D%5B2%5D%5Bfilters%5D%5B0%5D%5B" +
                "field%5D=type_id&searchCriteria%5BfilterGroups%5D%5B2%5D%5Bfilters%5D%5B0%5D%5B" +
                "value%5D=simple&searchCriteria%5BfilterGroups%5D%5B2%5D%5Bfilters%5D%5B0%5D%5B" +
                "condition_type%5D=eq"
        assertEquals(requestPath, productRequest.path)

        argumentCaptor<List<Product>>().apply {
            verify(callback).onResult(capture())

            assertEquals("USD", firstValue[0].currency)
            assertEquals("24-MG04", firstValue[0].id)
        }
    }

    @Test
    fun getProductListWithRelevantSortTypeShouldReturnEmptyList() {

        val perPage = 10
        val sortBy = SortType.RELEVANT
        val callback: ApiCallback<List<Product>> = mock()
        api.getProductList(perPage, null, sortBy, null, null, callback)

        argumentCaptor<List<Product>>().apply {
            verify(callback).onResult(capture())

            assertTrue(firstValue.isEmpty())
        }
    }

    @Test
    fun searchProductListShouldReturnProductList() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductListResponse.json")))

        val perPage = 10
        val paginationValue = "12"
        val searchQuery = "testSearchQuery"
        val callback: ApiCallback<List<Product>> = mock()
        api.searchProductList(perPage, paginationValue, searchQuery, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        val requestPath = "/products?searchCriteria%5BpageSize%5D=$perPage&searchCriteria%5BcurrentPage" +
                "%5D=$paginationValue&searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D%" +
                "5Bfield%5D=name&searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D%5B" +
                "condition_type%5D=like&searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D" +
                "%5Bvalue%5D=%25$searchQuery%25&searchCriteria%5BfilterGroups%5D%5B1%5D%5Bfilters" +
                "%5D%5B0%5D%5Bvalue%5D=simple&searchCriteria%5BfilterGroups%5D%5B1%5D%5Bfilters%5D%" +
                "5B0%5D%5Bcondition_type%5D=eq&searchCriteria%5BfilterGroups%5D%5B1%5D%5Bfilters%5D" +
                "%5B0%5D%5Bfield%5D=type_id"
        assertEquals(requestPath, productRequest.path)

        argumentCaptor<List<Product>>().apply {
            verify(callback).onResult(capture())

            assertEquals("USD", firstValue[0].currency)
            assertEquals("24-MG04", firstValue[0].id)
        }
    }

    @Test
    fun getProductVariantListShouldReturnProductVariantList() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("StoreConfigResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("ProductListResponse.json")))

        val firstVariantId = "firstVariantId"
        val secondVariantId = "secondVariantId"
        val ids = listOf(firstVariantId, secondVariantId)
        val callback: ApiCallback<List<ProductVariant>> = mock()
        api.getProductVariantList(ids, callback)

        val storeConfigRequest = server.takeRequest()
        assertEquals("/store/storeConfigs", storeConfigRequest.path)

        val productRequest = server.takeRequest()
        val requestPath = "/products?searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D%5B" +
                "field%5D=type_id&searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D%5B" +
                "condition_type%5D=eq&searchCriteria%5BfilterGroups%5D%5B0%5D%5Bfilters%5D%5B0%5D%5B" +
                "value%5D=simple&searchCriteria%5BfilterGroups%5D%5B1%5D%5Bfilters%5D%5B0%5D%5B" +
                "value%5D=$firstVariantId&searchCriteria%5BfilterGroups%5D%5B1%5D%5Bfilters" +
                "%5D%5B0%5D%5Bcondition_type%5D=eq&searchCriteria%5BfilterGroups%5D%5B1%5D%5B" +
                "filters%5D%5B1%5D%5Bfield%5D=sku&searchCriteria%5BfilterGroups%5D%5B1%5D%5B" +
                "filters%5D%5B1%5D%5Bcondition_type%5D=eq&searchCriteria%5BfilterGroups%5D%5B1%5D%5B" +
                "filters%5D%5B0%5D%5Bfield%5D=sku&searchCriteria%5BfilterGroups%5D%5B1%5D%5B" +
                "filters%5D%5B1%5D%5Bvalue%5D=$secondVariantId"
        assertEquals(requestPath, productRequest.path)

        argumentCaptor<List<ProductVariant>>().apply {
            verify(callback).onResult(capture())

            assertEquals("24-MG04", firstValue[0].id)
        }
    }
}