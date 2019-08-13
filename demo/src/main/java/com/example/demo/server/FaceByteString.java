package com.example.demo.server;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RestController;


/*
* 人脸特征通过字符写入
* */
@Mapper
@RestController
public class FaceByteString {
    /*
     * byte[]转二进制字符串
     * */
    public String conver2HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(Long.toString(b[i] & 0xff, 2) + ",");
        }
        return result.toString().substring(0, result.length() - 1);
    }

    /*
     * 二进制字符串转换为byte数组,每个字节以","隔开
     **/
    public byte[] conver2HexToByte(String hex2Str) {
        String[] temp = hex2Str.split(",");
        byte[] b = new byte[temp.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Long.valueOf(temp[i], 2).byteValue();
        }
        return b;
    }

}
