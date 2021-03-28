package com.hyvu.protobufdecoder

class ProtocolBuffer {
    companion object {
        private val protocolBufferParser = ProtocolBufferParser()

        init {
            protocolBufferParser.generateStructure()
        }

        fun setHexString(data: String) {
            protocolBufferParser.setHexString(data)
        }

        fun getHexString(): String {
            return protocolBufferParser.getHexString()
        }

        fun getParseString(): String {
            return protocolBufferParser.getParseString()
        }

        fun getListData(): ArrayList<DataEntity>? {
            return protocolBufferParser.getListStructureEntity()
        }
    }
}