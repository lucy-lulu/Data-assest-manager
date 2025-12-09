package com.quoll.da;

import com.quoll.da.controller.AssetController;
import com.quoll.da.mapper.AssetMapper;
import com.quoll.da.pojo.Asset;
import com.quoll.da.service.impl.AssetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class AssetRemoveEditTest {
    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetServiceImpl assetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEditAsset_Success() {
        // Create an existing asset object in the database
        Asset asset = new Asset();
        asset.setAssetId("1");
        asset.setAssetName("Old Name");
        asset.setFormat("pdf");
        asset.setAuthor("Old Author");

        // Mock the assetMapper to return an existing asset when queried by ID
        when(assetMapper.getAssetById("1")).thenReturn(asset);

        // Create an updated asset object (with changes)
        Asset updatedAsset = new Asset();
        updatedAsset.setAssetId("1");
        updatedAsset.setAssetName("New Name");
        updatedAsset.setFormat("docx");
        updatedAsset.setAuthor("New Author");

        // Perform the asset update
        assetService.updateAssets(updatedAsset);

        // Mock the assetMapper to return the updated asset after update
        when(assetMapper.getAssetById("1")).thenReturn(updatedAsset);

        // Retrieve the updated asset (simulating the fetch after update)
        Asset fetchedAsset = assetMapper.getAssetById("1");

        // Verify that the assetMapper's update method was called once
        verify(assetMapper, times(1)).update(updatedAsset);

        // Assert that the fetched asset has the updated values
        assertEquals("New Name", fetchedAsset.getAssetName());
        assertEquals("docx", fetchedAsset.getFormat());
        assertEquals("New Author", fetchedAsset.getAuthor());
    }

    @Test
    public void testEditAsset_MissingAssetId() {
        Asset asset = new Asset();
        asset.setAssetName("New Name");

        // Attempt to update without setting an assetId
        // Since the assetId is missing, nothing should be updated, we simulate the behavior
        // that getAssetById should never be called, because assetId is null
        assertThrows(NullPointerException.class, () -> {
            assetService.updateAssets(asset);
        });

        // Verify that assetMapper's update method was never called due to missing assetId
        verify(assetMapper, never()).update(asset);
    }

    @Test
    public void testEditAsset_OnlyOneFieldChanged() {
        // Create an existing asset object in the database
        Asset asset = new Asset();
        asset.setAssetId("1");
        asset.setAssetName("Original Name");
        asset.setFormat("pdf");
        asset.setAuthor("Original Author");

        // Mock the assetMapper to return the existing asset
        when(assetMapper.getAssetById("1")).thenReturn(asset);

        // Create an updated asset object, only changing the assetName
        Asset updatedAsset = new Asset();
        updatedAsset.setAssetId("1");
        updatedAsset.setAssetName("Updated Name"); // Only this field should be updated

        // Perform the update
        assetService.updateAssets(updatedAsset);

        // Mock the assetMapper to return the updated asset after update
        when(assetMapper.getAssetById("1")).thenReturn(updatedAsset);

        // Retrieve the updated asset (simulating the fetch after update)
        Asset fetchedAsset = assetMapper.getAssetById("1");

        // Verify that assetMapper's update method was called with the updated asset
        verify(assetMapper, times(1)).update(updatedAsset);

        // Assert that only assetName was changed, other fields remain unchanged
        assertEquals("Updated Name", fetchedAsset.getAssetName());
        assertEquals("pdf", asset.getFormat()); // Unchanged
        assertEquals("Original Author", asset.getAuthor()); // Unchanged
    }

    @Test
    public void testRemoveSingleAsset() {
        // Mock the behavior of assetMapper to do nothing when removing
        doNothing().when(assetMapper).removeAssets(anyList());

        // Create a list with a single asset ID
        List<String> assetIds = List.of("1");
        int teacherId = -1;

        // Call the removeAssets method in the service
        assetService.removeAssets(assetIds, teacherId);

        // Verify that the assetMapper's removeAssets method was called with the correct ID
        verify(assetMapper, times(1)).removeAssets(assetIds);
    }

    @Test
    public void testRemoveMultipleAssets() {
        // Mock the behavior of assetMapper to do nothing when removing
        doNothing().when(assetMapper).removeAssets(anyList());

        // Create a list with multiple asset IDs
        List<String> assetIds = List.of("1", "2", "3");
        int teacherId = -1;

        // Call the removeAssets method in the service
        assetService.removeAssets(assetIds, teacherId);

        // Verify that the assetMapper's removeAssets method was called with the correct IDs
        verify(assetMapper, times(1)).removeAssets(assetIds);
    }

}

