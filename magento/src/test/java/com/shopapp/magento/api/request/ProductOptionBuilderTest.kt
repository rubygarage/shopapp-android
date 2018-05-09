package com.shopapp.magento.api.request

import com.shopapp.magento.api.Constant
import com.shopapp.magento.api.Constant.ASC_DIRECTION
import com.shopapp.magento.api.Constant.CREATED_AT_FIELD
import com.shopapp.magento.api.Constant.DESC_DIRECTION
import com.shopapp.magento.api.request.ProductOptionBuilder.Companion.SEARCH_CRITERIA_PATTERN
import com.shopapp.magento.api.request.ProductOptionBuilder.Companion.SORT_ORDER_DIRECTION
import com.shopapp.magento.api.request.ProductOptionBuilder.Companion.SORT_ORDER_FIELD
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductOptionBuilderTest {

    @Test
    fun shouldCreateQueryMapWithSortOrder() {
        val query = ProductOptionBuilder().addSortOrder(CREATED_AT_FIELD)
            .build()
        assertEquals(2, query.size)
        assertEquals(CREATED_AT_FIELD, query[SORT_ORDER_FIELD])
        assertEquals(ASC_DIRECTION, query[SORT_ORDER_DIRECTION])
    }

    @Test
    fun shouldCreateQueryMapWithSortOrderReverse() {
        val query = ProductOptionBuilder().addSortOrder(CREATED_AT_FIELD, true)
            .build()
        assertEquals(2, query.size)
        assertEquals(CREATED_AT_FIELD, query[SORT_ORDER_FIELD])
        assertEquals(DESC_DIRECTION, query[SORT_ORDER_DIRECTION])
    }

    @Test
    fun shouldCreateQueryMapWithSingleSearchCriteria() {
        val query = ProductOptionBuilder().addSearchCriteria(Pagination.PAGE_SIZE.value, 1)
            .build()
        assertEquals(1, query.size)
        assertEquals(1.toString(), query[SEARCH_CRITERIA_PATTERN.format(Pagination.PAGE_SIZE.value)])
    }

    @Test
    fun shouldCreateQueryMapWithSingleSearchCriteriaWhenAddSameMultiplyTimes() {
        val query = ProductOptionBuilder().addSearchCriteria(Pagination.PAGE_SIZE.value, 1)
            .addSearchCriteria(Pagination.PAGE_SIZE.value, 1)
            .build()
        assertEquals(1, query.size)
        assertEquals(1.toString(), query[SEARCH_CRITERIA_PATTERN.format(Pagination.PAGE_SIZE.value)])
    }

    @Test
    fun shouldCreateQueryMapWithSingleFilterGroup() {
        val query = ProductOptionBuilder().addFilterGroup(Constant.TYPE_ID_FIELD, Constant.PRODUCT_DEFAULT_TYPE_ID)
            .build()
        assertEquals(3, query.size)

        assertTrue(query.containsKey("searchCriteria[filterGroups][0][filters][0][field]"))
        assertTrue(query.containsKey("searchCriteria[filterGroups][0][filters][0][value]"))
        assertTrue(query.containsKey("searchCriteria[filterGroups][0][filters][0][condition_type]"))

        assertTrue(query.containsValue(Constant.TYPE_ID_FIELD))
        assertTrue(query.containsValue(Constant.PRODUCT_DEFAULT_TYPE_ID))
        assertTrue(query.containsValue(Constant.TYPE_ID_FIELD))
    }

    @Test
    fun shouldCreateQueryMapWithMultiplyFilterGroup() {
        val query = ProductOptionBuilder().addFilterGroup(Constant.TYPE_ID_FIELD, Constant.PRODUCT_DEFAULT_TYPE_ID)
            .addFilterGroup(Constant.TYPE_ID_FIELD, Constant.PRODUCT_DEFAULT_TYPE_ID)
            .build()
        assertEquals(6, query.size)

        assertTrue(query.containsKey("searchCriteria[filterGroups][0][filters][0][field]"))
        assertTrue(query.containsKey("searchCriteria[filterGroups][1][filters][0][field]"))
    }

    @Test
    fun shouldCreateQueryMapWithMultiplyFilterGroupWithBuilder() {
        val query = ProductOptionBuilder().addFilterGroup({ groupIndex ->
            ProductOptionBuilder.FilterBuilder()
                .addFilter(groupIndex, Constant.TYPE_ID_FIELD, Constant.PRODUCT_DEFAULT_TYPE_ID)
                .addFilter(groupIndex, Constant.TYPE_ID_FIELD, Constant.PRODUCT_DEFAULT_TYPE_ID)
        })
            .build()
        assertEquals(6, query.size)

        assertTrue(query.containsKey("searchCriteria[filterGroups][0][filters][0][field]"))
        assertTrue(query.containsKey("searchCriteria[filterGroups][0][filters][1][field]"))
    }
}