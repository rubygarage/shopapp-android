package com.client.shop.ui.product

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.cart.CartActivity
import com.client.shop.ui.gallery.GalleryFragment
import com.client.shop.ui.product.contract.DetailsPresenter
import com.client.shop.ui.product.contract.DetailsView
import com.client.shop.ui.product.di.ProductDetailsModule
import com.client.shop.ui.product.view.OptionsGroupContainer
import com.domain.entity.Product
import com.domain.entity.ProductVariant
import com.domain.entity.SortType
import com.domain.formatter.NumberFormatter
import com.ui.base.lce.BaseActivity
import com.ui.custom.SimpleAnimationListener
import com.ui.ext.collapseAnimation
import com.ui.ext.expandAnimation
import kotlinx.android.synthetic.main.activity_product_details.*
import javax.inject.Inject

class ProductDetailsActivity :
    BaseActivity<Product, DetailsView, DetailsPresenter>(),
    DetailsView,
    OptionsGroupContainer.OnVariantSelectListener {

    companion object {
        private const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
        private const val EXTRA_PRODUCT_VARIANT = "extra_product_variant"
        private const val EXPAND_DURATION = 400L
        private const val SCROLL_DURATION = 200L

        fun getStartIntent(
            context: Context,
            productId: String
        ): Intent {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_ID, productId)
            return intent
        }

        fun getStartIntent(
            context: Context,
            productVariant: ProductVariant
        ): Intent {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_ID, productVariant.productId)
            intent.putExtra(EXTRA_PRODUCT_VARIANT, productVariant)
            return intent
        }
    }

    @Inject
    lateinit var detailsPresenter: DetailsPresenter
    private lateinit var productId: String
    private lateinit var formatter: NumberFormatter
    private var galleryFragment: GalleryFragment? = null
    private var product: Product? = null
    private var selectedProductVariant: ProductVariant? = null
    private var isAddedToCart = false

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = intent.getStringExtra(EXTRA_PRODUCT_ID)
        formatter = NumberFormatter()

        setupGallery()
        setupCartButton()
        optionsContainer.variantSelectListener = this

        descriptionLabel.setOnClickListener {
            val descriptionHeight = description.layoutParams.height
            if (descriptionHeight == WRAP_CONTENT) {
                descriptionBottomSpace.visibility = View.GONE
                description.collapseAnimation(EXPAND_DURATION)
                descriptionLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0)
            } else {
                descriptionBottomSpace.visibility = View.VISIBLE
                description.expandAnimation(EXPAND_DURATION, object : SimpleAnimationListener() {
                    override fun onAnimationEnd(animation: Animation?) {
                        scrollView.post {
                            ObjectAnimator.ofInt(scrollView, "scrollY", descriptionLabel.y.toInt())
                                .setDuration(SCROLL_DURATION)
                                .start()
                        }
                    }
                })
                descriptionLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0)
            }
        }

        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    override fun onStop() {
        super.onStop()
        quantityEditText.clearFocus()
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachDetailsComponent(ProductDetailsModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_product_details

    override fun createPresenter(): DetailsPresenter {
        return detailsPresenter
    }

    //SETUP

    private fun setupGallery() {
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
                    val variant = it
                    product?.let {
                        presenter.addProductToCart(variant, it.title,
                            it.currency, quantityEditText.text.toString())
                    }
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
        description.text = data.productDescription
        galleryFragment?.updateProduct(data)
        optionsContainer.setProduct(data, intent.getParcelableExtra(EXTRA_PRODUCT_VARIANT))

        val relatedFragment = ProductHorizontalFragment.newInstance(SortType.TYPE, data.type)
        supportFragmentManager.beginTransaction().replace(R.id.relatedContainer, relatedFragment).commit()
    }

    override fun productAddedToCart() {
        cartButton.text = getString(R.string.added_to_cart)
        isAddedToCart = true
    }

    //CALLBACK

    override fun onVariantSelected(productVariant: ProductVariant?) {
        if (productVariant != null && productVariant.isAvailable) {
            priceValue.text = product?.let { formatter.formatPrice(productVariant.price, it.currency) }
            priceValue.isEnabled = true
            cartButton.isEnabled = true
            cartButton.setText(R.string.add_to_cart)
            selectedProductVariant = productVariant
        } else {
            priceValue.setText(R.string.n_a_placeholder)
            priceValue.isEnabled = false
            cartButton.isEnabled = false
            cartButton.setText(R.string.product_unavailable)
            selectedProductVariant = null
        }
    }

}