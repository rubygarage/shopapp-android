package com.shopapp.ui.address.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.times
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Error
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.const.Extra
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.bottom_sheet_picker.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import kotlinx.android.synthetic.main.view_lce_error.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BaseAddressActivityTest {

    private lateinit var context: Context
    private lateinit var activity: TestBaseAddressActivity
    private lateinit var address: Address

    @Before
    fun setUpTest() {
        address = MockInstantiator.newAddress()
        context = RuntimeEnvironment.application.baseContext
        activity = Robolectric.buildActivity(TestBaseAddressActivity::class.java).create().resume().get()
    }

    @Test
    fun shouldLoadDataWhenOnCreate() {
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
        assertEquals(ContextCompat.getDrawable(context, R.color.colorBackgroundLightTranslucent), activity.lceLayout.loadingView.background)
        verify(activity.presenter).getCountriesList()
    }

    @Test
    fun shouldSetupPickersWhenCountriesLoaded() {
        val size = 5
        val countries = MockInstantiator.newList(MockInstantiator.newCountry(), size)
        activity.countriesLoaded(countries)

        activity.countryInput.getOnClickListener()?.onClick(activity.countryInput)
        val dialog = activity.supportFragmentManager.findFragmentByTag(CountryBottomSheetPicker::class.java.name) as? CountryBottomSheetPicker
        assertNotNull(dialog)
        assertEquals(size, dialog!!.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldFillFieldsWhenShowContent() {
        activity.showContent(address)

        assertEquals(address.firstName, activity.firstNameInput.text.toString())
        assertEquals(address.lastName, activity.lastNameInput.text.toString())
        assertEquals(address.address, activity.addressInput.text.toString())
        assertEquals(address.secondAddress, activity.secondAddressInput.text.toString())
        assertEquals(address.city, activity.cityInput.text.toString())
        assertEquals(address.state, activity.stateInput.text.toString())
        assertEquals(address.country, activity.countryInput.text.toString())
        assertEquals(address.zip, activity.postalCodeInput.text.toString())
        assertEquals(address.phone, activity.phoneInput.text.toString())
    }

    @Test
    fun shouldSetResultAndFinishWhenAddressChanged() {
        activity.addressChanged(address)
        val shadowActivity = shadowOf(activity)

        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertTrue(shadowActivity.isFinishing)
    }

    @Test
    fun shouldSetupNewAddressModeByDefault() {
        assertEquals(context.getString(R.string.add_new_address), activity.toolbar.toolbarTitle.text)
        assertEquals(context.getString(R.string.submit), activity.submitButton.text)
    }

    @Test
    fun shouldSetupEditModeWhenAddressIsInExtra() {
        val intent = Intent(context, TestBaseAddressActivity::class.java)
        intent.putExtra(Extra.ADDRESS, address)
        val activity = Robolectric.buildActivity(TestBaseAddressActivity::class.java, intent).create().resume().get()

        assertEquals(context.getString(R.string.edit_address), activity.toolbar.toolbarTitle.text)
        assertEquals(context.getString(R.string.edit), activity.submitButton.text)

        assertEquals(address.firstName, activity.firstNameInput.text.toString())
        assertEquals(address.lastName, activity.lastNameInput.text.toString())
        assertEquals(address.address, activity.addressInput.text.toString())
        assertEquals(address.secondAddress, activity.secondAddressInput.text.toString())
        assertEquals(address.city, activity.cityInput.text.toString())
        assertEquals(address.state, activity.stateInput.text.toString())
        assertEquals(address.country, activity.countryInput.text.toString())
        assertEquals(address.zip, activity.postalCodeInput.text.toString())
        assertEquals(address.phone, activity.phoneInput.text.toString())
    }

    @Test
    fun shouldSubmitButtonBeEnabledWhenAllFieldsFilled() {
        assertFalse(activity.submitButton.isEnabled)
        activity.showContent(address)
        assertTrue(activity.submitButton.isEnabled)
    }

    @Test
    fun shouldSubmitButtonBeDisabledWhenNotAllFieldsFilled() {
        assertFalse(activity.submitButton.isEnabled)
        activity.showContent(address)
        activity.firstNameInput.setText("")
        assertFalse(activity.submitButton.isEnabled)
    }

    @Test
    fun shouldRemoveWatchersOnPause() {
        val activity = Robolectric
            .buildActivity(TestBaseAddressActivity::class.java)
            .create()
            .resume()
            .pause()
            .get()

        assertEquals(1, shadowOf(activity.firstNameInput).watchers.size)
        assertEquals(1, shadowOf(activity.lastNameInput).watchers.size)
        assertEquals(1, shadowOf(activity.cityInput).watchers.size)
        assertEquals(1, shadowOf(activity.postalCodeInput).watchers.size)
        assertEquals(1, shadowOf(activity.phoneInput).watchers.size)
        assertEquals(1, shadowOf(activity.countryInput).watchers.size)
        assertEquals(1, shadowOf(activity.stateInput).watchers.size)
    }

    @Test
    fun shouldReloadCountries() {
        val intent = Intent(context, TestBaseAddressActivity::class.java)
        intent.putExtra(Extra.ADDRESS, address)
        val activity = Robolectric.buildActivity(TestBaseAddressActivity::class.java, intent).create().resume().get()
        activity.changeState(LceLayout.LceState.ErrorState(Error.Content()))
        activity.tryAgainButton.performClick()

        verify(activity.presenter, times(2)).getCountriesList()
    }

    @Test
    fun shouldOpenStatePicker() {
        val intent = Intent(context, TestBaseAddressActivity::class.java)
        intent.putExtra(Extra.ADDRESS, address)
        val activity = Robolectric.buildActivity(TestBaseAddressActivity::class.java, intent).create().resume().get()

        activity.countriesLoaded(listOf(MockInstantiator.newCountry()))
        activity.stateInput.getOnClickListener()?.onClick(activity.stateInput)
        val tag = StateBottomSheetPicker::class.java.name
        assertNotNull(activity.supportFragmentManager.findFragmentByTag(tag))
    }

    @Test
    fun shouldHideStateViewOnNullStates() {
        val intent = Intent(context, TestBaseAddressActivity::class.java)
        intent.putExtra(Extra.ADDRESS, address)
        val activity = Robolectric.buildActivity(TestBaseAddressActivity::class.java, intent).create().resume().get()

        val country = MockInstantiator.newCountry()
        given(country.states).willReturn(null)

        activity.countriesLoaded(listOf(country))

        assertEquals(View.GONE, activity.stateInputContainer.visibility)
    }

    @Test
    fun shouldHideStateViewOnEmptyStates() {
        val country = MockInstantiator.newCountry()
        given(country.states).willReturn(listOf())
        val intent = Intent(context, TestBaseAddressActivity::class.java)
        intent.putExtra(Extra.ADDRESS, address)
        val activity = Robolectric.buildActivity(TestBaseAddressActivity::class.java, intent).create().resume().get()

        activity.countriesLoaded(listOf(country))
        assertEquals(View.GONE, activity.stateInputContainer.visibility)
    }

    @Test
    fun shouldShowContentState() {
        val intent = Intent(context, TestBaseAddressActivity::class.java)
        intent.putExtra(Extra.ADDRESS, address)
        val activity = Robolectric.buildActivity(TestBaseAddressActivity::class.java, intent).create().resume().get()

        activity.changeState(LceLayout.LceState.ErrorState(Error.Content()))
        assertEquals(View.VISIBLE, activity.errorView.visibility)
        activity.submitAddressError()
        assertEquals(View.VISIBLE, activity.contentView.visibility)
        assertEquals(View.GONE, activity.errorView.visibility)
    }

    class TestBaseAddressActivity : BaseAddressActivity<AddressView, AddressPresenter<AddressView>>() {


        override fun inject() {
            TestShopApplication.testAppComponent.attachAddressComponent().inject(this)
        }
    }
}