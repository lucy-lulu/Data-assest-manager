package com.quoll.da;

import com.quoll.da.pojo.User;
import com.quoll.da.service.CategoryService;
import com.quoll.da.service.LoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @SneakyThrows
    @Test
    public void testUserLogin0() {
        User user = new User("1", "admin1", "password1", "admin1@email.com", 0);
        Mockito.when(loginService.login("admin1", "password1", 1)).thenReturn(user);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/login?username=admin1&password=password1&role=1"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testAddTeacherCategory: " + result.getResponse().getContentAsString()));
    }

    @SneakyThrows
    @Test
    public void testUserLogin1() {
        User user = new User(null, null, null, null, 1);
        Mockito.when(loginService.login("admin0", "password1", 1)).thenReturn(user);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/login?username=admin0&password=password1&role=1"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testAddTeacherCategory: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":0,\"msg\":\"Username does not exist\",\"data\":null}"));
    }

    @SneakyThrows
    @Test
    public void testUserLogin2() {
        User user = new User("1", "admin1", "password1", "admin1@email.com", 2);
        Mockito.when(loginService.login("admin1", "password2", 1)).thenReturn(user);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/login?username=admin1&password=password2&role=1"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testAddTeacherCategory: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":0,\"msg\":\"Password is incorrect\",\"data\":null}"));
    }
}

