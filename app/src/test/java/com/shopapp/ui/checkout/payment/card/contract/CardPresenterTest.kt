package com.shopapp.ui.checkout.payment.card.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.domain.interactor.checkout.CheckCreditCardUseCase
import com.shopapp.domain.interactor.checkout.GetAcceptedCardTypesUseCase
import com.shopapp.domain.validator.CardValidator
import com.shopapp.gateway.entity.Card
import com.shopapp.gateway.entity.CardType
import com.shopapp.gateway.entity.Error
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CardPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: CardView

    @Mock
    private lateinit var checkCreditCardUseCase: CheckCreditCardUseCase

    @Mock
    private lateinit var getAcceptedCardTypesUseCase: GetAcceptedCardTypesUseCase

    @Mock
    private lateinit var cardValidator: CardValidator

    @Mock
    private lateinit var card: Card

    private lateinit var presenter: CardPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CardPresenter(checkCreditCardUseCase, getAcceptedCardTypesUseCase, cardValidator)
        presenter.attachView(view)
        checkCreditCardUseCase.mock()
        getAcceptedCardTypesUseCase.mock()
    }

    @Test
    fun shouldShowContentOnSingleSuccessWhenGetAcceptedCardTypes() {
        val cardTypeList: List<CardType> = mock()
        given(getAcceptedCardTypesUseCase.buildUseCaseSingle(any())).willReturn(Single.just(cardTypeList))
        presenter.getAcceptedCardTypes()

        val inOrder = inOrder(view, getAcceptedCardTypesUseCase)
        inOrder.verify(getAcceptedCardTypesUseCase).execute(any(), any(), any())
        inOrder.verify(view).showContent(cardTypeList)
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalErrorWhenGetAcceptedCardTypes() {
        given(getAcceptedCardTypesUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getAcceptedCardTypes()

        val inOrder = inOrder(view, getAcceptedCardTypesUseCase)
        inOrder.verify(getAcceptedCardTypesUseCase).execute(any(), any(), any())
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentErrorWhenGetAcceptedCardTypes() {
        given(getAcceptedCardTypesUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getAcceptedCardTypes()

        val inOrder = inOrder(view, getAcceptedCardTypesUseCase)
        inOrder.verify(getAcceptedCardTypesUseCase).execute(any(), any(), any())
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun shouldReceiveCardTokenOnSingleSuccessWhenGetToken() {
        val token = "token"
        given(checkCreditCardUseCase.buildUseCaseSingle(any())).willReturn(Single.just(token))
        presenter.getToken(card)

        verify(checkCreditCardUseCase).execute(any(), any(), any())
        argumentCaptor<Pair<Card, String>>().apply {
            verify(view).cardTokenReceived(capture())
            assertEquals(card, firstValue.first)
            assertEquals(token, firstValue.second)
        }
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalErrorWhenGetToken() {
        given(checkCreditCardUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getToken(card)

        val inOrder = inOrder(view, checkCreditCardUseCase)
        inOrder.verify(checkCreditCardUseCase).execute(any(), any(), any())
        inOrder.verify(view, never()).cardTokenReceived(any())
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentErrorWhenGetToken() {
        given(checkCreditCardUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getToken(card)

        val inOrder = inOrder(view, checkCreditCardUseCase)
        inOrder.verify(checkCreditCardUseCase).execute(any(), any(), any())
        inOrder.verify(view, never()).cardTokenReceived(any())
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun shouldCardPassValidation() {
        val testPair = Pair("", "")
        given(cardValidator.isCardValid(any())).willReturn(CardValidator.CardValidationResult.VALID)
        given(cardValidator.splitHolderName(any())).willReturn(testPair)
        presenter.processCardData("", "", "", "", "")

        val inOrder = inOrder(view, cardValidator)
        inOrder.verify(cardValidator).splitHolderName(any())
        inOrder.verify(cardValidator).isCardValid(any())
        inOrder.verify(view).cardPassValidation(any())
    }

    @Test
    fun shouldShowHolderNameCardError() {
        given(cardValidator.splitHolderName(any())).willReturn(null)
        presenter.processCardData("", "", "", "", "")

        val inOrder = inOrder(view, cardValidator)
        inOrder.verify(cardValidator).splitHolderName(any())
        inOrder.verify(cardValidator, never()).isCardValid(any())
        inOrder.verify(view).cardValidationError(R.string.card_name_error)
    }

    @Test
    fun shouldShowInvalidNameCardError() {
        val testPair = Pair("", "")
        given(cardValidator.isCardValid(any())).willReturn(CardValidator.CardValidationResult.INVALID_NAME)
        given(cardValidator.splitHolderName(any())).willReturn(testPair)
        presenter.processCardData("", "", "", "", "")

        val inOrder = inOrder(view, cardValidator)
        inOrder.verify(cardValidator).splitHolderName(any())
        inOrder.verify(cardValidator).isCardValid(any())
        inOrder.verify(view).cardValidationError(R.string.card_name_error)
    }

    @Test
    fun shouldShowInvalidDateCardError() {
        val testPair = Pair("", "")
        given(cardValidator.isCardValid(any())).willReturn(CardValidator.CardValidationResult.INVALID_DATE)
        given(cardValidator.splitHolderName(any())).willReturn(testPair)
        presenter.processCardData("", "", "", "", "")

        val inOrder = inOrder(view, cardValidator)
        inOrder.verify(cardValidator).splitHolderName(any())
        inOrder.verify(cardValidator).isCardValid(any())
        inOrder.verify(view).cardValidationError(R.string.card_date_error)
    }

    @Test
    fun shouldShowInvalidCvvCardError() {
        val testPair = Pair("", "")
        given(cardValidator.isCardValid(any())).willReturn(CardValidator.CardValidationResult.INVALID_CVV)
        given(cardValidator.splitHolderName(any())).willReturn(testPair)
        presenter.processCardData("", "", "", "", "")

        val inOrder = inOrder(view, cardValidator)
        inOrder.verify(cardValidator).splitHolderName(any())
        inOrder.verify(cardValidator).isCardValid(any())
        inOrder.verify(view).cardValidationError(R.string.card_cvv_error)
    }

    @Test
    fun shouldShowInvalidNumberCardError() {
        val testPair = Pair("", "")
        given(cardValidator.isCardValid(any())).willReturn(CardValidator.CardValidationResult.INVALID_NUMBER)
        given(cardValidator.splitHolderName(any())).willReturn(testPair)
        presenter.processCardData("", "", "", "", "")

        val inOrder = inOrder(view, cardValidator)
        inOrder.verify(cardValidator).splitHolderName(any())
        inOrder.verify(cardValidator).isCardValid(any())
        inOrder.verify(view).cardValidationError(R.string.card_number_error)
    }
}