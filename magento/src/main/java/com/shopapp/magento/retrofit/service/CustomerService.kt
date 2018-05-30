package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.Constant.AUTHORIZATION_HEADER
import com.shopapp.magento.api.request.*
import com.shopapp.magento.api.response.CustomerResponse
import io.reactivex.Single
import retrofit2.http.*

interface CustomerService {

    @POST("integration/customer/token")
    fun signIn(@Body signInRequest: SignInRequest): Single<String>

    @POST("customers")
    fun signUp(@Body signUpRequest: SignUpRequest): Single<Unit>

    @GET("customers/me")
    fun getCustomer(@Header(AUTHORIZATION_HEADER) token: String): Single<CustomerResponse>

    @PUT("customers/me")
    fun editCustomer(@Header(AUTHORIZATION_HEADER) token: String, @Body editCustomerRequest: EditCustomerRequest): Single<CustomerResponse>

    @PUT("customers/me/password")
    fun changePassword(@Header(AUTHORIZATION_HEADER) token: String, @Body changePasswordRequest: ChangePasswordRequest): Single<Boolean>

    @PUT("customers/password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Single<Boolean>
}