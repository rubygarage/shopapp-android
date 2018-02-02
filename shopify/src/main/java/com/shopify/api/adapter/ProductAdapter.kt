package com.shopify.api.adapter

import com.domain.entity.Image
import com.domain.entity.Product
import com.domain.entity.ProductOption
import com.domain.entity.ProductVariant
import com.shopify.api.ext.isSingleOptions
import com.shopify.buy3.Storefront
import com.ui.const.Constant.DEFAULT_STRING

object ProductAdapter {

    private const val DEFAULT_PRICE = 0f

    fun adapt(shopAdaptee: Storefront.Shop,
              productAdaptee: Storefront.Product,
              paginationValue: String? = null,
              isConvertVariants: Boolean = true): Product {
        return if (isConvertVariants) {
            adapt(
                shopAdaptee,
                productAdaptee,
                paginationValue,
                productAdaptee.variants.edges.mapNotNull { ProductVariantAdapter.adapt(it.node) }
            )
        } else {
            adapt(
                shopAdaptee,
                productAdaptee,
                paginationValue,
                listOf()
            )
        }
    }

    private fun adapt(shopAdaptee: Storefront.Shop,
                      productAdaptee: Storefront.Product,
                      paginationValue: String? = null,
                      variants: List<ProductVariant>): Product {

        val productImages = convertImage(productAdaptee)

        if (productAdaptee.isSingleOptions()) {
            productAdaptee.variants.edges.forEach { it.node.title = DEFAULT_STRING }
        }

        val pricePair = convertPrice(productAdaptee)
        return Product(
            id = productAdaptee.id.toString(),
            title = productAdaptee.title,
            productDescription = productAdaptee.description,
            additionalDescription = productAdaptee.descriptionHtml,
            vendor = productAdaptee.vendor,
            currency = shopAdaptee.paymentSettings.currencyCode.toString(),
            type = productAdaptee.productType,
            tags = productAdaptee.tags,
            createdAt = productAdaptee.createdAt.toDate(),
            updatedAt = productAdaptee.updatedAt.toDate(),
            price = pricePair.first,
            hasAlternativePrice = pricePair.first != pricePair.second,
            images = productImages,
            options = convertProductOptionList(productAdaptee.options),
            variants = variants,
            paginationValue = paginationValue
        )
    }

    private fun convertPrice(productAdaptee: Storefront.Product): Pair<Float, Float> {
        val variantsList = productAdaptee.variants.edges
        return if (variantsList.size > 0) {
            val mappedList = variantsList.mapNotNull { it.node.price.toFloat() }
            Pair(mappedList.min() ?: DEFAULT_PRICE, mappedList.max() ?: DEFAULT_PRICE)
        } else {
            Pair(DEFAULT_PRICE, DEFAULT_PRICE)
        }
    }

    private fun convertImage(productAdaptee: Storefront.Product): List<Image> =
        productAdaptee.images.edges.mapNotNull  { ImageAdapter.adapt(it.node)!! }

    private fun convertProductOptionList(options: List<Storefront.ProductOption>?): List<ProductOption> {
        return if (options != null) {
            ProductOptionListAdapter.adapt(options)
        } else {
            listOf()
        }
    }


}
