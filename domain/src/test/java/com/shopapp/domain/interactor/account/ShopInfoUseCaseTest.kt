package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.ShopRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShopInfoUseCaseTest {

    private lateinit var useCase: ShopInfoUseCase

    @Mock
    private lateinit var shopRepository: ShopRepository

    @Before
    fun setUpTest() {
        useCase = ShopInfoUseCase(shopRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseSingle(Unit)
        verify(shopRepository).getShop()
    }

}