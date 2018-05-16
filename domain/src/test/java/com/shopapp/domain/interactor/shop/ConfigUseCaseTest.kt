package com.shopapp.domain.interactor.shop

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.ShopRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConfigUseCaseTest {

    private lateinit var useCase: ConfigUseCase

    @Mock
    private lateinit var shopRepository: ShopRepository

    @Before
    fun setUpTest() {
        useCase = ConfigUseCase(shopRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseSingle(Unit)
        verify(shopRepository).getConfig()
    }
}