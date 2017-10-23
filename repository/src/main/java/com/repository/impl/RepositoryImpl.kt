package com.repository.impl

import com.domain.entity.*
import com.repository.*
import io.reactivex.Single

class RepositoryImpl(private val shopRepository: ShopRepository,
                     private val blogRepository: BlogRepository,
                     private val productRepository: ProductRepository,
                     private val categoryRepository: CategoryRepository) : Repository {

    override fun getShop(): Single<Shop> {
        return shopRepository.getShop()
    }

    override fun getCategory(categoryId: String, productPerPage: Int, productPaginationValue: String?,
                             sortBy: SortType?, reverse: Boolean): Single<Category> {
        return categoryRepository.getCategory(categoryId, productPerPage, productPaginationValue,
                sortBy, reverse)
    }

    override fun getCategoryList(perPage: Int, paginationValue: String?, sortBy: SortType?,
                                 reverse: Boolean): Single<List<Category>> {
        return categoryRepository.getCategoryList(perPage, paginationValue, sortBy, reverse)
    }

    override fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                reverse: Boolean): Single<List<Product>> {
        return productRepository.getProductList(perPage, paginationValue, sortBy, reverse)
    }

    override fun searchProductListByQuery(searchQuery: String, perPage: Int,
                                          paginationValue: String?): Single<List<Product>> {
        return productRepository.searchProductListByQuery(searchQuery, perPage, paginationValue)
    }

    override fun getProduct(productId: String): Single<Product> {
        return productRepository.getProduct(productId)
    }

    override fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                reverse: Boolean): Single<List<Article>> {
        return blogRepository.getArticleList(perPage, paginationValue, sortBy, reverse)
    }
}