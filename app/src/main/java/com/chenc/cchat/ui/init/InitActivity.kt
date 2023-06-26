package com.chenc.cchat.ui.init

import android.Manifest
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
import com.chenc.cchat.MainActivity
import com.chenc.cchat.data.AccountInfo
import com.chenc.cchat.databinding.ActivityInitBinding
import com.chenc.cchat.im.RecMesService
import com.chenc.cchat.im.helper.ConnectStatusListener
import com.chenc.cchat.im.helper.IMStatusCode
import com.chenc.cchat.utils.Permissions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class InitActivity : AppCompatActivity() {
    private val TAG : String = "InitActivity"

    private lateinit var binding : ActivityInitBinding
    private var conn : ServiceConnection? = null
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
        AccountInfo.init()
        if (!Permissions.reqPermission(this, Manifest.permission.INTERNET)) {
            return
        }

        checkLogin()
    }

    private fun initService() {
        // init RecService...
        val intent = Intent(this, RecMesService::class.java)
        startService(intent)
        createServiceConnection()
        conn?.let { bindService(intent, it, BIND_AUTO_CREATE) }
    }

    private fun createServiceConnection() {
        conn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                (service as RecMesService.RecMesBinder).setOnConnectListener(object : ConnectStatusListener {
                    override fun onSuccess() {
                        Log.i(TAG, "bindService success")
                        gotoMainPage()
                    }

                    override fun onError(code: IMStatusCode) {
                        Log.e(TAG, "bindService error. code = $code")
                        when (code) {
                            IMStatusCode.TIME_OUT, IMStatusCode.INTERNET_ERROR -> gotoMainPage()
                            IMStatusCode.UN_LOGIN, IMStatusCode.UN_AUTHORIZED -> gotoLoginPage()
                            else -> {
                                gotoLoginPage()
                            }
                        }
                    }
                })
            }
            override fun onServiceDisconnected(name: ComponentName?) {
                Log.i(TAG, "onServiceDisconnected")
            }
        }
    }

    private fun checkLogin() {
        if (AccountInfo.userName == null) {
            gotoLoginPage()
        }



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
        // TODO
        Log.i(TAG, "gotoLoginPage")
        gotoMainPage()
    }

    override fun onDestroy() {
        super.onDestroy()
        conn?.let { unbindService(it) }
    }
}