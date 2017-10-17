package com.shopify.api.adapter

import com.shopapicore.entity.Image
import com.shopapicore.entity.Product
import com.shopapicore.entity.ProductDetails
import com.shopify.buy3.Storefront

class ProductAdapter(shopAdaptee: Storefront.Shop, productAdaptee: Storefront.Product) : Product(
        id = productAdaptee.id.toString(),
        title = productAdaptee.title,
        productDescription = productAdaptee.description,
        vendor = productAdaptee.vendor,
        currency = shopAdaptee.paymentSettings.currencyCode.toString(),
        type = productAdaptee.productType,
        tags = productAdaptee.tags,
        createdAt = productAdaptee.createdAt.toDate(),
        updatedAt = productAdaptee.updatedAt.toDate(),
        price = priceExtractor(productAdaptee),
        images = imageExtractor(productAdaptee),
        productDetails = productDetailsExtractor(productAdaptee),
        options = if (productAdaptee.options != null) ProductOptionListAdapter(productAdaptee.options) else listOf()
) {

    companion object {

        private val DEFAULT_PRICE = "0"

        private fun priceExtractor(productAdaptee: Storefront.Product): String {
            val variantsList = productAdaptee.variants.edges
            return if (variantsList.size > 0) {
                variantsList[0].node.price.toString()
            } else {
                DEFAULT_PRICE
            }
        }

        private fun imageExtractor(productAdaptee: Storefront.Product): List<Image> {
            return productAdaptee.images.edges.map { ImageAdapter(it.node) }
        }

        private fun productDetailsExtractor(productAdaptee: Storefront.Product) =
                ProductDetails(productAdaptee.variants
                        .edges
                        .map { ProductVariantAdapter(it.node) }, productAdaptee.descriptionHtml)
    }

}
