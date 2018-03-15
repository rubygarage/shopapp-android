package com.shopapp.ui.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.shopapp.R
import com.shopapp.ext.replaceOnce
import com.shopapp.gateway.entity.Product
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_PRODUCT = "product"
        const val EXTRA_SELECTED_POSITION = "selected_position"

        fun getStartIntent(context: Context, product: Product?, selectedPosition: Int = 0): Intent {
            val intent = Intent(context, GalleryActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT, product)
            intent.putExtra(EXTRA_SELECTED_POSITION, selectedPosition)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val product: Product? = intent.getParcelableExtra(EXTRA_PRODUCT)
        val selectedPosition = intent.getIntExtra(EXTRA_SELECTED_POSITION, 0)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white)
        }

        supportFragmentManager.replaceOnce(R.id.galleryContainer, GalleryFragment::javaClass.name, {
            val fragment = GalleryFragment.newInstance(product = product, selectedPosition = selectedPosition)
            fragment.imageClickListener = View.OnClickListener {
                toolbar.visibility =
                        if (toolbar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            fragment
        }, false).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}