package com.shopify.api.adapter

import com.domain.entity.Image
import com.domain.entity.Product
import com.domain.entity.ProductOption
import com.shopify.buy3.Storefront

class ProductAdapter {

    companion object {

        private val DEFAULT_PRICE = "0"

        fun adapt(shopAdaptee: Storefront.Shop, productAdaptee: Storefront.Product, paginationValue: String? = null): Product {
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
                    price = convertPrice(productAdaptee),
                    images = convertImage(productAdaptee),
                    options = convertProductOptionList(productAdaptee.options),
                    variants = productAdaptee.variants.edges.map { ProductVariantAdapter.adapt(it.node) },
                    paginationValue = paginationValue
            )
        }

        private fun convertPrice(productAdaptee: Storefront.Product): String {
            val variantsList = productAdaptee.variants.edges
            return if (variantsList.size > 0) {
                variantsList[0].node.price.toString()
            } else {
                DEFAULT_PRICE
            }
        }

        private fun convertImage(productAdaptee: Storefront.Product): List<Image> {
            return productAdaptee.images.edges.map { ImageAdapter.adapt(it.node)!! }
        }

        private fun convertProductOptionList(options: List<Storefront.ProductOption>?): List<ProductOption> {
            return if (options != null) {
                ProductOptionListAdapter.adapt(options)
            } else {
                listOf()
            }
        }
    }

}
