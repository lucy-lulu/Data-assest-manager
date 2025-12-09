package com.quoll.da.mapper;

import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Feedback;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import java.util.List;

@Mapper
public interface AssetMapper {

    // add asset(call by both admin and teacher)
    @Insert("INSERT INTO Asset (assetId, assetName, format, createTime, author, description, tag, canvasLink, studentType, subjectNumber, url) " +
            "VALUES (#{assetId}, #{assetName}, #{format}, #{createTime}, #{author}, #{description}, #{tag}, #{canvasLink}, #{studentType}, #{subjectNumber}, #{url})")
    @Options(useGeneratedKeys = true, keyProperty = "assetId")
    void add(Asset asset);

    // insert a new record into TeacherUploaded table(call by teacher)
    @Insert("INSERT INTO TeacherUploaded (teacherId, assetId) VALUES (#{teacherId}, #{assetId})")
    void addTeacherUploadedAsset(String teacherId, String assetId);

    @Select("SELECT assetId, assetName, format, createTime, author, description, tag, canvasLink, url, studentType, subjectNumber FROM Asset WHERE assetId = #{assetId}")
    Asset getAssetById(String assetId);

    // get all assets uploaded by a specific teacher
    @Select("SELECT * " +
            "FROM Asset a " +
            "JOIN TeacherUploaded tu ON a.assetId = tu.assetId " +
            "WHERE tu.teacherId = #{teacherId} " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<Asset> getUploadedAssetsByTeacher(@Param("teacherId") String teacherId,
                                           @Param("pageSize") int pageSize,
                                           @Param("offset") int offset);
    // total number of assets uploaded by a teacher
    @Select("SELECT COUNT(*) FROM TeacherUploaded WHERE teacherId = #{teacherId}")
    int countUploadedAssetsByTeacher(@Param("teacherId") String teacherId);
    // check if teacher Id exist or not
    @Select("SELECT COUNT(1) FROM Teacher WHERE teacherId = #{teacherId}")
    boolean teacherExists(String teacherId);

    // Update asset information
    void update(Asset asset);

    void removeAssets(List<String> ids);

    @Select("SELECT assetId FROM TeacherUploaded WHERE teacherId = #{teacherId}")
    List<String> findAssetsByTeacherId(int teacherId);

    @Select("SELECT * FROM Asset ORDER BY visitCount DESC LIMIT #{limit}")
    List<Asset> findTopAssetsByVisitCount(int limit);

    @Select("SELECT * FROM Feedback WHERE status = 'In Progress'")
    List<Feedback> findInProgressFeedback();

    // get Asset by ID
    Asset selectAssetById(Integer assetId);

    // update visit count
    void updateVisitCount(Integer assetId);

    // get all asset in database
    @Select("SELECT * " +
            "FROM Asset " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<Asset> getAllAssets(@Param("pageSize") int pageSize, @Param("offset") int offset);
    // total number of assets
    @Select("SELECT COUNT(*) FROM Asset")
    int countAllAssets();

    List<Asset> searchAssets(
            String keyword,
            String author,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String format,
            String studentType,
            String subjectNumber,
            @Param("sortBy") String sortBy,
            @Param("order") String order
    );

    List<Asset> filterAssets(
            String format,
            String studentType,
            String subjectNumber
    );

    List<Asset> sortAssets(
            @Param("sortBy") String sortBy,
            @Param("order") String order
    );

    // insert into TeacherCollected table when a teacher collects an asset
    @Insert("INSERT INTO TeacherCollected (teacherId, assetId) VALUES (#{teacherId}, #{assetId})")
    void addTeacherCollectedAsset(@Param("teacherId") String teacherId, @Param("assetId") String assetId);

    @Select("SELECT a.* FROM Asset a " +
            "JOIN TeacherCollected tc ON a.assetId = tc.assetId " +
            "WHERE tc.teacherId = #{teacherId} " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<Asset> getCollectedAssetsByTeacher(@Param("teacherId") String teacherId,
                                            @Param("pageSize") int pageSize,
                                            @Param("offset") int offset);
    // Count the total number of collected assets for a specific teacher
    @Select("SELECT COUNT(*) FROM TeacherCollected WHERE teacherId = #{teacherId}")
    int countCollectedAssetsByTeacher(@Param("teacherId") String teacherId);
    // Check if an asset exists by its ID
    @Select("SELECT COUNT(1) FROM Asset WHERE assetId = #{assetId}")
    boolean assetExists(@Param("assetId") String assetId);


    // Query the collected assets by teacher ID (with pagination)
    List<Asset> selectCollectedAssetsByTeacherIdWithPagination(@Param("teacherId") String teacherId);

    // Query the total number of collected assets by teacher ID
    Long countCollectedAssetsByTeacherId(@Param("teacherId") String teacherId);

    // Query all collected assets by teacher ID (without pagination)
    List<Asset> selectCollectedAssetsByTeacherId(@Param("teacherId") String teacherId);

    int isAssetCollected(String teacherId, Integer assetId);

    int isAssetCollectedByTeacher(String teacherId, String assetId);
}
