package com.chenc.cchat

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.chenc.cchat.databinding.ActivityInitBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class InitActivity : AppCompatActivity() {

    private lateinit var binding : ActivityInitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()
        initUI()
        initData()
    }

    private fun hideActionBar() {
        val controller = ViewCompat.getWindowInsetsController(binding.root)
        controller?.hide(WindowInsetsCompat.Type.statusBars())
        controller?.hide(WindowInsetsCompat.Type.navigationBars())
        supportActionBar?.hide()
    }
    private fun initUI() {
        val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.fillAfter = true
        animation.duration = 2000
        animation.repeatMode = Animation.RESTART
        animation.repeatCount = Animation.INFINITE
        animation.interpolator = LinearInterpolator()
        binding.initLoading.startAnimation(animation)
    }
    private fun initData() {
        // TODO 初始化必要组件等
        // initNetwork...

        // after init
        lifecycleScope.launch {
            delay(5000)
            val intent = Intent(this@InitActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}