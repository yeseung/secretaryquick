package com.gongdaeoppa.secretaryquick

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        val pageUrl = "https://퀵서비스비서퀵.com"

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            val intent = MainActivity.newIntent(this, pageUrl)
            startActivity(intent)
            finish()
        }
        handler.postDelayed(runnable, 1500)

        val imageView: ImageView = findViewById(R.id.imageView)

        imageView.setOnClickListener {
            handler.removeCallbacks(runnable)
            val intent = MainActivity.newIntent(this, pageUrl)
            startActivity(intent)
            finish()
        }
    }
}