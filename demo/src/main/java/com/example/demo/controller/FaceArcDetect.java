package com.example.demo.controller;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.ImageFormat;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



@Mapper
@RestController
public class FaceArcDetect {



    public float faceEngineTest(byte[] src,byte[] dst) throws UnsupportedEncodingException {

//        ImageInfo imageInfo = getRGBData(new File(src));


        FaceEngine faceEngine = new FaceEngine();

        EngineConfiguration engineConfiguration = EngineConfiguration.builder().functionConfiguration(
                FunctionConfiguration.builder()
                        .supportAge(false)
                        .supportFace3dAngle(false)
                        .supportFaceDetect(true)
                        .supportFaceRecognition(true)
                        .supportGender(false)
                        .build()).build();
        //初始化引擎
        faceEngine.init(engineConfiguration);

        //人脸检测
/*
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);


        //提取人脸特征
        FaceFeature faceFeature = new FaceFeature();
        faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);


*/


        FaceFeature faceFeature = new FaceFeature();
//        faceEngine.extractFaceFeature(imageInfo2.getRgbData(), imageInfo2.getWidth(), imageInfo2.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature2);


        faceFeature.setFeatureData(src);

       FaceFeature faceFeature2 = new FaceFeature();
//        faceEngine.extractFaceFeature(imageInfo2.getRgbData(), imageInfo2.getWidth(), imageInfo2.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature2);


        faceFeature2.setFeatureData(dst);


/*
        //人脸对比
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
*/





      FaceSimilar faceSimilar = new FaceSimilar();
        int r =  faceEngine.compareFaceFeature(faceFeature, faceFeature2, faceSimilar);


        float score = faceSimilar.getScore();


/*
        int processResult = faceEngine.process(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList, FunctionConfiguration.builder().supportAge(true).supportFace3dAngle(true).supportGender(true).build());
        //性别提取
        List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
        int genderCode = faceEngine.getGender(genderInfoList);
        //年龄提取
        List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
        int ageCode = faceEngine.getAge(ageInfoList);
        //3D信息提取
        List<Face3DAngle> face3DAngleList = new ArrayList<Face3DAngle>();
        int face3dCode = faceEngine.getFace3DAngle(face3DAngleList);*/


        faceEngine.unInit();


        return score;


    }

    public ImageInfo getRGBData(File file) {
        if (file == null)
            return null;
        ImageInfo imageInfo;
        try {
            //将图片文件加载到内存缓冲区
            BufferedImage image = ImageIO.read(file);
            imageInfo = bufferedImage2ImageInfo(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageInfo;
    }

    public byte[] faceEngineGetByte(String img) {

        FaceArcDetect.ImageInfo imageInfo = getRGBData(new File(img));
//        ImageInfo imageInfo2 = getRGBData(new File(dst));

        FaceEngine faceEngine = new FaceEngine();

        EngineConfiguration engineConfiguration = EngineConfiguration.builder().functionConfiguration(
                FunctionConfiguration.builder()
                        .supportAge(false)
                        .supportFace3dAngle(false)
                        .supportFaceDetect(true)
                        .supportFaceRecognition(true)
                        .supportGender(false)
                        .build()).build();
        //初始化引擎
        faceEngine.init(engineConfiguration);

        //人脸检测
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);


        //提取人脸特征
        FaceFeature faceFeature = new FaceFeature();
        faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);

        byte[] charactorByte = faceFeature.getFeatureData();


        return charactorByte;
    }
    private ImageInfo bufferedImage2ImageInfo(BufferedImage image) {
        ImageInfo imageInfo = new ImageInfo();
        int width = image.getWidth();
        int height = image.getHeight();
        // 使图片居中
        width = width & (~3);
        height = height & (~3);
        imageInfo.width = width;
        imageInfo.height = height;
        //根据原图片信息新建一个图片缓冲区
        BufferedImage resultImage = new BufferedImage(width, height, image.getType());
        //得到原图的rgb像素矩阵
        int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
        //将像素矩阵 绘制到新的图片缓冲区中
        resultImage.setRGB(0, 0, width, height, rgb, 0, width);
        //进行数据格式化为可用数据
        BufferedImage dstImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        if (resultImage.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
            ColorConvertOp colorConvertOp = new ColorConvertOp(cs, dstImage.createGraphics().getRenderingHints());
            colorConvertOp.filter(resultImage, dstImage);
        } else {
            dstImage = resultImage;
        }

        //获取rgb数据
        imageInfo.rgbData = ((DataBufferByte) (dstImage.getRaster().getDataBuffer())).getData();
        return imageInfo;
    }


    static class ImageInfo {
        public byte[] rgbData;
        public int width;
        public int height;

        public byte[] getRgbData() {
            return rgbData;
        }

        public void setRgbData(byte[] rgbData) {
            this.rgbData = rgbData;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

}



