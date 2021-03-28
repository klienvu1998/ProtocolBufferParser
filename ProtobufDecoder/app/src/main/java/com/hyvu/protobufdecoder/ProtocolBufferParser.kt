package com.hyvu.protobufdecoder

import com.hyvu.protobufdecoder.utils.ProtocolBufferUtils

class ProtocolBufferParser {

    companion object {
        const val VARINT = 0
        const val BIT64 = 1
        const val LENGTH_DELIMITED = 2
        const val START_GROUP = 3
        const val END_GROUP = 4
        const val BIT32 = 5
    }

    private var hexString = "08E50F12145772617070657220436C61737320537472696E" +
            "671A1608960112114E65737420436C61737320537472696E" +
            "67" + "08E50F12145772617070657220436C61737320537472696E" +
            "671A1608960112114E65737420436C61737320537472696E" +
            "67"
    private var listData: ArrayList<DataEntity>? = null
    private val output = StringBuilder()
    private var depthNestClass = 0

    fun setHexString(data: String) {
        hexString = data
    }

    fun getHexString(): String {
        return hexString
    }

    fun generateStructure() {
        listData = ArrayList()
        val byteArray = ProtocolBufferUtils.hexStringToByteArray(hexString)
        var index = 0
        while (index < byteArray.size) {
            index = generateStructureObject(byteArray, index, DataEntity(), listData!!)
        }
        checkNestClass(byteArray, listData!!)
    }

    fun getParseString(): String {
        return exportLog(listData!!, depthNestClass)
    }

    fun getListStructureEntity(): ArrayList<DataEntity>? {
        if (listData != null) {
            return listData
        }
        return null
    }

    private fun checkNestClass(byteArray: ByteArray, listDataEntity: ArrayList<DataEntity>) {
        for (structure in listDataEntity) {
            if (structure.wireType?.let { ProtocolBufferUtils.getWireTypeInt(it) } == LENGTH_DELIMITED) {
                if (structure.value?.message == "Wrapper Class String") {

                } else {
                    if (structure.value?.innerObject == null) {
                        structure.value?.innerObject = ArrayList()
                    }
                    var dataEntity: DataEntity?
                    var index = structure.value?.startIndex!!
                    while (index < (structure.value?.message?.length!! + structure.value?.startIndex!!)) {
                        dataEntity = DataEntity()
                        index = generateStructureObject(byteArray, index, dataEntity, structure.value?.innerObject!!)
                    }
                    checkNestClass(byteArray, structure.value?.innerObject!!)
                }
            }
        }
    }

    private fun generateStructureObject(byteArray: ByteArray, index: Int, dataEntity: DataEntity, listDataEntity: ArrayList<DataEntity>): Int {
        dataEntity.value = ValueEntity()
        var i = index
        val oldIndex = i + 2
        if (i >= byteArray.size) {
            return i
        }
        val currentByte = byteArray[i]
        var value: Int? = null
        val utf8String = StringBuilder()

        val mbs = ProtocolBufferUtils.getBit(currentByte, 0)
        val fieldNumber = ProtocolBufferUtils.getFieldNumber(currentByte)
        val wireType = ProtocolBufferUtils.getWireType(currentByte)

        val nextByte = byteArray[++i]
        // Varint
        if (wireType == VARINT) {
            val secondByte = byteArray[++i]
            value = ProtocolBufferUtils.calculateValue(nextByte, secondByte)
        }
        // Length-delimited
        else if (wireType == LENGTH_DELIMITED) {
            val lengthData = nextByte.toInt()
            i++
            val stopLength = i + lengthData
            while (i < stopLength) {
                val char = byteArray[i].toChar()
                utf8String.append(char)
                i++
            }
            i--
            dataEntity.value?.message = utf8String.toString()
        } else {
            return byteArray.size
        }
        dataEntity.wireType = ProtocolBufferUtils.getWireTypeString(wireType)
        dataEntity.fieldNumber = fieldNumber
        if (value != null) {
            dataEntity.value?.value = value
        }
        if (utf8String.toString().isNotEmpty()) {
            dataEntity.value?.message = utf8String.toString()
            dataEntity.value?.startIndex = oldIndex
        }
        listDataEntity.add(dataEntity)
        return ++i
    }

    fun exportLog(listDataEntity: ArrayList<DataEntity>, depth: Int): String {
        output.clear()
        return formatLog(listDataEntity, depth)
    }

    private fun formatLog(listDataEntity: ArrayList<DataEntity>, depth: Int): String {
        depthNestClass = 0
        var field = 1
        var space = ""
        for (i in 0..depth) {
            space += "\t"
        }
        listDataEntity.forEach { structure ->
            output.append("$space+ Field $field:\n")
            output.append("$space\t+ Field Number: ${structure.fieldNumber}\n")
            if (structure.value != null) {
                if (structure.value?.value != null) {
                    output.append("$space\t+ Value: ${structure.value?.value}\n")
                } else {
                    output.append("$space\t+ Value:\n")
                    output.append("$space\t\t+ Cast as a UTF8 String with ${structure.value?.message?.length} bytes length:\n")
                    output.append("$space\t\t\t${structure.value?.message}\n")

                    if (structure.value?.innerObject != null) {
                        if (structure.value?.innerObject!!.isNotEmpty()) {
                            output.append("$space\t\tCast as a sub-object:\n")
                            depthNestClass += 5
                            formatLog(structure.value?.innerObject!!, depthNestClass)
                        } else {
                            output.append("$space\t\tCast as a sub-object: N/A\n")
                        }
                    } else {
                        output.append("$space\t\tCast as a sub-object: N/A\n")
                    }
                }
            }
            field++
        }
        return output.toString()
    }

}