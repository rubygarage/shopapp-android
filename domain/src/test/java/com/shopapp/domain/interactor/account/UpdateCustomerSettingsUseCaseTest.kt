package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CustomerRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateCustomerSettingsUseCaseTest {

    private lateinit var useCase: UpdateCustomerSettingsUseCase

    @Mock
    private lateinit var repository: CustomerRepository

    @Before
    fun setUpTest() {
        useCase = UpdateCustomerSettingsUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseCompletable(false)
        verify(repository).updateCustomerSettings(false)
    }
}