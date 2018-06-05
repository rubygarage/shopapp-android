package com.shopapp.magento.api

import com.nhaarman.mockito_kotlin.*
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
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

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("TokenResponse.json"))
        )

        val email = "test@mail.ru"
        val password = "password"
        val callback: ApiCallback<Unit> = mock()
        api.signIn(email, password, callback)

        val request = server.takeRequest()
        assertEquals("/integration/customer/token", request.path)

        verify(callback).onResult(Unit)

        verify(sharedPreferences.edit()).putString(Constant.ACCESS_KEY, password)
        verify(sharedPreferences.edit()).putString(
            Constant.ACCESS_TOKEN,
            "$ACCESS_TOKEN_PREFIX cpylcc7caabqmta9u21ixsavl3f1i3hq"
        )
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

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("TokenResponse.json"))
        )

        val password = "test password"

        val callback: ApiCallback<Unit> = mock()
        api.signUp("", "", "", password, "", callback)

        val signUpRequest = server.takeRequest()
        assertEquals("/customers", signUpRequest.path)

        val tokenRequest = server.takeRequest()
        assertEquals("/integration/customer/token", tokenRequest.path)

        verify(callback).onResult(Unit)
        verify(sharedPreferences.edit()).putString(Constant.ACCESS_KEY, password)
        verify(sharedPreferences.edit()).putString(
            Constant.ACCESS_TOKEN,
            "$ACCESS_TOKEN_PREFIX cpylcc7caabqmta9u21ixsavl3f1i3hq"
        )
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

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )

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

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("TokenResponse.json"))
        )

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
    fun changePasswordShouldReturnErrorWhenOldPasswordDoesNotExist() {

        val token = "test token"
        val newPassword = "new password"

        mockSession(token, null)
        val callback: ApiCallback<Unit> = mock()
        api.changePassword(newPassword, callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun changePasswordShouldReturnErrorWhenSessionAndOldPasswordDoesNotExist() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("TokenResponse.json"))
        )

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

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("BooleanResponse.json"))
        )

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

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )

        val firstName = "firstName"
        val secondName = "secondName"

        mockSession("", "")
        val callback: ApiCallback<Customer> = mock()
        api.updateCustomer(firstName, secondName, "", callback)

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)

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

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)

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

    @Test
    fun createCustomerAddressShouldReturnUnit() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )

        mockSession("", "")
        val address: Address = mock {
            val countryMock: Country = mock {
                on { id } doReturn "id"
            }
            on { country } doReturn countryMock
        }
        val callback: ApiCallback<Unit> = mock()
        api.createCustomerAddress(address, callback)
        verify(callback).onResult(Unit)

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun createCustomerAddressShouldReturnError() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        val errorResponse = MockResponse()
        errorResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        errorResponse.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(errorResponse)

        mockSession("", "")
        val address: Address = mock {
            val countryMock: Country = mock {
                on { id } doReturn "id"
            }
            on { country } doReturn countryMock
        }
        val callback: ApiCallback<Unit> = mock()
        api.createCustomerAddress(address, callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun createCustomerAddressShouldReturnErrorWhenSessionDoesNotExist() {

        mockSession(null, null)
        val address: Address = mock()
        val callback: ApiCallback<Unit> = mock()
        api.createCustomerAddress(address, callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun deleteCustomerAddressShouldReturnUnit() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )

        mockSession("", "")
        val callback: ApiCallback<Unit> = mock()
        api.deleteCustomerAddress("id", callback)
        verify(callback).onResult(Unit)

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun deleteCustomerAddressShouldReturnError() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        val errorResponse = MockResponse()
        errorResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        errorResponse.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(errorResponse)

        mockSession("", "")
        val callback: ApiCallback<Unit> = mock()
        api.deleteCustomerAddress("id", callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun deleteCustomerAddressShouldReturnErrorWhenSessionDoesNotExist() {

        mockSession(null, null)
        val callback: ApiCallback<Unit> = mock()
        api.deleteCustomerAddress("id", callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun editCustomerAddressShouldReturnUnit() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )

        mockSession("", "")
        val address: Address = mock {
            val countryMock: Country = mock {
                on { id } doReturn "id"
            }
            on { country } doReturn countryMock
        }
        val callback: ApiCallback<Unit> = mock()
        api.editCustomerAddress(address, callback)
        verify(callback).onResult(Unit)

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun editCustomerAddressShouldReturnError() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        val errorResponse = MockResponse()
        errorResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        errorResponse.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(errorResponse)

        mockSession("", "")
        val address: Address = mock {
            val countryMock: Country = mock {
                on { id } doReturn "id"
            }
            on { country } doReturn countryMock
        }
        val callback: ApiCallback<Unit> = mock()
        api.editCustomerAddress(address, callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun editCustomerAddressShouldReturnErrorAndClearSession() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        val errorResponse = MockResponse()
        errorResponse.setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        errorResponse.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(errorResponse)

        mockSession("", "")
        val address: Address = mock {
            val countryMock: Country = mock {
                on { id } doReturn "id"
            }
            on { country } doReturn countryMock
        }
        val callback: ApiCallback<Unit> = mock()
        api.editCustomerAddress(address, callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)

        verify(sharedPreferences.edit()).remove(Constant.ACCESS_KEY)
        verify(sharedPreferences.edit()).remove(Constant.ACCESS_TOKEN)
    }

    @Test
    fun editCustomerAddressShouldReturnErrorWhenSessionDoesNotExist() {

        mockSession(null, null)
        val address: Address = mock()
        val callback: ApiCallback<Unit> = mock()
        api.editCustomerAddress(address, callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun setDefaultShippingAddressShouldReturnUnit() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )

        mockSession("", "")
        val callback: ApiCallback<Unit> = mock()
        api.setDefaultShippingAddress("id", callback)
        verify(callback).onResult(Unit)

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun setDefaultShippingAddressShouldReturnError() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CustomerResponse.json"))
        )
        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )
        val errorResponse = MockResponse()
        errorResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        errorResponse.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(errorResponse)

        mockSession("", "")
        val callback: ApiCallback<Unit> = mock()
        api.setDefaultShippingAddress("id", callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }

        val customerRequest = server.takeRequest()
        assertEquals("/customers/me", customerRequest.path)
        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun setDefaultShippingAddressShouldReturnErrorWhenSessionDoesNotExist() {

        mockSession(null, null)
        val callback: ApiCallback<Unit> = mock()
        api.setDefaultShippingAddress("id", callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }

    @Test
    fun getCountriesShouldReturnUnit() {

        server.enqueue(
            MockResponse().setBody(jsonHelper.getJsonContents("CountryListResponse.json"))
        )

        mockSession("", "")
        val callback: ApiCallback<List<Country>> = mock()
        api.getCountries(callback)
        argumentCaptor<List<Country>>().apply {
            verify(callback).onResult(capture())

            assertTrue(firstValue.isNotEmpty())
        }

        val countryRequest = server.takeRequest()
        assertEquals("/directory/countries", countryRequest.path)
    }

    @Test
    fun getCountriesShouldReturnError() {

        val errorResponse = MockResponse()
        errorResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        errorResponse.setBody(jsonHelper.getJsonContents("ErrorResponse.json"))
        server.enqueue(errorResponse)

        mockSession("", "")
        val callback: ApiCallback<List<Country>> = mock()
        api.getCountries(callback)

        argumentCaptor<Error>().apply {
            verify(callback, never()).onResult(any())
            verify(callback).onFailure(capture())

            assertTrue(firstValue is Error.NonCritical)
        }
    }
}