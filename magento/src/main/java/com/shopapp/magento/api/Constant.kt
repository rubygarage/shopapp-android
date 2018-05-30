package com.shopapp.magento.api

internal object Constant {
    const val PRODUCT_IMAGE_PATH = "pub/media/catalog/product"
    const val CATEGORY_IMAGE_PATH = "pub/media/catalog/category"

    const val PAGINATION_START_VALUE = 1
    const val PAGINATION_END_VALUE = -1

    const val DESCRIPTION_ATTRIBUTE = "description"
    const val THUMBNAIL_ATTRIBUTE = "thumbnail"
    const val IMAGE_ATTRIBUTE = "image"

    const val NAME_FIELD = "name"
    const val SKU_FIELD = "sku"
    const val TYPE_ID_FIELD = "type_id"
    const val CREATED_AT_FIELD = "created_at"
    const val CATEGORY_ID_FIELD = "category_id"
    const val PRICE_FIELD = "price"
    const val ATTRIBUTE_SET_ID_FIELD = "attribute_set_id"

    const val PRODUCT_DEFAULT_TYPE_ID = "simple"
    const val ASC_DIRECTION = "ASC"
    const val DESC_DIRECTION = "DESC"

    const val AUTHORIZATION_HEADER = "Authorization"
    const val ACCESS_TOKEN = "access_token"
    const val ACCESS_TOKEN_PREFIX = "Bearer"
    const val ACCESS_KEY = "access_key"
    const val UNAUTHORIZED_ERROR = "Unauthorized"
}