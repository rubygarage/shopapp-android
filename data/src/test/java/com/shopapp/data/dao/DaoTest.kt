package com.shopapp.data.dao

import com.nhaarman.mockito_kotlin.*
import com.shopapp.data.dao.entity.CartProductDataEntity
import com.shopapp.data.dao.impl.DaoImpl
import com.shopapp.data.util.ConstantHolder
import com.shopapp.data.util.MockInstantiator
import com.shopapp.data.util.RequeryMockInstantiator
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.Error
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import io.requery.Persistable
import io.requery.kotlin.Deletion
import io.requery.kotlin.Selection
import io.requery.kotlin.WhereAndOr
import io.requery.query.Condition
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.reactivex.ReactiveResult
import io.requery.reactivex.ReactiveScalar
import io.requery.util.CloseableIterator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DaoTest {

    companion object {
        private const val PRODUCT_VARIANT_ID = "product_variant_id"
    }

    @Mock
    private lateinit var store: KotlinReactiveEntityStore<Persistable>

    private lateinit var daoImpl: DaoImpl
    private lateinit var cartProductData: CartProductDataEntity
    private lateinit var cartProduct: CartProduct
    private val error = Error.Content()
    private val unitObserver = TestObserver<Unit>()
    private val cartProductListObserver = TestObserver<List<CartProduct>>()
    private val cartProductObserver = TestObserver<CartProduct>()

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        daoImpl = DaoImpl(store)
        cartProductData = mock {
            val mock = RequeryMockInstantiator.newProductVariant()

            on { id } doReturn ConstantHolder.DEFAULT_ID
            on { title } doReturn ConstantHolder.DEFAULT_TITLE
            on { productVariant } doReturn mock
            on { quantity } doReturn ConstantHolder.DEFAULT_QUANTITY
            on { currency } doReturn ConstantHolder.DEFAULT_CURRENCY
        }
        cartProduct = MockInstantiator.newCartProduct()
    }

    @Test
    fun getCartDataListShouldReturnValue() {
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        given(iterator.hasNext()).willReturn(true, false)
        given(iterator.next()).willReturn(cartProductData)
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val observableResult: Observable<ReactiveResult<CartProductDataEntity>> = Observable.just(selectionResult)
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.get()).willReturn(selectionResult)
        given(selectionResult.observableResult()).willReturn(observableResult)
        given(selectionResult.iterator()).willReturn(iterator)
        val results = mutableListOf<List<CartProduct>>()
        daoImpl.getCartDataList().subscribe({ results.add(it) })

        assertEquals(1, results.size)
        val result = results.first()
        val cartProduct = result.first()
        assertEquals(ConstantHolder.DEFAULT_TITLE, cartProduct.title)
        assertEquals(ConstantHolder.DEFAULT_QUANTITY, cartProduct.quantity)
        assertEquals(ConstantHolder.DEFAULT_CURRENCY, cartProduct.currency)
        assertNotNull(cartProduct.productVariant)
    }

    @Test
    fun getCartDataListShouldReturnError() {
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val observableResult: Observable<ReactiveResult<CartProductDataEntity>> = Observable.error(error)
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.get()).willReturn(selectionResult)
        given(selectionResult.observableResult()).willReturn(observableResult)
        daoImpl.getCartDataList().subscribe(cartProductListObserver)

        cartProductListObserver.assertError(error)
    }

    @Test
    fun addCartProductShouldReturnValueWhenProductDoesNotExist() {
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val where: WhereAndOr<ReactiveResult<CartProductDataEntity>> = mock()
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(selectionResult)
        given(selectionResult.iterator()).willReturn(iterator)
        given(store.upsert(any<CartProductDataEntity>())).willReturn(Single.just(cartProductData))
        var result: CartProduct? = null
        daoImpl.addCartProduct(cartProduct).subscribe(Consumer {
            result = it
        })

        argumentCaptor<CartProductDataEntity>().apply {
            verify(store).upsert(capture())
            assertEquals(cartProductData.quantity, firstValue.quantity)
        }
        assertNotNull(result)
        assertEquals(ConstantHolder.DEFAULT_TITLE, result?.title)
        assertEquals(ConstantHolder.DEFAULT_QUANTITY, result?.quantity)
        assertEquals(ConstantHolder.DEFAULT_CURRENCY, result?.currency)
        assertNotNull(result?.productVariant)
    }

    @Test
    fun addCartProductShouldReturnValueWhenProductExist() {
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val where: WhereAndOr<ReactiveResult<CartProductDataEntity>> = mock()
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(selectionResult)
        given(iterator.hasNext()).willReturn(true, false)
        given(iterator.next()).willReturn(cartProductData)
        given(selectionResult.iterator()).willReturn(iterator)
        given(store.upsert(any<CartProductDataEntity>())).willReturn(Single.just(cartProductData))
        var result: CartProduct? = null
        daoImpl.addCartProduct(cartProduct).subscribe(Consumer {
            result = it
        })

        argumentCaptor<CartProductDataEntity>().apply {
            verify(store).upsert(capture())
            assertEquals(cartProductData.quantity, firstValue.quantity)
        }
        assertNotNull(result)
        assertEquals(ConstantHolder.DEFAULT_TITLE, result?.title)
        assertEquals(ConstantHolder.DEFAULT_QUANTITY, result?.quantity)
        assertEquals(ConstantHolder.DEFAULT_CURRENCY, result?.currency)
        assertNotNull(result?.productVariant)
    }

    @Test
    fun addCartProductShouldReturnValueWhenProductExistWithMaxQuantity() {
        given(cartProductData.quantity).willReturn(DaoImpl.MAX_QUANTITY)
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val where: WhereAndOr<ReactiveResult<CartProductDataEntity>> = mock()
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(selectionResult)
        given(iterator.hasNext()).willReturn(true, false)
        given(iterator.next()).willReturn(cartProductData)
        given(selectionResult.iterator()).willReturn(iterator)
        given(store.upsert(any<CartProductDataEntity>())).willReturn(Single.just(cartProductData))
        var result: CartProduct? = null
        daoImpl.addCartProduct(cartProduct).subscribe(Consumer {
            result = it
        })

        argumentCaptor<CartProductDataEntity>().apply {
            verify(store).upsert(capture())
            assertNotEquals(cartProduct.quantity, firstValue.quantity)
            assertEquals(DaoImpl.MAX_QUANTITY, firstValue.quantity)
        }
        assertNotNull(result)
        assertEquals(ConstantHolder.DEFAULT_TITLE, result?.title)
        assertEquals(DaoImpl.MAX_QUANTITY, result?.quantity)
        assertEquals(ConstantHolder.DEFAULT_CURRENCY, result?.currency)
        assertNotNull(result?.productVariant)
    }

    @Test
    fun deleteProductFromCartShouldReturnCompleteWhenProductExist() {
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val where: WhereAndOr<ReactiveResult<CartProductDataEntity>> = mock()
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(selectionResult)
        given(iterator.hasNext()).willReturn(true, false)
        given(iterator.next()).willReturn(cartProductData)
        given(selectionResult.iterator()).willReturn(iterator)
        given(store.delete(cartProductData)).willReturn(Completable.complete())
        daoImpl.deleteProductFromCart(PRODUCT_VARIANT_ID).subscribe(unitObserver)

        verify(store).delete(cartProductData)
        unitObserver.assertComplete()
    }

    @Test
    fun deleteProductFromCartShouldReturnCompleteWhenProductDoesNotExist() {
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val where: WhereAndOr<ReactiveResult<CartProductDataEntity>> = mock()
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(selectionResult)
        given(selectionResult.iterator()).willReturn(iterator)
        daoImpl.deleteProductFromCart(PRODUCT_VARIANT_ID).subscribe(unitObserver)

        verify(store, never()).delete(any<CartProductDataEntity>())
        unitObserver.assertComplete()
    }

    @Test
    fun deleteProductFromCartShouldReturnError() {
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val where: WhereAndOr<ReactiveResult<CartProductDataEntity>> = mock()
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(selectionResult)
        given(iterator.hasNext()).willReturn(true, false)
        given(iterator.next()).willReturn(cartProductData)
        given(selectionResult.iterator()).willReturn(iterator)
        given(store.delete(cartProductData)).willReturn(Completable.error(error))
        daoImpl.deleteProductFromCart(PRODUCT_VARIANT_ID).subscribe(unitObserver)

        verify(store).delete(cartProductData)
        unitObserver.assertError(error)
    }

    @Test
    fun deleteProductListFromCartShouldReturnComplete() {
        val singleResult = Single.just(0)
        val scalar: ReactiveScalar<Int> = mock()
        val where: WhereAndOr<ReactiveScalar<Int>> = mock()
        val deletion: Deletion<ReactiveScalar<Int>> = mock()
        given(store.delete(CartProductDataEntity::class)).willReturn(deletion)
        given(deletion.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(scalar)
        given(scalar.single()).willReturn(singleResult)

        daoImpl.deleteProductListFromCart(listOf(PRODUCT_VARIANT_ID)).subscribe(unitObserver)

        verify(store).delete(CartProductDataEntity::class)
        unitObserver.assertComplete()
    }

    @Test
    fun deleteProductListFromCartShouldReturnError() {
        val singleResult = Single.error<Int>(error)
        val scalar: ReactiveScalar<Int> = mock()
        val where: WhereAndOr<ReactiveScalar<Int>> = mock()
        val deletion: Deletion<ReactiveScalar<Int>> = mock()
        given(store.delete(CartProductDataEntity::class)).willReturn(deletion)
        given(deletion.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(scalar)
        given(scalar.single()).willReturn(singleResult)

        daoImpl.deleteProductListFromCart(listOf(PRODUCT_VARIANT_ID)).subscribe(unitObserver)

        verify(store).delete(CartProductDataEntity::class)
        unitObserver.assertError(error)
    }

    @Test
    fun deleteAllProductsFromCartShouldReturnComplete() {
        val singleResult = Single.just(0)
        val scalar: ReactiveScalar<Int> = mock()
        val deletion: Deletion<ReactiveScalar<Int>> = mock()
        given(store.delete(CartProductDataEntity::class)).willReturn(deletion)
        given(deletion.get()).willReturn(scalar)
        given(scalar.single()).willReturn(singleResult)
        daoImpl.deleteAllProductsFromCart().subscribe(unitObserver)

        verify(store).delete(CartProductDataEntity::class)
        unitObserver.assertComplete()
    }

    @Test
    fun deleteAllProductsFromCartShouldReturnError() {
        val singleResult = Single.error<Int>(error)
        val scalar: ReactiveScalar<Int> = mock()
        val deletion: Deletion<ReactiveScalar<Int>> = mock()
        given(store.delete(CartProductDataEntity::class)).willReturn(deletion)
        given(deletion.get()).willReturn(scalar)
        given(scalar.single()).willReturn(singleResult)
        daoImpl.deleteAllProductsFromCart().subscribe(unitObserver)

        verify(store).delete(CartProductDataEntity::class)
        unitObserver.assertError(error)
    }

    @Test
    fun changeCartProductQuantityShouldReturnValueWhenProductExist() {
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val where: WhereAndOr<ReactiveResult<CartProductDataEntity>> = mock()
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(selectionResult)
        given(iterator.hasNext()).willReturn(true, false)
        given(iterator.next()).willReturn(cartProductData)
        given(selectionResult.iterator()).willReturn(iterator)
        given(store.update(any<CartProductDataEntity>())).willReturn(Single.just(cartProductData))
        val newQuantity = 100
        var result: CartProduct? = null
        daoImpl.changeCartProductQuantity(PRODUCT_VARIANT_ID, newQuantity).subscribe(Consumer { result = it })

        argumentCaptor<CartProductDataEntity>().apply {
            verify(store).update(capture())
            assertNotEquals(newQuantity, firstValue.quantity)
        }

        assertNotNull(result)
        assertEquals(cartProductData.title, result?.title)
        assertEquals(cartProductData.quantity, result?.quantity)
        assertEquals(cartProductData.currency, result?.currency)
        assertNotNull(cartProductData.productVariant.id, result?.productVariant?.id)
    }

    @Test
    fun changeCartProductQuantityShouldReturnErrorWhenProductDoesNotExist() {
        val iterator: CloseableIterator<CartProductDataEntity> = mock()
        val selectionResult: ReactiveResult<CartProductDataEntity> = mock()
        val where: WhereAndOr<ReactiveResult<CartProductDataEntity>> = mock()
        val selection: Selection<ReactiveResult<CartProductDataEntity>> = mock()
        given(store.select(CartProductDataEntity::class)).willReturn(selection)
        given(selection.where(any<Condition<String, String>>())).willReturn(where)
        given(where.get()).willReturn(selectionResult)
        given(selectionResult.iterator()).willReturn(iterator)
        val newQuantity = 100
        daoImpl.changeCartProductQuantity(PRODUCT_VARIANT_ID, newQuantity).subscribe(cartProductObserver)

        verify(store, never()).update(any<CartProductDataEntity>())
        cartProductObserver.assertErrorMessage(DaoImpl.PRODUCT_NOT_FOUND_ERROR)
    }
}