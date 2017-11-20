package com.client.shop.ui.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.client.shop.R
import com.client.shop.ext.shareText
import com.domain.entity.Article
import kotlinx.android.synthetic.main.activity_article.*


class ArticleActivity : AppCompatActivity() {

    private lateinit var article: Article

    companion object {
        private const val EXTRA_ARTICLE = "extra_article"

        fun getStartIntent(context: Context, article: Article): Intent {
            val intent = Intent(context, ArticleActivity::class.java)
            intent.putExtra(EXTRA_ARTICLE, article)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        setSupportActionBar(toolbar)

        article = intent.getParcelableExtra(EXTRA_ARTICLE)
        articleTitle.text = article.title
        content.text = article.content
        author.text = article.author.fullName
        val src = article.image?.src
        image.setImageURI(src)
        image.visibility = if (src != null) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return when (item.itemId) {
                R.id.share -> {
                    shareText(article.url, "Share")
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}