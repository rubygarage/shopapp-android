package com.shopapp.magento.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date> {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        try {
            return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(json.asString)
        } catch (ignored: ParseException) {
        } catch (ignored: IllegalArgumentException) {
        }

        throw JsonParseException("Unparseable date: \"" + json.asString
                + "\". Supported format: " + DATE_FORMAT)
    }
}