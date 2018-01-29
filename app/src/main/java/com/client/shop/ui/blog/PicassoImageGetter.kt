package com.client.shop.ui.blog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView

import com.client.shop.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class PicassoImageGetter(target: TextView, private val context: Context) : Html.ImageGetter {
    private var textView: TextView? = null

    init {
        textView = target
    }

    override fun getDrawable(source: String): Drawable {
        val drawable = BitmapDrawablePlaceHolder()
        //todo fix url
        Picasso.with(context)
            .load("http://cdn.shopify.com/s/files/1/2806/8208/products/best-sneakers-2017-virgil-abloh-air-jordan-1-gallery-1200x800_large.jpg")
            .resize(300, 300)
            .placeholder(R.drawable.ic_cross)
            .into(drawable)
        return drawable
    }

    private inner class BitmapDrawablePlaceHolder : BitmapDrawable(), Target {

        var htmlImage: Drawable? = null

        override fun draw(canvas: Canvas) {
            if (htmlImage != null) {
                htmlImage!!.draw(canvas)
            }
        }

        fun setImage(drawable: Drawable) {
            this.htmlImage = drawable
            val width = 300
            val height = 300
            drawable.setBounds(0, 0, width, height)
            setBounds(0, 0, width, height)
            if (textView != null) {
                textView!!.text = textView!!.text
            }
        }

        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
            setImage(BitmapDrawable(context.resources, bitmap))
        }

        override fun onBitmapFailed(errorDrawable: Drawable) {}

        override fun onPrepareLoad(placeHolderDrawable: Drawable) {

        }

    }
}
