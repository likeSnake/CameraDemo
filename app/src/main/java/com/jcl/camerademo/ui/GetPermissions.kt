package com.jcl.camerademo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jcl.camerademo.R
import com.jcl.camerademo.Util.CheckPermissions
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.Toast
import android.content.Intent

class GetPermissions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_start)
        //CheckPermissions();
        val permissionList = CheckPermissions.CheckAllPermissions(this)
        if (!permissionList.isEmpty()) {
            val permissions = permissionList.toTypedArray()
            ActivityCompat.requestPermissions(this@GetPermissions, permissions, 1)
        } else {
            startMain()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty()) {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "拒绝权限无法使用", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                startMain()
            } else {
                Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> {}
        }
    }

    fun startMain() {
        val intent = Intent()
        intent.setClass(this@GetPermissions, MainCamera::class.java)
        startActivity(intent)
        finish()
    }
}