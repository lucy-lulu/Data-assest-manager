package com.quoll.da;

import com.quoll.da.pojo.Category;
import com.quoll.da.service.CategoryService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    String jwt;

    @BeforeEach
    public void setUp() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", "1");
        claims.put("username", "adminUser");
        jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "quoll")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .compact();
    }

    @SneakyThrows
    @Test
    public void testGetTeacherCategory() {
        Category category1 = new Category("1", "name1", "1", 1);
        Category category2 = new Category("2", "name2", "1", 2);
        List<Category> categoryList = List.of(category1, category2);
        Mockito.when(categoryService.getTeacherCategory("1")).thenReturn(categoryList);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/category/teacher?teacherId=1").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testGetTeacherCategory: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":1,\"msg\":\"success\",\"data\":[{\"categoryId\":\"1\",\"categoryName\":\"name1\",\"teacherId\":\"1\",\"total\":1},{\"categoryId\":\"2\",\"categoryName\":\"name2\",\"teacherId\":\"1\",\"total\":2}]}"));
    }

    @SneakyThrows
    @Test
    public void testAddTeacherCategory0() {
        Mockito.when(categoryService.checkName("1", "nameNew")).thenReturn(0);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/category/teacher/add?teacherId=1&categoryName=nameNew").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testAddTeacherCategory: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":1,\"msg\":\"success\",\"data\":null}"));
    }

    @SneakyThrows
    @Test
    public void testAddTeacherCategory1() {
        Mockito.when(categoryService.checkName("1", "nameExisted")).thenReturn(1);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/category/teacher/add?teacherId=1&categoryName=nameExisted").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testAddTeacherCategory: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":0,\"msg\":\"This name already exists\",\"data\":null}"));
    }

    @SneakyThrows
    @Test
    public void testRenameTeacherCategory0() {
        Mockito.when(categoryService.checkName("1", "nameNew")).thenReturn(0);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/category/teacher/rename?teacherId=1&categoryId=1&newName=nameNew").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testRenameTeacherCategory: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":1,\"msg\":\"success\",\"data\":null}"));
    }

    @SneakyThrows
    @Test
    public void testRenameTeacherCategory1() {
        Mockito.when(categoryService.checkName("1", "nameExisted")).thenReturn(1);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/category/teacher/rename?teacherId=1&categoryId=1&newName=nameExisted").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testRenameTeacherCategory: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":0,\"msg\":\"This name already exists\",\"data\":null}"));
    }

    @SneakyThrows
    @Test
    public void testDeleteTeacherCategory() {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/category/teacher/delete/1,2").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testDeleteTeacherCategory: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":1,\"msg\":\"success\",\"data\":null}"));
    }

    @SneakyThrows
    @Test
    public void testClassifyAsset0() {
        Mockito.when(categoryService.checkAsset("1", "1")).thenReturn(0);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/category/asset/classify?categoryId=1&assetId=1").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testClassifyAsset: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":1,\"msg\":\"success\",\"data\":null}"));
    }

    @SneakyThrows
    @Test
    public void testClassifyAsset1() {
        Mockito.when(categoryService.checkAsset("1", "1")).thenReturn(1);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/category/asset/classify?categoryId=1&assetId=1").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testClassifyAsset: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":0,\"msg\":\"This asset already exists\",\"data\":null}"));
    }

    @SneakyThrows
    @Test
    public void testRemoveAsset() {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/category/asset/remove/1,2?categoryId=1").header("token", jwt));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andDo(result -> System.out.println("testRemoveAsset: " + result.getResponse().getContentAsString()));
        resultActions.andExpect(content().json("{\"code\":1,\"msg\":\"success\",\"data\":null}"));
    }
}
