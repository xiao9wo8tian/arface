package com.example.demo.server;

import com.example.demo.entity.UserHttp;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Mapper
@RestController
public class FaceImgBase  {
    @Resource
    protected HttpServletRequest request;

    @Autowired
    private UserHttp userHttp;


    public UserHttp srcFacePath(String srcPath) throws IOException {

        InputStreamReader reader = new InputStreamReader(request.getInputStream(), "UTF-8");
        char[] buff = new char[1024];
        int length = 0;
        String inStream = "";
        while ((length = reader.read(buff)) != -1) {
            String x = new String(buff, 0, length);
            inStream = inStream.concat(x);

        }

        inStream = "[" + inStream + "]";


        JSONArray jn = JSONArray.fromObject(inStream.toString());


        JSONObject jo = (JSONObject) jn.get(0);
        String userName = (String) jo.get("userName");
        String userFace = (String) jo.get("userFace");
        String userDevice = (String) jo.get("userDevice");
        String userPasswd = (String) jo.get("userPasswd");
        String userLastTime = (String) jo.get("userLastTime");
        userFace = userFace.substring(userFace.indexOf(",") + 1);


        BASE64Decoder decoderIn = new BASE64Decoder();
        byte[] inDetect = decoderIn.decodeBuffer(userFace);
        OutputStream in = new FileOutputStream(srcPath);
        in.write(inDetect);
        in.flush();

        userHttp.userName = userName;
        userHttp.userFace = userFace;
        userHttp.userDevice = userDevice;
        userHttp.userPath = srcPath;
        userHttp.userPasswd = userPasswd;
        userHttp.userLastTime = userLastTime;


        return userHttp;
    }



}