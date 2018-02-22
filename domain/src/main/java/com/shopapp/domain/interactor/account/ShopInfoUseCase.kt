package com.shopapp.domain.interactor.account

import com.shopapp.gateway.entity.Shop
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.ShopRepository
import io.reactivex.Single
import javax.inject.Inject

class ShopInfoUseCase @Inject constructor(
    private val shopRepository: ShopRepository
) : SingleUseCase<Shop, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Shop> {
        return shopRepository.getShop()
    }
}