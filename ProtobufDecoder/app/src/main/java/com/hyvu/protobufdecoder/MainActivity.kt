package com.hyvu.protobufdecoder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.hyvu.protobufdecoder.utils.StorageUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val PERMISSION_READ_STORAGE_RESULT = 1000
    }

    private var listStructure: ArrayList<DataEntity>? = ArrayList()
    private var storageUtils: StorageUtils? = null
    private var isHexData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storageUtils = StorageUtils(applicationContext)
        tvInput.text = "Default data: ${ProtocolBuffer.getInputString()}"
        listStructure = ProtocolBuffer.getListData()

        btnLoadData.setOnClickListener(this)
        btnExportData.setOnClickListener(this)
        btnParse.setOnClickListener(this)
        rbBinary.setOnClickListener(this)
        rbHex.setOnClickListener(this)
    }

    private fun loadDataFromTxt() {
        storageUtils?.readDataFile("input")?.let { it1 -> ProtocolBuffer.setInputString(it1) }
        tvInput.text = ProtocolBuffer.getInputString()
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

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btnExportData -> {
                storageUtils?.writeToTxtFile(ProtocolBuffer.getParseString())
            }
            R.id.btnLoadData -> {
                if (isReadStoragePermissionGranted()) {
                    loadDataFromTxt()
                } else {
                    checkReadStoragePermission()
                }
            }
            R.id.rbBinary -> {
                if (rbBinary.isChecked) {
                    rbHex.isChecked = false
                    isHexData = false
                }
            }
            R.id.rbHex -> {
                if (rbHex.isChecked) {
                    rbBinary.isChecked = false
                    isHexData = true
                }
            }
            R.id.btnParse -> {
                ProtocolBuffer.parseData(isHexData)
                val log = ProtocolBuffer.getParseString()
                if (log.isEmpty()) {
                    tvLog.text = getString(R.string.input_error)
                } else {
                    tvLog.text = log
                }

            }
        }
    }

}