package com.shopapp.ui.address.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.checkout.adapter.CheckoutAddressListAdapter
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.RequestCode
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CheckoutAddressListActivityTest {

    companion object {
        const val SHIPPING_ADDRESS = "shipping_address"
        const val BILLING_ADDRESS = "billing_address"
    }

    private lateinit var context: Context
    private lateinit var activity: CheckoutAddressListActivity
    private lateinit var address: Address
    private lateinit var shippingAddress: Address
    private lateinit var billingAddress: Address
    private lateinit var addressList: List<Address>

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        address = MockInstantiator.newAddress()
        shippingAddress = MockInstantiator.newAddress()
        given(shippingAddress.address).willReturn(SHIPPING_ADDRESS)

        billingAddress = MockInstantiator.newAddress()
        given(billingAddress.address).willReturn(BILLING_ADDRESS)

        addressList = listOf(address, billingAddress, shippingAddress)
    }

    @Test
    fun shouldSetBillingTitle() {
        activity = getBillingModeActivity()
        assertEquals(context.getString(R.string.billing_address), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldSetSippingTitle() {
        activity = getShippingModeActivity()
        assertEquals(context.getString(R.string.shipping_address), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldStartCheckoutAddressActivity() {
        activity = getBillingModeActivity()
        activity.onClick(null)
        verify(activity.router).showCheckoutAddressForResult(activity, RequestCode.ADD_SHIPPING_ADDRESS)
    }

    @Test
    fun shouldExtractDataFromBundle() {
        activity = getBillingModeActivity()

        val shippingAddress: Address = activity.intent.getParcelableExtra(Extra.SHIPPING_ADDRESS)
        assertNotNull(shippingAddress)
        assertEquals(this.shippingAddress, shippingAddress)

        val billingAddress: Address = activity.intent.getParcelableExtra(Extra.BILLING_ADDRESS)
        assertNotNull(billingAddress)
        assertEquals(this.billingAddress, billingAddress)

        val selectedAddress: Address = activity.intent.getParcelableExtra(Extra.SELECTED_ADDRESS)
        assertNotNull(selectedAddress)
        assertEquals(this.shippingAddress, selectedAddress)

        val checkoutId = activity.intent.getStringExtra(Extra.CHECKOUT_ID)
        assertNotNull(checkoutId)
        assertEquals(MockInstantiator.DEFAULT_ID, checkoutId)
    }

    @Test
    fun shouldSetResultAndFinishWhenAddressSelected() {
        activity = getBillingModeActivity()
        activity.onAddressSelected(address)
        val shadowActivity = shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent

        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertEquals(address, resultIntent.extras.getParcelable(Extra.ADDRESS))
        assertTrue(resultIntent.extras.getBoolean(Extra.IS_ADDRESS_CHANGED))
    }

    @Test
    fun shouldSetShippingAddressWhenAddressSelected() {
        activity = getShippingModeActivity()
        activity.onAddressSelected(address)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
        verify(activity.presenter).setShippingAddress(MockInstantiator.DEFAULT_ID, address)
    }

    @Test
    fun shouldShowDeleteAlertDialog() {
        activity = getBillingModeActivity()
        activity.onDeleteButtonClicked(shippingAddress)
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowAlertDialog = shadowOf(dialog)

        assertEquals(context.getString(R.string.delete_address_message), shadowAlertDialog.message)
        assertEquals(context.getString(R.string.delete), dialog.getButton(AlertDialog.BUTTON_POSITIVE).text.toString())
    }

    @Test
    fun shouldSetResultClearSippingAddress() {
        activity = getBillingModeActivity()
        activity.showContent(shippingAddress to addressList)
        activity.onDeleteButtonClicked(shippingAddress)

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()

        val shadowActivity = shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent
        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertTrue(resultIntent.extras.getBoolean(Extra.CLEAR_SHIPPING))
        verify(activity.presenter).deleteAddress(shippingAddress.id)
    }

    @Test
    fun shouldSetResultClearBillingAddress() {
        activity = getShippingModeActivity()
        activity.showContent(shippingAddress to addressList)
        activity.onDeleteButtonClicked(billingAddress)

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()

        val shadowActivity = shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent
        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertTrue(resultIntent.extras.getBoolean(Extra.CLEAR_BILLING))
        verify(activity.presenter).deleteAddress(billingAddress.id)
    }

    @Test
    fun shouldDismissDialog() {
        activity = getShippingModeActivity()
        activity.showContent(shippingAddress to addressList)
        activity.onDeleteButtonClicked(billingAddress)

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick()

        assertFalse(dialog.isShowing)
        verify(activity.presenter, never()).deleteAddress(billingAddress.id)
    }

    @Test
    fun shouldDeleteAddressWithoutDialog() {
        activity = getShippingModeActivity()
        activity.showContent(shippingAddress to addressList)
        activity.onDeleteButtonClicked(address)
        assertNull(ShadowAlertDialog.getLatestAlertDialog())
        verify(activity.presenter).deleteAddress(address.id)
    }

    @Test
    fun shouldShowEditAlertDialog() {
        activity = getBillingModeActivity()
        activity.onEditButtonClicked(shippingAddress)
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowAlertDialog = shadowOf(dialog)

        assertEquals(context.getString(R.string.edit_address_message), shadowAlertDialog.message)
        assertEquals(context.getString(R.string.edit), dialog.getButton(AlertDialog.BUTTON_POSITIVE).text.toString())
    }

    @Test
    fun shouldShowCheckoutAddressActivityWithoutDialog() {
        activity = getShippingModeActivity()
        activity.showContent(shippingAddress to addressList)
        activity.onEditButtonClicked(address)

        assertNull(ShadowAlertDialog.getLatestAlertDialog())


        verify(activity.router).showCheckoutAddressForResult(activity, RequestCode.EDIT_SHIPPING_ADDRESS, address, false)
    }

    @Test
    fun shouldShowCheckoutAddressActivity() {
        activity = getShippingModeActivity()
        activity.showContent(address to addressList)
        activity.onEditButtonClicked(shippingAddress)

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        assertNotNull(dialog)

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()

        verify(activity.router).showCheckoutAddressForResult(activity, RequestCode.EDIT_SHIPPING_ADDRESS, shippingAddress, true)
    }

    @Test
    fun shouldReloadDataWhenAddressAdded() {
        activity = getShippingModeActivity()
        val shadowActivity = shadowOf(activity)

        activity.startActivityForResult(
            CheckoutAddressActivity.getStartIntent(context, address, true),
            RequestCode.ADD_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, Intent())

        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
        verify(activity.presenter, times(2)).getAddressList()
    }

    @Test
    fun shouldReloadDataWhenAddressEdited() {
        activity = getShippingModeActivity()
        val shadowActivity = shadowOf(activity)

        activity.startActivityForResult(
            CheckoutAddressActivity.getStartIntent(context, address, true),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, Intent())

        verify(activity.presenter, times(2)).getAddressList()
    }

    @Test
    fun shouldSelectAddressWhenAddressEdited() {
        activity = getShippingModeActivity()
        val shadowActivity = shadowOf(activity)

        activity.startActivityForResult(
            CheckoutAddressActivity.getStartIntent(context, address, true),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        val resultIntent = Intent()
        resultIntent.putExtra(Extra.ADDRESS, address)
        resultIntent.putExtra(Extra.IS_SELECTED_ADDRESS, true)
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, resultIntent)

        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
        verify(activity.presenter).setShippingAddress(MockInstantiator.DEFAULT_ID, address)
        verify(activity.presenter, times(2)).getAddressList()

    }

    @Test
    fun shouldSetResultWhenAddressEdited() {
        activity = getBillingModeActivity()
        val shadowActivity = shadowOf(activity)

        activity.startActivityForResult(
            CheckoutAddressActivity.getStartIntent(context, address, true),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        val resultIntent = Intent()
        resultIntent.putExtra(Extra.ADDRESS, address)
        resultIntent.putExtra(Extra.IS_SELECTED_ADDRESS, true)

        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, resultIntent)

        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertEquals(address, resultIntent.extras.getParcelable(Extra.ADDRESS))
        assertTrue(resultIntent.extras.getBoolean(Extra.IS_SELECTED_ADDRESS))

        verify(activity.presenter, times(2)).getAddressList()
    }

    @Test
    fun shouldSetDefaultAddressAsSelected() {
        activity = getShippingModeActivity()
        activity.showContent(address to addressList)
        activity.onAddressSelected(shippingAddress)
        activity.onDeleteButtonClicked(shippingAddress)

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()

        val adapter = activity.recyclerView.adapter as CheckoutAddressListAdapter
        assertEquals(address, adapter.selectedAddress)
    }

    private fun getShippingModeActivity(): CheckoutAddressListActivity {
        val intent = CheckoutAddressListActivity.getStartIntent(context,
            MockInstantiator.DEFAULT_ID,
            shippingAddress,
            true,
            shippingAddress,
            billingAddress)

        return Robolectric.buildActivity(CheckoutAddressListActivity::class.java, intent).create().start().resume().visible().get()
    }

    private fun getBillingModeActivity(): CheckoutAddressListActivity {
        val intent = CheckoutAddressListActivity.getStartIntent(context,
            MockInstantiator.DEFAULT_ID,
            shippingAddress,
            false,
            shippingAddress,
            billingAddress)

        return Robolectric.buildActivity(CheckoutAddressListActivity::class.java, intent).create().start().resume().visible().get()
    }

}