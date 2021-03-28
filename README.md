# ProtocolBufferParser

#USE

ProtocolBuffer.setInputString(StorageUtils().readDataFile("input"))   // set string data for decoding, which get from input.txt file on phone storage (/storage/emulated/0/input.txt)

ProtocolBuffer.getDataString()  // get current data for decoding

ProtocolBuffer.getParseString()  // get result after decoding

ProtocolBuffer.getListData()  // get list object after decoding

ProtocolBuffer.parseData()  // decoding input data

#UI

EXPORT TXT (Button): export result after parsing to the app specific storage (/storage/emulated/0/Android/data/"app-name"/files/ProtobufOutput/)

LOAD DATA TXT (Button): load hex string from txt file that saved in the phone storage

PARSE (Button): decoding data

Binary (RadioButton): check if input is binary string

Hex (RadioButton): check if input is hex string

![image](https://user-images.githubusercontent.com/41892926/112756595-3059cc80-9010-11eb-8d4e-84f5493c1170.png)


#FLOWCHART

![image](https://user-images.githubusercontent.com/41892926/112756797-fe953580-9010-11eb-87a2-2a674d60d1bc.png)

