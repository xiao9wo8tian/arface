package com.example.demo.controller;


import com.example.demo.dao.FaceMapper;
import com.example.demo.entity.UserHttp;
import com.example.demo.server.FaceByteString;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.*;


@Getter
@Setter
@RestController
@RequestMapping("/user")
@ConfigurationProperties(prefix = "path")
public  class  FaceController {

    @Autowired
    private FaceArcDetect faceArcDetect;
    @Autowired
    private FaceMapper faceMapper;
    @Autowired
    private FaceByteString faceByteString;

    public  String directorName;

    /*
    * param UserHttp实体属性
    * 后台接收数据入口
    * 查询功能
    * */
    @PostMapping("/face/search")
    public boolean faceSearch(@RequestBody UserHttp param) throws IOException {
        String srcPath = baseToimg(param);
        if(faceDetect(srcPath)){
            return true;
        }else {
            return false;
        }
    }


    /*
    * 人脸添加
    * */
    @PostMapping("/face/add")
    public boolean faceAdd(@RequestBody UserHttp param) throws IOException {
        String srcPath = baseToimg(param);
        byte[] srcData =  faceArcDetect.faceEngineGetByte(srcPath);
        String srcDatatoString = faceByteString.conver2HexStr(srcData);
        boolean bool = faceMapper.addUser(param.userName,srcDatatoString,param.userDevice,param.userPasswd,param.userLastTime);
        return bool;
    }

    /*
    * 人脸删除
    * */
    @PostMapping("/face/delete")
    public boolean faceDelete(@RequestBody UserHttp param){
        int id = Integer.parseInt(param.userId);
        boolean bool = faceMapper.deleteUser(id);
        return bool;
    }

    /*
    * 人脸解析识别
    * */
    public boolean faceDetect(String src) throws UnsupportedEncodingException {
        byte[] srcByte =  faceArcDetect.faceEngineGetByte(src);
        int counts;
        int index=-1;
        counts = faceMapper.selectUserCounts();
        if(counts == 0){
            index=-1;
        }else {
            for(int i = 1; i <= counts; i++){
                String dstString = faceMapper.selectUserFaceById(i);
                byte[] dstByte = faceByteString.conver2HexToByte(dstString);
                float score = faceArcDetect.faceEngineTest(srcByte,dstByte);
                if(score >=0.8){
                    index=1;
                    break;
                }else if(i==counts) {
                    index=-1;
                }
            }
        }
        if(index==-1){
            return false;
        }else {
            return true;
        }
    }

    /*
     * base64转img
     * */
    public String baseToimg(UserHttp param) throws IOException {
        String srcPath = directorName + "/src.jpg";
        String userFace = param.userFace;
        userFace = userFace.replaceAll("data:image/jpeg;base64,","");
        BASE64Decoder decoderIn = new BASE64Decoder();
        byte[] inDetect = decoderIn.decodeBuffer(userFace);
        OutputStream in = new FileOutputStream(srcPath);
        in.write(inDetect);
        in.flush();
        in.close();

        return srcPath;
    }

}