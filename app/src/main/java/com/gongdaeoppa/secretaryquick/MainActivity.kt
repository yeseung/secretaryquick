package com.gongdaeoppa.secretaryquick

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    companion object {
        const val PAGE_URL = "pageUrl"
        const val MAX_PROGRESS = 100

        fun newIntent(context: Context, pageUrl: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(PAGE_URL, pageUrl)
            return intent
        }
    }

    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null
    private lateinit var pageUrl: String
    private var backKeyPressedTime: Long = 0

    //var webView: WebView = findViewById(R.id.webView)
    //var progressBar: ProgressBar = findViewById(R.id.progressBar)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // get pageUrl from String
        pageUrl = intent.getStringExtra(PAGE_URL)
                ?: throw IllegalStateException("field $PAGE_URL missing in Intent")



        initWebView()
        setWebClient()
        handlePullToRefresh()
        loadUrl(pageUrl)
    }



    private fun handlePullToRefresh() {}

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {

        webView = findViewById(R.id.webView)

        webView?.settings?.javaScriptEnabled = true
        //webView.settings.setSupportMultipleWindows(true)
        webView?.settings?.javaScriptCanOpenWindowsAutomatically = true
        webView?.settings?.loadWithOverviewMode = true
        webView?.settings?.useWideViewPort = true
        webView?.settings?.domStorageEnabled = true

        webView?.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                if (url.startsWith("tel:")) {
                    //tel:01000000000
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                    startActivity(intent)
                    return true
                } else if (url.startsWith("mailto:")) {
                    //mailto:ironnip@test.com
                    val i = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                    startActivity(i)
                    return true
                }
                return false
            }
        }


    }

    private fun setWebClient() {

        webView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                    view: WebView,
                    newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)

                progressBar = findViewById(R.id.progressBar)

                progressBar?.progress = newProgress
                if (newProgress < MAX_PROGRESS && progressBar?.visibility == ProgressBar.GONE) {
                    progressBar?.visibility = ProgressBar.VISIBLE
                }

                if (newProgress == MAX_PROGRESS) {
                    progressBar?.visibility = ProgressBar.GONE
                }
            }
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && webView!!.canGoBack()) {
            webView?.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, exit the activity)
        return super.onKeyDown(keyCode, event)
    }

    private fun loadUrl(pageUrl: String) {
        webView?.loadUrl(pageUrl)
    }


    override fun onBackPressed() {
        // 기존의 뒤로가기 버튼의 기능 제거
        // super.onBackPressed();

        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 2초 이내에 뒤로가기 버튼을 한번 더 클릭시 finish()(앱 종료)
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
        }
    }







}