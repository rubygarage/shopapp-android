package com.shopapp.magento.api.response

import com.shopapp.magento.retrofit.RestClient
import com.shopapp.magento.test.JsonFileHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class CustomerResponseTest : BaseResponseTest<CustomerResponse>(CustomerResponse::class.java) {

    override fun getFilename() = "CustomerResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val fileHelper = JsonFileHelper()
        val json = fileHelper.getJsonContents("CountryListResponse.json")
        val countries = RestClient.createGson().fromJson(json, CountryListResponse::class.java)
        val result = response.mapToEntity(countries.mapToEntityList())

        assertEquals("9", result.id)
        assertEquals("test@mail.com", result.email)
        assertEquals("test firstname", result.firstName)
        assertEquals("test lastname", result.lastName)
        assertEquals(1, result.addressList.size)

        val address = result.addressList.first()
        assertEquals(24.toString(), address.id)
        assertEquals("addr1", address.address)
        assertEquals("addr2", address.secondAddress)
        assertEquals("cty", address.city)
        assertEquals("test firstname", address.firstName)
        assertEquals("test lastname", address.lastName)
        assertEquals("49000", address.zip)
        assertEquals("558866666", address.phone)
        assertEquals("AT", address.country.id)
        assertEquals("Austria", address.country.name)
        assertEquals("101", address.state?.id)
        assertEquals("Tirol", address.state?.name)

        assertEquals(address, result.defaultAddress)
    }
}