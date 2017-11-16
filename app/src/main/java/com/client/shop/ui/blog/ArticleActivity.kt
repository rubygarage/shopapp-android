package com.client.shop.ui.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ext.shareText
import com.client.shop.ui.blog.contract.ArticlePresenter
import com.client.shop.ui.blog.contract.ArticleView
import com.client.shop.ui.blog.di.BlogModule
import com.domain.entity.Article
import com.ui.lce.BaseActivity
import kotlinx.android.synthetic.main.activity_article.*
import javax.inject.Inject


class ArticleActivity :
        BaseActivity<Article, ArticleView, ArticlePresenter>(),
        ArticleView {

    @Inject lateinit var articlePresenter: ArticlePresenter

    var shareUrl: String? = null
    var shareMenuItem: MenuItem? = null

    companion object {
        private const val EXTRA_ARTICLE_ID = "extra_article_id"

        fun getStartIntent(context: Context, articleId: String): Intent {
            val intent = Intent(context, ArticleActivity::class.java)
            intent.putExtra(EXTRA_ARTICLE_ID, articleId)
            return intent
        }
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadData(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_article, menu)
        shareMenuItem = menu.findItem(R.id.share)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return when (item.itemId) {
                R.id.share -> {
                    shareUrl?.let { shareText(it, "Share") }
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachBlogComponent(BlogModule()).inject(this)
    }

    override fun getContentView(): Int = R.layout.activity_article

    override fun createPresenter(): ArticlePresenter = articlePresenter

    //LCE

    override fun showContent(data: Article) {
        super.showContent(data)
        shareMenuItem?.isVisible = true
        shareUrl = data.url
        articleTitle.text = data.title
        content.text = data.content
        author.text = data.author.fullName
        val src = data.image?.src
        image.setImageURI(src)
        image.visibility = if (src != null) View.VISIBLE else View.GONE
    }

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadArticles(intent.getStringExtra(EXTRA_ARTICLE_ID))
    }
}