package com.repository.impl

import com.domain.entity.Product
import com.domain.entity.SortType
import com.repository.ProductRepository
import com.repository.rx.RxCallback
import com.apicore.Api
import io.reactivex.Single

class ProductRepositoryImpl(private val api: Api) : ProductRepository {

    override fun getProduct(productId: String): Single<Product> {
        return Single.create<Product> { emitter ->
            api.getProduct(productId, RxCallback<Product>(emitter))
        }
    }

    override fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?, reverse: Boolean): Single<List<Product>> {
        return Single.create<List<Product>> { emitter ->
            api.getProductList(perPage, paginationValue, sortBy, reverse,
                    RxCallback<List<Product>>(emitter))
        }
    }

    override fun searchProductListByQuery(searchQuery: String, perPage: Int, paginationValue: String?): Single<List<Product>> {
        return Single.create<List<Product>> { emitter ->
            api.searchProductList(perPage, paginationValue, searchQuery,
                    RxCallback<List<Product>>(emitter))
        }
    }
}