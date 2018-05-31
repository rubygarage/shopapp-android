package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.Constant.AUTHORIZATION_HEADER
import com.shopapp.magento.api.request.*
import com.shopapp.magento.api.response.CustomerResponse
import io.reactivex.Single
import retrofit2.http.*

interface CustomerService {

    companion object {
        const val SIGN_IN_URL = "integration/customer/token"
        const val SIGN_UP_URL = "customers"
        const val CUSTOMER_URL = "customers/me"
        const val CHANGE_PASSWORD_URL = "customers/me/password"
        const val FORGOT_PASSWORD_URL = "customers/password"
    }

    @POST(SIGN_IN_URL)
    fun signIn(@Body signInRequest: SignInRequest): Single<String>

    @POST(SIGN_UP_URL)
    fun signUp(@Body signUpRequest: SignUpRequest): Single<Unit>

    @GET(CUSTOMER_URL)
    fun getCustomer(@Header(AUTHORIZATION_HEADER) token: String): Single<CustomerResponse>

    @PUT(CUSTOMER_URL)
    fun editCustomer(@Header(AUTHORIZATION_HEADER) token: String, @Body editCustomerRequest: EditCustomerRequest): Single<CustomerResponse>

    @PUT(CHANGE_PASSWORD_URL)
    fun changePassword(@Header(AUTHORIZATION_HEADER) token: String, @Body changePasswordRequest: ChangePasswordRequest): Single<Boolean>

    @PUT(FORGOT_PASSWORD_URL)
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Single<Boolean>
}