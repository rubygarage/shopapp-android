package com.shopapp.ui.gallery.router

import android.content.Context
import com.shopapp.gateway.entity.Product
import com.shopapp.ui.gallery.GalleryActivity

class GalleryRouter {

    fun showFullGallery(context: Context?, product: Product?, selectedPosition: Int = 0) {
        context?.let { it.startActivity(GalleryActivity.getStartIntent(it, product, selectedPosition)) }
    }

}
