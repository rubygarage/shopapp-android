package com.shopapp.ui.base.browser

import android.graphics.Bitmap
import android.support.v4.widget.SwipeRefreshLayout
import android.webkit.WebView
import android.webkit.WebViewClient

class LoadingWebViewClient(private val refreshLayout: SwipeRefreshLayout) : WebViewClient() {

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        refreshLayout.isRefreshing = true
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        refreshLayout.isRefreshing = false
    }
}