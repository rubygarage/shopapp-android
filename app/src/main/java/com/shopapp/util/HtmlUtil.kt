package com.shopapp.util

import org.jsoup.Jsoup

object HtmlUtil {

    fun fitHtmlImages(html: String): String {
        val document = Jsoup.parse(html)
        document.select("img").forEach {
            if (it.hasAttr("style")) {
                var styleValue = it.attr("style")
                styleValue = removeStyleAttr(styleValue, "max-width")
                styleValue = removeStyleAttr(styleValue, "width")
                styleValue = removeStyleAttr(styleValue, "height")
                styleValue += "display: inline; width:auto; height: auto; max-width: 100%;"
                it.attr("style", styleValue)
            } else {
                it.removeAttr("max-width")
                it.removeAttr("width")
                it.removeAttr("height")
                it.attr("style", "display: inline; width:auto; height: auto; max-width: 100%;")
            }
        }
        return document.outerHtml()
    }

    fun fitHtmlFrames(html: String, height: Int): String {
        val document = Jsoup.parse(html)
        document.select("iframe").forEach {
            if (it.hasAttr("style")) {
                var styleValue = it.attr("style")
                styleValue = removeStyleAttr(styleValue, "width")
                styleValue = removeStyleAttr(styleValue, "height")
                styleValue += "display: inline; width:100%; height: $height;"
                it.attr("style", styleValue)
            } else {
                it.attr("width", "100%")
                it.attr("height", "$height")
            }
        }
        return document.outerHtml()
    }

    private fun removeStyleAttr(styleValue: String, styleAttr: String): String {
        var styleValue1 = styleValue
        val startIndex = styleValue1.indexOf(styleAttr)
        if (startIndex >= 0) {
            val endIndex = styleValue1.indexOf(";", startIndex, true) + 1
            styleValue1 = styleValue1.removeRange(startIndex, endIndex)
        }
        return styleValue1
    }
}