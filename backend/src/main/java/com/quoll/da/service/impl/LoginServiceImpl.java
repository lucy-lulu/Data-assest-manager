package com.quoll.da.service.impl;

import com.quoll.da.mapper.LoginMapper;
import com.quoll.da.pojo.Teacher;
import com.quoll.da.pojo.User;
import com.quoll.da.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;

@Service 
public class LoginServiceImpl implements LoginService { 

    @Autowired
    private LoginMapper loginMapper; 

    @Override
    public List<Teacher> getTeacher() {
        // Retrieve the list of teachers from the database
        List<Teacher> teacherList = loginMapper.getTeacherInfo();
        System.out.println("get teacher: " + teacherList); 
        return teacherList; 
    }

    @Override
    public User login(String username, String password, Integer role) {
         // Hash the provided password for secure comparison
        password = hashString(password);
        System.out.println("Hashed password: " + password);

        // Check user role and retrieve the user information accordingly
        if (role == 1) {
            User user = loginMapper.getAdmin(username);
            return verifyUser(password, user);
        } else if (role == 2) {
            User user = loginMapper.getTeacher(username);
            return verifyUser(password, user);
        } else {
            User user = loginMapper.getSubTeacher(username);
            return verifyUser(password, user);
        }
    }

     // Method to verify the hashed password with the user data from the database
    private User verifyUser(String password, User user) {
        if (user != null) {
            if (user.getPassword().equals(password)) {
                user.setVerifier(0);
                return user;
            } else {
                user.setVerifier(2);
                return user;
            }
        } else {
            User emptyUser = new User();
            emptyUser.setVerifier(1);
            return emptyUser;
        }
    }

    // Utility method to hash the provided string (password) using SHA-256
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Hash error: " + e.toString());
        }
    }
}
