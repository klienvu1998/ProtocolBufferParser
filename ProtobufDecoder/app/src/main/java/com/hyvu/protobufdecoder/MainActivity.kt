package com.hyvu.protobufdecoder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.hyvu.protobufdecoder.utils.StorageUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_READ_STORAGE_RESULT = 1000
    }

    private var listStructure: ArrayList<DataEntity>? = ArrayList()
    private var storageUtils: StorageUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storageUtils = StorageUtils(applicationContext)
        tvInput.text = "Default data: ${ProtocolBuffer.getHexString()}"
        tvLog.text = ProtocolBuffer.getParseString()
        listStructure = ProtocolBuffer.getListData()

        btnLoadData.setOnClickListener {
            if (isReadStoragePermissionGranted()) {
                loadDataFromTxt()
            } else {
                checkReadStoragePermission()
            }
        }

        btnExportData.setOnClickListener {
            storageUtils?.writeToTxtFile(ProtocolBuffer.getParseString())
        }
    }

    private fun loadDataFromTxt() {
        storageUtils?.readDataFile("input")?.let { it1 -> ProtocolBuffer.setHexString(it1) }
        tvInput.text = ProtocolBuffer.getHexString()
        tvLog.text = ProtocolBuffer.getParseString()
        listStructure = ProtocolBuffer.getListData()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_READ_STORAGE_RESULT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadDataFromTxt()
            } else {
                Toast.makeText(this, "NEED PERMISSION", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkReadStoragePermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_READ_STORAGE_RESULT)
        }
    }

    private fun isReadStoragePermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

}