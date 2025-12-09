package com.quoll.da.mapper;

import com.quoll.da.pojo.Teacher;
import com.quoll.da.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper 
public interface LoginMapper {
    @Select("select * from Teacher")
    List<Teacher> getTeacherInfo();

    @Select("select adminId as userId, username, passwordHash as password, email from Administrator where username = #{username}")
    User getAdmin(String username);

    @Select("select teacherId as userId, username, passwordHash as password, email from Teacher where username = #{username}")
    User getTeacher(String username);

    @Select("select subTeacherId as userId, username, passwordHash as password, email from SubTeacher where username = #{username}")
    User getSubTeacher(String username);
}
