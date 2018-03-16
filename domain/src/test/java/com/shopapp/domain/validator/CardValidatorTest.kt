package com.shopapp.domain.validator

import com.shopapp.gateway.entity.Card
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

class CardValidatorTest {

    @Mock
    private lateinit var card: Card
    private val cardValidator = CardValidator()

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        given(card.firstName).willReturn("FIRST")
        given(card.lastName).willReturn("LAST")
        given(card.cardNumber).willReturn("4111111111111111")
        given(card.verificationCode).willReturn("391")
        given(card.expireMonth).willReturn("08")
        given(card.expireYear).willReturn("2020")
    }

    @Test
    fun splitExpireDateWithCorrectDateShouldReturnValue() {
        assertEquals(cardValidator.splitExpireDate("08/20"), Pair("08", "20"))
    }

    @Test
    fun splitExpireDateWithIncorrectDateShouldReturnShouldReturnNull() {
        assertNull(cardValidator.splitExpireDate("0820"))
    }

    @Test
    fun splitExpireDateWithEmptyStringShouldReturnShouldReturnNull() {
        assertNull(cardValidator.splitExpireDate(""))
    }

    @Test
    fun splitHolderNameWithCorrectNameShouldReturnValue() {
        assertEquals(cardValidator.splitHolderName("FIRST SECOND"), Pair("FIRST", "SECOND"))
    }

    @Test
    fun splitHolderNameWithIncorrectNameShouldReturnNull() {
        assertNull(cardValidator.splitHolderName("FIRST"))
    }

    @Test
    fun splitHolderNameWithEmptyNameShouldReturnNull() {
        assertNull(cardValidator.splitHolderName(""))
    }

    @Test
    fun isCardValidWithVisaAndCorrectDataShouldReturnValid() {
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.VALID)
    }

    @Test
    fun isCardValidWithAmericanExpressAndCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("378282246310005")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.VALID)
    }

    @Test
    fun isCardValidWithDinersClubAndCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("30569309025904")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.VALID)
    }

    @Test
    fun isCardValidWithDiscoverAndCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("6011111111111117")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.VALID)
    }

    @Test
    fun isCardValidWithJcbCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("3530111333300000")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.VALID)
    }

    @Test
    fun isCardValidWithMasterCardCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("5555555555554444")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.VALID)
    }

    @Test
    fun isCardValidWithEmptyFirstNameShouldReturnInvalidName() {
        given(card.firstName).willReturn("")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NAME)
    }

    @Test
    fun isCardValidWithEmptyLastNameShouldReturnInvalidName() {
        given(card.lastName).willReturn("")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NAME)
    }

    @Test
    fun isCardValidWithEmptyYearShouldInvalidDate() {
        given(card.expireYear).willReturn("")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithIncorrectExpiredYearShouldInvalidDate() {
        given(card.expireYear).willReturn("15")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithEmptyMonthShouldInvalidDate() {
        given(card.expireMonth).willReturn("")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithIncorrectMonthShouldInvalidDate() {
        given(card.expireMonth).willReturn("13")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithExpiredMonthAndSameYearShouldInvalidDate() {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        given(card.expireMonth).willReturn(currentMonth.toString())
        given(card.expireYear).willReturn(currentYear.toString())
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun isCardValidWithEmptyCvvShouldCvvReturnInvalidCvv() {
        given(card.verificationCode).willReturn("")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun isCardValidWithShortCvvShouldCvvReturnInvalidCvv() {
        given(card.verificationCode).willReturn("22")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun isCardValidWithLongCvvShouldCvvReturnInvalidCvv() {
        given(card.verificationCode).willReturn("55555")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun isCardValidWithEmptyNumberShouldReturnInvalidNumber() {
        given(card.cardNumber).willReturn("")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

    @Test
    fun isCardValidWithShortNumberShouldReturnInvalidNumber() {
        given(card.cardNumber).willReturn("4111")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

    @Test
    fun isCardValidWithIncorrectNumberShouldReturnInvalidNumber() {
        given(card.cardNumber).willReturn("1111111111111111")
        assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

}