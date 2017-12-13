package com.data.impl

import com.data.rx.RxCallbackSingle
import com.domain.entity.Product
import com.domain.entity.SortType
import com.domain.network.Api
import com.domain.repository.ProductRepository
import io.reactivex.Single

class ProductRepositoryImpl(private val api: Api) : ProductRepository {

    override fun getProduct(productId: String): Single<Product> {
        return Single.create<Product> { emitter ->
            api.getProduct(productId, RxCallbackSingle<Product>(emitter))
        }
    }

    override fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?, reverse: Boolean): Single<List<Product>> {
        return Single.create<List<Product>> { emitter ->
            api.getProductList(perPage, paginationValue, sortBy, reverse,
                    RxCallbackSingle<List<Product>>(emitter))
        }
    }

    override fun searchProductListByQuery(searchQuery: String, perPage: Int, paginationValue: String?): Single<List<Product>> {
        return Single.create<List<Product>> { emitter ->
            api.searchProductList(perPage, paginationValue, searchQuery,
                    RxCallbackSingle<List<Product>>(emitter))
        }
    }
}