package com.shopapp.magento.adapter

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.magento.api.response.ProductResponse
import com.shopapp.magento.test.TestConstant.DEFAULT_HOST
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

class ImageAdapterTest {

    companion object {
        private const val CATALOG_PATH = "catalog/"
        private const val IMAGE_PATH = "image/"
    }

    @Test
    fun shouldReturnImageWhenAdaptPathToImage() {
        val image = ImageAdapter.adapt(DEFAULT_HOST, CATALOG_PATH, IMAGE_PATH)
        val src = DEFAULT_HOST + CATALOG_PATH + IMAGE_PATH
        assertNotNull(image!!)
        assertEquals(image.id, src)
        assertEquals(image.src, src)
    }

    @Test
    fun shouldReturnNullWhenImagePathIsNull() {
        val image = ImageAdapter.adapt(DEFAULT_HOST, CATALOG_PATH, null)
        assertNull(image)
    }

    @Test
    fun shouldReturnNullWhenImagePathIsProhibited() {
        val image = ImageAdapter.adapt(DEFAULT_HOST, CATALOG_PATH, ImageAdapter.NO_SELECTION)
        assertNull(image)
    }

    @Test
    fun shouldReturnImageWhenAdaptResponse() {
        val entry: ProductResponse.GalleryEntry = mock()

        val imageId = "id"
        val imageSrc = "src"
        val imageText = "message"

        given(entry.id).willReturn(imageId)
        given(entry.file).willReturn(imageSrc)
        given(entry.label).willReturn(imageText)
        given(entry.mediaType).willReturn(ImageAdapter.IMAGE_MEDIA_TYPE)

        val image = ImageAdapter.adapt(DEFAULT_HOST, CATALOG_PATH, entry)
        assertNotNull(image!!)
        assertEquals(image.id, imageId)
        assertEquals(image.src, DEFAULT_HOST + CATALOG_PATH + imageSrc)
        assertEquals(image.alt, imageText)
    }

    @Test
    fun shouldReturnNullWhenIsNotMediaType() {
        val entry: ProductResponse.GalleryEntry = mock()
        val image = ImageAdapter.adapt(DEFAULT_HOST, CATALOG_PATH, entry)
        assertNull(image)
    }
}