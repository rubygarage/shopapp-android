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
        val testedString = Reader.readFile("testImage.html")!!
        val expected = Reader.readFile("resultImage.html")!!.replaceCommandSymbols()
        val actual = HtmlUtil.fitHtmlImages(testedString).replaceCommandSymbols()
        assertEquals(expected, actual)
    }

    @Test
    fun shouldSetSizeToAllFrames() {
        val testedString = Reader.readFile("testFrame.html")!!
        val expected = Reader.readFile("resultFrame.html")!!.replaceCommandSymbols()
        val actual = HtmlUtil.fitHtmlFrames(testedString, HEIGHT).replaceCommandSymbols()
        assertEquals(expected, actual)
    }

    @Test
    fun shouldSetSizeToAllFramesInStyles() {
        val testedString = Reader.readFile("testFrameStyle.html")!!
        val expected = Reader.readFile("resultFrameStyle.html")!!.replaceCommandSymbols()
        val actual = HtmlUtil.fitHtmlFrames(testedString, HEIGHT).replaceCommandSymbols()
        assertEquals(expected, actual)
    }

}