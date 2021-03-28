package com.hyvu.protobufdecoder

class ProtocolBuffer {
    companion object {
        private val protocolBufferParser = ProtocolBufferParser()

        fun parseData(isHexData: Boolean) {
            protocolBufferParser.generateStructure(isHexData)
        }

        fun setInputString(data: String) {
            protocolBufferParser.setInputString(data)
        }

        fun getInputString(): String {
            return protocolBufferParser.getInputString()
        }

        fun getParseString(): String {
            return protocolBufferParser.getParseString()
        }

        fun getListData(): ArrayList<DataEntity>? {
            return protocolBufferParser.getListStructureEntity()
        }
    }
}