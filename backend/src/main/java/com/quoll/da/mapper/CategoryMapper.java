package com.quoll.da.mapper;

import com.quoll.da.pojo.Asset;
import com.quoll.da.pojo.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Select("SELECT ca.categoryId, categoryName, teacherId, COALESCE(COUNT(cl.categoryId), 0) AS total\n" +
            "FROM Category ca\n" +
            "LEFT JOIN Classification cl ON ca.categoryId = cl.categoryId\n" +
            "GROUP BY ca.categoryId;")
    List<Category> getTeacherCategory(String teacherId);

    @Select("select count(*) from Category where teacherId = #{teacherId} and categoryName = #{categoryName}")
    Integer checkName(String teacherId, String categoryName);

    @Insert("insert into Category (teacherId, categoryName) values (#{teacherId}, #{categoryName})")
    void addTeacherCategory(String teacherId, String categoryName);

    @Update("update Category set categoryName = #{categoryName} where teacherId = #{teacherId} and categoryId = #{categoryId}")
    void renameTeacherCategory(String teacherId, String categoryId, String categoryName);

    //SQL is written in CategoryMapper.xml
    void clearTeacherCategory(List<String> ids);

    //SQL is written in CategoryMapper.xml
    void deleteTeacherCategory(List<String> ids);

    @Select("select count(*) from Classification where categoryId = #{categoryId} and assetId = #{assetId}")
    Integer checkAsset(String categoryId, String assetId);

    @Insert("insert into Classification (categoryId, assetId) values (#{categoryId}, #{assetId})")
    void classifyAsset(String categoryId, String assetId);

    //SQL is written in CategoryMapper.xml
    void removeAsset(List<String> ids, String categoryId);

    //SQL is written in CategoryMapper.xml
    List<Asset> getCategoryAsset(String categoryId);
}

