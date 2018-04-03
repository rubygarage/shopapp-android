package com.shopapp.ui.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import android.view.View
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.registerKeyboardVisibilityListener
import com.shopapp.gateway.entity.*
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.checkout.contract.CheckoutPresenter
import com.shopapp.ui.checkout.contract.CheckoutView
import com.shopapp.ui.checkout.router.CheckoutRouter
import com.shopapp.ui.checkout.view.CheckoutEmailView
import com.shopapp.ui.checkout.view.CheckoutShippingOptionsView
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.PaymentType
import com.shopapp.ui.const.RequestCode
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.success.OrderSuccessActivity
import kotlinx.android.synthetic.main.activity_checkout.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import java.math.BigDecimal
import javax.inject.Inject

class CheckoutActivity :
    BaseLceActivity<Checkout, CheckoutView, CheckoutPresenter>(),
    CheckoutView,
    CheckoutShippingOptionsView.OnOptionSelectedListener,
    CheckoutEmailView.EmailChangeListener {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, CheckoutActivity::class.java)
    }

    @Inject
    lateinit var checkoutPresenter: CheckoutPresenter

    @Inject
    lateinit var router: CheckoutRouter

    private var checkout: Checkout? = null
    private var customer: Customer? = null
    private var unregistrar: Unregistrar? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.checkout))
        setupShippingAddressViewListeners()
        setupPaymentViewListeners()
        setupListeners()
        loadData()
    }

    override fun onResume() {
        super.onResume()
        unregistrar = registerKeyboardVisibilityListener(KeyboardVisibilityEventListener {
            placeOrderButton.visibility = if (it) View.INVISIBLE else View.VISIBLE
        })
    }

    override fun onPause() {
        super.onPause()
        unregistrar?.unregister()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val isAddShippingAddress = requestCode == RequestCode.ADD_SHIPPING_ADDRESS
        val isEditShippingAddress = requestCode == RequestCode.EDIT_SHIPPING_ADDRESS
        val isAddBillingAddress = requestCode == RequestCode.ADD_BILLING_ADDRESS
        val isEditBillingAddress = requestCode == RequestCode.EDIT_BILLING_ADDRESS
        val isPayment = requestCode == RequestCode.PAYMENT
        val isCard = requestCode == RequestCode.CARD
        val isOkResult = resultCode == Activity.RESULT_OK

        val isAddressChanged = data?.getBooleanExtra(Extra.IS_ADDRESS_CHANGED, false) ?: false
        val isClearShippingAddress = data?.getBooleanExtra(Extra.CLEAR_SHIPPING, false) ?: false
        val isClearBillingAddress = data?.getBooleanExtra(Extra.CLEAR_BILLING, false) ?: false

        if (isOkResult) {
            if ((isAddShippingAddress || isEditShippingAddress) && isAddressChanged) {
                loadData()
            } else if (isPayment) {
                val paymentType: PaymentType? = data?.getSerializableExtra(Extra.PAYMENT_TYPE) as? PaymentType
                paymentView.setPaymentType(paymentType)
            } else if (isCard) {
                val card: Card? = data?.getParcelableExtra(Extra.CARD)
                val cardToken: String? = data?.getStringExtra(Extra.CARD_TOKEN)
                paymentView.setCardData(card, cardToken)
            } else if ((isAddBillingAddress || isEditBillingAddress) && isAddressChanged) {
                val address: Address? = data?.getParcelableExtra(Extra.ADDRESS)
                paymentView.setAddressData(address)
            }
        }

        if (isClearBillingAddress) {
            paymentView.setAddressData(null)
        } else if (isClearShippingAddress) {
            shippingAddressView.setAddress(null)
            shippingOptionsView.unSelectAddress()
        }

        verifyCheckoutData()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCheckoutComponent().inject(this)
    }

    override fun getContentView() = R.layout.activity_checkout

    override fun createPresenter() = checkoutPresenter

    override fun useModalStyle() = true

    //SETUP

    private fun setupShippingAddressViewListeners() {
        shippingAddressView.setClickListeners(
            editClickListener = View.OnClickListener {
                checkout?.let {
                    val address = it.address
                    if (customer != null) {
                        router.showCheckoutAddressListForResult(activity = this,
                            checkoutId = it.checkoutId,
                            selectedAddress = address,
                            isShippingAddress = true,
                            shippingAddress = null,
                            billingAddress = paymentView.getAddress(),
                            requestCode = RequestCode.EDIT_SHIPPING_ADDRESS)
                    } else if (address != null) {
                        checkout?.let {
                            router.showCheckoutUnAuthAddressListForResult(
                                this,
                                it.checkoutId,
                                address,
                                true,
                                RequestCode.EDIT_SHIPPING_ADDRESS)
                        }
                    }
                }
            },
            addAddressClickListener = View.OnClickListener {
                checkout?.let {
                    if (customer != null) {
                        router.showCheckoutAddressListForResult(
                            activity = this,
                            checkoutId = it.checkoutId,
                            selectedAddress = shippingAddressView.getAddress(),
                            isShippingAddress = true,
                            shippingAddress = null,
                            billingAddress = paymentView.getAddress(),
                            requestCode = RequestCode.ADD_SHIPPING_ADDRESS
                        )
                    } else {
                        router.showCheckoutUnAuthAddressListForResult(
                            this, it.checkoutId, isShippingAddress = true, requestCode = RequestCode.ADD_SHIPPING_ADDRESS
                        )
                    }
                }
            }
        )
    }

    private fun setupPaymentViewListeners() {
        paymentView.setClickListeners(
            paymentClickListener = View.OnClickListener {
                router.showPaymentResult(this, paymentView.getPaymentType(), RequestCode.PAYMENT)
            },
            cardClickListener = View.OnClickListener {
                router.showCardForResult(this, paymentView.getCardData().first, RequestCode.CARD)
            },
            addAddressClickListener = View.OnClickListener {
                if (customer != null) {
                    router.showCheckoutAddressListForResult(
                        activity = this,
                        selectedAddress = paymentView.getAddress(),
                        isShippingAddress = false,
                        shippingAddress = checkout?.address,
                        billingAddress = null,
                        requestCode = RequestCode.ADD_BILLING_ADDRESS)
                } else {
                    router.showCheckoutUnAuthAddressListForResult(
                        activity = this,
                        selectedAddress = paymentView.getAddress(),
                        isShippingAddress = false,
                        requestCode = RequestCode.ADD_BILLING_ADDRESS
                    )
                }
            }
        )
    }

    private fun setupListeners() {
        checkoutEmailView.emailChangeListener = this
        shippingOptionsView.onOptionSelectedListener = this

        val placeOrderClickListener = View.OnClickListener {
            placeOrderButton.visibility = View.VISIBLE
            failureView.visibility = View.GONE
            when (paymentView.getPaymentType()) {
                PaymentType.CARD_PAYMENT -> {
                    val email = checkoutEmailView.getEmail()
                    val billingAddress = paymentView.getAddress()
                    val cardToken = paymentView.getCardData().second
                    presenter.completeCheckoutByCard(checkout, email, billingAddress, cardToken)
                }
                else -> {
                }
            }
        }

        placeOrderButton.setOnClickListener(placeOrderClickListener)
        failureView.setListeners(
            tryAgainClickListener = placeOrderClickListener,
            backToShopClickListener = View.OnClickListener {
                router.showHome(this, true)
            }
        )
    }

    private fun verifyCheckoutData() {
        val shippingAddress = shippingAddressView.getAddress()
        val email = checkoutEmailView.getEmail()
        val paymentType = paymentView.getPaymentType()
        val cardData = paymentView.getCardData()
        val shippingData = paymentView.getAddress()
        presenter.verifyCheckoutData(checkout, shippingAddress, email, paymentType, cardData.first,
            cardData.second, shippingData)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        val checkout = this.checkout
        if (checkout != null) {
            presenter.getCheckout(checkout.checkoutId)
        } else {
            presenter.getCheckoutData()
        }
    }

    override fun showContent(data: Checkout) {
        super.showContent(data)
        checkout = data
        shippingAddressView.setAddress(data.address)
        priceView.setData(
            data.subtotalPrice, BigDecimal.ZERO,
            data.shippingRate?.price ?: BigDecimal.ZERO, data.totalPrice, data.currency
        )
        if (data.address != null) {
            presenter.getShippingRates(data.checkoutId)
        } else {
            shippingOptionsView.unSelectAddress()
        }
        verifyCheckoutData()
    }

    override fun customerReceived(customer: Customer?) {
        this.customer = customer
        checkoutEmailView.setData(customer)
    }

    override fun cartProductListReceived(cartProductList: List<CartProduct>) {
        myCartView.setData(cartProductList)
    }

    override fun shippingRatesReceived(shippingRates: List<ShippingRate>) {
        checkout?.let {
            shippingOptionsView.setData(it, shippingRates)
        }
    }

    override fun checkoutValidationPassed(isValid: Boolean) {
        placeOrderButton.isEnabled = isValid
    }

    override fun checkoutInProcess() {
        changeState(LceLayout.LceState.LoadingState(true))
    }

    override fun checkoutCompleted(order: Order) {
        changeState(LceLayout.LceState.ContentState)
        router.showSuccessOrder(this, order.id, order.orderNumber)
    }

    override fun checkoutError() {
        changeState(LceLayout.LceState.ContentState)
        placeOrderButton.visibility = View.GONE
        failureView.visibility = View.VISIBLE
    }

    //CALLBACK

    override fun onOptionSelected(shippingRate: ShippingRate) {
        shippingOptionsView.selectShippingRate(shippingRate)
        changeState(LceLayout.LceState.LoadingState(true))
        checkout?.let { presenter.setShippingRates(it, shippingRate) }
    }

    override fun onEmailChanged() {
        verifyCheckoutData()
    }
}