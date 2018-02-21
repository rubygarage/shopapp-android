package com.data.impl

import com.client.shop.gateway.Api
import com.client.shop.gateway.entity.Product
import com.client.shop.gateway.entity.SortType
import com.data.rx.RxCallbackSingle
import com.domain.repository.ProductRepository
import io.reactivex.Single

class ProductRepositoryImpl(private val api: Api) : ProductRepository {

    override fun getProduct(productId: String): Single<Product> {
        return Single.create<Product> { emitter ->
            api.getProduct(productId, RxCallbackSingle<Product>(emitter))
        }
    }

    override fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                keyword: String?, excludeKeyword: String?): Single<List<Product>> {
        return Single.create<List<Product>> { emitter ->
            api.getProductList(perPage, paginationValue, sortBy,
                keyword, excludeKeyword, RxCallbackSingle<List<Product>>(emitter))
        }
    }

    override fun searchProductListByQuery(searchQuery: String, perPage: Int,
                                          paginationValue: String?): Single<List<Product>> {
        return Single.create<List<Product>> { emitter ->
            api.searchProductList(perPage, paginationValue, searchQuery,
                RxCallbackSingle<List<Product>>(emitter))
        }
    }
}