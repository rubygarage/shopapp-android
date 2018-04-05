package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.dao.Dao
import com.shopapp.domain.repository.CartRepository
import com.shopapp.gateway.entity.CartProduct
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CartRepositoryTest {

    companion object {
        private const val ID = "id"
    }
    
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
        repository.deleteProductFromCart(ID)
        verify(dao).deleteProductFromCart(ID)
    }

    @Test
    fun deleteProductListFromCartShouldDelegateCallToDao() {
        repository.deleteProductListFromCart(listOf(ID))
        verify(dao).deleteProductListFromCart(listOf(ID))
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
        repository.changeCartProductQuantity(ID, 3)
        verify(dao).changeCartProductQuantity(ID, 3)
    }
}