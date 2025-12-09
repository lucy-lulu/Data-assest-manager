package com.quoll.da;

import com.quoll.da.controller.AssetController;
import com.quoll.da.mapper.AssetMapper;
import com.quoll.da.pojo.Asset;
import com.quoll.da.service.AssetService;
import com.quoll.da.service.impl.AssetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssetCollectTest {

    private MockMvc mockMvc;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetController assetController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assetController).build();
    }

    // Test case 1: Teacher collects valid asset
    @Test
    void testCollectAsset_Success() throws Exception {
        when(assetService.collectAssetAsTeacher("1", "1")).thenReturn("Asset collected successfully");

        ResultActions resultActions = mockMvc.perform(post("/asset/collect")
                .param("teacherId", "1")
                .param("assetId", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.msg").value("success"));

        // Verify that the service method was called exactly once
        verify(assetService, times(1)).collectAssetAsTeacher("1", "1");
    }

    // Test case 2: Teacher tries to collect an already collected asset
    @Test
    void testCollectAsset_AlreadyCollected() throws Exception {
        when(assetService.collectAssetAsTeacher("1", "1")).thenReturn("The asset has already been collected");

        ResultActions resultActions = mockMvc.perform(post("/asset/collect")
                .param("teacherId", "1")
                .param("assetId", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(0));
        resultActions.andExpect(jsonPath("$.msg").value("The asset has already been collected by the teacher."));

        verify(assetService, times(1)).collectAssetAsTeacher("1", "1");
    }

    // Test case 3: Teacher tries to collect a non-existent asset
    @Test
    void testCollectAsset_NonExistentAsset() throws Exception {
        when(assetService.collectAssetAsTeacher("1", "-1")).thenReturn("Cannot add non-existent asset");

        ResultActions resultActions = mockMvc.perform(post("/asset/collect")
                .param("teacherId", "1")
                .param("assetId", "-1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(0));
        resultActions.andExpect(jsonPath("$.msg").value("The asset does not exist or assetId is missing."));

        verify(assetService, times(1)).collectAssetAsTeacher("1", "-1");
    }

    // Test case 4: Teacher collects asset with invalid assetId
    @Test
    void testCollectAsset_InvalidAssetId() throws Exception {
        when(assetService.collectAssetAsTeacher("1", "invalid!@#")).thenReturn("Cannot add non-existent asset");

        ResultActions resultActions = mockMvc.perform(post("/asset/collect")
                .param("teacherId", "1")
                .param("assetId", "invalid!@#")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(0));
        resultActions.andExpect(jsonPath("$.msg").value("The asset does not exist or assetId is missing."));

        verify(assetService, times(1)).collectAssetAsTeacher("1", "invalid!@#");
    }

    // Test case 5: Teacher collects asset with missing assetId
    @Test
    void testCollectAsset_MissingAssetId() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/asset/collect")
                .param("teacherId", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isBadRequest());
    }

    // Test case 6: Teacher collects asset with missing teacherId
    @Test
    void testCollectAsset_MissingTeacherId() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/asset/collect")
                .param("assetId", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isBadRequest());
    }

    // Test case 7: Teacher requests collected assets but no assets are found
    @Test
    void testGetCollectedAssets_NoAssetsFound() throws Exception {
        when(assetService.getCollectedAssetsByTeacher("2", 1, 10)).thenReturn(new ArrayList<>());
        when(assetService.countCollectedAssetsByTeacher("2")).thenReturn(0);

        ResultActions resultActions = mockMvc.perform(get("/asset/collectedAssets/2")
                .param("page", "1")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isNoContent());

        verify(assetService, times(1)).getCollectedAssetsByTeacher("2", 1, 10);
        verify(assetService, times(1)).countCollectedAssetsByTeacher("2");
    }

    // Test case 8: Teacher requests collected assets with missing teacherId
    @Test
    void testGetCollectedAssets_MissingTeacherId() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/asset/collectedAssets/")
                .param("page", "1")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isBadRequest());
    }

    // Test case 9: Teacher requests collected assets with default page and size
    @Test
    void testGetCollectedAssets_DefaultPageSize() throws Exception {
        List<Asset> mockAssets = new ArrayList<>();

        Asset asset1 = new Asset();
        asset1.setAssetId("1");
        mockAssets.add(asset1);

        Asset asset2 = new Asset();
        asset2.setAssetId("2");
        mockAssets.add(asset2);

        when(assetService.getCollectedAssetsByTeacher("1", 1, 10)).thenReturn(mockAssets);
        when(assetService.countCollectedAssetsByTeacher("1")).thenReturn(2);

        ResultActions resultActions = mockMvc.perform(get("/asset/collectedAssets/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.collectedAssets").isArray());
        resultActions.andExpect(jsonPath("$.collectedAssets[0].assetId").value("1"));
        resultActions.andExpect(jsonPath("$.collectedAssets[1].assetId").value("2"));
        resultActions.andExpect(jsonPath("$.currentPage").value(1));
        resultActions.andExpect(jsonPath("$.totalItems").value(2));
        resultActions.andExpect(jsonPath("$.totalPages").value(1));

        verify(assetService, times(1)).getCollectedAssetsByTeacher("1", 1, 10);
        verify(assetService, times(1)).countCollectedAssetsByTeacher("1");
    }
}
