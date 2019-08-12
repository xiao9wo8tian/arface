package com.example.demo.controller;

import com.example.demo.dao.FaceMapper;
import com.example.demo.entity.User;
import com.example.demo.entity.UserHttp;
import com.example.demo.server.FaceByteString;
import com.example.demo.server.FaceImgBase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;


@Setter
@Getter
@Component
@RestController
@RequestMapping("/user")
@ConfigurationProperties(prefix = "path")
public class FaceController {


    private String directorName;

    @Autowired
    private FaceMapper faceMapper;

    @Autowired
    private FaceArcDetect faceArcDetect;

    @Autowired
    private User user;

    @Autowired
    private FaceByteString faceByteString;

    @Autowired
    private FaceImgBase faceImgBase;

    @Autowired
    private  UserHttp userHttp;

    @Resource
    protected HttpServletRequest request;






    /*
     * 后端接受数据入口
     * 查询人脸
     *
     * */
    @RequestMapping(value = {"/"}, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Boolean faceSelect() throws IOException, SQLException {


//      保存图片路径
        String srcPath = directorName + "/src.jpg";

        UserHttp hp  = faceImgBase.srcFacePath(srcPath);


        String hpName = hp.userName;
        String hpPasswd = hp.userPasswd;
        String hpDevice = hp.userDevice;
        String srcpath = hp.userPath;
        String hpLastTime = hp.userLastTime;

        long startTime = System.currentTimeMillis();   //获取开始时间
        byte[] srcByte = faceArcDetect.faceEngineGetByte(srcpath);

        long endTime = System.currentTimeMillis();   //获取开始时间

        System.out.println("时间:" + (endTime-startTime));
        String srcByteTOstring = faceByteString.conver2HexStr(srcByte);

        int counts = faceMapper.selectUserCounts();

        int index = -1;
        String pName = "";

        if (counts == 0) {
//          初始化用户人脸库
            index = -1 ;
            faceAdd(hpName,srcByteTOstring,hpDevice,hpPasswd,hpLastTime);

        } else if (counts > 0) {
            for (int i = 1; i <= counts; i++) {

                String dstString = faceMapper.selectUserFaceById(i);

                byte[] dstByte = faceByteString.conver2HexToByte(dstString);

                float score = faceDectect(srcByte,dstByte);


                if (score > 0.80) {
                    User person = faceMapper.selectUserById(i);
                    pName = person.userName;
                    index = 1;
                    break;
                } else if (i == counts) {
                    index = -1;
                }

            }
        } else {
            System.out.println("数据库操作错误,请查看！");
        }


        if (index == -1) {
            faceAdd(hpName,srcByteTOstring,hpDevice,hpPasswd,hpLastTime);
            return false;
        }else {
            return true;
        }


    }
    /*
     * 人脸特征比对
     * output score
     *
     * */

    public float faceDectect(byte[] src,byte[] dst) throws IOException, SQLException {

        float score = faceArcDetect.faceEngineTest(src, dst);
        return score;
    }

    /*
     * 添加用户
     *
     * */
    public void faceAdd(String name, String faceString ,String device ,String passwd ,String time) {
        faceMapper.addUser(name,faceString,device,passwd,time);

    }





















/*

    @RequestMapping(value={"/deleteUser"}, method=RequestMethod.GET)
    public String deleteUser(String id){
        if(faceMapper.deleteUser(Integer.parseInt(id))){
            return "删除成功！";
        };
        return id;
    }
*/



}

