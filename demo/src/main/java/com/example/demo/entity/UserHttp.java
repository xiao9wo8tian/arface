package com.example.demo.entity;


import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RestController;

@Setter
@Getter
@Mapper
@RestController
public class UserHttp {
    public String userName;
    public String userPasswd;
    public String userDevice;
    public String userLastTime;
    public String userFace;
    public String userPath;

}
