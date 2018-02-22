package com.shopapp.domain.validator

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class FieldValidatorTest {

    private val fieldValidator = FieldValidator()

    @Test
    fun isEmailValidWithCorrectEmailShouldReturnTrue() {
        assertThat(fieldValidator.isEmailValid("name@email.com"), `is`(true))
    }

    @Test
    fun isEmailValidWithEmptyEmailShouldReturnFalse() {
        assertThat(fieldValidator.isEmailValid(""), `is`(false))
    }

    @Test
    fun isEmailValidWithEmptyEmailNameShouldReturnFalse() {
        assertThat(fieldValidator.isEmailValid("@email.com"), `is`(false))
    }

    @Test
    fun isEmailValidWithEmptyEmailSecondLevelDomainShouldReturnFalse() {
        assertThat(fieldValidator.isEmailValid("name@.com"), `is`(false))
    }

    @Test
    fun isEmailValidWithEmptyEmailFirstLevelDomainShouldReturnFalse() {
        assertThat(fieldValidator.isEmailValid("name@email"), `is`(false))
    }

    @Test
    fun isPasswordValidWithCorrectPasswordShouldReturnTrue() {
        assertThat(fieldValidator.isPasswordValid("password"), `is`(true))
    }

    @Test
    fun isPasswordValidWithEmptyPasswordShouldReturnFalse() {
        assertThat(fieldValidator.isPasswordValid(""), `is`(false))
    }

    @Test
    fun isPasswordValidWithShortPasswordShouldReturnFalse() {
        assertThat(fieldValidator.isPasswordValid("pass"), `is`(false))
    }
}