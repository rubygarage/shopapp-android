package com.domain.validator

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("FunctionName")
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class FieldValidatorTest {

    private val fieldValidator = FieldValidator()

    @Test
    fun fieldValidator_CorrectEmail_ReturnsTrue() {
        assertThat(fieldValidator.isEmailValid("name@email.com"), `is`(true))
    }

    @Test
    fun fieldValidator_EmptyEmail_ReturnsFalse() {
        assertThat(fieldValidator.isEmailValid(""), `is`(false))
    }

    @Test
    fun fieldValidator_IncorrectEmail_case1_ReturnsFalse() {
        assertThat(fieldValidator.isEmailValid("@email.com"), `is`(false))
    }

    @Test
    fun fieldValidator_IncorrectEmail_case2_ReturnsFalse() {
        assertThat(fieldValidator.isEmailValid("name@.com"), `is`(false))
    }

    @Test
    fun fieldValidator_IncorrectEmail_case3_ReturnsFalse() {
        assertThat(fieldValidator.isEmailValid("name@email"), `is`(false))
    }

    @Test
    fun fieldValidator_CorrectPassword_ReturnsTrue() {
        assertThat(fieldValidator.isPasswordValid("password"), `is`(true))
    }

    @Test
    fun fieldValidator_EmptyPassword_ReturnsFalse() {
        assertThat(fieldValidator.isPasswordValid(""), `is`(false))
    }

    @Test
    fun fieldValidator_IncorrectPassword_ReturnsFalse() {
        assertThat(fieldValidator.isPasswordValid("pass"), `is`(false))
    }
}