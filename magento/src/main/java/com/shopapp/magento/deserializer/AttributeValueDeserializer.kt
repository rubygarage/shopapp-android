package com.shopapp.magento.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.shopapp.magento.api.response.util.AttributeValue
import java.lang.reflect.Type

class AttributeValueDeserializer : JsonDeserializer<AttributeValue?> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AttributeValue? {
        return if (json.isJsonPrimitive || json.isJsonObject) {
            AttributeValue(data = json.asString)
        } else if (json.isJsonArray) {
            AttributeValue(dataList = json.asJsonArray.map { it.toString() })
        } else {
            null
        }
    }
}