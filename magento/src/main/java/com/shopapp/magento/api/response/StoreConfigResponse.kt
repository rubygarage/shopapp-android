package com.shopapp.magento.api.response

class StoreConfigResponse : ArrayList<StoreConfigResponse.StoreConfig>() {

    class StoreConfig(val baseCurrencyCode: String)

    fun getCurrency() = this[0].baseCurrencyCode
}