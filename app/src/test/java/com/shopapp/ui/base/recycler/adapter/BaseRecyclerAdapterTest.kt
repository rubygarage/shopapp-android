package com.shopapp.ui.base.recycler.adapter

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BaseRecyclerAdapterTest {

    private lateinit var context: Context
    private lateinit var dataList: List<Any>
    private lateinit var adapter: TestBaseRecyclerAdapter

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        dataList = listOf("", "", "")
        adapter = TestBaseRecyclerAdapter(dataList)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(dataList.size + 2, adapter.itemCount)
    }

    @Test
    fun shouldReturnDefaultViewHolder() {
        val parent = FrameLayout(context)
        val viewHolder = adapter.createViewHolder(parent, adapter.getItemViewType(1))
        assertTrue(viewHolder is BaseRecyclerAdapter.DefaultViewHolder)
        assertNotNull(viewHolder.itemView)
    }

    @Test
    fun shouldReturnHeaderViewHolder() {
        val parent = FrameLayout(context)
        val viewHolder = adapter.createViewHolder(parent, adapter.getItemViewType(0))
        assertTrue(viewHolder is BaseRecyclerAdapter.HeaderViewHolder)
        assertNotNull(viewHolder.itemView)
    }

    @Test
    fun shouldReturnFooterViewHolder() {
        val parent = FrameLayout(context)
        val viewHolder = adapter.createViewHolder(parent, adapter.getItemViewType(dataList.size + 1))
        assertTrue(viewHolder is BaseRecyclerAdapter.FooterViewHolder)
        assertNotNull(viewHolder.itemView)
    }

    @Test
    fun shouldBindFooter() {
        val viewHolder = adapter.createViewHolder(LinearLayout(context), 300)
        adapter.onBindViewHolder(viewHolder, dataList.size + 1)
        verify(adapter.bindFooterCallListener).onCall()
    }


    private class TestBaseRecyclerAdapter(dataList: List<Any>) : BaseRecyclerAdapter<Any>(dataList) {

        val bindFooterCallListener: CallListener = mock()

        init {
            withHeader = true
            withFooter = true
        }

        override fun getItemView(context: Context, viewType: Int): View = mock()

        override fun getHeaderView(context: Context): View = mock()

        override fun getFooterView(context: Context): View = mock()

        override fun bindData(itemView: View, data: Any, position: Int) {

        }

        override fun bindFooterData(itemView: View, position: Int) {
            super.bindFooterData(itemView, position)
            bindFooterCallListener.onCall()
        }
    }

    private interface CallListener {
        fun onCall()
    }
}