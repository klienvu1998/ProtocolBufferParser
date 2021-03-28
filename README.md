# ProtocolBufferParser

#USE

ProtocolBuffer.setHexString(StorageUtils().readDataFile("input"))   // set hex string data, which get from input.txt file on phone storage (/storage/emulated/0/input.txt)

ProtocolBuffer.getHexString()  // get hex data, which is using for parsing

ProtocolBuffer.getParseString()  // get result after parsing

ProtocolBuffer.getListData()  // get list object after parsing

#UI

EXPORT TXT (Button): export result after parsing to the app specific storage (/storage/emulated/0/Android/data/"app-name"/files/ProtobufOutput/)

LOAD DATA TXT + PARSE (Button): load hex string from txt file that saved in the phone storage and auto parse data.

#FLOWCHART
