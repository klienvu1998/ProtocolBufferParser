package com.hyvu.protobufdecoder.utils

import java.nio.charset.Charset

class ProtocolBufferUtils {
    companion object {
        fun hexStringToByteArray(hexString: String): ByteArray {
            if (hexString.length % 2 == 1) {
                throw IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
            }
            val byteArray = ByteArray(hexString.length / 2)
            for (i in hexString.indices step 2) {
                byteArray[i/2] =
                    hexToByte(
                        hexString.substring(i, i + 2)
                    )
            }
            return byteArray
        }

        fun hexToByte(hexString: String): Byte {
            val firstDigit =
                toDigit(
                    hexString[0]
                )
            val secondDigit =
                toDigit(
                    hexString[1]
                )
            return ((firstDigit shl 4) + secondDigit).toByte()
        }

        fun toDigit(hexChar: Char): Int {
            val digit = Character.digit(hexChar, 16)
            if (digit == -1) {
                throw IllegalArgumentException(
                    "Invalid Hex Character: $hexChar"
                )
            }
            return digit
        }

        fun getBit(value: Byte, position: Int): Int {
            return (value.toInt() shr position) and 1
        }

        fun getFieldNumber(byte: Byte): Int {
            val bitSix =
                getBit(
                    byte,
                    6
                )
            val bitFive =
                getBit(
                    byte,
                    5
                )
            val bitFour =
                getBit(
                    byte,
                    4
                )
            val bitThree =
                getBit(
                    byte,
                    3
                )
            val stringBinary = "$bitSix$bitFive$bitFour$bitThree"
            return stringBinary.toInt(2)
        }

        fun getWireType(byte: Byte): Int {
            val bitTwo =
                getBit(
                    byte,
                    2
                )
            val bitOne =
                getBit(
                    byte,
                    1
                )
            val bitZero =
                getBit(
                    byte,
                    0
                )
            val stringBinary = "$bitTwo$bitOne$bitZero"
            return stringBinary.toInt(2)
        }

        fun getWireTypeString(wireType: Int): String {
            when (wireType) {
                0 -> return "Varint"
                1 -> return "64-bit"
                2 -> return "Length-delimited"
                3 -> return "Start group"
                4 -> return "End group"
                5 -> return "32-bit"
            }
            return ""
        }

        fun getWireTypeInt(wireTypeString: String): Int {
            when (wireTypeString) {
                "Varint" -> return 0
                "64-bit" -> return 1
                "Length-delimited" -> return 2
                "Start group" -> return 3
                "End group" -> return 4
                "32-bit" -> return 5
            }
            return -1
        }

        fun calculateValue(firstByte: Byte, secondByte: Byte): Int {
            var stringResult = ""
            for (i in 6 downTo 0) {
                stringResult += getBit(
                    secondByte,
                    i
                )
            }
            for (i in 6 downTo 0) {
                stringResult += getBit(
                    firstByte,
                    i
                )
            }

            return stringResult.toInt(2)
        }
    }
}