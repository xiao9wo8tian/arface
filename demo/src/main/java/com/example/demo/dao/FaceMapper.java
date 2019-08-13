package com.example.demo.dao;


import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FaceMapper {
    @Select("select * from user where id = #{id}")
    public User selectUserById(int id);

    @Select("select count(id) from user ")
    public Integer selectUserCounts();


    @Select("select userFace from user where id = #{id}")
    public String selectUserFaceById(int id);

    @Select("select userName from user where id = #{id}")
    public String selectUserNameById(int id);

    @Select("select * from user where userName = #{userName}")
    public List<User> selectUserByName(String userName);

    @Insert("insert into user(userName,userFace,userDevice,userPasswd,userLastTime) " +
            "values (#{userName},#{userFace},#{userDevice},#{userPasswd},#{userLastTime})")
    public boolean addUser(@Param("userName") String userName,
                           @Param("userFace") String userFace,
                           @Param("userDevice") String userDevice,
                           @Param("userPasswd") String userPasswd,
                           @Param("userLastTime") String userLastTime);

    @Update("update user set userName=#{userName},userFace=#{userFace} where id=#{id}")
    public void updateUser(User user);

    @Delete("delete from user where id=#{id}")
    public boolean deleteUser(int id);



}
