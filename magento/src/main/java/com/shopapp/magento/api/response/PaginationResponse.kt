package com.shopapp.magento.api.response

import com.shopapp.magento.api.Constant

abstract class PaginationResponse(private val totalCount: Int) {

    protected fun calculatePaginationValue(page: Int, perPage: Int): Int {
        return if (totalCount >= page * perPage && page != Constant.PAGINATION_END_VALUE) {
            page + 1
        } else {
            Constant.PAGINATION_END_VALUE
        }
    }
}