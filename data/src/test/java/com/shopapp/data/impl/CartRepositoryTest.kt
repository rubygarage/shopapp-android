package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.database.Dao
import com.shopapp.domain.repository.CartRepository
import com.shopapp.gateway.entity.CartProduct
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CartRepositoryTest {
    
    @Mock
    private lateinit var dao: Dao

    private lateinit var repository: CartRepository

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = CartRepositoryImpl(dao)
    }

    @Test
    fun deleteCartProductsShouldDelegateCallToDao() {
        repository.deleteAllProductsFromCart()
        verify(dao).deleteAllProductsFromCart()
    }

    @Test
    fun deleteProductShouldDelegateCallToDao() {
        repository.deleteProductFromCart("id")
        verify(dao).deleteProductFromCart("id")
    }


    @Test
    fun getCartProductsShouldDelegateCallToDao() {
        repository.getCartProductList()
        verify(dao).getCartDataList()
    }

    @Test
    fun addProductShouldDelegateCallToDao() {
        val product: CartProduct = mock()
        repository.addCartProduct(product)
        verify(dao).addCartProduct(product)
    }

    @Test
    fun changeQuantityShouldDelegateCallToDao() {
        repository.changeCartProductQuantity("id", 3)
        verify(dao).changeCartProductQuantity("id", 3)
    }
}