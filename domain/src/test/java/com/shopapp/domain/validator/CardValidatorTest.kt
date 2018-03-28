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
    fun splitExpireDateWithIncorrectDateShouldReturnNull() {
        assertNull(cardValidator.splitExpireDate("0820"))
    }

    @Test
    fun splitExpireDateWithEmptyStringShouldReturnNull() {
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
        assertEquals(CardValidator.CardValidationResult.VALID, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithAmericanExpressAndCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("378282246310005")
        assertEquals(CardValidator.CardValidationResult.VALID, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithDinersClubAndCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("30569309025904")
        assertEquals(CardValidator.CardValidationResult.VALID, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithDiscoverAndCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("6011111111111117")
        assertEquals(CardValidator.CardValidationResult.VALID, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithJcbCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("3530111333300000")
        assertEquals(CardValidator.CardValidationResult.VALID, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithMasterCardCorrectDataShouldReturnValid() {
        given(card.cardNumber).willReturn("5555555555554444")
        assertEquals(CardValidator.CardValidationResult.VALID, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithEmptyFirstNameShouldReturnInvalidName() {
        given(card.firstName).willReturn("")
        assertEquals(CardValidator.CardValidationResult.INVALID_NAME, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithEmptyLastNameShouldReturnInvalidName() {
        given(card.lastName).willReturn("")
        assertEquals(CardValidator.CardValidationResult.INVALID_NAME, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithEmptyYearShouldReturnInvalidDate() {
        given(card.expireYear).willReturn("")
        assertEquals(CardValidator.CardValidationResult.INVALID_DATE, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithIncorrectExpiredYearShouldReturnInvalidDate() {
        given(card.expireYear).willReturn("15")
        assertEquals(CardValidator.CardValidationResult.INVALID_DATE, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithEmptyMonthShouldReturnInvalidDate() {
        given(card.expireMonth).willReturn("")
        assertEquals(CardValidator.CardValidationResult.INVALID_DATE, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithIncorrectMonthShouldReturnInvalidDate() {
        given(card.expireMonth).willReturn("13")
        assertEquals(CardValidator.CardValidationResult.INVALID_DATE, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithExpiredMonthAndSameYearShouldReturnInvalidDate() {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        given(card.expireMonth).willReturn(currentMonth.toString())
        given(card.expireYear).willReturn(currentYear.toString())
        assertEquals(CardValidator.CardValidationResult.INVALID_DATE, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithValidMonthAndSameYearShouldReturnValid() {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        given(card.expireMonth).willReturn(currentMonth.toString())
        given(card.expireYear).willReturn(currentYear.toString())
        assertEquals(CardValidator.CardValidationResult.VALID, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithEmptyCvvShouldReturnInvalidCvv() {
        given(card.verificationCode).willReturn("")
        assertEquals(CardValidator.CardValidationResult.INVALID_CVV, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithShortCvvShouldReturnInvalidCvv() {
        given(card.verificationCode).willReturn("22")
        assertEquals(CardValidator.CardValidationResult.INVALID_CVV, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithLongCvvShouldReturnInvalidCvv() {
        given(card.verificationCode).willReturn("55555")
        assertEquals(CardValidator.CardValidationResult.INVALID_CVV, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithEmptyNumberShouldReturnInvalidNumber() {
        given(card.cardNumber).willReturn("")
        assertEquals(CardValidator.CardValidationResult.INVALID_NUMBER, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithShortNumberShouldReturnInvalidNumber() {
        given(card.cardNumber).willReturn("4111")
        assertEquals(CardValidator.CardValidationResult.INVALID_NUMBER, cardValidator.isCardValid(card))
    }

    @Test
    fun isCardValidWithIncorrectNumberShouldReturnInvalidNumber() {
        given(card.cardNumber).willReturn("1111111111111111")
        assertEquals(CardValidator.CardValidationResult.INVALID_NUMBER, cardValidator.isCardValid(card))
    }

}