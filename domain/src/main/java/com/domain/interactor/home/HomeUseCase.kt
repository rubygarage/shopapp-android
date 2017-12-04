package com.domain.interactor.home

import com.domain.entity.Category
import com.domain.entity.Shop
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CategoryRepository
import com.domain.repository.ShopRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class HomeUseCase @Inject constructor(
        private val shopRepository: ShopRepository,
        private val categoryRepository: CategoryRepository
) : SingleUseCase<Pair<Shop, List<Category>>, Int>() {

    override fun buildUseCaseSingle(params: Int): Single<Pair<Shop, List<Category>>> {
        return Single.zip<Shop, List<Category>, Pair<Shop, List<Category>>>(
                shopRepository.getShop(), categoryRepository.getCategoryList(params),
                BiFunction { shop, categories -> Pair(shop, categories) }
        )
    }
}