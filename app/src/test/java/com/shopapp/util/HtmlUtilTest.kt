package com.shopapp.util

import android.content.Context
import com.shopapp.util.ext.replaceCommandSymbols
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class HtmlUtilTest {

    companion object {
        private const val HEIGHT = 200
    }

    private lateinit var context: Context


    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldAddStyleToAllImages() {
        val testedString = Reader.readFile("testImageHtml")!!
        val expected = replaceCommandSymbols(Reader.readFile("resultImageHtml")!!)
        val actual = replaceCommandSymbols(HtmlUtil.fitHtmlImages(testedString))
        assertEquals(expected, actual)
    }

    @Test
    fun shouldSetSizeToAllFrames() {
        val testedString = Reader.readFile("testFrameHtml")!!
        val expected = Reader.readFile("resultFrameHtml")!!.replaceCommandSymbols()
        val actual = HtmlUtil.fitHtmlFrames(testedString, HEIGHT).replaceCommandSymbols()
        assertEquals(expected, actual)
    }

    @Test
    fun shouldSetSizeToAllFramesInStyles() {
        val testedString = Reader.readFile("testFrameStyleHtml")!!
        val expected = Reader.readFile("resultFrameStyleHtml")!!.replaceCommandSymbols()
        val actual = HtmlUtil.fitHtmlFrames(testedString, HEIGHT).replaceCommandSymbols()
        assertEquals(expected, actual)
    }

    private fun replaceCommandSymbols(src: String) =
        src.filter { it != ' ' && it != '\n' && it != '\r' }
}