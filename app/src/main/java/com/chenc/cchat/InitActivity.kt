package com.chenc.cchat

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.chenc.cchat.databinding.ActivityInitBinding
import com.chenc.cchat.im.RecMesService
import com.chenc.cchat.im.helper.ConnectStatusListener
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
        val controller = WindowCompat.getInsetsController(window, binding.root)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.isAppearanceLightStatusBars = true
        supportActionBar?.hide()
    }
    private fun initUI() {
        val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.fillAfter = true
        animation.duration = 1500
        animation.repeatMode = Animation.RESTART
        animation.repeatCount = Animation.INFINITE
        animation.interpolator = LinearInterpolator()
        binding.initLoading.startAnimation(animation)
    }
    private fun initData() {
        // TODO 初始化必要组件等
        // initNetwork...
        val intent = Intent(this, RecMesService::class.java)
        startService(intent)
        bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                (service as RecMesService.RecMesBinder).setOnConnectListener(object : ConnectStatusListener {
                    override fun onSuccess() {
                        TODO("Not yet implemented")
                    }

                    override fun onError() {
                        TODO("Not yet implemented")
                    }

                })
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }

        }, BIND_AUTO_CREATE)
        // after init
        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(this@InitActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}