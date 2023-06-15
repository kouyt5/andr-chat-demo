package com.chenc.cchat.utils

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chenc.cchat.R

class Permissions {

    companion object {
        fun reqPermission(context: AppCompatActivity, permission: String): Boolean {
            var res: Boolean = false
            if (ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                res = true
            } else if (context.shouldShowRequestPermissionRationale(permission)) {
                showReason(context)  // 需要权限的原因
            } else {
                context.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        res = true
                    } else {
                        res = false
                        showFailed(context)
                    }
                }.launch(permission)
            }
            return res
        }

        private fun showFailed(context: Context) {
            Toast.makeText(context, R.string.permission_fail_message, Toast.LENGTH_SHORT).show()
        }

        private fun showReason(context: Context) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.permission_reason_title)
                .setMessage(R.string.permission_reason_message)
                .setCancelable(true)
                .setPositiveButton(
                    R.string.permission_reason_button
                ) { dialog, which -> dialog?.dismiss() }.create().show()
        }
    }


}