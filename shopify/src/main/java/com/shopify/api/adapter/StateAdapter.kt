package com.shopify.api.adapter

import com.domain.entity.State
import com.shopify.api.entity.ApiState

object StateAdapter {

    fun adapt(data: ApiState): State {
        return State(
            id = data.id,
            countryId = data.countryId,
            name = data.name,
            code = data.code
        )
    }
}