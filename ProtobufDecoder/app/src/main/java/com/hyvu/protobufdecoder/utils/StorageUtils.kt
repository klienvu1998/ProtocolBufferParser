package com.hyvu.protobufdecoder.utils

import android.content.Context
import android.os.Environment
import java.io.*
import java.lang.Exception

class StorageUtils {

    private var context: Context? = null

    companion object {
        const val PROTOBUF_OUTPUT_DIR_NAME = "ProtobufOutput"
    }

    constructor(context: Context?) {
        this.context = context
    }

    fun getBaseAppStoragePath(): String {
        return context?.getExternalFilesDir(null)?.absolutePath ?: ""
    }

    fun writeToTxtFile(message: String) {
        try {
            var file = File(getBaseAppStoragePath(), PROTOBUF_OUTPUT_DIR_NAME)
            if (!file.exists()) {
                file.mkdir()
            }
            file = File(file, "output.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            val writer: FileOutputStream? = FileOutputStream(file, true)
            val outputStream = OutputStreamWriter(writer)
            if (writer != null) {
                val data = message + "\n"
                outputStream.append(data)
                outputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readDataFile(filename: String): String {
        var content = ""
        try {
            val file = File(Environment.getExternalStorageDirectory(), "${filename}.txt")
            val fileReader = FileReader(file)
            val bufferReader = BufferedReader(fileReader)
            content = bufferReader.readLine()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content
    }

}