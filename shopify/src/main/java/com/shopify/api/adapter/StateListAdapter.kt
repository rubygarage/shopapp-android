package com.shopify.api.adapter

import com.domain.entity.State
import com.shopify.api.entity.ApiState

object StateListAdapter {

    fun adapt(states: List<ApiState>?): List<State> =
        states?.map { StateAdapter.adapt(it) } ?: listOf()
}
