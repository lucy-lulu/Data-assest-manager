package com.quoll.da;

import com.quoll.da.mapper.AssetMapper;
import com.quoll.da.pojo.Asset;
import com.quoll.da.service.impl.AssetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AssetUploadTest {

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetServiceImpl assetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case 1: Admin upload - valid input
    @Test
    public void testAdminUpload_Success() {
        Asset asset = new Asset();
        asset.setAssetName("Admin Asset");
        asset.setFormat("text");
        asset.setAuthor("Admin");

        //admin upload
        assetService.add(asset);
        // verify the asset was inserted into the Asset table
        verify(assetMapper, times(1)).add(asset);
        // ensure that no edit was made into the TeacherUploaded table
        verify(assetMapper, never()).addTeacherUploadedAsset(anyString(), anyString());
    }

    // Test case 2: Admin upload - missing file
    @Test
    public void testAdminUpload_MissingFile() {
        Asset asset = new Asset();
        asset.setAssetName("Admin Asset");
        asset.setFormat("text");
        asset.setAuthor("Admin");
        asset.setFile(null);  // No file attached

        assetService.add(asset);

        // verify the asset was inserted into the Asset table
        verify(assetMapper, times(1)).add(asset);
        // ensure that no edit was made into the TeacherUploaded table
        verify(assetMapper, never()).addTeacherUploadedAsset(anyString(), anyString());
        // validate that asset URL is handled properly (e.g., null)
        assertNull(asset.getUrl());
    }

    // Test case 3: Teacher upload - valid input
    @Test
    public void testTeacherUpload_Success() {
        Asset asset = new Asset();
        asset.setAssetName("Teacher Asset");
        asset.setFormat("image");
        asset.setAuthor("Teacher");
        String teacherId = "1";

        // return true for a valid teacher ID
        when(assetMapper.teacherExists(teacherId)).thenReturn(true);

        // teacher upload
        assetService.addAsTeacher(asset, teacherId);

        // verify the asset was inserted into the Asset table
        verify(assetMapper, times(1)).add(asset);
        // verify a record was inserted into the TeacherUploaded table
        verify(assetMapper, times(1)).addTeacherUploadedAsset(teacherId, asset.getAssetId());
    }

    // Test case 4: Teacher upload - null teacher ID
    @Test
    public void testTeacherUpload_InvalidTeacherId() {
        Asset asset = new Asset();
        asset.setAssetName("Teacher Asset");
        asset.setFormat("pdf");
        asset.setAuthor("Teacher");
        String invalidTeacherId = null;

        // Expect an exception when uploading with an invalid teacher ID
        assertThrows(RuntimeException.class, () -> assetService.addAsTeacher(asset, invalidTeacherId));

        // Ensure that no entry was made into the Asset table
        verify(assetMapper, never()).add(any(Asset.class));
    }

    // Test case 5: Teacher upload - not exist teacher ID
    @Test
    public void testTeacherUpload_NonExistentTeacherId() {
        Asset asset = new Asset();
        asset.setAssetName("Teacher Asset");
        asset.setFormat("pdf");
        asset.setAuthor("Teacher");
        String nonExistentTeacherId = "nonExistentTeacher";

        // check teacher does not exist
        when(assetMapper.teacherExists(nonExistentTeacherId)).thenReturn(false);
        // expect a RuntimeException due to the teacher ID not existing
        assertThrows(RuntimeException.class, () -> assetService.addAsTeacher(asset, nonExistentTeacherId));
        // verify that no asset was inserted into the Asset table
        verify(assetMapper, never()).add(any(Asset.class));
    }

    // Test case 6: Teacher upload - missing file
    @Test
    public void testTeacherUpload_MissingFile() {
        Asset asset = new Asset();
        asset.setAssetName("Teacher Asset");
        asset.setFormat("pdf");
        asset.setAuthor("Teacher");
        asset.setFile(null);  // No file attached
        String teacherId = "1";
        // return true for a valid teacher ID
        when(assetMapper.teacherExists(teacherId)).thenReturn(true);

        assetService.addAsTeacher(asset, teacherId);

        // verify the asset was inserted into the Asset table
        verify(assetMapper, times(1)).add(asset);
        // verify a record was inserted into the TeacherUploaded table
        verify(assetMapper, times(1)).addTeacherUploadedAsset(teacherId, asset.getAssetId());
        // validate that asset URL is handled properly (e.g., null)
        assertNull(asset.getUrl());
    }

}
