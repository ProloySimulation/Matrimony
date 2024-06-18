package com.kuhu.xosstech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.kuhu.xosstech.R

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView: WebView = findViewById(R.id.webView)

        // Retrieve URLs from intent extras
        val paymentUrl = intent.getStringExtra(EXTRA_URL)
        val successUrl = intent.getStringExtra(SUCCESS_URL)
        val userId = intent.getStringExtra(USER_ID)

        // Ensure URLs are not null
        if (paymentUrl != null && successUrl != null) {
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    // Check if the URL matches the success URL
                    if (url != null && url.startsWith(successUrl)) {
                        // Payment is successful, finish the activity
                        finish()
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
            // Load the payment URL
            if(userId!=null)
                webView.loadUrl(paymentUrl+userId)
            else
                webView.loadUrl(paymentUrl)
        } else {
            // Handle case where URLs are null
            Toast.makeText(this, "URLs not provided", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    companion object {
        const val EXTRA_URL = "extra_url"
        const val SUCCESS_URL = "success_url"
        const val USER_ID = "user_id"
    }
}