package com.quoll.da.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Feedback;
import com.quoll.da.pojo.PageBean;
import com.quoll.da.pojo.Result;
import com.quoll.da.service.AssetService;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/asset")
@RestController
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/{assetId}")
    public Result getAssetById(@PathVariable Integer assetId) {
        Asset asset = assetService.getAssetByIdAndIncrementVisitCount(assetId);
        return Result.success(asset);
    }

    @PostMapping("/addAsset")
    public Result createAsset(
            @RequestParam("assetName") String assetName,
            @RequestParam("format") String format,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam("canvasLink") String canvasLink,
            @RequestParam("studentType") String studentType,
            @RequestParam("subjectNumber") String subjectNumber,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // Create an Asset object
        Asset asset = new Asset();
        asset.setAssetName(assetName);
        asset.setFormat(format);
        asset.setAuthor(author);
        asset.setDescription(description);
        asset.setTag(tag);
        asset.setCanvasLink(canvasLink);
        asset.setStudentType(studentType);
        asset.setSubjectNumber(subjectNumber);
        asset.setFile(file);

        // Set the createTime to the current time
        LocalDateTime now = LocalDateTime.now();
        asset.setCreateTime(now);

        assetService.add(asset);
        return Result.success("Asset created successfully");
    }

    // Teacher upload endpoint (inserts into both Asset and TeacherUploaded tables)
    @PostMapping("/addAsset/teacher")
    public Result createAssetAsTeacher(
            @RequestParam("assetName") String assetName,
            @RequestParam("format") String format,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam("canvasLink") String canvasLink,
            @RequestParam("studentType") String studentType,
            @RequestParam("subjectNumber") String subjectNumber,
            @RequestParam("teacherId") String teacherId,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // Create an Asset object
        Asset asset = new Asset();
        asset.setAssetName(assetName);
        asset.setFormat(format);
        asset.setAuthor(author);
        asset.setDescription(description);
        asset.setTag(tag);
        asset.setCanvasLink(canvasLink);
        asset.setFile(file);
        asset.setStudentType(studentType);
        asset.setSubjectNumber(subjectNumber);

        // Set the createTime to the current time
        LocalDateTime now = LocalDateTime.now();
        asset.setCreateTime(now);

        // Create the asset and associate it with the teacher
        assetService.addAsTeacher(asset, teacherId);

        return Result.success("Asset created successfully by teacher");
    }

    // Get uploaded assets by teacher
    @GetMapping("/getUploaded/{teacherId}")
    public Result getUploadedAssetsByTeacher(
            @PathVariable String teacherId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {

        List<Asset> assets = assetService.getUploadedAssetsByTeacher(teacherId, page, pageSize);
        int totalAssets = assetService.countUploadedAssetsByTeacher(teacherId);

        Map<String, Object> response = new HashMap<>();
        response.put("assets", assets);
        response.put("currentPage", page);
        response.put("totalItems", totalAssets);
        response.put("totalPages", (int) Math.ceil((double) totalAssets / pageSize));

        return Result.success(response);
    }

    // Get asset by ID
    @GetMapping("/getAsset/{assetId}")
    public Result getAssetById(@PathVariable String assetId) {
        Asset asset = assetService.getAssetById(assetId);
        if (asset != null) {
            return Result.success(asset);
        } else {
            return Result.error("Asset not found");
        }
    }

    // Get all assets
    @GetMapping("/all")
    public Result getAllAssets(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        List<Asset> assets = assetService.getAllAssets(page, pageSize);
        int totalAssets = assetService.countAllAssets();

        Map<String, Object> response = new HashMap<>();
        response.put("assets", assets);
        response.put("currentPage", page);
        response.put("totalItems", totalAssets);
        response.put("totalPages", (int) Math.ceil((double) totalAssets / pageSize));

        return Result.success(response);
    }


    @PostMapping("/editAsset")
    public Result editAsset(
            @RequestParam("assetId") String assetId,
            @RequestParam("teacherId") int teacherId,
            @RequestParam(value = "assetName", required = false) String assetName,
            @RequestParam(value = "format", required = false) String format,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "canvasLink", required = false) String canvasLink,
            @RequestParam(value = "studentType", required = false) String studentType,
            @RequestParam(value = "subjectNumber", required = false) String subjectNumber,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Asset asset = assetService.getAssetById(assetId);
        if (asset == null) {
            return Result.error("Asset not found");
        }

        if (teacherId != -1) {
            boolean isAuthorized = assetService.isTeacherAuthorizedToEditAsset(teacherId, assetId);
            if (!isAuthorized) {
                return Result.error("You are not authorized to edit this asset");
            }
        }

        if (assetName != null) asset.setAssetName(assetName);
        if (format != null) asset.setFormat(format);
        if (author != null) asset.setAuthor(author);
        if (description != null) asset.setDescription(description);
        if (tag != null) asset.setTag(tag);
        if (canvasLink != null) asset.setCanvasLink(canvasLink);
        if (studentType != null) asset.setStudentType(studentType);
        if (subjectNumber != null) asset.setSubjectNumber(subjectNumber);
        asset.setFile(file);

        // Save the updated asset
        assetService.updateAssets(asset);

        return Result.success("Asset updated successfully");
    }


    @DeleteMapping("/remove/{ids}")
    public Result removeAsset(@PathVariable List<String> ids, @RequestParam("teacherId") int teacherId) {
        assetService.removeAssets(ids, teacherId);
        return Result.success();
    }

    @GetMapping("/generateReport")
    public Result generateAssetReport() throws IOException {
        List<Asset> topAssets = assetService.getTopAssets(2);
        List<Feedback> inProgressFeedbacks = assetService.getInProgressFeedback();

        // Generate the report file and get the file path
        String filePath = assetService.generateAssetReport(topAssets, inProgressFeedbacks);

        // Construct the download URL (assuming filePath is the relative path to the file)
        String downloadUrl = "/reports/Report.xlsx";

        // Return the result with the download URL
        return Result.success(downloadUrl);
    }


    @GetMapping("/search")
    public Result searchAssets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String createDate,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String studentType,
            @RequestParam(required = false) String subjectNumber,
            @RequestParam(required = false, defaultValue = "date") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order) {

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (createDate != null) {
            // transfer string to LocalDate
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(createDate, dateFormatter);

            // calculate start date time and end date time
            startDateTime = date.atStartOfDay(); // 00:00:00
            endDateTime = date.atTime(23, 59, 59, 999999999); // 23:59:59.999999999
        }

        List<Asset> assets = assetService.searchAssets(keyword, author, startDateTime, endDateTime,format,studentType,subjectNumber,sortBy,order);
        return Result.success(assets);
    }


    @GetMapping("/filter")
    public Result filterAssets(
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String studentType,
            @RequestParam(required = false) String subjectNumber) {
        List<Asset> assets = assetService.filterAssets(format,studentType,subjectNumber);
        return Result.success(assets);
    }

    @GetMapping("/sort")
    public Result sortAssets(
            @RequestParam(required = false, defaultValue = "date") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order) {

        List<Asset> assets = assetService.sortAssets(sortBy, order);

        return Result.success(assets);
    }

    @PostMapping("/collect")
    public Result collectAsset(@RequestParam String teacherId, @RequestParam String assetId) {
        String result = assetService.collectAssetAsTeacher(teacherId, assetId);
        switch (result) {
            case "Asset collected successfully":
                return Result.success();

            case "Cannot add non-existent asset":
                return Result.error("The asset does not exist or assetId is missing.");

            case "The asset has already been collected":
                return Result.error("The asset has already been collected by the teacher.");

            default:
                return Result.error("An unknown error occurred while collecting the asset.");
        }
    }

    @GetMapping("/collectedAssets/{teacherId}")
    public ResponseEntity<Map<String, Object>> getCollectedAssets(
            @PathVariable String teacherId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Asset> collectedAssets = assetService.getCollectedAssetsByTeacher(teacherId, page, pageSize);
        int totalAssets = assetService.countCollectedAssetsByTeacher(teacherId);

        if (collectedAssets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("collectedAssets", collectedAssets);
        response.put("currentPage", page);
        response.put("totalItems", totalAssets);
        response.put("totalPages", (int) Math.ceil((double) totalAssets / pageSize));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/fetchAsset")
    public Result getFilteredData(
            @RequestParam("url") String resource,
            @RequestParam("token") String token) {
        token = "Bearer " + token; // bearer token format
        JsonObject data = assetService.parseURI(resource,token);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(data);


        return Result.success(jsonResponse);

    }
}
