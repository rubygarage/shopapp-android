package com.domain.interactor.account

import com.client.shop.getaway.entity.Shop
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.ShopRepository
import io.reactivex.Single
import javax.inject.Inject

class ShopInfoUseCase @Inject constructor(
    private val shopRepository: ShopRepository
) : SingleUseCase<Shop, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Shop> {
        return shopRepository.getShop()
    }
}