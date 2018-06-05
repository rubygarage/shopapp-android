package com.shopapp.magento.adapter

import com.shopapp.gateway.entity.Product
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.util.*

class ProductVariantAdapterTest {

    @Test
    fun shouldReturnProductVariant() {
        val product = Product(
            "id",
            "title",
            "description",
            "description",
            "USD",
            BigDecimal.ONE,
            false,
            null,
            "vendor",
            "type",
            Date(),
            Date(),
            listOf(),
            listOf(),
            listOf(),
            listOf()
        )

        val variant = ProductVariantAdapter.adapt(product)
        assertEquals(product.id, variant.id)
        assertEquals(product.title, variant.title)
        assertEquals(product.id, variant.productId)
    }
}