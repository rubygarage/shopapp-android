package com.shopify.api.adapter

import com.client.shop.getaway.entity.Country
import com.shopify.api.entity.ApiCountry

object CountryAdapter {

    fun adapt(data: ApiCountry): Country {
        return Country(
            id = data.id,
            code = data.code,
            name = data.name,
            states = StateListAdapter.adapt(data.states)
        )
    }

}