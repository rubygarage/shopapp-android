package com.shopapp.domain.validator

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.gateway.entity.Address
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FieldValidatorTest {

    private val fieldValidator = FieldValidator()

    @Test
    fun isEmailValidWithCorrectEmailShouldReturnTrue() {
        assertTrue(fieldValidator.isEmailValid("name@email.com"))
    }

    @Test
    fun isEmailValidWithEmptyEmailShouldReturnFalse() {
        assertFalse(fieldValidator.isEmailValid(""))
    }

    @Test
    fun isEmailValidWithEmptyEmailNameShouldReturnFalse() {
        assertFalse(fieldValidator.isEmailValid("@email.com"))
    }

    @Test
    fun isEmailValidWithEmptyEmailSecondLevelDomainShouldReturnFalse() {
        assertFalse(fieldValidator.isEmailValid("name@.com"))
    }

    @Test
    fun isEmailValidWithEmptyEmailFirstLevelDomainShouldReturnFalse() {
        assertFalse(fieldValidator.isEmailValid("name@email"))
    }

    @Test
    fun isPasswordValidWithCorrectPasswordShouldReturnTrue() {
        assertTrue(fieldValidator.isPasswordValid("password"))
    }

    @Test
    fun isPasswordValidWithEmptyPasswordShouldReturnFalse() {
        assertFalse(fieldValidator.isPasswordValid(""))
    }

    @Test
    fun isPasswordValidWithShortPasswordShouldReturnFalse() {
        assertFalse(fieldValidator.isPasswordValid("pass"))
    }

    @Test
    fun isAddressValidWithCorrectDataShouldReturnTrue() {
        val address = prepareAddress()
        assertTrue(fieldValidator.isAddressValid(address))
    }

    @Test
    fun isAddressValidWithEmptyAddressShouldReturnFalse() {
        val address = prepareAddress()
        given(address.address).willReturn("")
        assertFalse(fieldValidator.isAddressValid(address))
    }

    @Test
    fun isAddressValidWithEmptyCityShouldReturnFalse() {
        val address = prepareAddress()
        given(address.city).willReturn("")
        assertFalse(fieldValidator.isAddressValid(address))
    }

    @Test
    fun isAddressValidWithEmptyCountryShouldReturnFalse() {
        val address = prepareAddress()
        given(address.country).willReturn("")
        assertFalse(fieldValidator.isAddressValid(address))
    }

    @Test
    fun isAddressValidWithEmptyFirstNameShouldReturnFalse() {
        val address = prepareAddress()
        given(address.firstName).willReturn("")
        assertFalse(fieldValidator.isAddressValid(address))
    }

    @Test
    fun isAddressValidWithEmptyLastNameShouldReturnFalse() {
        val address = prepareAddress()
        given(address.lastName).willReturn("")
        assertFalse(fieldValidator.isAddressValid(address))
    }

    @Test
    fun isAddressValidWithEmptyZipShouldReturnFalse() {
        val address = prepareAddress()
        given(address.zip).willReturn("")
        assertFalse(fieldValidator.isAddressValid(address))
    }

    @Test
    fun isAddressValidWithEmptyDataShouldReturnFalse() {
        val address = prepareAddress()
        given(address.address).willReturn("")
        given(address.country).willReturn("")
        given(address.firstName).willReturn("")
        given(address.lastName).willReturn("")
        given(address.zip).willReturn("")
        assertFalse(fieldValidator.isAddressValid(address))
    }

    private fun prepareAddress(): Address = mock {
        on { address }.doReturn("address")
        on { secondAddress }.doReturn("secondAddress")
        on { city }.doReturn("city")
        on { country }.doReturn("country")
        on { state }.doReturn("state")
        on { firstName }.doReturn("firstName")
        on { lastName }.doReturn("lastName")
        on { zip }.doReturn("zip")
        on { phone }.doReturn("phone")
    }

}