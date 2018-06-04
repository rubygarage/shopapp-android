package com.shopapp.magento.api.response

import com.shopapp.gateway.entity.Country
import com.shopapp.gateway.entity.State

class CountryResponse(val id: String, val fullNameLocale: String, val availableRegions: List<RegionData>?) {

    fun mapToEntity(): Country {
        return Country(
            id = id,
            code = id,
            name = fullNameLocale,
            states = availableRegions?.map { it.mapToEntity(id) }
        )
    }

    class RegionData(val id: String, val code: String, val name: String) {

        fun mapToEntity(countryId: String): State {
            return State(
                id = id,
                countryId = countryId,
                code = code,
                name = name
            )
        }
    }
}