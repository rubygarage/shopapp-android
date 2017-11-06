package com.client.shop.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ext.getScreenSize
import com.client.shop.ui.base.ui.lce.BaseActivity
import com.client.shop.ui.cart.CartActivity
import com.client.shop.ui.container.OptionsContainer
import com.client.shop.ui.details.contract.DetailsPresenter
import com.client.shop.ui.details.contract.DetailsView
import com.client.shop.ui.details.di.DetailsModule
import com.client.shop.ui.gallery.GalleryFragment
import com.domain.entity.Product
import com.domain.entity.ProductVariant
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity :
        BaseActivity<Product, DetailsView, DetailsPresenter>(),
        DetailsView,
        OptionsContainer.OnVariantSelectListener {

    companion object {
        private const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
        private const val MIN_TOUCHABLE_ALPHA = 0.9f

        fun getStartIntent(context: Context, productId: String): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_ID, productId)
            return intent
        }
    }

    @Inject lateinit var detailsPresenter: DetailsPresenter
    private var galleryFragment: GalleryFragment? = null
    private lateinit var productId: String
    private var product: Product? = null
    private var selectedProductVariant: ProductVariant? = null
    private var isAddedToCart = false

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        productId = intent.getStringExtra(EXTRA_PRODUCT_ID)

        val typedValue = TypedValue()
        resources.getValue(R.dimen.image_aspect_ratio, typedValue, true)
        val galleryHeight = (getScreenSize().x / typedValue.float).toInt()

        setupScrollView(galleryHeight)
        setupGallery(galleryHeight)
        setupCartButton()

        optionsContainer.variantSelectListener = this
        loadData(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //INITIAL

    override fun inject(component: AppComponent) {
        component.attachDetailsComponent(DetailsModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_details

    override fun createPresenter(): DetailsPresenter {
        return detailsPresenter
    }

    //SETUP

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

    private fun setupGallery(galleryHeight: Int) {

        galleryContainer.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                galleryHeight)
        galleryFragment = GalleryFragment.newInstance(product, true)
        galleryFragment?.let {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.galleryContainer, it)
                    .commit()
        }
    }

    private fun setupCartButton() {
        cartButton.setOnClickListener {
            if (isAddedToCart) {
                startActivity(CartActivity.getStartIntent(this))
            } else {
                selectedProductVariant?.let {
                    presenter.addProductToCart(it, productId,
                            product?.currency ?: "", quantityEditText.text.toString())
                }
            }
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductDetails(productId)
    }

    override fun showContent(data: Product) {
        super.showContent(data)
        product = data
        productTitle.text = data.title
        this.productDescription.text = data.productDescription
        galleryFragment?.updateProduct(data)
        optionsContainer.setProduct(data)
    }

    override fun productAddedToCart() {
        cartButton.text = getString(R.string.added_to_cart)
        isAddedToCart = true
    }

    //CALLBACK

    override fun onVariantSelected(productVariant: ProductVariant?) {
        if (productVariant != null && productVariant.isAvailable) {
            priceValue.text = getString(R.string.price_pattern, productVariant.price, product?.currency)
            cartButton.isEnabled = true
            selectedProductVariant = productVariant
        } else {
            priceValue.text = ""
            cartButton.isEnabled = false
            selectedProductVariant = null
        }
    }

}