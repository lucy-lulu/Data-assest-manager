package com.quoll.da;

import com.quoll.da.mapper.AssetMapper;
import com.quoll.da.pojo.Asset;
import com.quoll.da.service.impl.AssetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetSearchTest {

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetServiceImpl assetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case 1: Search assets by keyword and other filters
    @Test
    public void testSearchAssets_Success() {
        // Prepare the mock data
        List<Asset> mockAssets = new ArrayList<>();
        mockAssets.add(new Asset("100","Asset1", "pdf", LocalDateTime.now(), "Author1", "Description1", "Tag1", "link", "url", "Graduate", "SUBJ001", 0, null));
        mockAssets.add(new Asset("101", "Asset2", "pdf", LocalDateTime.now(), "Author2", "Description2", "Tag2", "link", "url", "type", "SUBJ002", 0, null));

        // Set up mock behavior
        when(assetMapper.searchAssets(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockAssets);

        // Test the service method
        List<Asset> assets = assetService.searchAssets("tag1", "Author1", LocalDateTime.now().minusDays(1), LocalDateTime.now(), "pdf", "studentType", "SUBJ002", "visitCount", "desc");

        // Assertions
        assertNotNull(assets);
        assertEquals(2, assets.size());
        verify(assetMapper, times(1)).searchAssets(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    // Test case 2: Filter assets by format, student type, and subject number
    @Test
    public void testFilterAssets_Success() {
        // Prepare the mock data
        List<Asset> mockAssets = new ArrayList<>();
        mockAssets.add(new Asset("102", "Asset1", "pdf", LocalDateTime.now(), "Author1", "Description1", "Tag1", "link", "url", "type", "SUBJ001", 0, null));

        // Set up mock behavior
        when(assetMapper.filterAssets(anyString(), anyString(), anyString()))
                .thenReturn(mockAssets);

        // Test the service method
        List<Asset> assets = assetService.filterAssets("pdf", "studentType", "SUBJ001");

        // Assertions
        assertNotNull(assets);
        assertEquals(1, assets.size());
        verify(assetMapper, times(1)).filterAssets(anyString(), anyString(), anyString());
    }

    // Test case 3: Sort assets by visit count in descending order
    @Test
    public void testSortAssets_Success() {
        // Prepare the mock data
        List<Asset> mockAssets = new ArrayList<>();
        mockAssets.add(new Asset("103", "Asset1", "pdf", LocalDateTime.now(), "Author1", "Description1", "Tag1", "link", "url", "type", "SUBJ001", 0, null));
        mockAssets.add(new Asset("104", "Asset2", "pdf", LocalDateTime.now(), "Author2", "Description2", "Tag2", "link", "url", "type", "SUBJ002",0 , null));

        // Set up mock behavior
        when(assetMapper.sortAssets(anyString(), anyString()))
                .thenReturn(mockAssets);

        // Test the service method
        List<Asset> assets = assetService.sortAssets("visitCount", "desc");

        // Assertions
        assertNotNull(assets);
        assertEquals(2, assets.size());
        verify(assetMapper, times(1)).sortAssets(anyString(), anyString());
    }

    // Test case 4: Search with no matching results
    @Test
    public void testSearchAssets_NoResults() {
        // Prepare the mock data
        List<Asset> mockAssets = new ArrayList<>();

        // Set up mock behavior
        when(assetMapper.searchAssets(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockAssets);

        // Test the service method
        List<Asset> assets = assetService.searchAssets("nonExistingTag", "nonExistingAuthor", LocalDateTime.now().minusDays(1), LocalDateTime.now(), "pdf", "studentType", "SUBJ002", "visitCount", "desc");

        // Assertions
        assertNotNull(assets);
        assertEquals(0, assets.size());
        verify(assetMapper, times(1)).searchAssets(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class), anyString(), anyString(), anyString(), anyString(), anyString());
    }
}
