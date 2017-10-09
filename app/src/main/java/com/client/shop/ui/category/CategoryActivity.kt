package com.client.shop.ui.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.PaginationActivity
import com.client.shop.ui.base.ui.ProductAdapter
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.category.contract.CategoryPresenter
import com.client.shop.ui.category.contract.CategoryView
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.modal.SortBottomSheet
import com.shopapicore.entity.Category
import com.shopapicore.entity.Product
import com.shopapicore.entity.SortType
import javax.inject.Inject

class CategoryActivity : PaginationActivity<Product, CategoryView, CategoryPresenter, CategoryViewState>(),
        CategoryView {

    @Inject lateinit var categoryPresenter: CategoryPresenter
    private lateinit var category: Category
    private var selectedSortType: SortType = SortType.NAME

    companion object {
        private const val PER_PAGE = 10
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

        fun getStartIntent(context: Context, category: Category): Intent {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra(EXTRA_CATEGORY, category)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = intent.getParcelableExtra(EXTRA_CATEGORY)
        supportActionBar?.let {
            it.title = category.title
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun inject(component: AppComponent) {
        component.inject(this)
    }

    override fun isGrid() = true

    override fun initAdapter() = ProductAdapter(dataList, this)

    override fun createPresenter() = categoryPresenter

    override fun createViewState() = CategoryViewState()

    override fun onNewViewStateInstance() {
        fetchData()
    }

    override fun fetchData() {
        presenter.loadProductList(PER_PAGE, paginationValue, category.id, selectedSortType)
    }

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(DetailsActivity.getStartIntent(this, data))
    }

    override fun productListLoaded(productList: List<Product>?) {
        val size = productList?.size ?: 0
        if (productList != null && size > 0) {
            paginationValue = productList.last().paginationValue
            this.dataList.addAll(productList)
            adapter.notifyDataSetChanged()
        }
    }
}
