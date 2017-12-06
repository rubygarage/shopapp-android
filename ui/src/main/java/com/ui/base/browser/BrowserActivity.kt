package com.ui.base.browser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ui.R
import kotlinx.android.synthetic.main.activity_browser.*

class BrowserActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {

        private const val URL = "url"
        private const val TITLE = "title"

        fun getStartIntent(context: Context, url: String, title: String): Intent {
            val intent = Intent(context, BrowserActivity::class.java)
            intent.putExtra(URL, url)
            intent.putExtra(TITLE, title)
            return intent
        }
    }

    //ANDROID

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        setSupportActionBar(toolbar)
        toolbar.setTitle(intent.getStringExtra(TITLE))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeResources(R.color.colorAccent)
        webView.webViewClient = LoadingWebViewClient(refreshLayout)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(intent.getStringExtra(URL))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //CALLBACK

    override fun onRefresh() {
        webView.reload()
    }
}