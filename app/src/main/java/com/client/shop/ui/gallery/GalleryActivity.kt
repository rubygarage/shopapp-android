package com.client.shop.ui.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.client.shop.R
import com.shopapicore.entity.Product

class GalleryActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_PRODUCT = "product"
        private const val EXTRA_SELECTED_POSITION = "selected_position"

        fun getStartIntent(context: Context, product: Product, selectedPosition: Int = 0): Intent {
            val intent = Intent(context, GalleryActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT, product)
            intent.putExtra(EXTRA_SELECTED_POSITION, selectedPosition)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val product: Product = intent.getParcelableExtra(EXTRA_PRODUCT)
        val selectedPosition = intent.getIntExtra(EXTRA_SELECTED_POSITION, 0)

        supportFragmentManager.beginTransaction()
                .replace(R.id.galleryContainer, GalleryFragment.newInstance(product = product,
                        selectedPosition = selectedPosition))
                .commit()
    }
}