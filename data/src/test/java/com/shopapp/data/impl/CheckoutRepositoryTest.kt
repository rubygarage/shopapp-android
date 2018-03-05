package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Card
import com.shopapp.gateway.entity.CardType
import com.shopapp.gateway.entity.Error
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CheckoutRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var cardTypes: List<CardType>

    @Mock
    private lateinit var card: Card

    private lateinit var repository: CheckoutRepository
    private lateinit var cardTypesObserver: TestObserver<List<CardType>>
    private lateinit var cardVerificationObserver: TestObserver<String>

    @Before
    fun setUpTest() {
        repository = CheckoutRepositoryImpl(api)
        cardTypesObserver = TestObserver()
        cardVerificationObserver = TestObserver()
    }

    @Test
    fun getAcceptedCardTypesShouldDelegateCallToApi() {
        repository.getAcceptedCardTypes().subscribe()
        verify(api).getAcceptedCardTypes(any())
    }

    @Test
    fun getAcceptedCardTypesShouldReturnValueWhenOnResult() {
        given(api.getAcceptedCardTypes(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<CardType>>>(0)
            callback.onResult(cardTypes)
        })
        repository.getAcceptedCardTypes().subscribe(cardTypesObserver)
        cardTypesObserver.assertValue(cardTypes)
    }

    @Test
    fun getAcceptedCardTypesShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getAcceptedCardTypes(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<CardType>>>(0)
            callback.onFailure(error)
        })
        repository.getAcceptedCardTypes().subscribe(cardTypesObserver)
        cardTypesObserver.assertError(error)
    }

    @Test
    fun getCardTokenShouldDelegateCallToApi() {
        repository.getCardToken(card).subscribe()
        verify(api).getCardToken(eq(card), any())
    }

    @Test
    fun getCardTokenShouldReturnValueWhenOnResult() {
        val token = "token"
        given(api.getCardToken(eq(card), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<String>>(1)
            callback.onResult(token)
        })
        repository.getCardToken(card).subscribe(cardVerificationObserver)
        cardVerificationObserver.assertValue(token)
    }

    @Test
    fun getCardTokenShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getCardToken(eq(card), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<String>>(1)
            callback.onFailure(error)
        })
        repository.getCardToken(card).subscribe(cardVerificationObserver)
        cardVerificationObserver.assertError(error)
    }
}