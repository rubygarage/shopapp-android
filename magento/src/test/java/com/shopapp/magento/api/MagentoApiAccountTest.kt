package com.shopapp.magento.api

import com.nhaarman.mockito_kotlin.*
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.Error
import com.shopapp.magento.api.Constant.ACCESS_TOKEN_PREFIX
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.HttpURLConnection

class MagentoApiAccountTest : BaseMagentoApiTest() {

    @Test
    fun signInShouldReturnUnit() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("TokenResponse.json")))

        val email = "test@mail.ru"
        val password = "password"
        val callback: ApiCallback<Unit> = mock()
        api.signIn(email, password, callback)

        val request = server.takeRequest()
        assertEquals("/integration/customer/token", request.path)

        verify(callback).onResult(Unit)

        verify(sharedPreferences.edit()).putString(Constant.ACCESS_KEY, password)
        verify(sharedPreferences.edit()).putString(Constant.ACCESS_TOKEN, "$ACCESS_TOKEN_PREFIX cpylcc7caabqmta9u21ixsavl3f1i3hq")
    }

    @Test
    fun signInShouldReturnError() {

        val response = MockResponse()
        response.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        response.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(response)

        val email = "test@mail.ru"
        val password = "password"
        val callback: ApiCallback<Unit> = mock()
        api.signIn(email, password, callback)

        val request = server.takeRequest()
        assertEquals("/integration/customer/token", request.path)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun signOutShouldReturnUnit() {

        val callback: ApiCallback<Unit> = mock()
        api.signOut(callback)

        verify(callback).onResult(Unit)
        verify(sharedPreferences.edit()).remove(Constant.ACCESS_KEY)
        verify(sharedPreferences.edit()).remove(Constant.ACCESS_TOKEN)
    }

    @Test
    fun signUpShouldReturnUnit() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("TokenResponse.json")))

        val password = "test password"

        val callback: ApiCallback<Unit> = mock()
        api.signUp("", "", "", password, "", callback)

        val signUpRequest = server.takeRequest()
        assertEquals("/customers", signUpRequest.path)

        val tokenRequest = server.takeRequest()
        assertEquals("/integration/customer/token", tokenRequest.path)

        verify(callback).onResult(Unit)
        verify(sharedPreferences.edit()).putString(Constant.ACCESS_KEY, password)
        verify(sharedPreferences.edit()).putString(Constant.ACCESS_TOKEN, "$ACCESS_TOKEN_PREFIX cpylcc7caabqmta9u21ixsavl3f1i3hq")
    }

    @Test
    fun signUpShouldReturnError() {

        val response = MockResponse()
        response.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        response.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(response)

        val password = "test password"

        val callback: ApiCallback<Unit> = mock()
        api.signUp("", "", "", password, "", callback)

        val signUpRequest = server.takeRequest()
        assertEquals("/customers", signUpRequest.path)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun isLoggedInShouldReturnTrueIfSessionExist() {

        given(sharedPreferences.getString(anyOrNull(), anyOrNull())).willReturn("token")

        val callback: ApiCallback<Boolean> = mock()
        api.isSignedIn(callback)

        verify(callback).onResult(true)
    }

    @Test
    fun isLoggedInShouldReturnFalseIfSessionDoesNotExist() {

        given(sharedPreferences.getString(anyOrNull(), anyOrNull())).willReturn(null)

        val callback: ApiCallback<Boolean> = mock()
        api.isSignedIn(callback)

        verify(callback).onResult(false)
    }

    @Test
    fun getCustomerShouldReturnCustomerWhenSessionExist() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json")))

        val token = "test token"
        val password = "test password"
        mockSession(token, password)
        val callback: ApiCallback<Customer?> = mock()
        api.getCustomer(callback)

        val request = server.takeRequest()
        assertEquals("/customers/me", request.path)

        argumentCaptor<Customer>().apply {
            verify(callback).onResult(capture())

            assertEquals(9.toString(), firstValue.id)
        }
    }

    @Test
    fun getCustomerShouldReturnErrorWhenSessionExist() {

        val response = MockResponse()
        response.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        response.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(response)

        val token = "test token"
        val password = "test password"
        mockSession(token, password)
        val callback: ApiCallback<Customer?> = mock()
        api.getCustomer(callback)

        val request = server.takeRequest()
        assertEquals("/customers/me", request.path)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun getCustomerShouldReturnErrorWhenSessionDoesNotExist() {

        mockSession(null, null)
        val callback: ApiCallback<Customer?> = mock()
        api.getCustomer(callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun changePasswordShouldReturnUnitWhenSessionAndOldPasswordExist() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("TokenResponse.json")))

        val token = "test token"
        val oldPassword = "old password"
        val newPassword = "new password"

        mockSession(token, oldPassword)
        val callback: ApiCallback<Unit> = mock()
        api.updatePassword(newPassword, callback)

        val request = server.takeRequest()
        assertEquals("/customers/me/password", request.path)

        verify(callback).onResult(Unit)

        verify(sharedPreferences.edit()).putString(Constant.ACCESS_KEY, newPassword)
        verify(sharedPreferences.edit()).putString(Constant.ACCESS_TOKEN, token)
    }

    @Test
    fun changePasswordShouldReturnErrorWhenSessionAndOldPasswordDoesNotExist() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("TokenResponse.json")))

        val newPassword = "new password"
        mockSession(null, null)
        val callback: ApiCallback<Unit> = mock()
        api.updatePassword(newPassword, callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun changePasswordShouldReturnErrorWhenSessionAndOldPasswordExist() {

        val response = MockResponse()
        response.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        response.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(response)

        val token = "test token"
        val oldPassword = "old password"
        val newPassword = "new password"

        mockSession(token, oldPassword)
        val callback: ApiCallback<Unit> = mock()
        api.updatePassword(newPassword, callback)

        val request = server.takeRequest()
        assertEquals("/customers/me/password", request.path)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun forgotPasswordShouldReturnUnit() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("BooleanResponse.json")))

        val email = "test@mail.com"
        val callback: ApiCallback<Unit> = mock()
        api.resetPassword(email, callback)

        val request = server.takeRequest()
        assertEquals("/customers/password", request.path)

        verify(callback).onResult(Unit)
    }

    @Test
    fun forgotPasswordShouldReturnError() {

        val response = MockResponse()
        response.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        response.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(response)

        val email = "test@mail.com"
        val callback: ApiCallback<Unit> = mock()
        api.resetPassword(email, callback)

        val request = server.takeRequest()
        assertEquals("/customers/password", request.path)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun editCustomerInfoShouldReturnCustomerWhenSessionExist() {

        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json")))
        server.enqueue(MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json")))

        val firstName = "firstName"
        val secondName = "secondName"

        mockSession("", "")
        val callback: ApiCallback<Customer> = mock()
        api.updateCustomer(firstName, secondName, "", callback)

        val request = server.takeRequest()
        assertEquals("/customers/me", request.path)

        argumentCaptor<Customer>().apply {
            verify(callback).onResult(capture())

            assertEquals("9", firstValue.id)
        }
    }

    @Test
    fun editCustomerInfoShouldReturnErrorWhenSessionExist() {

        val response = MockResponse()
        response.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        response.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(response)

        val firstName = "firstName"
        val secondName = "secondName"

        mockSession("", "")
        val callback: ApiCallback<Customer> = mock()
        api.updateCustomer(firstName, secondName, "", callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun editCustomerInfoShouldReturnErrorWhenSessionDoesNotExist() {

        val firstName = "firstName"
        val secondName = "secondName"

        val callback: ApiCallback<Customer> = mock()
        mockSession(null, null)
        api.updateCustomer(firstName, secondName, "", callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }
}