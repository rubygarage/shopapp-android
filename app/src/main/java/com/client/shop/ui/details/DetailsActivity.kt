package com.client.shop.ui.details

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ext.getScreenSize
import com.client.shop.ui.base.ui.BaseMvpActivity
import com.client.shop.ui.details.contract.DetailsPresenter
import com.client.shop.ui.details.contract.DetailsView
import com.client.shop.ui.gallery.GalleryFragment
import com.shopapicore.entity.Product
import com.shopapicore.entity.ProductVariant
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject


class DetailsActivity : BaseMvpActivity<DetailsView, DetailsPresenter, DetailsViewState>(), DetailsView {

    @Inject lateinit var detailsPresenter: DetailsPresenter
    private var galleryFragment: GalleryFragment? = null
    private lateinit var product: Product

    companion object {
        private const val EXTRA_PRODUCT = "EXTRA_PRODUCT"
        private const val MIN_TOUCHABLE_ALPHA = 0.9f

        fun getStartIntent(context: Context, product: Product): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT, product)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        product = intent.getParcelableExtra(EXTRA_PRODUCT)

        this.productTitle.text = product.title
        this.productDescription.text = product.productDescription
        this.priceValue.text = getString(R.string.price_holder, product.price, product.currency)

        val typedValue = TypedValue()
        resources.getValue(R.dimen.image_aspect_ratio, typedValue, true)
        val screenOrientation = resources.configuration.orientation
        val screenSize =
                if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) getScreenSize().x
                else getScreenSize().y
        val galleryHeight = (screenSize / typedValue.float).toInt()

        setupScrollView(galleryHeight)
        setupGallery(galleryHeight, product)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject(component: AppComponent) {
        component.inject(this)
    }

    override fun createPresenter(): DetailsPresenter {
        return detailsPresenter
    }

    override fun onNewViewStateInstance() {
        detailsPresenter.loadProductDetails(product.id)
    }

    override fun createViewState() = DetailsViewState()

    private fun setupScrollView(galleryHeight: Int) {
        scrollView.setPadding(0, galleryHeight, 0, 0)
        scrollView.setOnTouchListener { _, event ->
            if (galleryContainer.alpha > MIN_TOUCHABLE_ALPHA) {
                galleryContainer.dispatchTouchEvent(event)
            } else {
                false
            }

        }
        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
        { _, _, scrollY, _, _ -> galleryContainer.alpha = 1 - (scrollY / galleryHeight.toFloat()) })
    }

    private fun setupGallery(galleryHeight: Int, product: Product) {

        galleryContainer.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                galleryHeight)
        galleryFragment = GalleryFragment.newInstance(product, true)
        galleryFragment?.let {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.galleryContainer, it)
                    .commit()
        }
    }

    private fun fillVariants(product: Product) {

        val variants = product.productDetails.variants
        if (variants.size > 1) {
            variantLabel.visibility = View.VISIBLE
            variantSpinner.visibility = View.VISIBLE
            variantSpinner.adapter = SpinnerVariantAdapter(variants)
            variantSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(aV: AdapterView<*>?, v: View?, position: Int, id: Long) {
                    setupVariant(variants[position], product.currency)
                }

                override fun onNothingSelected(aV: AdapterView<*>?) {
                }
            }
        } else if (variants.size == 1) {
            variantLabel.visibility = View.GONE
            variantSpinner.visibility = View.GONE
            setupVariant(variants[0], product.currency)
        }
    }

    private fun setupVariant(variant: ProductVariant, currency: String) {

        priceValue.text = getString(R.string.price_holder, variant.price, currency)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun productLoaded(product: Product) {

        viewState.setProduct(product)
        productTitle.text = product.title
        this.productDescription.text = product.productDescription
        galleryFragment?.updateProduct(product)
        fillVariants(product)
    }

    override fun showProgress() {
        recentProgress.show()
    }

    override fun hideProgress() {
        recentProgress.hide()
    }
}