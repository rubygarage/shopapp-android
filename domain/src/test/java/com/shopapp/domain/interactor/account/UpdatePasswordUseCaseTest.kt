package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CustomerRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdatePasswordUseCaseTest {

    private lateinit var useCase: UpdatePasswordUseCase

    @Mock
    private lateinit var repository: CustomerRepository

    @Before
    fun setUpTest() {
        useCase = UpdatePasswordUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val newPassword = "123456789"
        useCase.buildUseCaseCompletable(newPassword)
        verify(repository).updatePassword(newPassword)
    }

}