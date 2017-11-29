package com.client.shop.ui.home.contract

import com.client.shop.const.Constant.MAXIMUM_PER_PAGE_COUNT
import com.domain.entity.Category
import com.domain.entity.Shop
import com.domain.interactor.base.SingleUseCase
import com.repository.CategoryRepository
import com.repository.ShopRepository
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

interface HomeView : BaseView<Pair<Shop, List<Category>>>

class HomePresenter @Inject constructor(private val homeUseCase: HomeUseCase) :
        BasePresenter<Pair<Shop, List<Category>>, HomeView>(arrayOf(homeUseCase)) {

    fun requestData() {

        homeUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                Unit)
    }
}

class HomeUseCase @Inject constructor(
        private val shopRepository: ShopRepository,
        private val categoryRepository: CategoryRepository
) : SingleUseCase<Pair<Shop, List<Category>>, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Pair<Shop, List<Category>>> {
        return Single.zip<Shop, List<Category>, Pair<Shop, List<Category>>>(
                shopRepository.getShop(), categoryRepository.getCategoryList(MAXIMUM_PER_PAGE_COUNT),
                BiFunction { shop, categories -> Pair(shop, categories) }
        )
    }
}