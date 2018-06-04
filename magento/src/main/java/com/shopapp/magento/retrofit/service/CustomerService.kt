package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.Constant.AUTHORIZATION_HEADER
import com.shopapp.magento.api.request.*
import com.shopapp.magento.api.response.CountryListResponse
import com.shopapp.magento.api.response.CountryResponse
import com.shopapp.magento.api.response.CustomerResponse
import io.reactivex.Single
import retrofit2.http.*

interface CustomerService {

    companion object {
        const val SIGN_IN_URL = "integration/customer/token"
        const val SIGN_UP_URL = "customers"
        const val CUSTOMER_URL = "customers/me"
        const val UPDATE_PASSWORD_URL = "customers/me/password"
        const val RESET_PASSWORD_URL = "customers/password"
        const val COUNTRIES_URL = "directory/countries"
    }

    @POST(SIGN_IN_URL)
    fun signIn(@Body request: SignInRequest): Single<String>

    @POST(SIGN_UP_URL)
    fun signUp(@Body request: SignUpRequest): Single<Unit>

    @GET(CUSTOMER_URL)
    fun getCustomer(@Header(AUTHORIZATION_HEADER) token: String): Single<CustomerResponse>

    @PUT(CUSTOMER_URL)
    fun updateCustomer(
        @Header(AUTHORIZATION_HEADER) token: String,
        @Body request: UpdateCustomerRequest
    ): Single<CustomerResponse>

    @PUT(UPDATE_PASSWORD_URL)
    fun updatePassword(
        @Header(AUTHORIZATION_HEADER) token: String,
        @Body request: UpdatePasswordRequest
    ): Single<Boolean>

    @PUT(RESET_PASSWORD_URL)
    fun resetPassword(@Body request: ResetPasswordRequest): Single<Boolean>

    @GET(COUNTRIES_URL)
    fun getCountries(): Single<List<CountryListResponse.CountryResponse>>
}