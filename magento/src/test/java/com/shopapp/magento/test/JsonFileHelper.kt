package com.shopapp.magento.test

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class JsonFileHelper {

    fun getJsonContents(filename: String): String? {
        val inputStream = ClassLoader.getSystemResourceAsStream(filename)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val stringBuilder = StringBuilder()

        var done = false

        try {
            while (!done) {
                val line = reader.readLine()
                done = line == null

                if (line != null) {
                    stringBuilder.append(line)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                reader.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return stringBuilder.toString()
    }
}