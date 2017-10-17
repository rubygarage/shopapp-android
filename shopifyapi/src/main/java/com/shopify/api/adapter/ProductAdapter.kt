package com.shopify.api.adapter

import com.shopapicore.entity.Image
import com.shopapicore.entity.Product
import com.shopapicore.entity.ProductDetails
import com.shopapicore.entity.ProductOption
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
        price = convertPrice(productAdaptee),
        images = convertImage(productAdaptee),
        productDetails = convertProductDetails(productAdaptee),
        options = convertProductOptionList(productAdaptee.options)
) {

    companion object {

        private val DEFAULT_PRICE = "0"

        private fun convertPrice(productAdaptee: Storefront.Product): String {
            val variantsList = productAdaptee.variants.edges
            return if (variantsList.size > 0) {
                variantsList[0].node.price.toString()
            } else {
                DEFAULT_PRICE
            }
        }

        private fun convertImage(productAdaptee: Storefront.Product): List<Image> {
            return productAdaptee.images.edges.map { ImageAdapter.newInstance(it.node)!! }
        }

        private fun convertProductDetails(productAdaptee: Storefront.Product) =
                ProductDetails(productAdaptee.variants
                        .edges
                        .map { ProductVariantAdapter(it.node) }, productAdaptee.descriptionHtml)

        private fun convertProductOptionList(options: List<Storefront.ProductOption>?): List<ProductOption> {
            return if (options != null) {
                ProductOptionListAdapter(options)
            } else {
                listOf()
            }
        }
    }

}
