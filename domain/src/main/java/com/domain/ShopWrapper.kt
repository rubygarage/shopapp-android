package com.domain

import com.domain.network.Api
import com.domain.router.Router

interface ShopWrapper {

    val api: Api
    val router: Router
}