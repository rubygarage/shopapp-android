package com.shopapp.magento.api.request

import com.shopapp.magento.api.Constant.ASC_DIRECTION
import com.shopapp.magento.api.Constant.DESC_DIRECTION

class ProductOptionBuilder {

    companion object {
        private const val FILTER_GROUP_PATTERN = "searchCriteria[filterGroups][%1\$d][filters][%2\$d][%3\$s]"
        const val SEARCH_CRITERIA_PATTERN = "searchCriteria[%s]"

        const val SORT_ORDER_FIELD = "searchCriteria[sortOrders][0][field]"
        const val SORT_ORDER_DIRECTION = "searchCriteria[sortOrders][0][direction]"
    }

    private val filterGroup = mutableListOf<Map<String, String>>()
    private val searchCriteriaMap = hashMapOf<String, String>()

    fun addFilterGroup(field: String, value: String, condition: ConditionType = ConditionType.DEFAULT): ProductOptionBuilder {
        val groupIndex = this.filterGroup.size
        val filterOption = FilterBuilder().addFilter(groupIndex, field, value, condition).build()
        filterGroup.add(filterOption)
        return this
    }

    fun addFilterGroup(filterBuilder: (groupIndex: Int) -> FilterBuilder): ProductOptionBuilder {
        val groupIndex = this.filterGroup.size
        val filters = filterBuilder(groupIndex).build()
        filterGroup.add(filters)
        return this
    }

    fun addSortOrder(field: String, isReverse: Boolean = false): ProductOptionBuilder {
        val direction = if (isReverse) DESC_DIRECTION else ASC_DIRECTION
        searchCriteriaMap[SORT_ORDER_FIELD] = field
        searchCriteriaMap[SORT_ORDER_DIRECTION] = direction
        return this
    }

    fun addSearchCriteria(criteria: String, data: Int): ProductOptionBuilder {
        val searchCriteria = SEARCH_CRITERIA_PATTERN.format(criteria)
        searchCriteriaMap[searchCriteria] = data.toString()
        return this
    }

    fun build(): MutableMap<String, String> {
        val result = mutableMapOf<String, String>()
        result.putAll(searchCriteriaMap)
        filterGroup.forEach { result.putAll(it) }
        return result
    }

    class FilterBuilder {

        companion object {
            private const val FILTER_PARTS_SIZE = 3
        }

        private val filterMap = hashMapOf<String, String>()

        fun addFilter(groupIndex: Int, field: String, value: String, condition: ConditionType = ConditionType.DEFAULT): FilterBuilder {
            val filterIndex = filterMap.size / FILTER_PARTS_SIZE
            filterMap[FILTER_GROUP_PATTERN.format(groupIndex, filterIndex, Filter.FIELD.value)] = field
            filterMap[FILTER_GROUP_PATTERN.format(groupIndex, filterIndex, Filter.VALUE.value)] = value
            filterMap[FILTER_GROUP_PATTERN.format(groupIndex, filterIndex, Filter.CONDITION.value)] = condition.value
            return this
        }

        fun build() = filterMap
    }
}