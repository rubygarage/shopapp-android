package com.client.shop.ui.recent.contract

import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.SingleUseCase
import com.domain.entity.Product
import com.domain.entity.SortType
import com.repository.ProductRepository
import io.reactivex.Single
import javax.inject.Inject

interface RecentView : BaseView<List<Product>>

class RecentPresenter @Inject constructor(private val recentUseCase: RecentUseCase) :
        BasePresenter<List<Product>, RecentView>(arrayOf(recentUseCase)) {

    fun loadProductList(perPage: Int, paginationValue: String? = null) {

        recentUseCase.execute(
                { view?.showContent(it) },
                { it.printStackTrace() },
                RecentUseCase.Params(perPage, paginationValue)
        )
    }
}

class RecentUseCase @Inject constructor(private val productRepository: ProductRepository) :
        SingleUseCase<List<Product>, RecentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Product>> {
        return with(params) {
            //TODO MOVE REVERSE TO SHOPIFY
            productRepository.getProductList(perPage, paginationValue, SortType.RECENT, true)
        }
    }

    data class Params(
            val perPage: Int,
            val paginationValue: String?
    )
}