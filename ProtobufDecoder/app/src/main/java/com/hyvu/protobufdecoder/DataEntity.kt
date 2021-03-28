package com.hyvu.protobufdecoder

class DataEntity() {

    var fieldNumber: Int? = null
    var wireType: String? = null
    var value: ValueEntity? = null

}

class ValueEntity() {
    var value: Int? = null
    var message: String? = null
    var startIndex: Int? = null
    var innerObject: ArrayList<DataEntity>? = null

}