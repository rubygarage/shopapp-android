package com.shopapp.data.impl

import com.shopapp.data.rx.RxCallbackSingle
import com.shopapp.domain.repository.ProductRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.gateway.entity.SortType
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

    override fun getProductVariantList(productVariantIdList: List<String>): Single<List<ProductVariant>> {
        return Single.create<List<ProductVariant>> { emitter ->
            api.getProductVariantList(productVariantIdList, RxCallbackSingle<List<ProductVariant>>(emitter))
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