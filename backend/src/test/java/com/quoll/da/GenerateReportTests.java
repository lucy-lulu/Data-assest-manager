package com.quoll.da;


import com.quoll.da.service.AssetService;
import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Feedback;
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

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class GenerateReportTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetService assetService;

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
    public void testGenerateAssetReport() {
    // Mock the data returned from the service layer
    List<Asset> topAssets = Arrays.asList(new Asset(), new Asset()); // Add relevant mock data here
    List<Feedback> inProgressFeedbacks = Arrays.asList(new Feedback(), new Feedback()); // Add relevant mock data here
    String mockFilePath = "/mock/path/to/report";

    // Mocking service layer methods
        Mockito.when(assetService.getTopAssets(2)).thenReturn(topAssets);
        Mockito.when(assetService.getInProgressFeedback()).thenReturn(inProgressFeedbacks);
        Mockito.when(assetService.generateAssetReport(topAssets, inProgressFeedbacks)).thenReturn(mockFilePath);

    // Perform the request to generate report
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/asset/generateReport")
                .header("token", jwt));

    // Assert the status and the content
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().string("/asset/downloadReport?filePath=" + mockFilePath));

    // Optionally print the response for debugging
        resultActions.andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()));
    }
}
