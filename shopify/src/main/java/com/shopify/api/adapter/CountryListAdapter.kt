package com.shopify.api.adapter

import com.client.shop.gateway.entity.Country
import com.shopify.api.entity.ApiCountry

object CountryListAdapter {

    fun adapt(countries: List<ApiCountry>?): List<Country> =
        countries?.map { CountryAdapter.adapt(it) } ?: listOf()

}
