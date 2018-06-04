package com.shopapp.magento.api.request

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdatePasswordRequestTest {

    @Test
    fun shouldCorrectlySerialize() {

        val currentPassword = "currentTestPassword"
        val newPassword = "newTestPassword"
        val request = UpdatePasswordRequest(currentPassword, newPassword)

        val gson = Gson()
        val result = gson.toJson(request)

        assertEquals("{\"currentPassword\":\"$currentPassword\",\"newPassword\":\"$newPassword\"}", result)
    }
}