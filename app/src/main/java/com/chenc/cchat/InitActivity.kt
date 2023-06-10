package com.chenc.cchat

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
    private val TAG : String = "InitActivity"

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
        val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.fillAfter = true
        animation.duration = 1500
        animation.repeatMode = Animation.RESTART
        animation.repeatCount = Animation.INFINITE
        animation.interpolator = LinearInterpolator()
        binding.initLoading.startAnimation(animation)
    }
    private fun initData() {
        // TODO 初始化必要组件等
        // init RecService...
        val intent = Intent(this, RecMesService::class.java)
        startService(intent)
        bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                (service as RecMesService.RecMesBinder).setOnConnectListener(object : ConnectStatusListener {
                    override fun onSuccess() {
                        Log.i(TAG, "bindService success")
                        gotoMainPage()
                    }

                    override fun onError() {
                        Log.e(TAG, "bindService error")
                        gotoLoginPage()
                    }

                    override fun onNoNetwork() {
                        Log.e(TAG, "bindService error")
                        gotoLoginPage()
                    }
                })
            }
            override fun onServiceDisconnected(name: ComponentName?) {
                Log.i(TAG, "onServiceDisconnected")
            }
        }, BIND_AUTO_CREATE)
    }

    private fun gotoMainPage() {
        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(this@InitActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun gotoLoginPage() {

    }
}