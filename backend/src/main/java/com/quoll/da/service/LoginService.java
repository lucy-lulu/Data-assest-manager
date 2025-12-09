package com.quoll.da.service;

import com.quoll.da.pojo.Teacher;
import com.quoll.da.pojo.User;

import java.util.List;

public interface LoginService {
    List<Teacher> getTeacher(); 

    User login(String username, String password, Integer role);
}
