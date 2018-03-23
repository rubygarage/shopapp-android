package com.shopapp.ui.checkout.payment

import android.app.Activity
import android.content.Context
import com.shopapp.TestShopApplication
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.PaymentType
import kotlinx.android.synthetic.main.activity_payment.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.fakes.RoboMenuItem

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class PaymentActivityTest {

    private lateinit var context: Context
    private lateinit var activity: PaymentActivity

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = PaymentActivity.getStartIntent(context, null)
        activity = Robolectric.buildActivity(PaymentActivity::class.java, intent).create().start().resume().get()
    }

    @Test
    fun shouldRadioButtonUnCheckedByDefault() {
        assertFalse(activity.cardPayRadioButton.isChecked)
        assertFalse(activity.androidPayRadioButton.isChecked)
        assertFalse(activity.webPayRadioButton.isChecked)
    }

    @Test
    fun shouldRadioButtonCheckedWithPreSelectedData() {
        val intent = PaymentActivity.getStartIntent(context, PaymentType.CARD_PAYMENT)
        val activity = Robolectric.buildActivity(PaymentActivity::class.java, intent).create().start().resume().get()

        assertTrue(activity.cardPayRadioButton.isChecked)
        assertFalse(activity.androidPayRadioButton.isChecked)
        assertFalse(activity.webPayRadioButton.isChecked)
    }

    @Test
    fun shouldSetCardPaymentAsResultAndFinishActivityWhenPaymentChecked() {
        activity.onCheckedChanged(activity.cardPayRadioButton, true)

        val shadowActivity = shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent
        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertEquals(PaymentType.CARD_PAYMENT, resultIntent.extras.getSerializable(Extra.PAYMENT_TYPE))
        assertTrue(shadowActivity.isFinishing)
    }

    @Test
    fun shouldSetAndroidPaymentAsResultAndFinishActivityWhenPaymentChecked() {
        activity.onCheckedChanged(activity.androidPayRadioButton, true)

        val shadowActivity = shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent
        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertEquals(PaymentType.ANDROID_PAYMENT, resultIntent.extras.getSerializable(Extra.PAYMENT_TYPE))
        assertTrue(shadowActivity.isFinishing)
    }

    @Test
    fun shouldSetWebPaymentAsResultAndFinishActivityWhenPaymentChecked() {
        activity.onCheckedChanged(activity.webPayRadioButton, true)

        val shadowActivity = shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent
        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertEquals(PaymentType.WEB_PAYMENT, resultIntent.extras.getSerializable(Extra.PAYMENT_TYPE))
        assertTrue(shadowActivity.isFinishing)
    }

    @Test
    fun shouldCheckAndroidPayment() {
        val intent = PaymentActivity.getStartIntent(context, PaymentType.ANDROID_PAYMENT)
        val activity = Robolectric.buildActivity(PaymentActivity::class.java, intent).create().start().resume().get()
        assertTrue(activity.androidPayRadioButton.isChecked)
    }

    @Test
    fun shouldCheckWebPayment() {
        val intent = PaymentActivity.getStartIntent(context, PaymentType.WEB_PAYMENT)
        val activity = Robolectric.buildActivity(PaymentActivity::class.java, intent).create().start().resume().get()
        assertTrue(activity.webPayRadioButton.isChecked)
    }

    @Test
    fun shouldFinishActivity() {
        val item = RoboMenuItem(android.R.id.home)
        assertTrue(activity.onOptionsItemSelected(item))
        assertTrue(activity.isFinishing)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}