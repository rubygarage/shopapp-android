package com.domain.validator

import com.domain.entity.Card
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
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
        Mockito.doReturn("20").`when`(card).expireYear
    }

    @Test
    fun cardValidator_CorrectDate_ReturnsValue() {
        Assert.assertEquals(cardValidator.splitExpireDate("08/20"), Pair("08", "20"))
    }

    @Test
    fun cardValidator_IncorrectDate_case1_ReturnsNull() {
        Assert.assertNull(cardValidator.splitExpireDate("0820"))
    }

    @Test
    fun cardValidator_IncorrectDate_case2_ReturnsNull() {
        Assert.assertNull(cardValidator.splitExpireDate(""))
    }

    @Test
    fun cardValidator_CorrectName_ReturnsValue() {
        Assert.assertEquals(cardValidator.splitHolderName("FIRST SECOND"), Pair("FIRST", "SECOND"))
    }

    @Test
    fun cardValidator_IncorrectName_case1_ReturnsNull() {
        Assert.assertNull(cardValidator.splitHolderName("FIRST"))
    }

    @Test
    fun cardValidator_IncorrectName_case2_ReturnsNull() {
        Assert.assertNull(cardValidator.splitHolderName(""))
    }

    @Test
    fun cardValidator_ValidCard_ReturnsValid() {
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.VALID)
    }

    @Test
    fun cardValidator_InvalidCard_case1_ReturnsInvalidName() {
        Mockito.doReturn("").`when`(card).firstName
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NAME)
    }

    @Test
    fun cardValidator_InvalidCard_case2_ReturnsInvalidName() {
        Mockito.doReturn("").`when`(card).lastName
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NAME)
    }

    @Test
    fun cardValidator_InvalidMonth_case1_ReturnsInvalidDate() {
        Mockito.doReturn("").`when`(card).expireYear
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun cardValidator_InvalidMonth_case2_ReturnsInvalidDate() {
        Mockito.doReturn("15").`when`(card).expireYear
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun cardValidator_InvalidMonth_case3_ReturnsInvalidDate() {
        Mockito.doReturn("12").`when`(card).expireYear
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun cardValidator_InvalidYear_case1_ReturnsInvalidDate() {
        Mockito.doReturn("").`when`(card).expireYear
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun cardValidator_InvalidYear_case2_ReturnsInvalidDate() {
        Mockito.doReturn("16").`when`(card).expireYear
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_DATE)
    }

    @Test
    fun cardValidator_InvalidCvv_case1_ReturnsInvalidCvv() {
        Mockito.doReturn("").`when`(card).verificationCode
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun cardValidator_InvalidCvv_case2_ReturnsInvalidCvv() {
        Mockito.doReturn("22").`when`(card).verificationCode
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun cardValidator_InvalidCvv_case3_ReturnsInvalidCvv() {
        Mockito.doReturn("55555").`when`(card).verificationCode
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun cardValidator_InvalidCvv_case4_ReturnsInvalidCvv() {
        Mockito.doReturn("22").`when`(card).verificationCode
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_CVV)
    }

    @Test
    fun cardValidator_InvalidNumber_case1_ReturnsInvalidNumber() {
        Mockito.doReturn("").`when`(card).cardNumber
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

    @Test
    fun cardValidator_InvalidNumber_case2_ReturnsInvalidNumber() {
        Mockito.doReturn("4111").`when`(card).cardNumber
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

    @Test
    fun cardValidator_InvalidNumber_case3_ReturnsInvalidNumber() {
        Mockito.doReturn("1111111111111111").`when`(card).cardNumber
        Assert.assertEquals(cardValidator.isCardValid(card), CardValidator.CardValidationResult.INVALID_NUMBER)
    }

}