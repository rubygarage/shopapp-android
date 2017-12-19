package com.domain

import com.domain.network.Api
import com.domain.router.ExternalRouter

interface ShopWrapper {

    val api: Api
    val router: ExternalRouter
}