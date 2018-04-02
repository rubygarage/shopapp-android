package com.shopapp.ui.base.contract

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Error
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BaseLcePresenterTest {

    @Mock
    private lateinit var view: BaseLceView<Any>
    private lateinit var presenter: BaseLcePresenterWrapper

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = BaseLcePresenterWrapper()
        presenter.attachView(view)
    }

    @Test
    fun shouldResolveContentError() {
        val error = Error.Content()
        val result = presenter.resolveError(error)
        assertTrue(result)
        argumentCaptor<Error>().apply {
            verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun shouldResolveContentNetworkError() {
        val error = Error.Content(true)
        val result = presenter.resolveError(error)
        assertTrue(result)
        argumentCaptor<Error>().apply {
            verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
            assertTrue((firstValue as Error.Content).isNetworkError)
        }
    }

    @Test
    fun shouldResolveNonCriticalError() {
        val errorMessage = "Critical error"
        val error = Error.NonCritical(errorMessage)
        val result = presenter.resolveError(error)
        assertTrue(result)
        verify(view).showMessage(errorMessage)
    }

    private class BaseLcePresenterWrapper : BaseLcePresenter<Any, BaseLceView<Any>>() {

        public override fun resolveError(error: Throwable): Boolean {
            return super.resolveError(error)
        }
    }
}