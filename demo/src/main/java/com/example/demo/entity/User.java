package com.example.demo.entity;


import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RestController;


@Setter
@Getter
@Mapper
@RestController
public class User {

    public int id;
    public String userName;
    public String userFace;
    public String userDevice;
    public String userPasswd;
    public String userLastTime;

}
