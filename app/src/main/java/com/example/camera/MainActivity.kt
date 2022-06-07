package com.example.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.innfinity.permissionflow.lib.requestPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var txv: TextView
    lateinit var btn: Button
    lateinit var img: ImageView
    lateinit var btnTel:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txv = findViewById(R.id.txv)
        txv.text = "您尚未允許拍照權限，無法使用相機功能。如果您選擇永久拒絕，需要到設定->應用程式，選擇App手動允許相機權限。"
        btn = findViewById(R.id.btn)
        btn.visibility = View.GONE
        img = findViewById(R.id.img)
        img.visibility = View.GONE

        GlobalScope.launch(Dispatchers.Main) {
//            requestPermissions(Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE,
//                Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE)
                .collect { permissions ->
                    val allGranted = permissions.find { !it.isGranted } == null
                    txv.visibility = View.GONE
                    btn.visibility = View.VISIBLE
                }
        }

        btn.setOnClickListener {
            takePictureResult.launch(null)
        }

        btnTel = findViewById(R.id.btnTel)
        btnTel.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL)
            val u = Uri.parse("tel:0982803129")
            intent.setData(u)
            startActivity(intent)
        }
    }

    private val takePictureResult =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            img.setImageBitmap(bitmap)
            img.visibility = View.VISIBLE
        }


}