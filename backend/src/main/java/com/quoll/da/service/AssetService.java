package com.quoll.da.service;

import com.google.gson.JsonObject;
import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Feedback;
import org.springframework.cglib.core.Local;
import com.quoll.da.pojo.PageBean;


import java.io.IOException;
import java.time.LocalDateTime;

import java.util.List;

public interface AssetService {
    void add(Asset asset);  // add new asset

    // for teacher upload, including inserting into TeacherUploaded table
    void addAsTeacher(Asset asset, String teacherId);

    Asset getAssetById(String assetId); // get asset detail by id

    List<Asset> getUploadedAssetsByTeacher(String teacherId, int page, int pageSize); // get list of asset by teacher ID
    int countUploadedAssetsByTeacher(String teacherId);

    List<Asset> getAllAssets(int page, int pageSize);
    int countAllAssets();

    void updateAssets(Asset asset);

    void removeAssets(List<String> ids, int teacherId);

    public List<Feedback> getInProgressFeedback();

    public List<Asset> getTopAssets(int limit);

    List<Asset> searchAssets(String keyword, String author, LocalDateTime startDateTime, LocalDateTime endDateTime,String format,
                             String studentType,
                             String subjectNumber,
                             String sortBy,
                             String order);

    List<Asset> filterAssets(String format, String studentType, String subjectNumber);

    List<Asset> sortAssets(String sortBy, String order);

    Asset getAssetByIdAndIncrementVisitCount(Integer assetId);


    String collectAssetAsTeacher(String teacherId, String assetId);

    List<Asset> getCollectedAssetsByTeacher(String teacherId, int page, int pageSize);
    // total collected assets for pagination
    int countCollectedAssetsByTeacher(String teacherId);

    boolean isTeacherAuthorizedToEditAsset(int teacherId, String assetId);

    public String generateAssetReport(List<Asset> assets, List<Feedback> feedbacks) throws IOException;

    JsonObject parseURI(String URI,String userToken);
}
