package com.shopapp.magento.api.response.util

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.shopapp.magento.api.response.ErrorResponse
import retrofit2.HttpException

object NetworkErrorParser {

    fun parse(httpexception: HttpException): String? {
        val errorBody = httpexception.response().errorBody()?.string()
        return errorBody?.let {
            var message: String? = null
            try {
                message = Gson().fromJson(it, ErrorResponse::class.java).message
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
            return message
        }
    }
}