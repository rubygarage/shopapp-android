package com.shopify.api.adapter

import com.shopapicore.entity.ProductVariant
import com.shopapicore.entity.VariantOption
import com.shopify.buy3.Storefront

class ProductVariantAdapter(productVariant: Storefront.ProductVariant) : ProductVariant(
        productVariant.id.toString(),
        productVariant.title,
        productVariant.price.toString(),
        productVariant.availableForSale == true,
        convertSelectedOptions(productVariant.selectedOptions),
        ImageAdapter.newInstance(productVariant.image)
) {

    companion object {

        private fun convertSelectedOptions(selectedOptions: List<Storefront.SelectedOption>?): List<VariantOption> {
            return if (selectedOptions != null) {
                VariantOptionListAdapter(selectedOptions)
            } else {
                listOf()
            }
        }
    }
}