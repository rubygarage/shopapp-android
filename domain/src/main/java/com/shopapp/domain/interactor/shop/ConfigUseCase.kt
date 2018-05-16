package com.shopapp.domain.interactor.shop

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.ShopRepository
import com.shopapp.gateway.entity.Config
import io.reactivex.Single
import javax.inject.Inject

class ConfigUseCase @Inject constructor(
    private val shopRepository: ShopRepository
) : SingleUseCase<Config, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Config> {
        return shopRepository.getConfig()
    }
}