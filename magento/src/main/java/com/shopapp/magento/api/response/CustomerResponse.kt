package com.shopapp.magento.api.response

import com.google.gson.annotations.SerializedName
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.State

class CustomerResponse(

    @SerializedName("firstname")
    val firstName: String,

    @SerializedName("lastname")
    val lastName: String,

    @SerializedName("default_shipping")
    val defaultShippingId: String,

    val id: Int,
    val email: String,
    val addresses: List<AddressData>
) {

    fun mapToEntity(countries: List<Country>): Customer {

        val addressList = addresses.mapNotNull { it.mapToEntity(countries) }
        val defaultAddress = addressList.find { it.id == defaultShippingId }

        return Customer(
            id = id.toString(),
            email = email,
            firstName = firstName,
            lastName = lastName,
            phone = null,
            isAcceptsMarketing = false,
            addressList = addressList,
            defaultAddress = defaultAddress
        )
    }

    class AddressData(
        val id: String,
        val countryId: String,
        val street: List<String>,
        val telephone: String,
        val postcode: String,
        val city: String,
        val firstname: String,
        val lastname: String,
        val region: RegionData,
        val defaultShipping: Boolean
    ) {

        fun mapToEntity(countries: List<Country>): Address? {
            return countries.find { it.id == countryId }
                ?.let {

                    val state = region.mapToEntity(it.id)

                    Address(
                        id = id,
                        address = street.firstOrNull() ?: "",
                        secondAddress = street.getOrNull(1),
                        city = city,
                        country = it,
                        state = state,
                        firstName = firstname,
                        lastName = lastname,
                        zip = postcode,
                        phone = telephone
                    )
                }
        }
    }

    class RegionData(val regionCode: String?, val region: String?, val regionId: String) {

        fun mapToEntity(countryId: String): State? {
            return if (regionCode != null && region != null) {
                State(regionId, countryId, regionCode, region)
            } else {
                null
            }
        }
    }
}