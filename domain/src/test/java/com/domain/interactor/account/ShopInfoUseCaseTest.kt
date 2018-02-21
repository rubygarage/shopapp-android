package com.domain.interactor.account

import com.domain.repository.ShopRepository
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
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