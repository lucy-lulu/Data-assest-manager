package com.quoll.da.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quoll.da.mapper.AssetMapper;
import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Feedback;
import com.quoll.da.pojo.PageBean;
import com.quoll.da.pojo.Result;
import com.quoll.da.service.AssetService;
import org.apache.ibatis.jdbc.Null;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.ibatis.annotations.Param;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetMapper assetMapper;

    // define the relative path for file storage within the project
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    private static final String REPORTS_DIR = "src/main/resources/static/reports/";

    @Override
    public void add(Asset asset) {

        // handle file upload
        MultipartFile file = asset.getFile();
        if (file != null && !file.isEmpty()) {
            try {
                // create the upload directory if it doesn't exist
                // path uploadPath = Paths.get(UPLOAD_DIR);
                Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // save the file to the relative path within the project
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());

                // set the relative URL path for the file
                asset.setUrl("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store the file.", e);
            }
        }

        assetMapper.add(asset);
    }

    @Override
    public void addAsTeacher(Asset asset, String teacherId) {
        // Check if the teacher exists in the database
        if (!assetMapper.teacherExists(teacherId) || teacherId == null) {
            throw new RuntimeException("Teacher ID does not exist");
        }
        // reuse the same method to insert the asset into the Asset table(same with admin)
        add(asset);
        // insert a record into the TeacherUploaded table with teacherId and assetId
        assetMapper.addTeacherUploadedAsset(teacherId, asset.getAssetId());
    }

    @Override
    public List<Asset> getUploadedAssetsByTeacher(String teacherId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return assetMapper.getUploadedAssetsByTeacher(teacherId, pageSize, offset);
    }
    public int countUploadedAssetsByTeacher(String teacherId) {
        return assetMapper.countUploadedAssetsByTeacher(teacherId);
    }

    // return asset detail by asset id
    public Asset getAssetById(String assetId) {
        return assetMapper.getAssetById(assetId);
    }

    @Override
    public List<Asset> getAllAssets(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return assetMapper.getAllAssets(pageSize, offset);
    }

    public int countAllAssets() {
        return assetMapper.countAllAssets();
    }

    @Override
    public void updateAssets(Asset asset) {
        Asset existingAsset = assetMapper.getAssetById(asset.getAssetId());
        String oldFileUrl = existingAsset.getUrl();

        MultipartFile file = asset.getFile();
        if (file != null && !file.isEmpty()) {
            try {
                // create the upload directory if it doesn't exist
                Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // save the file to the relative path within the project
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());

                // set the relative URL path for the file
                asset.setUrl("/uploads/" + fileName);

                if (oldFileUrl != null) {
                    Path oldFilePath = Paths.get(uploadPath.toString(), oldFileUrl.replace("/uploads/", ""));
                    if (Files.exists(oldFilePath)) {
                        Files.delete(oldFilePath);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to store the file.", e);
            }
        }
        assetMapper.update(asset);
    }

    public boolean isTeacherAuthorizedToEditAsset(int teacherId, String assetId) {
        List<String> teacherUploadedAssets = assetMapper.findAssetsByTeacherId(teacherId);
        return teacherUploadedAssets.contains(assetId);
    }

    @Override
    public void removeAssets(List<String> ids, int teacherId) {
        if (teacherId == -1) {
            assetMapper.removeAssets(ids);
        } else {
            List<String> teacherUploadedAssets = assetMapper.findAssetsByTeacherId(teacherId);
            List<String> validIds = ids.stream()
                    .filter(teacherUploadedAssets::contains)
                    .collect(Collectors.toList());

            if (!validIds.isEmpty()) {
                assetMapper.removeAssets(validIds);
            } else {
                throw new RuntimeException("You are not authorized to delete these assets.");
            }
        }
    }

    public List<Asset> getTopAssets(int limit) {
        return assetMapper.findTopAssetsByVisitCount(limit);
    }

    public List<Feedback> getInProgressFeedback() {
        return assetMapper.findInProgressFeedback();
    }

    public String generateAssetReport(List<Asset> assets, List<Feedback> feedbacks) throws IOException {
        Path reportDir = Paths.get(REPORTS_DIR).toAbsolutePath().normalize();
        if (!Files.exists(reportDir)) {
            Files.createDirectories(reportDir);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Top Assets");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"Asset ID", "Asset Name", "Format", "Author", "Visit Count", "Description", "Tag", "Canvas Link", "URL", "Student Type", "Subject Number"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (Asset asset : assets) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(asset.getAssetId());
            row.createCell(1).setCellValue(asset.getAssetName());
            row.createCell(2).setCellValue(asset.getFormat());
            row.createCell(3).setCellValue(asset.getAuthor());
            row.createCell(4).setCellValue(asset.getVisitCount());
            row.createCell(5).setCellValue(asset.getDescription());
            row.createCell(6).setCellValue(asset.getTag());
            row.createCell(7).setCellValue(asset.getCanvasLink());
            row.createCell(8).setCellValue(asset.getUrl());
            row.createCell(9).setCellValue(asset.getStudentType());
            row.createCell(10).setCellValue(asset.getSubjectNumber());
        }

        Sheet feedbackSheet = workbook.createSheet("In Progress Feedback");

        Row feedbackHeaderRow = feedbackSheet.createRow(0);
        String[] feedbackColumns = {"Feedback ID", "Teacher ID", "Asset ID", "Description", "Send Time", "Status"};
        for (int i = 0; i < feedbackColumns.length; i++) {
            Cell cell = feedbackHeaderRow.createCell(i);
            cell.setCellValue(feedbackColumns[i]);
        }

        int feedbackRowNum = 1;
        for (Feedback feedback : feedbacks) {
            Row row = feedbackSheet.createRow(feedbackRowNum++);
            row.createCell(0).setCellValue(feedback.getFeedbackId());
            row.createCell(1).setCellValue(feedback.getTeacherId());
            row.createCell(2).setCellValue(feedback.getAssetId());
            row.createCell(3).setCellValue(feedback.getDescription());
            row.createCell(4).setCellValue(feedback.getSendTime().toString());
            row.createCell(5).setCellValue(feedback.getStatus());
        }

        Path path = Paths.get(REPORTS_DIR, "Report.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(path.toString())) {
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        workbook.close();
        return path.toString();
    }

    @Override
    public List<Asset> searchAssets(String keyword, String author, LocalDateTime startDateTime, LocalDateTime endDateTime,String format,
                                    String studentType,
                                    String subjectNumber,
                                    String sortBy,
                                    String order) {
        return assetMapper.searchAssets(keyword, author, startDateTime, endDateTime,format,
                studentType,
                subjectNumber,
                sortBy,
                order);
    }

    @Override
    public List<Asset> filterAssets(String format,String studentType, String subjectNumber) {
        return assetMapper.filterAssets(format, studentType, subjectNumber);
    }

    @Override
    public List<Asset> sortAssets(String sortBy, String order) {
        return assetMapper.sortAssets(sortBy, order);
    }
    @Override
    public Asset getAssetByIdAndIncrementVisitCount(Integer assetId) {
        // increase visited count
        assetMapper.updateVisitCount(assetId);

        // get detial about this asset
        return assetMapper.selectAssetById(assetId);
    }

    @Override
    // Insert collected asset into the TeacherCollected table
    public String collectAssetAsTeacher(String teacherId, String assetId) {
        if (assetId == null || assetId.isEmpty()) {
            return "Cannot add non-existent asset";
        }

        // Check if the asset exists
        boolean assetExists = assetMapper.assetExists(assetId);
        if (!assetExists) {
            return "Cannot add non-existent asset";
        }
        // Check if the asset has already been collected by the teacher
        int alreadyCollected = assetMapper.isAssetCollectedByTeacher(teacherId, assetId);
        if (alreadyCollected > 0) {
            return "The asset has already been collected";
        }

        assetMapper.addTeacherCollectedAsset(teacherId, assetId);
        return "Asset collected successfully";
    }

    @Override
    public List<Asset> getCollectedAssetsByTeacher(String teacherId, int page, int pageSize) {
        int offset = (page - 1) * pageSize; // Calculate the offset based on the current page
        return assetMapper.getCollectedAssetsByTeacher(teacherId, pageSize, offset);
    }

    @Override
    public int countCollectedAssetsByTeacher(String teacherId) {
        return assetMapper.countCollectedAssetsByTeacher(teacherId);
    }

    @Override
    public JsonObject parseURI(String URI,String userToken){
        String resource;
        String resourceType;
        JsonObject result = new JsonObject();

        if (!URI.startsWith("https://")) {
            return new JsonObject();
        }


        String[] pathSegments = URI.split("/");
        if (pathSegments.length == 9) {
            resourceType = "api";
        } else if (pathSegments.length == 7) {
            resourceType = "files";
        }else {
            resourceType = "invalid";
        }



        if (pathSegments.length == 9){
             resource = pathSegments[7];
        }else{
            resource = pathSegments[6];
            int index = resource.indexOf("?");
            resource = resource.substring(0,index).toLowerCase();
        }

        System.out.println(resource);



        String bodyJson = sendHttpRequest(URI,userToken,resourceType);
        JsonObject jsonObject = JsonParser.parseString(bodyJson).getAsJsonObject();

        // get asset name
        if (jsonObject.has("name")){
            result.addProperty("assetName",jsonObject.get("name").getAsString());
        } else if (jsonObject.has("display_name")) {
            result.addProperty("assetName",jsonObject.get("display_name").getAsString());
        } else if (jsonObject.has("filename")) {
            result.addProperty("assetName",jsonObject.get("filename").getAsString());
        } else if (jsonObject.has("title")) {
            result.addProperty("assetName",jsonObject.get("title").getAsString());
        } else{
            result.addProperty("assetName","not found");
        }
        //get assert description
        if (jsonObject.has("description")){
            result.addProperty("description",jsonObject.get("description").getAsString());
        }else{
            result.addProperty("description","not found");
        }

        //get url
        if (jsonObject.has("url")){
            result.addProperty("url",jsonObject.get("url").getAsString());
        } else if (jsonObject.has("html_url")) {
            result.addProperty("url",jsonObject.get("html_url").getAsString());
        }else{
            result.addProperty("url","not found");
        }

        //get thumbnail_url
        if (jsonObject.has("thumbnail_url")){
            if (jsonObject.get("thumbnail_url").isJsonNull()){
                result.addProperty("thumbnail_url","not found");
            }else{
                result.addProperty("thumbnail_url",jsonObject.get("thumbnail_url").getAsString());
            }
        }else{
            result.addProperty("thumbnail_url","not found");
        }

        //get file type

        if(jsonObject.has("content-type")){
            result.addProperty("content-type",jsonObject.get("content-type").getAsString());
        }else{
            result.addProperty("content-type","not found");
        }


        return result;

    }

    private String sendHttpRequest(String URI,String userToken,String resourceType) {
        RestTemplate restTemplate = new RestTemplate();
        if (resourceType.equals("invalid")){
            return "Invalid url";
        } else if (resourceType.equals("files")) {
            String fileID = getFileID(URI);
            URI = "https://canvas.lms.unimelb.edu.au/api/v1/files/" + fileID;//+id
        }
        System.out.print(URI);

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization",
                    userToken);  // Add Bearer token

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // send GET https request，and add request headers
            ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.GET, entity, String.class);


            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fetch files data: " + e.getMessage();
        }
    }

    private String getFileID(String URI){
        // get id preview=
        String regex = "preview=(\\d+)";


        Pattern pattern = Pattern.compile(regex);


        Matcher matcher = pattern.matcher(URI);


        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}
