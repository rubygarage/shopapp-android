package com.shopapp.domain.validator

import com.shopapp.gateway.entity.Card
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CardValidatorTest {

    @Mock
    private lateinit var card: Card
    private val cardValidator = CardValidator()

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        Mockito.doReturn("FIRST").`when`(card).firstName
        Mockito.doReturn("LAST").`when`(card).lastName
        Mockito.doReturn("4111111111111111").`when`(card).cardNumber
        Mockito.doReturn("391").`when`(card).verificationCode
        Mockito.doReturn("08").`when`(card).expireMonth
        Mockito.doReturn("2020").`when`(card).expireYear
    }

    @Test
    fun splitExpireDateWithCorrectDateShouldReturnValue() {
        Assert.assertEquals(cardValidator.splitExpireDate("08/20"), Pair("08", "20"))
    }

    @Test
    fun splitExpireDateWithIncorrectDateShouldReturnShouldReturnNull() {
        Assert.assertNull(cardValidator.splitExpireDate("0820"))
    }

    @Test
    fun splitExpireDateWithEmptyStringShouldReturnShouldReturnNull() {
        Assert.assertNull(cardValidator.splitExpireDate(""))
    }

    @Test
    fun splitHolderNameWithCorrectNameShouldReturnValue() {
        Assert.assertEquals(cardValidator.splitHolderName("FIRST SECOND"), Pair("FIRST", "SECOND"))
    }

    @Test
    fun splitHolderNameWithIncorrectNameShouldReturnNull() {
        Assert.assertNull(cardValidator.splitHolderName("FIRST"))
    }

    @Test
    fun splitHolderNameWithEmptyNameShouldReturnNull() {
        Assert.assertNull(cardValidator.splitHolderName(""))
    }

    @Test
    fun isCardValidWithCorrectDataShouldReturnValid() {
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.VALID)
    }

    @Test
    fun isCardValidWithEmptyFirstNameShouldReturnInvalidName() {
        Mockito.doReturn("").`when`(card).firstName
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NAME)
    }

    @Test
    fun isCardValidWithEmptyLastNameShouldReturnInvalidName() {
        Mockito.doReturn("").`when`(card).lastName
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NAME)
    }

    @Test
    fun isCardValidWithEmptyYearShouldInvalidDate() {
        Mockito.doReturn("").`when`(card).expireYear
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithIncorrectExpiredYearShouldInvalidDate() {
        Mockito.doReturn("15").`when`(card).expireYear
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithEmptyMonthShouldInvalidDate() {
        Mockito.doReturn("").`when`(card).expireMonth
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithIncorrectMonthShouldInvalidDate() {
        Mockito.doReturn("13").`when`(card).expireMonth
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithEmptyCvvShouldCvvReturnInvalidCvv() {
        Mockito.doReturn("").`when`(card).verificationCode
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun isCardValidWithShortCvvShouldCvvReturnInvalidCvv() {
        Mockito.doReturn("22").`when`(card).verificationCode
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun isCardValidWithLongCvvShouldCvvReturnInvalidCvv() {
        Mockito.doReturn("55555").`when`(card).verificationCode
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun isCardValidWithEmptyNumberShouldReturnInvalidNumber() {
        Mockito.doReturn("").`when`(card).cardNumber
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

    @Test
    fun isCardValidWithShortNumberShouldReturnInvalidNumber() {
        Mockito.doReturn("4111").`when`(card).cardNumber
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

    @Test
    fun isCardValidWithIncorrectNumberShouldReturnInvalidNumber() {
        Mockito.doReturn("1111111111111111").`when`(card).cardNumber
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

}