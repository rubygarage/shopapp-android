package com.shopapp.magento.api.request

import com.shopapp.magento.api.Constant.ASC_DIRECTION
import com.shopapp.magento.api.Constant.DESC_DIRECTION

class ProductOptionBuilder {

    companion object {
        private const val FILTER_PARTS_SIZE = 3

        private const val FILTER_GROUP_PATTERN = "searchCriteria[filterGroups][%1\$d][filters][0][%2\$s]"
        private const val SEARCH_CRITERIA_PATTERN = "searchCriteria[%s]"

        private const val SORT_ORDER_FIELD = "searchCriteria[sortOrders][0][field]"
        private const val SORT_ORDER_DIRECTION = "searchCriteria[sortOrders][0][direction]"
    }

    private val filterGroupMap = hashMapOf<String, String>()
    private val searchCriteriaMap = hashMapOf<String, String>()

    fun addFilterGroup(field: String, value: String, condition: ConditionType = ConditionType.DEFAULT): ProductOptionBuilder {
        val index = filterGroupMap.size / FILTER_PARTS_SIZE
        filterGroupMap[FILTER_GROUP_PATTERN.format(index, FilterPart.FIELD.value)] = field
        filterGroupMap[FILTER_GROUP_PATTERN.format(index, FilterPart.VALUE.value)] = value
        filterGroupMap[FILTER_GROUP_PATTERN.format(index, FilterPart.CONDITION.value)] = condition.value
        return this
    }

    fun addSortOrder(field: String, isReverse: Boolean = false): ProductOptionBuilder {
        val direction = if (isReverse) DESC_DIRECTION else ASC_DIRECTION
        searchCriteriaMap[SORT_ORDER_FIELD] = field
        searchCriteriaMap[SORT_ORDER_DIRECTION] = direction
        return this
    }

    fun addSearchCriteria(criteria: String, data: Int): ProductOptionBuilder {
        return addSearchCriteria(criteria, data.toString())
    }

    fun addSearchCriteria(criteria: String, data: String): ProductOptionBuilder {
        val searchCriteria = SEARCH_CRITERIA_PATTERN.format(criteria)
        searchCriteriaMap[searchCriteria] = data
        return this
    }

    fun build(): MutableMap<String, String> {
        val result = mutableMapOf<String, String>()
        result.putAll(filterGroupMap)
        result.putAll(searchCriteriaMap)
        return result
    }

}