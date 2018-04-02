package com.shopapp.ui.product

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.ChangeBounds
import android.support.transition.Transition
import android.support.transition.TransitionManager
import android.view.Menu
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.ext.registerKeyboardVisibilityListener
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.ui.FragmentVisibilityListener
import com.shopapp.ui.custom.SimpleTransitionListener
import com.shopapp.ui.gallery.GalleryFragment
import com.shopapp.ui.product.contract.ProductDetailsPresenter
import com.shopapp.ui.product.contract.ProductDetailsView
import com.shopapp.ui.product.view.OptionsGroupContainer
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import javax.inject.Inject

class ProductDetailsActivity :
    BaseLceActivity<Product, ProductDetailsView, ProductDetailsPresenter>(),
    ProductDetailsView,
    OptionsGroupContainer.OnVariantSelectListener {

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
        const val EXTRA_PRODUCT_VARIANT = "extra_product_variant"
        private const val SCROLL_DURATION = 400L

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
    lateinit var productDetailsPresenter: ProductDetailsPresenter
    private lateinit var productId: String
    private lateinit var formatter: NumberFormatter
    private var galleryFragment: GalleryFragment? = null
    private var product: Product? = null
    private var selectedProductVariant: ProductVariant? = null
    private var unregistrar: Unregistrar? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = intent.getStringExtra(EXTRA_PRODUCT_ID)
        formatter = NumberFormatter()

        setupGallery()
        setupCartButton()
        setupDescription()
        optionsContainer.variantSelectListener = this
        lceLayout.errorView.errorTarget = getString(R.string.product)

        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        unregistrar = registerKeyboardVisibilityListener(KeyboardVisibilityEventListener {
            cartButton.visibility = if (it) View.INVISIBLE else View.VISIBLE
        })
    }

    override fun onPause() {
        super.onPause()
        unregistrar?.unregister()
        quantityEditText.clearFocus()
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachProductComponent().inject(this)
    }

    override fun getContentView() = R.layout.activity_product_details

    override fun createPresenter(): ProductDetailsPresenter {
        return productDetailsPresenter
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
            selectedProductVariant?.let {
                val variant = it
                product?.let {
                    presenter.addProductToCart(variant, it.title,
                        it.currency, quantityEditText.text.toString())
                }
            }
        }
    }

    private fun setupDescription() {
        val transition = ChangeBounds()
        transition.addListener(object : SimpleTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                if (description.height != 0) {
                    scrollView.post {
                        ObjectAnimator.ofInt(scrollView, "scrollY", descriptionLabel.y.toInt())
                            .setDuration(SCROLL_DURATION)
                            .start()
                    }
                }
            }
        })

        descriptionLabel.setOnClickListener {
            TransitionManager.beginDelayedTransition(mainContainer, transition)
            val isExpanded = description.height != 0
            val icon = if (isExpanded) R.drawable.ic_plus else R.drawable.ic_minus
            descriptionLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
            val height = if (isExpanded) 0 else WRAP_CONTENT
            val constraintSet = ConstraintSet()
            constraintSet.clone(dataContainer)
            constraintSet.constrainHeight(description.id, height)
            constraintSet.applyTo(dataContainer)
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

        val relatedFragment = ProductHorizontalFragment.newInstance(SortType.TYPE, data.type, data.title)
        relatedFragment.visibilityListener = object : FragmentVisibilityListener {
            override fun changeVisibility(isVisible: Boolean) {
                relatedContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.relatedContainer, relatedFragment).commit()
    }

    override fun productAddedToCart() {
        Toast.makeText(this, R.string.product_added, Toast.LENGTH_SHORT).show()
    }

    //CALLBACK

    override fun onVariantSelected(productVariant: ProductVariant?) {
        val isEnabled = productVariant != null && productVariant.isAvailable
        priceValue.isEnabled = isEnabled
        cartButton.isEnabled = isEnabled
        if (isEnabled) {
            priceValue.text = product?.let { formatter.formatPrice(productVariant!!.price, it.currency) }
            selectedProductVariant = productVariant
        } else {
            priceValue.setText(R.string.n_a_placeholder)
            cartButton.setText(R.string.product_unavailable)
            selectedProductVariant = null
        }
    }

}