package com.shopapp.ext

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.shopapp.TestShopApplication
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class FragmentExtensionTest {

    private lateinit var activity: TestActivity
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
        activity =
                Robolectric.buildActivity(TestActivity::class.java).create().start().resume().get()
    }

    @Test
    fun shouldReplaceFragmentOnlyOnce() {
        val first = TestFragment()
        val second = TestFragment()
        activity
            .supportFragmentManager
            .replaceOnce(android.R.id.content, TestFragment::javaClass.name, { first }, false)
        activity
            .supportFragmentManager
            .replaceOnce(android.R.id.content, TestFragment::javaClass.name, { second }, false)

        assertEquals(1, activity.supportFragmentManager.fragments.size)
        assertEquals(first, activity.supportFragmentManager.fragments[0])
    }

    @Test
    fun shouldAddFragmentToBackStack() {
        activity
            .supportFragmentManager
            .replaceOnce(android.R.id.content, TestFragment::javaClass.name, { TestFragment() })
        assertEquals(1, activity.supportFragmentManager.backStackEntryCount)
    }

    @Test
    fun shouldReplaceFragmentByTag() {

        val first = TestFragment()
        val second = TestFragment()
        val third = TestFragment()
        val tag = "tag"
        val secondTag = "someTag"

        activity
            .supportFragmentManager
            .replaceByTag(android.R.id.content, tag, { first })

        assertEquals(first, activity.supportFragmentManager.fragments[0])

        activity
            .supportFragmentManager
            .replaceByTag(android.R.id.content, secondTag, { second })

        assertEquals(second, activity.supportFragmentManager.fragments[0])

        activity
            .supportFragmentManager
            .replaceByTag(android.R.id.content, tag, { third })

        assertEquals(first, activity.supportFragmentManager.fragments[0])
    }

    @Test
    fun shouldReplaceFragmentByTagIfNoAddToBackStack() {

        val first = TestFragment()
        val second = TestFragment()
        val third = TestFragment()
        val tag = "tag"
        val secondTag = "someTag"

        activity
            .supportFragmentManager
            .replaceByTag(android.R.id.content, tag, { first }, false)

        assertEquals(first, activity.supportFragmentManager.fragments[0])

        activity
            .supportFragmentManager
            .replaceByTag(android.R.id.content, secondTag, { second }, false)

        assertEquals(second, activity.supportFragmentManager.fragments[0])

        activity
            .supportFragmentManager
            .replaceByTag(android.R.id.content, tag, { third }, false)

        assertNotEquals(first, activity.supportFragmentManager.fragments[0])
        assertEquals(third, activity.supportFragmentManager.fragments[0])
    }

    private class TestActivity : AppCompatActivity()

    class TestFragment : Fragment()
}