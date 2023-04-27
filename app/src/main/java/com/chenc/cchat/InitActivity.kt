package com.chenc.cchat

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        hideActionBar()
        init()
    }

    private fun hideActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.decorView.windowInsetsController
            controller?.hide(WindowInsets.Type.statusBars())
            controller?.hide(WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        supportActionBar?.hide()
    }

    fun init() {
        // TODO 初始化必要组件等
        // initNetwork...

        // after init
        lifecycleScope.launch {
            delay(3000)
            val intent = Intent(this@InitActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}