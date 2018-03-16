package com.shopapp.ui.checkout.payment.card

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.CardType
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Extra
import kotlinx.android.synthetic.main.activity_card.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.*
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
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CardActivityTest {

    private lateinit var context: Context
    private lateinit var activityController: ActivityController<CardActivity>

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = CardActivity.getStartIntent(context, null)
        activityController = Robolectric.buildActivity(CardActivity::class.java, intent)
    }

    @Test
    fun shouldSetCorrectTitle() {
        val activity = activityController.create().start().resume().visible().get()
        assertEquals(context.getString(R.string.credit_card), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldLoadAcceptedCardTypesWhenOnCreate() {
        val activity = activityController.create().start().resume().visible().get()
        verify(activity.presenter).getAcceptedCardTypes()
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
        assertFalse(activity.submitButton.isEnabled)
    }

    @Test
    fun shouldShowAcceptedCardTypes() {
        val activity = activityController.create().start().resume().visible().get()
        assertEquals(View.GONE, activity.visa.visibility)
        assertEquals(View.GONE, activity.masterCard.visibility)
        assertEquals(View.GONE, activity.amex.visibility)
        assertEquals(View.GONE, activity.discover.visibility)
        assertEquals(View.GONE, activity.dc.visibility)
        assertEquals(View.GONE, activity.jcb.visibility)

        activity.showContent(listOf(CardType.VISA, CardType.MASTER_CARD, CardType.AMERICAN_EXPRESS,
            CardType.DISCOVER, CardType.DINERS_CLUB, CardType.JCB))

        assertEquals(View.VISIBLE, activity.visa.visibility)
        assertEquals(View.VISIBLE, activity.masterCard.visibility)
        assertEquals(View.VISIBLE, activity.amex.visibility)
        assertEquals(View.VISIBLE, activity.discover.visibility)
        assertEquals(View.VISIBLE, activity.dc.visibility)
        assertEquals(View.VISIBLE, activity.jcb.visibility)
    }

    @Test
    fun shouldSetResultAndFinishActivityWhenCardTokenReceived() {
        val activity = activityController.create().start().resume().visible().get()
        val card = MockInstantiator.newCard()
        val token = "token"
        val testData = Pair(card, token)
        activity.cardTokenReceived(testData)

        val shadowActivity = shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent
        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertEquals(card, resultIntent.extras.getParcelable(Extra.CARD))
        assertEquals(token, resultIntent.extras.getString(Extra.CARD_TOKEN))
        assertTrue(shadowActivity.isFinishing)
    }

    @Test
    fun shouldRequestTokenWhenCardValidationPassed() {
        val activity = activityController.create().start().resume().visible().get()
        val card = MockInstantiator.newCard()
        activity.cardPassValidation(card)

        verify(activity.presenter).getToken(card)
    }

    @Test
    fun shouldFinishActivityWhenCardValidationPassed() {
        val card = MockInstantiator.newCard()
        val intent = CardActivity.getStartIntent(context, card)
        val activity = Robolectric.buildActivity(CardActivity::class.java, intent).create().start().get()
        activity.cardPassValidation(card)

        assertTrue(shadowOf(activity).isFinishing)
    }

    @Test
    fun shouldPreFillInputFields() {
        val card = MockInstantiator.newCard()
        val intent = CardActivity.getStartIntent(context, card)
        val activity = Robolectric.buildActivity(CardActivity::class.java, intent).create().start().resume().get()

        assertEquals(activity.getString(R.string.full_name_pattern, card.firstName, card.lastName),
            activity.holderNameInput.text.toString())
        assertEquals(card.cardNumber, activity.cardNumberInput.text.toString())
        assertEquals(card.expireMonth, activity.monthInput.text.toString())
        assertEquals(card.expireYear, activity.yearInput.text.toString())
        assertEquals(card.verificationCode, activity.cvvInput.text.toString())
        assertTrue(activity.submitButton.isEnabled)
    }

    @Test
    fun shouldShowCardValidationError() {
        val activity = activityController.create().start().resume().visible().get()
        val errorRes = R.string.card_number_error
        activity.loadingView.visibility = View.VISIBLE
        activity.cardValidationError(errorRes)

        assertEquals(ShadowToast.getTextOfLatestToast(), context.getString(errorRes))
    }

    @Test
    fun shouldProcessCardDataWhenSubmitButtonClicked() {
        val card = MockInstantiator.newCard()
        val intent = CardActivity.getStartIntent(context, card)
        val activity = Robolectric.buildActivity(CardActivity::class.java, intent).create().start().resume().get()
        activity.submitButton.performClick()

        verify(activity.presenter).processCardData("${card.firstName} ${card.lastName}",
            card.cardNumber, card.expireMonth, card.expireYear, card.verificationCode)
    }

    @Test
    fun shouldShowMonthPickerWhenClickOnForm() {
        val activity = activityController.create().start().resume().visible().get()
        activity.monthInput.getOnClickListener()?.onClick(activity.monthInput)
        activity.supportFragmentManager.executePendingTransactions()

        val dialog = activity.supportFragmentManager.findFragmentByTag(DateBottomSheetPicker.DATE_TYPE_MONTH)
        assertNotNull(dialog)
        assertTrue(dialog.isVisible)
    }

    @Test
    fun shouldShowYearPickerWhenClickOnForm() {
        val activity = activityController.create().start().resume().visible().get()
        activity.yearInput.getOnClickListener()?.onClick(activity.yearInput)
        activity.supportFragmentManager.executePendingTransactions()

        val dialog = activity.supportFragmentManager.findFragmentByTag(DateBottomSheetPicker.DATE_TYPE_YEAR)
        assertNotNull(dialog)
        assertTrue(dialog.isVisible)
    }

    @Test
    fun shouldShowMonthPickerWhenClickOnNextKey() {
        val activity = activityController.create().start().resume().visible().get()
        activity.cardNumberInput.onEditorAction(EditorInfo.IME_ACTION_NEXT)

        val dialog = activity.supportFragmentManager.findFragmentByTag(DateBottomSheetPicker.DATE_TYPE_MONTH)
        assertNotNull(dialog)
        assertTrue(dialog.isVisible)
    }

    @Test
    fun shouldRemoveWatchers() {
        val activity = activityController.create().start().resume().visible().pause().get()
        assertEquals(1, shadowOf(activity.cardNumberInput).watchers.size)
        assertEquals(1, shadowOf(activity.holderNameInput).watchers.size)
        assertEquals(1, shadowOf(activity.monthInput).watchers.size)
        assertEquals(1, shadowOf(activity.yearInput).watchers.size)
        assertEquals(1, shadowOf(activity.cvvInput).watchers.size)
    }
}