package com.shopapp.ui.blog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Article
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.blog.contract.ArticlePresenter
import com.shopapp.ui.blog.contract.ArticleView
import com.shopapp.util.HtmlUtil
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import javax.inject.Inject


class ArticleActivity :
    BaseLceActivity<Pair<Article, String>, ArticleView, ArticlePresenter>(),
    ArticleView {

    @Inject
    lateinit var articlePresenter: ArticlePresenter

    private var shareUrl: String? = null

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
        private const val FRAME_HEIGHT_MULTIPLIER = 0.66

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
        lceLayout.errorView.errorTarget = getString(R.string.article)

        loadData()
        setupWebView()
    }

    //SETUP

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(content.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachBlogComponent().inject(this)
    }

    override fun getContentView(): Int = R.layout.activity_article

    override fun createPresenter(): ArticlePresenter = articlePresenter

    //LCE

    override fun showContent(data: Pair<Article, String>) {
        super.showContent(data)
        val article = data.first
        val baseUrl = data.second
        shareUrl = article.url
        articleTitle.text = article.title

        content.post {
            val width = (content.width / Resources.getSystem().displayMetrics.density).toInt()
            var html = HtmlUtil.fitHtmlImages(article.contentHTML)
            html = HtmlUtil.fitHtmlFrames(html, (width * FRAME_HEIGHT_MULTIPLIER).toInt())
            content.loadDataWithBaseURL(baseUrl, html, "text/html", "UTF-8", null)
        }

        author.text = article.author.fullName
        val src = article.image?.src
        image.setImageURI(src)
        image.visibility = if (src != null) View.VISIBLE else View.GONE
    }

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadArticles(intent.getStringExtra(EXTRA_ARTICLE_ID))
    }

}
